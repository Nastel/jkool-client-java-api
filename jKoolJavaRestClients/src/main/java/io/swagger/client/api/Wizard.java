package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.EventActivity;
import io.swagger.client.model.Property;
import io.swagger.client.model.Snapshot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

@SuppressWarnings("rawtypes")
public class Wizard {
	

	public static final Set<String> JKOOL_FIELDS = new HashSet<String>(Arrays.asList("status","source","sourceInfo","sourceUrl"));
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
		
			Iterator iActivityKeys = massagedData.keySet().iterator();
			// Loop on activities
			while (iActivityKeys.hasNext())
			{
				String activityKey = (String)iActivityKeys.next();
				EventActivity activity = (EventActivity)massagedData.get(activityKey);
				Iterator iEventKeys = activity.getEvents().keySet().iterator();
				// Loop on events
				for (int i = 0; i < activity.getEvents().size(); i++)
				{
					EventActivity event = (EventActivity)activity.getEvents().get(iEventKeys.next());
					// Loop on snapshots
					Iterator iSnapshotKeys = event.getSnapshots().keySet().iterator();
					ArrayList listOfSnapshots = new ArrayList();
					for (int j=0; j < event.getSnapshots().size(); j++)
					{
						Snapshot snapshot = event.getSnapshots().get(iSnapshotKeys.next());
						snapshot.setCount(snapshot.getProperties().size());
						snapshot.setType("SNAPSHOT");
						String snapshotUuid = UUID.randomUUID().toString();
						snapshot.setTrackId(snapshotUuid);
						snapshot.setFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
						listOfSnapshots.add(snapshot);
					//	builder = client.resource(basePath).accept("application/json");
					//	builder = builder.header("token", args[0]);
					//	builder.type("application/json").post(ClientResponse.class, serialize(listOfSnapshots));
					}
					event.setSnapshotList(listOfSnapshots);
					event.setType("EVENT");
					event.setSnapCount(event.getSnapshotList().size());
					event.setSourceFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
					builder = client.resource(basePath).accept("application/json");
					builder = builder.header("token", args[0]);
					builder.type("application/json").post(ClientResponse.class, serialize(event));
				}
				activity.setType("ACTIVITY");
				activity.setEvents(null);
				activity.setSnapshots(null);
				activity.setSnapCount(0);
				activity.setStatus("END");
				activity.setSourceFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
				builder = client.resource(basePath).accept("application/json");
				builder = builder.header("token", args[0]);
				builder.type("application/json").post(ClientResponse.class, serialize(activity));
			}
			
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
		  HashMap<String, EventActivity> massagedData = new HashMap<String, EventActivity>();
		  for(int cnt=0;cnt< lines.size(); cnt++)
		  {
			  // Read the line
			  HashMap line = (HashMap)lines.get(cnt);
			  
			  // Get the date
			  try
			  {
				  Date date = format.parse((String)line.get("Time"));
				  todaysDate = date.getTime() + "000";
			  }
			  catch (Exception e)
			  {
				  
			  }
			  
			  
			  // Get or create the activity.
			  if (massagedData.get(line.get("ATrId")) == null)
				  massagedData.put((String)line.get("ATrId"), new EventActivity());
			  // Get or create the event.
			  EventActivity activity = massagedData.get(line.get("ATrId"));
			  activity.setEventName((String)line.get("ATrId"));
			  activity.setTimeUsec(todaysDate);
			  String activityUuid = UUID.randomUUID().toString();
			  activity.setTrackingId(activityUuid);
			  if (activity.getEvents().get(line.get("ETrId")) == null)
			  {
				  activity.getEvents().put((String)line.get("ETrId"), new EventActivity());
			  }
			  EventActivity event = activity.getEvents().get((String)line.get("ETrId"));
			  //event.setTrackingId((String)line.get("ETrId"));
			  event.setParentTrackId(activityUuid);
			  event.setEventName((String)line.get("ETrId"));
			  event.setTimeUsec(todaysDate);
			  String eventUuid = UUID.randomUUID().toString();
			  event.setTrackingId(eventUuid);
			  
			  Iterator iKeyset = line.keySet().iterator();
			  // Loop through each field of the line.
			  while (iKeyset.hasNext())
			  {
				  String key = (String)iKeyset.next();
				  // predefined jKool fields
				  if (JKOOL_FIELDS.contains(key)&& line.get("ETrId") != null) // make generic for all pre-defined jKool fields
				  {
					  event.setStatus((String)line.get(key));
				  }
				  // snapshot property
				  else if (key.indexOf("/string/") > 0) 
				  {
					  String snapshotName = key.substring(0, key.indexOf("/string/"));
					  String propertyName = key.substring(key.indexOf("/string/") + 8, key.length());
					  if (event.getSnapshots() == null)
						  event.setSnapshots(new HashMap<String, Snapshot>());
					  HashMap<String, Snapshot> snapshots = event.getSnapshots();
					  
					  Property property = new Property();
					  property.setName(propertyName);
				      property.setValue((String)line.get(key));
				      property.setType("string");
				      if (snapshots.get(snapshotName) != null)
				      {
				    	  snapshot = snapshots.get(snapshotName);
				    	  //snapshot.getProperties().put(propertyName, property);
				      }
				      else
				      {
				    	  snapshot = new Snapshot();
				    	  snapshot.setProperties(new ArrayList<HashMap>());
				      }
				      snapshot.getProperties().add(property);
					  String snapshotUuid = UUID.randomUUID().toString();
					  snapshot.setTrackId(snapshotUuid);
					  snapshot.setType("SNAPSHOT");
					  snapshot.setName(snapshotName);
					  snapshot.setParentId(eventUuid);
					  snapshot.setTimeUsec(todaysDate);
					  event.getSnapshots().put(snapshotName, snapshot);
				  }
				  // event property
				  else if ((!JKOOL_FIELDS.contains(key)) && key.indexOf("p-string/") > -1)
				  {
					  Property property = new Property();
					  property.setName((String)line.get(key));
				      property.setValue((String)line.get(key));
				      property.setType("string");
				      event.getProperties().add(property);
				  }
				  activity.getEvents().put((String)line.get("ETrId"), event);
				  massagedData.put((String)line.get("ATrId"), activity);
				  
			  }

		  }
		  return massagedData;
	  }

}
