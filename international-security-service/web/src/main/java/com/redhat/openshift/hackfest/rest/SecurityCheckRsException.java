package com.redhat.openshift.hackfest.rest;

public class SecurityCheckRsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5859959132150646714L;

	private int code;
	
	private String message;
	
	public SecurityCheckRsException() {
		this.code = 500;
		this.message = "Internal server error";
	}

	public SecurityCheckRsException(String message, int code) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
