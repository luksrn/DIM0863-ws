package br.ufrn.dimap.dim0863.webserver.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class FIWAREController {
	
	public static final String IDAS_HOST = "192.168.1.100";
	public static final int IDAS_ADMIN_PORT = 4041;
	
	public static String token = "";
	public static String FIWAREService = "UFRNService";
	public static String FIWAREServicePath = "/ufrn";

	public static void sendCommand(String entityId, String deviceId, String command, List<String> params) throws ClientProtocolException, IOException {
        if(params == null) {
        		params = new ArrayList<String>();
        }

        String url = String.format("http://%s:%d/v1/updateContext", FIWAREController.IDAS_HOST, FIWAREController.IDAS_ADMIN_PORT);

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		
		post.addHeader("X-Auth-Token", FIWAREController.token);
		post.addHeader("Fiware-Service", FIWAREController.FIWAREService);
		post.addHeader("Fiware-ServicePath", FIWAREController.FIWAREServicePath);
	
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "application/json");
		
		String strParams = String.join("|", params);
        String value = strParams;
        
        String payload = String.format(
        					"{" +
                    		      "\"contextElements\": [" + 
                    		      	"{" + 
                    		      		"\"type\": \"thing\"," +
                    		      		"\"isPattern\": \"false\"," + 
                    		      		"\"id\": \"%s\"," + 
                    		      		"\"attributes\": [" +
                    		    				"{" + 
                    		    					"\"name\": \"%s\"," +
                    		    					"\"type\": \"command\"," +
                    		    					"\"value\": \"%s\"" +
                    		    				"}" +
                    		    			"]" +
                    		    		"}" +
                    		    	  "]," +
                    		    	  "\"updateAction\": \"UPDATE\"" +
                  		"}", entityId, command, value);
		
		post.setEntity(new StringEntity(payload));
		
		HttpResponse response = client.execute(post);
		
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + 
                                    response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());
	}
	
}

