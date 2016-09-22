package com.redhat.openshift.hackfest.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public boolean validate() {
		// very simple and stupid validation
		if (getPassportId() != null && !"".equals(getPassportId()) && getDateOfExpiry() != null && !"".equals(getDateOfExpiry())
				&& getNationality() != null && !"".equals(getNationality())) {
			try {
				// just used to do a simple format validation
				new SimpleDateFormat(DATE_FORMAT).parse(getDateOfExpiry());
			} catch (ParseException e) {
				return false;
			}			
			return true;

		} else  {
			return false;
		}
	}
	
	public boolean checkDateOfExpriry() {
		try {
			Date expireDate = new SimpleDateFormat(DATE_FORMAT).parse(getDateOfExpiry());
			return expireDate.after(new Date());
		} catch (ParseException e) {
			// by default, errors to false
			return false;
		}
	}
	
	public boolean checkDateOfExpriry(int monthsRequired) {
		try {
			Date expireDate = new SimpleDateFormat(DATE_FORMAT).parse(getDateOfExpiry());
			long futureTime = System.currentTimeMillis() + (monthsRequired * 7200 *1000);
			return expireDate.after(new Date(futureTime));
		} catch (ParseException e) {
			// by default, errors to false
			return false;
		}
	}

	@Override
	public String toString() {
		return "Passport [passportId=" + passportId + ", nationality=" + nationality + ", dateOfExpiry=" + dateOfExpiry + "]";
	}

}
