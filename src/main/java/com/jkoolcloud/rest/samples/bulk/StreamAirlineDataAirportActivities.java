package com.jkoolcloud.rest.samples.bulk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkoolcloud.client.api.model.Activity;
import com.jkoolcloud.client.api.model.Event;
import com.jkoolcloud.client.api.model.EventTypes;
import com.jkoolcloud.client.api.model.Property;
import com.jkoolcloud.client.api.model.Severities;
import com.jkoolcloud.client.api.service.JKStream;

@SuppressWarnings("rawtypes")
public class StreamAirlineDataAirportActivities {
	

	public static final Set<String> JKOOL_EVENT_FIELDS = new HashSet<String>(Arrays.asList("source","sourceInfo","sourceUrl","severity","type","typeNo","pid","tid","reasoCode","location","user","timeUsec","startTimeUsec","endTime-Usec","idCount","snapCount","idSet","msgText","msgSize","msgEncoding","msgCharset","corrid","resource","msgMimeType","msgAge","exception","msgTag","waitTimeUsec","compCode","reasonCode"));
	public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	public String streamingUrl = null;
	
	public StreamAirlineDataAirportActivities() {
	}
	
	public static void main(String[] args) {
		System.out.println("Initiate Streaming");
		String results = streamData("airlines-corr.csv");
		System.out.println(results);
	}

	public static String streamData(String fileName) {
		try
		{	
			System.out.println("Streaming data from the following file: " + fileName);
			processFile(fileName);
			return "Events processed successfully.";
		}

		catch (Exception ex)
		{
			System.out.println("Error streaming data: " + ex);
			return "failure";
		}
	}
	
	  // Read the .csv file.
     @SuppressWarnings("unchecked")
	  public static void processFile(String fileName) {

			BufferedReader br = null;
			HashMap<String, String> line = new HashMap<String, String>();
			List<String> fieldNames = new ArrayList<String>();
			JKStream jkSend = new JKStream(System.getProperty("jk.access.token", "access-token"));
			File outputFile = null;
			FileWriter fw = null;

			try {
				outputFile = new File("C:\\Users\\cbernardone\\Desktop\\outputFile.json");
				fw = new FileWriter("C:\\Users\\cbernardone\\Desktop\\outputFile.json",true); //the true will append the new data
				String sCurrentLine;
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				InputStream is = classloader.getResourceAsStream(fileName);
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				
				// Store the field names. Field names are obtained from the very first line of the .csv file.
				StringTokenizer st = new StringTokenizer(br.readLine(),",");
				while (st.hasMoreTokens()) 
				{
					fieldNames.add(st.nextToken());
				}


				// Read the remaining lines. Store each line in a HashMap (key=field name value=field value). Store all lines in a array of HashMaps.
				int eventNo = 0;
				while ((sCurrentLine = br.readLine()) != null) 
				{
					eventNo++;
					System.out.println("Streaming Activity and Events: " + eventNo);
					String[] vals = sCurrentLine.split(",", -1);
					line = new HashMap<String, String>();
					for (int i=0; i < vals.length; i++)
					{
						line.put(fieldNames.get(i), vals[i]);
					}

					HashMap<String, Object> results = massageData(line);
					List<Event> events = (List<Event>)results.get("events");
					
					// Loop on events
					for (int i = 0; i < events.size(); i++)
					{
						// Get the event
						Event event = events.get(i);
						// Stream the event to jKool		
						ObjectMapper mapper = new ObjectMapper();
						String jsonInString = mapper.writeValueAsString(event);
					    fw.write(jsonInString);//appends the string to the file
					    fw.write("\n");

						Response response = jkSend.post(event);
						response.close();
					}

				}

			} catch (Exception e) {
				System.out.println("Error reading data from streaming file:  " + e);
			} finally {
				try {
				    fw.close();
					if (br != null)br.close();
				} catch (IOException ex) {
					System.out.println("Error closing streaming file:  " + ex);
				}

			}
		}
	  
