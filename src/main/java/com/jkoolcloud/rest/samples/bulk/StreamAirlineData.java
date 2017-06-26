package com.jkoolcloud.rest.samples.bulk;

import java.io.BufferedReader;
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
import java.util.UUID;

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.model.Activity;
import com.jkoolcloud.rest.api.model.Event;
import com.jkoolcloud.rest.api.model.EventTypes;
import com.jkoolcloud.rest.api.model.Property;
import com.jkoolcloud.rest.api.service.JKStream;

@SuppressWarnings("rawtypes")
public class StreamAirlineData {
	

	public static final Set<String> JKOOL_EVENT_FIELDS = new HashSet<String>(Arrays.asList("source","sourceInfo","sourceUrl","severity","type","typeNo","pid","tid","reasoCode","location","user","timeUsec","startTimeUsec","endTime-Usec","idCount","snapCount","idSet","msgText","msgSize","msgEncoding","msgCharset","corrid","resource","msgMimeType","msgAge","exception","msgTag","waitTimeUsec","compCode","reasonCode"));
	public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	public String streamingUrl = null;
	
	public StreamAirlineData() {
	}
	
	public static void main(String[] args) {
		System.out.println("Initiate Streaming");
		String results = streamData("airlines.csv");
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

			try {

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
					Activity activity = (Activity)results.get("activity");
					
					// Loop on events
					for (int i = 0; i < events.size(); i++)
					{
						// Get the event
						Event event = events.get(i);
						// Stream the event to jKool		
						Response response = jkSend.post(event);
						response.close();
					}
					Response response = jkSend.post(activity);
					response.close();
				}

			} catch (Exception e) {
				System.out.println("Error reading data from streaming file:  " + e);
			} finally {
				try {
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
				  UUID eventTrackingId = java.util.UUID.randomUUID();
				  UUID activityTrackingId = java.util.UUID.randomUUID();
				  // Send Event
				  Event sendEvent = new Event();
				  List<Property> properties = new ArrayList<Property>();
				  sendEvent.setType(EventTypes.SEND);
				  Long startTime = dateFromString(line, "CRSDepTime");
				  Long endTime = dateFromString(line, "CRSDepTime");
				  sendEvent.setName((String)line.get("UniqueCarrier") + "-" + (String)line.get("FlightNum") + "-Depart");
				  sendEvent.setStartTime(startTime);
				  sendEvent.setElapsedTimeUsec(endTime - startTime);		  
				  sendEvent.setDataCenter((String)line.get("Origin"));
				  if (! line.get("TaxiOut").equals("NA"))
					  properties.add(new Property("TaxiOut", "double", line.get("TaxiOut"), null));
				  sendEvent.setProperties(properties);
				  setCommonFields(line, sendEvent, activityTrackingId, properties, eventTrackingId);
				  events.add(sendEvent);
				  
				  // Receive Event
				  Event receiveEvent = new Event();
				  properties = new ArrayList<Property>();
				  receiveEvent.setType(EventTypes.RECEIVE);
				  startTime = new Long(dateFromString(line, "CRSDepTime"));
				  endTime = new Long(dateFromString(line, "CRSDepTime"));
				  receiveEvent.setStartTime(startTime);
				  receiveEvent.setName((String)line.get("UniqueCarrier") + "-" + (String)line.get("FlightNum") + "-Arrive");
				  receiveEvent.setElapsedTimeUsec(endTime - startTime);		
				  receiveEvent.setDataCenter((String)line.get("Dest"));
				  if (! line.get("TaxiIn").equals("NA"))
					  properties.add(new Property("TaxiIn", "double", line.get("TaxiIn"), null));
				  if (! line.get("ArrDelay").equals("NA"))
					  properties.add(new Property("ArrDelay", "double", line.get("ArrDelay"), null));
				  setCommonFields(line, receiveEvent, activityTrackingId, properties, eventTrackingId);
				  receiveEvent.setProperties(properties);
				  events.add(receiveEvent);
				  
				  
				  
				  // Activity
				  activity.setTrackingId(activityTrackingId.toString());
				  activity.setName((String)line.get("UniqueCarrier") + "-" + (String)line.get("FlightNum") + "-WholeFlight");
				  properties = new ArrayList<Property>();
				  if (! line.get("Distance").equals("NA"))
					  properties.add(new Property("Distance", "double", line.get("Distance"), null));
				  properties.add(new Property("Cancelled", "string", line.get("Cancelled"), null));
				  properties.add(new Property("CancellationCode", "string", line.get("CancellationCode"), null));
				  properties.add(new Property("Diverted", "string", line.get("Diverted"), null));
				  if (! line.get("CarrierDelay").equals("NA"))
					  properties.add(new Property("CarrierDelay", "double", line.get("CarrierDelay"), null));
				  if (! line.get("WeatherDelay").equals("NA"))
					  properties.add(new Property("WeatherDelay", "double", line.get("WeatherDelay"), null));
				  if (! line.get("NASDelay").equals("NA"))
					  properties.add(new Property("NASDelay", "double", line.get("NASDelay"), null));
				  if (! line.get("LateAircraftDelay").equals("NA"))
					  properties.add(new Property("LateAircraftDelay", "double", line.get("LateAircraftDelay"), null));
				  if (! line.get("AirTime").equals("NA"))
					  properties.add(new Property("AirTime", "double", line.get("AirTime"), null));
				  properties.add(new Property("FlightNum", "string", line.get("FlightNum"), null));
				  if (! line.get("CRSElapsedTime").equals("NA"))
					  properties.add(new Property("CRSElapsedTime", "double", line.get("CRSElapsedTime"), null));
				  properties.add(new Property("DayOfWeek", "string", line.get("DayOfWeek"), null));
				  activity.setProperties(properties);
				  results.put("events", events);
				  results.put("activity", activity);
				  return results;
			  }
	
			  catch (Exception e)
			  {
					System.out.println("Error massaging streaming data:  " + e);
					return null;
			  }
	  }
	  
	  public static Long dateFromString(HashMap line, String timeType)
	  {
		  try
		  {
			  // Get the date
			  String stringDate = (String)line.get("Year") + "-" + String.format("%02d",Integer.parseInt((String)line.get("Month"))) + "-" +  String.format("%02d",Integer.parseInt((String)line.get("DayofMonth")));
			  timeType = String.format("%04d",Integer.parseInt(((String)line.get(timeType))));
			  String hours = timeType.substring(0,2);
			  String minutes = timeType.substring(3,4);
			  String stringTime = stringDate + " " +  hours + ":" + minutes + ":00";
			  
			  Date date = format.parse(stringTime);
			  return date.getTime();
		  }
		  catch (Exception e)
		  {
			  return null;
		  }
	  }
	  
	  public static void setCommonFields(HashMap line, Event event, UUID parentTrackingId, List<Property>  properties, UUID eventTrackingId)
	  {
		  try
		  {
		      event.setResource("sky");
		      event.setParentTrackId(parentTrackingId.toString());
		      event.setTrackingId(eventTrackingId.toString());
		      event.setServer("sky");
		      event.setAppl((String)line.get("TailNum"));

		  }
		  catch (Exception e)
		  {
				System.out.println("Error setting common fields:  " + e);
		  }
	  }
	  

}
