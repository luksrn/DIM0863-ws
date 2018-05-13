package br.ufrn.dimap.dim0863.webserver.fiware;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("fiware")
@EnableConfigurationProperties(FiwareProperties.class)
public class FIWAREController {
	
	private FiwareProperties properties;
	
	public FIWAREController(FiwareProperties props) {
		this.properties = props;
	}
	
	public String sendCommand(String entityId, String deviceId, String command, List<String> params) throws ClientProtocolException, IOException {
        if(params == null) {
        		params = new ArrayList<String>();
        }

        String url = String.format("http://%s:%d/v1/updateContext", properties.getIdas().getHost(), properties.getIdas().getPort() );

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		
		post.addHeader("X-Auth-Token", properties.getToken() );
		post.addHeader("Fiware-Service", properties.getService() );
		post.addHeader("Fiware-ServicePath", properties.getServicePath() );
	
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
		
		return result.toString();
		
	}
	
}

