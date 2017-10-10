<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<title>Application Usage Report</title>
				<style>td{background-color:#8FB2B2;}</style>
			</head>
			<body style="background-color:lightgrey;font-family:Lucida Console;text-align:center;">
				<h2>Applications Details</h2>	
				<table border="2"> 
					<tr bgcolor="#526666">  
						
						<th>Window Title</th>
						<th>Process Name</th>
						<th>Process Description</th>
						<th>Start Time</th>
						<th>End Time</th>
						<th>Total Active Time</th>
					</tr>
					<xsl:for-each select="ApplicationDetails/ApplicationInfo">
					<xsl:sort select="StartTime"/>

						<tr>
							<td><xsl:value-of select="WindowTitle"/></td>
							<td><xsl:value-of select="ProcessName"/></td>
							<td><xsl:value-of select="ProcessDescription"/></td>
							<td><xsl:value-of select="StartTime"/></td>
							<td><xsl:value-of select="EndTime"/></td>
							<td><xsl:value-of select="TotalActiveTime"/></td>	
						</tr>
						
					</xsl:for-each>  	
				</table> 
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>








