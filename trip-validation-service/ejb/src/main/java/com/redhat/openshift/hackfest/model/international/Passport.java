package com.redhat.openshift.hackfest.model.international;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class for modelling input data during passport validation process
 * 
 * @author dsancho Sep 12, 2016
 *
 */
public class Passport {

	private static final String DATE_FORMAT = "dd-mm-yyyy";

	private String passportId;

	private String nationality;

	private String dateOfExpiry;

	public Passport() {
	}

	public Passport(String passportId, String nationality, String dateOfExpiry) {
		this.passportId = passportId;
		this.nationality = nationality;
		try {
			// just used to do a simple format validation
			new SimpleDateFormat(DATE_FORMAT).parse(dateOfExpiry);
			this.dateOfExpiry = dateOfExpiry;
		} catch (ParseException e) {
			this.dateOfExpiry = null;
		}
	}

	public String getPassportId() {
		return passportId;
	}

	public String getNationality() {
		return nationality;
	}

	public String getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setDateOfExpiry(String dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry; 
	}

}
