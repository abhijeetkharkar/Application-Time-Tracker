package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

public class ApplicationWatcher
{
	private static final int MAX_TITLE_LENGTH = 1024;
	char windowText[];
	String processText;
	String processDesc;
	IntByReference ibr;
	WinDef.HWND hWnd;
	
	public ApplicationWatcher()
	{
		windowText=new char[MAX_TITLE_LENGTH*2];
		processText=new String();
		processDesc=new String();
		ibr=new IntByReference();
	}
	
	public String startWatch()
	{
		HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, windowText, MAX_TITLE_LENGTH);
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, ibr);
        Runtime rt=Runtime.getRuntime();
        Process p=null;
        try
        {
            String found=null;
        	p=rt.exec("C:\\Windows\\System32\\wbem\\wmic process where processid="+ibr.getValue()+" get caption,executablepath,processid");
        	BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=null;
            while((line=br.readLine())!=null)
            {
            	if(line.contains(ibr.getValue()+""))
            	{
            		found=line;
            		break;
            	}
            }
            p.destroy();
            processText=found.replaceAll("[ ]+", "#").split("#")[0];
            line=null;
            String filePath="\""+found.replaceAll("[ ][ ]+", "#").split("#")[1]+"\"";
            CreateDeleteBatch.createBatch();
            //p = Runtime.getRuntime().exec("toolTipInfo.bat "+filePath);
            p = Runtime.getRuntime().exec(System.getenv("APPDATA")+"\\toolTipInfo.bat "+filePath);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));		
			while ((line = input.readLine()) != null)
			{
				//System.out.println("line:"+line);
			    if(line.contains("File description:"))
			    	processDesc=line.split(":")[1].trim();
			}
			input.close();
			CreateDeleteBatch.deleteBatch();
            //File f=new File(found.replaceAll("[ ][ ]+", "#").split("#")[1]);
            //System.out.println(found.replaceAll("[ ][ ]+", "#").split("#")[1]);
    		//FileSystemView fileSystemView = FileSystemView.getFileSystemView();
            //processDesc=fileSystemView.getSystemTypeDescription(f);
        }
        catch(IOException e)
        {
        	Logger.getLogger(ApplicationWatcher.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return ibr.getValue()+"#~&@~&#"+Native.toString(windowText)+"#~&@~&#"+processText+"#~&@~&#"+processDesc;
	}
}
