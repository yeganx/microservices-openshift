package com.redhat.openshift.hackfest.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.redhat.openshift.hackfest.model.Passport;

/**
 * 
 * Security record entity
 * 
 * @author dsancho Sep 12, 2016
 *
 */
@Entity
@Table(name = "Security_records")
public class SecurityRecord {
	
	public enum Code {OK, REJECTED, PENDING, ERROR}
	
    @Id
    @GeneratedValue
	private Long recordId;

    @NotNull
    @Size(min = 1, max = 25)
	private String passportId;
    
    @NotNull
    @Size(min = 1, max = 64)
	private String nationality;
    
    @NotNull
    @Size(min = 1, max = 10)
	private String dateOfExpiry;
    
    @NotNull
    @Size(min = 1, max = 64)
	private String destinationCountry;
    

    @Size(min = 1, max = 10)
    @NotNull
	private String code;
	
    @Size(min = 0, max = 512)
	private String additionalInformation;
    
    @NotNull
    private Timestamp creationTimestamp;

	public SecurityRecord() {
		this.creationTimestamp = new Timestamp(System.currentTimeMillis());
	}
	

	public SecurityRecord(Passport passport, String destinationCountry,
			Code code, String additionalInformation) {
		this();
		this.passportId = passport.getPassportId();
		this.nationality = passport.getNationality();
		this.dateOfExpiry = passport.getDateOfExpiry();
		this.destinationCountry = destinationCountry;
		this.code = code.toString();
		this.additionalInformation = additionalInformation;
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

	public Timestamp getCreationTimestamp() {
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

	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Override
	public String toString() {
		return "SecurityRecord [recordId=" + (recordId!=null?recordId:"(none set)") + ", passportId=" + passportId + ", nationality=" + nationality
				+ ", dateOfExiry=" + dateOfExpiry + ", destinationCountry=" + destinationCountry + ", code=" + code
				+ ", additionalInformation=" + additionalInformation + ", creationTimestamp=" + creationTimestamp + "]";
	}
	
	
	
}

