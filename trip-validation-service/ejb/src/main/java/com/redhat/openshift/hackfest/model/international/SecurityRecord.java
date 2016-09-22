package com.redhat.openshift.hackfest.model.international;

/**
 * 
 * Security record
 * 
 * @author dsancho Sep 12, 2016
 *
 */
public class SecurityRecord {
	
	private Long recordId;

	private String passportId;

	private String nationality;

	private String dateOfExpiry;
    
	private String destinationCountry;
    
	private String code;

	private String additionalInformation;

    private Long creationTimestamp;


	public SecurityRecord() {
	}


	public Long getRecordId() {
		return recordId;
	}

	public String getPassportId() {
		return passportId;
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

	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getNationality() {
		return nationality;
	}

	public String getDateOfExpiry() {
		return dateOfExpiry;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public Long getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setDateOfExpiry(String dateOfExiry) {
		this.dateOfExpiry = dateOfExiry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public void setCreationTimestamp(Long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	
}

