package com.plivo.test.PlivoTest;

import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.testng.Reporter;

import static com.plivo.test.Constants.Constants.*;
import com.plivo.test.PlivoClient.DefaultResponse;
import com.plivo.test.PlivoClient.PlivoAPI;

public class Validator {
	PlivoAPI plivoClient;
	public Validator() {
		plivoClient = new PlivoAPI();
	}
	public void validateListAllRentedNumbersAPIResponse(DefaultResponse response) {
		Reporter.log("Validating ListAllRentedNumbers API response", true);
		assertEquals(response.responseCode, HttpResponseCodeOk);
		//code to validate response.responseBody format
		//code to validate response.responseBody against a local list of rented numbers
	}
	public void validateSendAMessageAPIResponse(DefaultResponse response) {
		Reporter.log("Validating SendAMessage API response", true);
		assertEquals(response.responseCode, HttpResponseCodeAccepted);
		//code to validate response.responseBody format
		//code to validate response.responseBody against a local list of rented numbers
	}
	public void validateGetDetailsOfAMessageAPIResponse(DefaultResponse response) {
		Reporter.log("Validating GetDetailsOfAMessage API response", true);
		assertEquals(response.responseCode, HttpResponseCodeOk);
		//code to validate response.responseBody format
		//code to validate response.responseBody against a local list of rented numbers
	}
	public void validateGetPricingAPIResponse(DefaultResponse response) {
		Reporter.log("Validating GetPricing API response", true);
		assertEquals(response.responseCode, HttpResponseCodeOk);
		//code to validate response.responseBody format
		//code to validate response.responseBody against a local list of rented numbers
	}
	public void validateGetAccountDetailsAPIResponse(DefaultResponse response) {
		Reporter.log("Validating GetAccountDetails API response", true);
		assertEquals(response.responseCode, HttpResponseCodeOk);
		//code to validate response.responseBody format
		//code to validate response.responseBody against a local list of rented numbers
	}
	public void validateAmountDeductedForAMessage(String msgUuid, String basicAuth) throws Exception {
		Reporter.log("Validating rate at which amount was deducted for a message", true);
		DefaultResponse response1 = plivoClient.getDetailsOfAMessage(msgUuid, basicAuth);
		JSONObject messageDetails = new JSONObject(response1.responseBody);
		double rateFromMessageDetails = messageDetails.getDouble("total_rate");
		DefaultResponse response2 = plivoClient.getPricing("{\"country_iso\":\"US\"}", basicAuth);
		JSONObject pricingDetails = new JSONObject(response2.responseBody);
		double rateFromPricingDetails = pricingDetails.getJSONObject("message").getJSONObject("outbound").getDouble("rate");
		assertEquals(rateFromMessageDetails, rateFromPricingDetails);
	}
	public void validateAccountCashCreditDeductedForAMessage(String msgUuid, String basicAuth, double initialAmount) throws Exception {
		Reporter.log("Validating total amount deducted for a message", true);
		DefaultResponse response1 = plivoClient.getDetailsOfAMessage(msgUuid, basicAuth);
		JSONObject messageDetails = new JSONObject(response1.responseBody);
		double amountDeductedFromMessageDetails = messageDetails.getDouble("total_amount");
		DefaultResponse response = plivoClient.getAccountDetails(basicAuth);
		double finalCashCredit = new JSONObject(response.responseBody).getDouble("cash_credits");
		double amountDeductedFromAccountDetails = initialAmount - finalCashCredit;
		assertEquals(amountDeductedFromMessageDetails, amountDeductedFromAccountDetails);
	}

}