package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.EventActivity;
import io.swagger.client.model.Property;
import io.swagger.client.model.Snapshot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.org.apache.bcel.internal.generic.Type;

@SuppressWarnings("rawtypes")
public class Wizard {
	

	public static final Set<String> JKOOL_EVENT_FIELDS = new HashSet<String>(Arrays.asList("source","sourceInfo","sourceUrl","severity","severityNo","type","typeNo","pid","tid","compCode","compCodeNo","reasonCode","location","user","timeUsec","startTimeUsec","endTimeUsec","elapsedTimeUsec","idCount","snapCount","idSet","msgText","msgSize","msgEncoding","msgCharset","corrId","resource","msgMimeType","msgAge","Exception","msgTag","parentTrackId","waitTimeUsec"));
	//public static final Set<String> JKOOL_ACTIVITY_FIELDS = new HashSet<String>(Arrays.asList("source","sourceInfo","sourceUrl","severity","severityNo","type","typeNo","pid","tid","compCode","compCodeNo","reasonCode","location","user","timeUsec","startTimeUsec","endTimeUsec","elapsedTimeUsec","idCount","snapCount","idSet","idCount","idSet","status","corrId","resource","msgMimeType","msgAge","Exception","msgTag","parentTrackId","waitTimeUsec"));
	public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	
	
	public static void main(String[] args) {
		try
		{
			
			ArrayList data = (ArrayList)readFile("C:\\Users\\cbernardone\\git\\jKoolRestClients\\jKoolJavaRestClients\\src\\resources\\weather.csv");
			HashMap massagedData = massageData(data);
			
			Builder builder;
			Client client = Client.create();
			ClientResponse response = null;
			String basePath = "http://11.0.0.40:6580/jKool/JKool_Service/rest";		

			EventActivity activity = (EventActivity)massagedData.get("activity");
			Iterator iEventKeys = activity.getEvents().keySet().iterator();
			// Loop on events
			for (int i = 0; i < activity.getEvents().size(); i++)
			{
				List listOfSnapshots = new ArrayList<Snapshot>();
				EventActivity event = (EventActivity)activity.getEvents().get(iEventKeys.next());
				Snapshot snapshot = event.getSnapshots().get("snapshot");
				if (snapshot.getProperties().size() > 0)
				{
					snapshot.setCount(snapshot.getProperties().size());
					snapshot.setFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
					listOfSnapshots.add(snapshot);				
					event.setSnapshotList(listOfSnapshots);
					event.setSnapCount(event.getSnapshotList().size());
					event.setSourceFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
				}
				builder = client.resource(basePath).accept("application/json");
				builder = builder.header("token", args[0]);
				builder.type("application/json").post(ClientResponse.class, serialize(event));
			}
			activity.setEvents(null);
			activity.setSnapshots(null);
			activity.setSnapCount(0);
			activity.setSourceFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", args[0]);
			builder.type("application/json").post(ClientResponse.class, serialize(activity));
			
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}
	
	 /**
	   * Serialize the given Java object into JSON string.
	   */
	  public static String serialize(Object obj) throws ApiException {
	    try {
	      if (obj != null)
	        return JsonUtil.getJsonMapper().writeValueAsString(obj);
	      else
	        return null;
	    }
	    catch (Exception e) {
	      throw new ApiException(500, e.getMessage());
	    }
	  }
	  
	  
	  
	  public static List readFile(String fileName) {

			BufferedReader br = null;
			HashMap<String, String> line = new HashMap<String, String>();
			List<HashMap<String, String>> lines = new ArrayList<HashMap<String, String>>();
			List<String> fieldNames = new ArrayList<String>();

			try {

				String sCurrentLine;

				br = new BufferedReader(new FileReader(fileName));
				
				StringTokenizer st = new StringTokenizer(br.readLine(),",");
				while (st.hasMoreTokens()) 
				{
					fieldNames.add(st.nextToken());
				}

				while ((sCurrentLine = br.readLine()) != null) 
				{
					st = new StringTokenizer(sCurrentLine,",");
					int i = 0;
					line = new HashMap<String, String>();
					while (st.hasMoreTokens())
					{
						line.put((String)fieldNames.get(i), st.nextToken());
						i++;
					}
					lines.add(line);
				}
				return lines;

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}

	  public static HashMap massageData(ArrayList lines)
	  {
		  Snapshot snapshot = null;
		  String todaysDate = null;
		  String eventUuid = null;
		  String activityUuid = null;
		  HashMap<String, Object> massagedData = new HashMap<String, Object>();
		  EventActivity activity = null;
		  // Create the activity and set fields on it.
		  activity = new EventActivity();
		  activity.setEventName("activity");
		  activity.setType("ACTIVITY");
		  activity.setStatus("END");
		  activity.setEvents(new HashMap<String, EventActivity>());
		  activity.setTimeUsec(todaysDate);
		  activityUuid = UUID.randomUUID().toString();
		  activity.setTrackingId(activityUuid);
		  massagedData.put("activity", new EventActivity());
		  
		  for(int cnt=0;cnt< lines.size(); cnt++)
		  {
			  EventActivity event = null;
			  HashMap<String, Snapshot> snapshots = null;
			  
			  // Read the line
			  HashMap line = (HashMap)lines.get(cnt);
			  
			  
			  try
			  {
			  // Get the date
			  Date date = format.parse((String)line.get("Time"));
			  todaysDate = date.getTime() + "000";

			  // Get/Create the event and set fields on it.
			  event = new EventActivity();
			  event.setType("EVENT");
			  event.setParentTrackId(activityUuid);
			  event.setEventName((String)line.get("ETrId"));
			  event.setTimeUsec(todaysDate);
			  eventUuid = UUID.randomUUID().toString();
			  event.setTrackingId(eventUuid);
			  snapshots = new HashMap<String, Snapshot>();
			  event.setSnapshots(snapshots);
			  
			  // Create the snapshot for the event
		      snapshot = new Snapshot();
		      snapshot.setProperties(new ArrayList<HashMap>());
		      String snapshotUuid = UUID.randomUUID().toString();
			  snapshot.setTrackId(snapshotUuid);
			  snapshot.setType("SNAPSHOT");
			  snapshot.setTimeUsec(todaysDate);
			  snapshot.setName("snapshot");
			  snapshot.setParentId(eventUuid);
			   
			  // Loop through each field of the line.
			  Iterator iKeyset = line.keySet().iterator();
			  while (iKeyset.hasNext())
			  {
				  String key = (String)iKeyset.next();
				  // predefined jKool event fields (uses reflection to set)
				  if (JKOOL_EVENT_FIELDS.contains(key)&& line.get("ETrId") != null) 
				  {
					  Class  aClass = EventActivity.class;
					  Field field = aClass.getField(key);
					  String fieldType = field.getType().getName();
					  if (fieldType.equals("java.lang.String"))
						  field.set(event, (String)line.get(key));
					  else if (fieldType.equals("java.lang.Integer"))
						  field.set(event,(Integer)line.get(key));
				  }
				  // snapshot properties
				  else if (! key.endsWith("ETrId"))
				  {
					  Property property = new Property();
					  property.setName(key);
				      property.setValue((String)line.get(key));
				      property.setType("string"); // Use logic here.
				      snapshot.getProperties().add(property);
				  }
			  }

			  event.getSnapshots().put("snapshot", snapshot);
			  activity.setTimeUsec(todaysDate);
			  activity.getEvents().put((String)line.get("ETrId"), event);
		  }

		  catch (Exception e)
		  {
			  e.printStackTrace();
		  }
		  }
		  massagedData.put("activity", activity);
		  return massagedData;
	  }

}