	  public static HashMap massageData(HashMap line)
	  {
		  List<Event> events = new ArrayList<Event>();
		  Activity activity = new Activity();
		  HashMap<String, Object> results = new HashMap<String, Object>();

			  try
			  {
				  String eventTrackingId = (String)line.get("Month") + (String)line.get("DayofMonth") + (String)line.get("Origin");
				  //UUID activityTrackingId = java.util.UUID.randomUUID();
				  // Send Event
				  Event sendEvent = new Event();
				  List<Property> properties = new ArrayList<Property>();
				  sendEvent.setType(EventTypes.SEND);
				  Long departureStartTime = dateFromString(line, "CRSDepTime", "Origin");
				  Long departureEndTime = dateFromString(line, "DepTime", "Origin");
				  sendEvent.setName((String)line.get("Origin") + "-" + (String)line.get("UniqueCarrier") + "-" + (String)line.get("FlightNum") + "-Depart");
				  sendEvent.setStartTime(departureStartTime);
			      sendEvent.setServer("server-terminal-" + (String)line.get("Origin"));
			      sendEvent.setNetAddr("runway-" + (String)line.get("Origin"));
				  if (departureEndTime > departureStartTime)
					  sendEvent.setElapsedTimeUsec((departureEndTime - departureStartTime) * 1000);	
				  else
					  sendEvent.setElapsedTimeUsec(0);
				  sendEvent.setDataCenter((String)line.get("Origin"));
				  if (! line.get("TaxiOut").equals("NA"))
					  properties.add(new Property("TaxiOut", line.get("TaxiOut"),  "double", null));
				  if (! line.get("DepDelay").equals("NA"))
					  properties.add(new Property("DepDelay", line.get("DepDelay"), "double", null));
				  sendEvent.addProperty(properties);
				  if (departureEndTime > departureStartTime)
				  {
					  sendEvent.setException("LateFlight");
					  sendEvent.setSeverity(Severities.ERROR);
				  }
				  else
					  sendEvent.setException("none");				  
				  
				  setCommonFields(line, sendEvent, properties, eventTrackingId);
				  events.add(sendEvent);
				  
				  // Receive Event
				  eventTrackingId = (String)line.get("Month") + (String)line.get("DayofMonth") + (String)line.get("Dest");
				  Event receiveEvent = new Event();
				  properties = new ArrayList<Property>();
				  receiveEvent.setType(EventTypes.RECEIVE);
				  Long arrivalStartTime = new Long(dateFromString(line, "CRSArrTime", "Dest"));
				  Long arrivalEndTime = new Long(dateFromString(line, "ArrTime", "Dest"));
				  
				  if (arrivalEndTime > arrivalStartTime)
				  {
					receiveEvent.setException("LateFlight");
				  	receiveEvent.setSeverity(Severities.ERROR);
				  }
				  else
					  receiveEvent.setException("none");				  
				  
				  receiveEvent.setStartTime(arrivalStartTime);
				  receiveEvent.setName((String)line.get("Dest") + "-" + (String)line.get("UniqueCarrier") + "-" + (String)line.get("FlightNum") + "-Arrive");
				  if (arrivalEndTime > arrivalStartTime)
					  receiveEvent.setElapsedTimeUsec((arrivalEndTime - arrivalStartTime) * 1000);		
				  else
					  receiveEvent.setElapsedTimeUsec(0);
				  receiveEvent.setDataCenter((String)line.get("Dest"));
			      receiveEvent.setServer("server-terminal-" + (String)line.get("Dest"));
			      receiveEvent.setNetAddr("runway-" + (String)line.get("Dest"));   
				  if (! line.get("TaxiIn").equals("NA"))
					  properties.add(new Property("TaxiIn", line.get("TaxiIn"), "double", null));
				  if (! line.get("ArrDelay").equals("NA"))
					  properties.add(new Property("ArrDelay", line.get("ArrDelay"), "double", null));
				  setCommonFields(line, receiveEvent, properties, eventTrackingId);
				  receiveEvent.addProperty(properties);
				  events.add(receiveEvent);
				  
				  results.put("events", events);
				  return results; 
				  
			  }
	
			  catch (Exception e)
			  {
					System.out.println("Error massaging streaming data:  " + e);
					return null;
			  }
	  }
	  
	  public static Long dateFromString(HashMap line, String timeType, String type)
	  {
		  try
		  {
			  // Get the date
			  format.setTimeZone(getTimeZone((String)line.get(type)));
			  String stringDate = (String)line.get("Year") + "-" + String.format("%02d",Integer.parseInt((String)line.get("Month"))) + "-" +  String.format("%02d",Integer.parseInt((String)line.get("DayofMonth")));
			  timeType = String.format("%04d",Integer.parseInt(((String)line.get(timeType))));
			  String hours = timeType.substring(0,2);
			  String minutes = timeType.substring(2,4);
			  String stringTime = stringDate + " " +  hours + ":" + minutes + ":00";
			  
			  Date date = format.parse(stringTime);
			  return date.getTime();
		  }
		  catch (Exception e)
		  {
			  return null;
		  }
	  }
	  
	  @SuppressWarnings({ "unchecked" })
	  public static void setCommonFields(HashMap line, Event event, List<Property>  properties, String eventTrackingId)
	  {
		  try
		  {
		      event.setResource("resource-sky");
		      //List corrIds = new ArrayList();
		      event.setTrackingId(eventTrackingId.toString());
		      event.setAppl((String)line.get("UniqueCarrier") + "AtTerminalAt" + event.getDataCenter());
		      //corrIds.add(event.getDataCenter() + (String)line.get("Year") + (String)line.get("Month") + (String)line.get("DayofMonth"));
		      //event.setCorrId(corrIds);
		  }
		  catch (Exception e)
		  {
				System.out.println("Error setting common fields:  " + e);
		  }
	  }
	  
	  public static TimeZone getTimeZone(String airport){
		  List<String> mdt = Arrays.asList("ABQ", "PHX", "SLC", "ELP");
		  List<String> edt = Arrays.asList("ATL", "CLT", "DTW","PHL", "PIT");
		  List<String> pdt = Arrays.asList("LAS", "LAX", "ONT", "SAN", "BUR");
		  List<String> cdt = Arrays.asList("HOU", "MCI", "MSP", "OKC", "OMA", "STL", "SAT");
		  if (mdt.contains(airport))
			  return TimeZone.getTimeZone("America/Phoenix");
		  else if (edt.contains(airport))
			  return TimeZone.getTimeZone("America/New_York");
		  else if (pdt.contains(airport))
			  return TimeZone.getTimeZone("America/Los_Angeles");
		  else if (cdt.contains(airport))
			  return TimeZone.getTimeZone("America/Chicago");
		  else
			  return null;
	  }
	  

}
