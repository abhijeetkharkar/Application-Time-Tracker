package helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateDeleteBatch
{
	static void createBatch()
	{
		String batchContent="@if (@X)==(@Y) @end /* JScript comment\n"
				+"	@echo off\n"
				+"	\n"
				+"	rem :: the first argument is the script name as it will be used for proper help message\n"
				+"	C:\\Windows\\System32\\cscript.exe //E:JScript //nologo \"%~f0\" %*\n"
				+"\n"
				+"	exit /b %errorlevel%\n"
				+"	\n"
				+"@if (@X)==(@Y) @end JScript comment */\n"
				+" \n"
				+"////// \n"
				+"FSOObj = new ActiveXObject(\"Scripting.FileSystemObject\");\n"
				+"var ARGS = WScript.Arguments;\n"
				+"if (ARGS.Length < 1 ) {\n"
				+" WScript.Echo(\"No file passed\");\n"
				+" WScript.Quit(1);\n"
				+"}\n"
				+"var filename=ARGS.Item(0);\n"
				+"var objShell=new ActiveXObject(\"Shell.Application\");\n"
				+"/////\n"
				+"\n"
				+"\n"
				+"//fso\n"
				+"ExistsItem = function (path) {\n"
				+"	return FSOObj.FolderExists(path)||FSOObj.FileExists(path);\n"
				+"}\n"
				+"\n"
				+"getFullPath = function (path) {\n"
				+"    return FSOObj.GetAbsolutePathName(path);\n"
				+"}\n"
				+"//\n"
				+"\n"
				+"//paths\n"
				+"getParent = function(path){\n"
				+"	var splitted=path.split(\"\\\\\");\n"
				+"	var result=\"\";\n"
				+"	for (var s=0;s<splitted.length-1;s++){\n"
				+"		if (s==0) {\n"
				+"			result=splitted[s];\n"
				+"		} else {\n"
				+"			result=result+\"\\\\\"+splitted[s];\n"
				+"		}\n"
				+"	}\n"
				+"	return result;\n"
				+"}\n"
				+"\n"
				+"\n"
				+"getName = function(path){\n"
				+"	var splitted=path.split(\"\\\\\");\n"
				+"	return splitted[splitted.length-1];\n"
				+"}\n"
				+"//\n"
				+"\n"
				+"function main(){\n"
				+"	if (!ExistsItem(filename)) {\n"
				+"		WScript.Echo(filename + \" does not exist\");\n"
				+"		WScript.Quit(2);\n"
				+"	}\n"
				+"	var fullFilename=getFullPath(filename);\n"
				+"	var namespace=getParent(fullFilename);\n"
				+"	var name=getName(fullFilename);\n"
				+"	var objFolder=objShell.NameSpace(namespace);\n"
				+"	var objItem=objFolder.ParseName(name);\n"
				+"	//https://msdn.microsoft.com/en-us/library/windows/desktop/bb787870(v=vs.85).aspx\n"
				+"	WScript.Echo(fullFilename + \" : \");\n"
				+"	WScript.Echo(objFolder.GetDetailsOf(objItem,-1));\n"
				+"	\n"
				+"}\n"
				+"\n"
				+"main();\n";
		File batchFile=new File(System.getenv("APPDATA")+"\\toolTipInfo.bat");
		BufferedWriter bw=null;
		try
		{
			bw=new BufferedWriter(new FileWriter(batchFile.getAbsolutePath(),false));
			bw.write(batchContent);
			bw.close();
		}
		catch(IOException e)
		{
			Logger.getLogger(CreateDeleteBatch.class.getName()).log(Level.SEVERE, null, e);
		}
		batchFile.setExecutable(true);
	}
	
	static void deleteBatch()
	{
		File batchFile=new File(System.getenv("APPDATA")+"\\toolTipInfo.bat");
		batchFile.delete();
	}
}
