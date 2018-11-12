package com.plivo.test.PlivoClient;

import org.json.JSONException;
import org.json.JSONObject;

import com.plivo.test.PlivoClient.DefaultHttpClient.HttpMethod;

public class PlivoAPI {
	private DefaultHttpClient client;
	private static final String baseUrl = "https://api.plivo.com/v1";
	
	public PlivoAPI() {
		client = new DefaultHttpClient();
	}
	
	public String getAuthId(String basicAuth) throws JSONException {
		JSONObject basicAuthJson = new JSONObject(basicAuth);
		return basicAuthJson.get("authId").toString();
	}
	public String getAuthToken(String basicAuth) throws JSONException {
		JSONObject basicAuthJson = new JSONObject(basicAuth);
		return basicAuthJson.get("authToken").toString();
	}
	
	public DefaultResponse sendListAllRentedNumbersRequest(String queryParametersAsJsonString, String basicAuth) throws Exception {
		String url = baseUrl + "/Account/" + getAuthId(basicAuth) + "/Number/";
		return client.execute(HttpMethod.GET, url, queryParametersAsJsonString, "", getAuthId(basicAuth), getAuthToken(basicAuth));
	}
	
	public DefaultResponse sendAMessage(String requestBody, String basicAuth) throws Exception {
		String url = baseUrl + "/Account/" + getAuthId(basicAuth) + "/Message/";
		return client.execute(HttpMethod.POST, url, "", requestBody, getAuthId(basicAuth), getAuthToken(basicAuth));
	}
	
	public DefaultResponse getDetailsOfAMessage(String msgUuid, String basicAuth) throws Exception {
		String url = baseUrl + "/Account/" + getAuthId(basicAuth) + "/Message/" + msgUuid;
		return client.execute(HttpMethod.GET, url, "", "", getAuthId(basicAuth), getAuthToken(basicAuth));
	}
	
	public DefaultResponse getPricing(String queryParametersAsJsonString, String basicAuth) throws Exception {
		String url = baseUrl + "/Account/" + getAuthId(basicAuth) + "/Pricing/";
		return client.execute(HttpMethod.GET, url, queryParametersAsJsonString, "", getAuthId(basicAuth), getAuthToken(basicAuth));
	}
	
	public DefaultResponse getAccountDetails(String basicAuth) throws Exception {
		String url = baseUrl + "/Account/" + getAuthId(basicAuth);
		return client.execute(HttpMethod.GET, url, "", "", getAuthId(basicAuth), getAuthToken(basicAuth));
	}
}