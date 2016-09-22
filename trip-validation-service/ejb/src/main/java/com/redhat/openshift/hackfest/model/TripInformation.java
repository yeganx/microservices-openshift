package com.redhat.openshift.hackfest.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class for modelling input data during passport validation process
 * 
 * @author dsancho Sep 12, 2016
 *
 */
public class TripInformation {
	
	private static final String DATE_FORMAT = "dd-mm-yyyy";
	
	private String passport;
	
	private String nationality;
	
	private String dateOfExpiry;
	
	private String destinationCountry;
	
	public TripInformation() {
	}

	public TripInformation(String passport, String nationality, String dateOfExpiry, String destinationCountry) {
		this.passport = passport;
		this.nationality = nationality;
		try {
			// just used to do a simple format validation
			new SimpleDateFormat(DATE_FORMAT).parse(dateOfExpiry);
			this.dateOfExpiry = dateOfExpiry;
		} catch (ParseException e) {
			this.dateOfExpiry = null;
		}
		this.destinationCountry = destinationCountry;
	}

	public String getPassport() {
		return passport;
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

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setDateOfExpiry(String dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	@Override
	public String toString() {
		return "TripInformation [passport=" + passport + ", nationality=" + nationality + ", dateOfExpiry="
				+ dateOfExpiry + ", destinationCountry=" + destinationCountry + "]";
	}

}
