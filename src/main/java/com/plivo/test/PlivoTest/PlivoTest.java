package com.plivo.test.PlivoTest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.plivo.test.PlivoClient.DefaultResponse;
import com.plivo.test.PlivoTest.RequestData;

public class PlivoTest extends PlivoBaseTest{
	JSONObject requestJson;
	String suiteName;
	String basicAuthJson; //Assuming this test class is only for 1 user test cases
	
	@BeforeClass
	public void beforeClass() throws Exception {
		suiteName = "IntegrationTest";
	}
	
	@DataProvider(name = "dataProvider")
	public Object[][] requestData() throws Exception {
		return RequestData.IntegrationTest;
	}
	
	@Test(dataProvider = "dataProvider")
	public void test(String... params) throws Exception {
		String testCase = params[0];
		requestJson = new JSONObject(getJSONString(suiteName, testCase));
		Reporter.log("-----------TestCase: " + testCase + " START------------", true);
		setupTestData();
		executeTestCase();
		Reporter.log("-----------" + testCase + " END------------", true);
	}
	
	public void setupTestData() throws Exception{
		Reporter.log("Setup Test Data:", true);
		JSONArray testCase = requestJson.getJSONArray("testData");
		for(int i=0; i<testCase.length(); i++) {
		    JSONObject nextTask = testCase.getJSONObject(i);
		    switch(JSONObject.getNames(nextTask)[0]) {
		    case "BasicAuth":
		    	basicAuthJson = nextTask.getJSONObject("BasicAuth").toString();
		    	Reporter.log("Updated Basic Auth Info", true);
		    	break;
		    default:
		    	throw new Exception("Invalid task");
		    		
		    }
		}
	}
	
	public void executeTestCase() throws Exception{
		Reporter.log("Execute Test Case:", true);
		DefaultResponse response = new DefaultResponse(0,"");
		String src = "", dst = "";
		String msgUuid = "";
		Double cashCredit = 0.0;
		//Above variables should be a part of a data structure that we maintain locally to store
		//info and actions and later use to it to validate against db.
		//Due to lack of time I am using these variables
		
		JSONArray testCase = requestJson.getJSONArray("testCase");
		for(int i=0; i<testCase.length(); i++) {
		    	JSONObject nextTask = testCase.getJSONObject(i);
		    	switch(JSONObject.getNames(nextTask)[0]) {
		    	case "ListAllRentedNumbers":
		    		Reporter.log("Calling List all rented numbers API", true);
		    		response = plivoClient.sendListAllRentedNumbersRequest(nextTask.getJSONObject("ListAllRentedNumbers").toString(), basicAuthJson);
		    		validator.validateListAllRentedNumbersAPIResponse(response);
		    		break;
		    	case "GetSrcAndDst":
		    		Reporter.log("Get source and destination from numbers list", true);
		    		JSONObject resJsonObject = new JSONObject(response.responseBody);
		    		JSONArray numbers = resJsonObject.getJSONArray("objects");
		    		if(numbers.length() >= 2) {
		    			src = (String)numbers.getJSONObject(0).get("number");
		    			dst = (String)numbers.getJSONObject(1).get("number");
		    		}
		    		break;
		    	case "SendAMessage":
		    		Reporter.log("Sending a message from:" + src + " to:" + dst, true);
		    		nextTask.getJSONObject("SendAMessage").put("src", src);
		    		nextTask.getJSONObject("SendAMessage").put("dst", dst);
		    		response = plivoClient.sendAMessage(nextTask.getJSONObject("SendAMessage").toString(), basicAuthJson);
		    		validator.validateSendAMessageAPIResponse(response);
		    		break;
		    	case "GetDetailsOfAMessage":
		    		Reporter.log("Calling get details of a message API", true);
		    		response = plivoClient.getDetailsOfAMessage(msgUuid, basicAuthJson);
		    		validator.validateGetDetailsOfAMessageAPIResponse(response);
		    		break;
		    	case "GetPricing":
		    		Reporter.log("Calling get pricing API", true);
		    		response = plivoClient.getPricing(nextTask.getJSONObject("GetPricing").toString(), basicAuthJson);
		    		validator.validateGetPricingAPIResponse(response);
		    		break;
		    	case "ValidateSendAMessage":
		    		msgUuid = getUuidFromSendAMessageResponse(response);
		    		validator.validateAmountDeductedForAMessage(msgUuid, basicAuthJson);
		    		validator.validateAccountCashCreditDeductedForAMessage(msgUuid, basicAuthJson, cashCredit);
		    		break;
		    	case "GetCashCredit":
		    		Reporter.log("Get cash credit by calling get account details API", true);
		    		response = plivoClient.getAccountDetails(basicAuthJson);
		    		cashCredit = new JSONObject(response.responseBody).getDouble("cash_credits");
		    		break;
		    	default:
			    	throw new Exception("Invalid task");
		    	}
		    		
		}
	}
}