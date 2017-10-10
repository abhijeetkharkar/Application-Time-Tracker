package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import helpers.ApplicationDataGenerator;

public class ApplicationTimeTracker
{
	public static void main(String[] args)
	{
		createXSL();
		ApplicationDataGenerator adg=new ApplicationDataGenerator();
		adg.getapplicationFinalData(1, 50);
	}
	
	static void createXSL()
	{
		String xslContent="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
				+"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n<xsl:output method=\"html\"/>\n"
				+"	<xsl:template match=\"/\">\n"
				+"		<html>\n"
				+"			<head>\n"
				+"				<title>Application Usage Report</title>\n"
				+"				<style>td{background-color:#8FB2B2;}</style>\n"
				+"			</head>\n"
				+"			<body style=\"background-color:lightgrey;font-family:Lucida Console;text-align:center;\">\n"
				+"				<h2>Applications Details</h2>	\n"
				+"				<table border=\"2\"> \n"
				+"					<tr bgcolor=\"#526666\">  \n"
				+"						\n"
				+"						<th>Window Title</th>\n"
				+"						<th>Process Name</th>\n"
				+"						<th>Process Description</th>\n"
				+"						<th>Start Time</th>\n"
				+"						<th>End Time</th>\n"
				+"						<th>Total Active Time</th>\n"
				+"					</tr>\n"
				+"					<xsl:for-each select=\"ApplicationDetails/ApplicationInfo\">\n"
				+"					<xsl:sort select=\"StartTime\"/>\n"
				+"\n"
				+"						<tr>\n"
				+"							<td><xsl:value-of select=\"WindowTitle\"/></td>\n"
				+"							<td><xsl:value-of select=\"ProcessName\"/></td>\n"
				+"							<td><xsl:value-of select=\"ProcessDescription\"/></td>\n"
				+"							<td><xsl:value-of select=\"StartTime\"/></td>\n"
				+"							<td><xsl:value-of select=\"EndTime\"/></td>\n"
				+"							<td><xsl:value-of select=\"TotalActiveTime\"/></td>	\n"
				+"						</tr>\n"
				+"						\n"
				+"					</xsl:for-each>  	\n"
				+"				</table> \n"
				+"			</body>\n"
				+"		</html>\n"
				+"	</xsl:template>\n"
				+"</xsl:stylesheet>\n";
		
		File xslFile=new File(System.getenv("APPDATA")+"\\ApplicationTracker.xsl");
		BufferedWriter bw=null;
		try
		{
			bw=new BufferedWriter(new FileWriter(xslFile.getAbsolutePath(),false));
			bw.write(xslContent);
			bw.close();
		}
		catch(IOException e)
		{
			Logger.getLogger(ApplicationTimeTracker.class.getName()).log(Level.SEVERE, null, e);
		}
		xslFile.setExecutable(true);
	}
}
