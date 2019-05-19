package com.faujor.entity.websocket;

public class AricResponse {
	private String sdata1;
	private String actionUrl;
	private String responseMessage;
	private String type;

	public AricResponse(String sdata1, String actionUrl, String responseMessage, String type) {
		this.sdata1 = sdata1;
		this.actionUrl = actionUrl;
		this.responseMessage = responseMessage;
		this.type = type;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public String getSdata1() {
		return sdata1;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public String getType() {
		return type;
	}

}
