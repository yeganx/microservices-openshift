package com.redhat.openshift.hackfest.model;

/**
 * 
 * Security record entity
 * 
 * @author dsancho Sep 21, 2016
 *
 */
public class SecurityRecord {

	public enum Code {
		OK, REJECTED, ERROR
	}

	private Long recordId;

	private String code;

	private String additionalInformation;

	public SecurityRecord() {
	}
	
	public SecurityRecord(Long recordId, Code code, String additionalInformation) {
		this.recordId = recordId;
		this.code = code.toString();
		this.additionalInformation = additionalInformation;
	}

	public Long getRecordId() {
		return recordId;
	}

	public String getCode() {
		return code;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Override
	public String toString() {
		return "SecurityRecord [recordId=" + recordId + ", code=" + code + ", additionalInformation="
				+ additionalInformation + "]";
	}

}
