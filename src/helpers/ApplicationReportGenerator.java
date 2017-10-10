package helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationReportGenerator
{
	static String dateTimeInReport=new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()); 
	public void generateReport(ArrayList<String> currentList, String reportName)
	{
		//File htmlReport=new File("D:\\"+reportName+".html");
		File htmlReport=new File(System.getenv("APPDATA")+"\\"+reportName+"_"+dateTimeInReport+".xml");
		BufferedWriter bw=null;
		try
		{
			bw=new BufferedWriter(new FileWriter(htmlReport.getAbsolutePath(),false));
			/*String data="<html>"
					+ "<head><title>Application Usage Report</title><style>td{background-color:#8FB2B2;}</style></head>"
					+ "<body style=\"background-color:lightgrey;font-family:Lucida Console;text-align:center;\">"
						+ "<h1>Application Usage @ "+new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(new Date())+"</h1>"
						+ "<table border=\"2\" align=\"center\"><tr bgcolor=\"#526666\"><th>Window Title</th><th>Process Name</th><th>Process Description</th><th>Start Time</th><th>End Time</th><th>Total Active Time</th></tr>";*/
			String data="<?xml version=\"1.0\"?><?xml-stylesheet type=\"text/xsl\" href=\"ApplicationTracker.xsl\"?><ApplicationDetails>";
			for(int i=0;i<currentList.size();i++)
			{
				String windowTitle=currentList.get(i).split("#~&@~&#")[0];
				String processName=currentList.get(i).split("#~&@~&#")[1];
				String processDesc=currentList.get(i).split("#~&@~&#")[2];
				String startTime=currentList.get(i).split("#~&@~&#")[3];
				String endTime=currentList.get(i).split("#~&@~&#")[4];
				String totalActiveTime=currentList.get(i).split("#~&@~&#")[5];
				String hh=((Integer.parseInt(totalActiveTime))/60/60)+"";
				if(hh.length()==1)
					hh="0"+hh;
				String mm=(((Integer.parseInt(totalActiveTime))/60)%60)+"";
				if(mm.length()==1)
					mm="0"+mm;
				String ss=((Integer.parseInt(totalActiveTime))%60)+"";
				if(ss.length()==1)
					ss="0"+ss;
				String totalActiveTimeFormatted=hh+":"+mm+":"+ss;
				//data=data+"<tr><td>"+windowTitle+"</td><td>"+processName+"</td><td>"+processDesc+"</td><td>"+startTime+"</td><td>"+endTime+"</td><td>"+totalActiveTime+"</td><td>"+totalActiveTimeFormatted+"</td></tr>";
				//Latest
				//data=data+"<tr><td>"+windowTitle+"</td><td>"+processName+"</td><td>"+processDesc+"</td><td>"+startTime+"</td><td>"+endTime+"</td><td>"+totalActiveTimeFormatted+"</td></tr>";
				data=data+"<ApplicationInfo><WindowTitle><![CDATA["+windowTitle+"]]></WindowTitle><ProcessName>"+processName+"</ProcessName><ProcessDescription>"+processDesc+"</ProcessDescription><StartTime>"+startTime+"</StartTime><EndTime>"+endTime+"</EndTime><TotalActiveTime>"+totalActiveTimeFormatted+"</TotalActiveTime></ApplicationInfo>";
			}
			data=data+"</ApplicationDetails>";
			bw.write(data);
			bw.close();
		}
		catch(IOException e)
		{
			Logger.getLogger(ApplicationReportGenerator.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
