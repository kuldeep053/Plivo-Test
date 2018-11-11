package com.plivo.test.PlivoClient;

public class DefaultResponse {
	public int responseCode;
	public String responseBody;
	public DefaultResponse(int responseCode, String responseBody) {
		this.responseCode = responseCode;
		this.responseBody = responseBody;
	}
	public String toString() {
		return new String("Response: {code=" + this.responseCode + ", message:" + this.responseBody + "}");
	}
}