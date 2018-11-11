package com.plivo.test.PlivoClient;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.HashMap;
import java.util.Iterator;

import org.testng.Reporter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultHttpClient {
	private OkHttpClient client;
	private Request request;
	private Response response;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	enum HttpMethod {
		GET, POST, PUT, DELETE;
	}
	public DefaultHttpClient() {
		client = new OkHttpClient();
	}
	private HashMap<String, String> queryStringToKeyValuePair(String queryParametersAsJsonString) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> jsonMap = new HashMap<String, String>();
        jsonMap = mapper.readValue(queryParametersAsJsonString,
                new TypeReference<HashMap<String, String>>(){});
        return jsonMap;
	}
	public DefaultResponse execute(HttpMethod method, String url, String queryParametersAsJsonString, String requestBodyAsJsonString, String authId, String authToken) throws Exception{
		//add query parameters
		HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
		if(!queryParametersAsJsonString.isEmpty()) {
			HashMap<String, String> queryStringAsMap = queryStringToKeyValuePair(queryParametersAsJsonString);
			Iterator<HashMap.Entry<String, String>> itr = queryStringAsMap.entrySet().iterator(); 
	        while(itr.hasNext()) 
	        { 
	             HashMap.Entry<String, String> entry = itr.next();
	             if(!entry.getValue().isEmpty()) {
	            	 urlBuilder.addQueryParameter(entry.getKey(), entry.getValue()); 
	             }
	        }
		}
		String credential = Credentials.basic(authId, authToken);
		//send request
		if(method == HttpMethod.GET) {
			request = new Request.Builder()
					.get()
					.header("Authorization", credential)
					.url(urlBuilder.build())
					.build();
		}
		else if(method == HttpMethod.POST){
			RequestBody body = RequestBody.create(JSON, requestBodyAsJsonString);
			  request = new Request.Builder()
					  .header("Authorization", credential)
					  .url(urlBuilder.build())
					  .post(body)
					  .build();
		}
		else if(method == HttpMethod.PUT) {
			//pending
		}
		else if(method == HttpMethod.DELETE) {
			//pending
		}
		else {
			throw new Exception("Invalid Http method");
		}
		Reporter.log("Sending: " + request.toString(), true);
		response = client.newCall(request).execute();
		DefaultResponse dResponse = new DefaultResponse(response.code(), response.body().string());
		Reporter.log("Reveived: " + dResponse.toString(), true);
		return dResponse;
	}
}