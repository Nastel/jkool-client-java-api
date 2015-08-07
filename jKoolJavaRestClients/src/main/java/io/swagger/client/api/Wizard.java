package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.EventActivity;
import io.swagger.client.model.Property;
import io.swagger.client.model.Snapshot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

@SuppressWarnings("rawtypes")
public class Wizard {
	
	//private static String todaysDate = (new Date()).getTime() + "000";
	public static final Set<String> JKOOL_FIELDS = new HashSet<String>(Arrays.asList("status","source","sourceInfo","sourceUrl"));
	
	public static void main(String[] args) {
		try
		{
			
			ArrayList data = (ArrayList)readFile("C:\\Users\\cbernardone\\git\\jKoolRestClients\\jKoolJavaRestClients\\src\\resources\\weather.csv");
			HashMap massagedData = massageData(data);
			
			Builder builder;
			Client client = Client.create();
			ClientResponse response = null;
			String basePath = "http://11.0.0.40:6580/jKool/JKool_Service/rest";
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", args[0]);
		
			Iterator iKeys = massagedData.keySet().iterator();
			while (iKeys.hasNext())
			{
				String key = (String)iKeys.next();
				EventActivity activity = (EventActivity)massagedData.get(key);
				for (int i = 0; i < activity.getEvents().size(); i++)
				{
					EventActivity event = (EventActivity)activity.getEvents().get(i);
					for (int j=0; j < event.getSnapshots().size(); j++)
					{
						Snapshot snapshot = event.getSnapshots().get(j);
						snapshot.setCount(snapshot.getProperties().size());
						snapshot.setFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
						builder.type("application/json").post(ClientResponse.class, serialize(snapshot));
					}
					event.setSnapCount(event.getSnapshots().size());
					event.setSourceFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
					builder.type("application/json").post(ClientResponse.class, serialize(event));
				}
				activity.setSourceFqn("APPL=" + args[1] + "#SERVER=" + args[2] + "#NETADDR=" + args[3] + "#DATACENTER=" + args[4] + "#GEOADDR=" + args[5]);
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
		  HashMap<String, EventActivity> massagedData = new HashMap<String, EventActivity>();
		  for(int cnt=0;cnt< lines.size(); cnt++)
		  {
			  HashMap line = (HashMap)lines.get(cnt);
			  if (massagedData.get(line.get("ATrId")) == null)
				  massagedData.put((String)line.get("ATrId"), new EventActivity());
			  EventActivity activity = massagedData.get(line.get("ATrId"));
			  if (activity.getEvents().get(line.get("ETrId")) == null)
			  {
				  activity.getEvents().put((String)line.get("ETrId"), new EventActivity());
			  }
			  EventActivity event = activity.getEvents().get((String)line.get("ETrId"));
			  Iterator iKeyset = line.keySet().iterator();
			  while (iKeyset.hasNext())
			  {
				  String key = (String)iKeyset.next();
				  if (JKOOL_FIELDS.contains(key)&& line.get("ETrId") != null) // make generic for all pre-defined jKool fields
				  {
					  event.setStatus((String)line.get(key));
				  }
				  else if (key.indexOf("/string/") > 0) // this is a snapshot property
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
				    	  snapshot.getProperties().put(propertyName, property);
				      }
				      else
				      {
				    	  snapshot = new Snapshot();
				      }
				      snapshot.getProperties().put(propertyName, property);
				      snapshot.setParentId(event.getTrackingId());
					  String snapshotUuid = UUID.randomUUID().toString();
					  snapshot.setTrackId(snapshotUuid);
					  snapshot.setType("SNAPSHOT");
					  event.getSnapshots().put(snapshotName, snapshot);
				  }
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
