package helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationDataGenerator
{
	ArrayList<String> applicationFinalData;
	
	public ApplicationDataGenerator()
	{
		applicationFinalData=new ArrayList<String>();
	}
	
	@SuppressWarnings("unused")
	//public ArrayList<String> getapplicationFinalData(int sleepInSeconds, int iterations)
	public void getapplicationFinalData(int sleepInSeconds, int iterations)
	{
		/*
		 * ApplicationWatcher "aw" is used to call the method to get the details of the Currently Active Process.
		 * activePID will store the ProcessID of the currently active process.
		 * masterPIDList will store the PIDs of the active processes over the entire duration. Each PID will be present only once in this list.
		 * masterList will store the PID, Window Tile and Process Name.
		 * startTime and endTime will store the time when the process starts and ends respectively.
		 * currentPID, currentWindowName, currentProcessName --> details  of the currently active process
		 * pastPID, pastWindowName, pastProcessName --> details of the process which was active before the current process.
		 * PIDTimeMap stores the "processID" as key and the "active time" as value.
		 * If a process becomes active for the second time, then the active time of that process will be stored as the sum of the 2 times.
		 * If a process is terminated and then reinvoked, then the processID will be different and so will the record.
		 * 
		 */
		ApplicationWatcher aw=new ApplicationWatcher();
		int activePID=0;
		ArrayList<Integer> masterPIDList=new ArrayList<Integer>();
		ArrayList<String> masterList=new ArrayList<String>();
		ArrayList<String> endTimeList=new ArrayList<String>();
		Date startTime=new Date();
		String pastWindowName="";
		String pastProcessName="";
		String pastProcessDesc="";
		int pastPID=0;
		Map<Integer, Integer> PIDTimeMap=new HashMap<Integer, Integer>();
		//for(int i=0;i<iterations;i++)
		while(true)
		{
			String record=aw.startWatch();
			int currentPID=Integer.parseInt(record.split("#~&@~&#")[0]);
			String currentWindowName=record.split("#~&@~&#")[1];
			String currentProcessName=record.split("#~&@~&#")[2];
			//System.out.println(record);
			String currentProcessDesc=record.split("#~&@~&#")[3];
			if(masterPIDList.size()==0 && !masterPIDList.contains(currentPID))
			{
				masterPIDList.add(currentPID);
				masterList.add(record+"#~&@~&#"+new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
			}
			if(currentPID!=activePID)
			{
				activePID=currentPID;
				if(!masterPIDList.contains(currentPID))
				{
					masterPIDList.add(currentPID);
					masterList.add(record+"#~&@~&#"+new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
				}
				Date endTime=new Date();
				if(masterPIDList.size()>1 && endTime.getTime()-startTime.getTime()>999)
				{
					int activeTime=(int)((endTime.getTime()-startTime.getTime())/1000);
					if(PIDTimeMap.containsKey(pastPID))
					{
						//System.out.println(pastPID);
						//System.out.println(currentPID);
						int oldActiveTime=PIDTimeMap.get(pastPID);
						int newActiveTime=oldActiveTime+activeTime;
						PIDTimeMap.put(pastPID, newActiveTime);
						//System.out.println("Inside IF");
						//System.out.println(endTimeList);
						//System.out.println(masterPIDList.indexOf(pastPID)+","+masterPIDList.size()+","+pastPID);
						endTimeList.remove(masterPIDList.indexOf(pastPID));
						endTimeList.add(masterPIDList.indexOf(pastPID),new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
					}
					else
					{
						PIDTimeMap.put(pastPID, activeTime);
						//System.out.println("Inside ELSE");
						//System.out.println(endTimeList);
						//System.out.println(masterPIDList.indexOf(pastPID)+","+masterPIDList.size()+","+pastPID);
						//endTimeList.remove(masterPIDList.indexOf(pastPID));
						endTimeList.add(masterPIDList.indexOf(pastPID),new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
					}
					//System.out.println(pastWindowName+"#"+pastProcessName+"#"+activeTime);
				}
				startTime=endTime;
			}
			
			/*
			 * The next part of the code was meant to get the time of the last active process in that interval of time.
			 */
			
			/*if(i==iterations-1)
			{
				int activeTime=(int)((new Date().getTime()-startTime.getTime())/1000);
				if(PIDTimeMap.containsKey(currentPID))
				{
					//System.out.println(currentPID);
					int oldActiveTime=PIDTimeMap.get(currentPID);
					int newActiveTime=oldActiveTime+activeTime;
					PIDTimeMap.put(currentPID, newActiveTime);
				}
				else
				{
					PIDTimeMap.put(currentPID, activeTime);
				}
				//System.out.println(currentWindowName+"#"+currentProcessName+"#"+activeTime);
			}*/
			
			//if(i%10==0)
			//{
				Map<Integer, Integer> tempMap=new HashMap<Integer, Integer>(PIDTimeMap);
				ArrayList<String> currentList=new ArrayList<String>();
				Iterator inside = tempMap.entrySet().iterator();
			    while (inside.hasNext())
			    {
			        Map.Entry pairs = (Map.Entry)inside.next();
			        String currentRecord=masterList.get(masterPIDList.indexOf(pairs.getKey()));
			        //System.out.println("Inside Data for Report");
			        //System.out.println(endTimeList+","+masterPIDList.indexOf(pairs.getKey()));
			        //System.out.println(endTimeList.get(masterPIDList.indexOf(pairs.getKey())));
			        //System.out.println(currentRecord);
			        //System.out.println(currentRecord.split("#")[1]+","+currentRecord.split("#")[2]+","+ pairs.getValue());
			        //applicationFinalData.add(currentRecord.split("#")[1]+","+currentRecord.split("#")[2]+","+ pairs.getValue());
			        currentList.add(currentRecord.split("#~&@~&#")[1]+"#~&@~&#"+currentRecord.split("#~&@~&#")[2]+"#~&@~&#"+currentRecord.split("#~&@~&#")[3]+"#~&@~&#"+currentRecord.split("#~&@~&#")[4]+"#~&@~&#"+endTimeList.get(masterPIDList.indexOf(pairs.getKey()))+"#~&@~&#"+pairs.getValue());
			        inside.remove(); // avoids a ConcurrentModificationException
			    }
			    
			    ApplicationReportGenerator arg=new ApplicationReportGenerator();
			    arg.generateReport(currentList,"CurrentReport");
			//}

			pastProcessName=currentProcessName;
			pastProcessDesc=currentProcessDesc;
			pastWindowName=currentWindowName;
			pastPID=currentPID;
			try
			{
				Thread.sleep((long)(sleepInSeconds*1000));
			}
			catch (InterruptedException e)
			{
				Logger.getLogger(ApplicationDataGenerator.class.getName()).log(Level.SEVERE, null, e);
			}
			//System.out.println(i);
		}
		
		/*Iterator it = PIDTimeMap.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String currentRecord=masterList.get(masterPIDList.indexOf(pairs.getKey()));
	        //System.out.println(currentRecord.split("#")[1]+","+currentRecord.split("#")[2]+","+ pairs.getValue());
	        applicationFinalData.add(currentRecord.split("#")[1]+","+currentRecord.split("#")[2]+","+currentRecord.split("#")[3]+","+ pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    ApplicationReportGenerator arg=new ApplicationReportGenerator();
	    arg.generateReport(applicationFinalData,"FinalApplicationReport");*/
	    //return applicationFinalData;
	}
}
