package com.plivo.test.PlivoTest.BaseTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.plivo.test.HttpClient.DefaultResponse;
import com.plivo.test.PlivoClient.PlivoAPI;
import com.plivo.test.PlivoTest.Validator.Validator;

public class PlivoBaseTest {
	protected static PlivoAPI plivoClient = new PlivoAPI();
	protected static Validator validator = new Validator();
	
	public String getJSONString(String foldername, String fileName) throws JSONException, Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File("" + System.getProperty("user.dir")
		+ "/src/main/resources/testData/" + fileName + ".json")));
		String line;
		StringBuilder jsonData = new StringBuilder();
		
		while ((line = br.readLine()) != null) {
			jsonData.append(line.trim());
		}
		br.close();
		return jsonData.toString();
	}
	public String getUuidFromSendAMessageResponse(DefaultResponse response) throws JSONException {
		JSONObject resJsonObject = new JSONObject(response.responseBody);
		String msgUuid = resJsonObject.getJSONArray("message_uuid").getString(0);
		return msgUuid;
	}
}