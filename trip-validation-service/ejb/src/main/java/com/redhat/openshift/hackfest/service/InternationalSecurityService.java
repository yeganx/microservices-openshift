package com.redhat.openshift.hackfest.service;

import java.net.URISyntaxException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.redhat.openshift.hackfest.model.TripInformation;
import com.redhat.openshift.hackfest.model.ValidationResponse;
import com.redhat.openshift.hackfest.model.ValidationResponse.ValidationCode;
import com.redhat.openshift.hackfest.model.international.Passport;
import com.redhat.openshift.hackfest.model.international.SecurityRecord;

/**
 * Service in charge of validate passport against Blacklist service
 * @author dsancho Sep 13, 2016
 *
 */
@LocalBean
@Stateless
public class InternationalSecurityService extends AbstractRestClient {

	private static final String INT_SECURITY_HOST = "international.security.host";
	private static final String INT_SECURITY_PORT = "international.security.port";
	private static final String INT_SECURITY_PROTOCOL = "international.security.protocol";
	private static final String INT_SECURITY_BASEURI = "international.security.baseuri";
	
	private static final String RECORDS_PATH = "/records/";

	public ValidationResponse checkTrip(TripInformation trip) {
		ValidationResponse vr = initializeErrorResponse(ValidationCode.ERROR, "Unexpected error");
		
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(System.getProperty(INT_SECURITY_BASEURI)).append("/").append(trip.getDestinationCountry());

		ClientRequest request;
		try {
			request = buildRequest(pathBuilder.toString());
			Passport passport = new Passport(trip.getPassport(), trip.getNationality(), trip.getDateOfExpiry());
			request.body(MediaType.APPLICATION_JSON_TYPE, passport);
			ClientResponse<SecurityRecord> clientResponse = request.post(SecurityRecord.class);
			if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_OK)) {
				return toValidationResponse(clientResponse.getEntity());
			} else if (isError(clientResponse.getStatus())) {
				return vr;
			}
		} catch (Exception e) {
			// TODO throw error (so far, just reject)
			return vr;
		}
		return vr;
	}
	
	public ValidationResponse getPendingCases(Long caseId) {
		ValidationResponse vr = initializeErrorResponse(ValidationCode.ERROR, "Unexpected error");

		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(System.getProperty(INT_SECURITY_BASEURI)).append(RECORDS_PATH).append(caseId);

		ClientRequest request;
		try {
			request = buildRequest(pathBuilder.toString());
			ClientResponse<SecurityRecord> clientResponse = request.get(SecurityRecord.class);
			if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_OK)) {
				return toValidationResponse(clientResponse.getEntity());
			} else if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_NOT_FOUND)) {
				return vr = initializeErrorResponse(ValidationCode.NOT_FOUND, "Case not found");
			} else if (isError(clientResponse.getStatus())) {
				return vr;
			}
		} catch (Exception e) {
			// TODO throw error (so far, just reject)
			return vr;
		}
		return vr;
	}
	
	private ClientRequest buildRequest(String path) throws URISyntaxException {
		return new ClientRequest(
				buildURI(getSystemProperty(INT_SECURITY_PROTOCOL), getSystemProperty(INT_SECURITY_HOST),
						getSystemProperty(INT_SECURITY_PORT), path)).accept(MediaType.APPLICATION_JSON); 
	}
	
	private ValidationResponse toValidationResponse(SecurityRecord securityRecord) {
		ValidationResponse vr = new ValidationResponse(ValidationCode.valueOf(securityRecord.getCode()));
		vr.setAdditionalInformation(securityRecord.getAdditionalInformation());
		vr.setCaseId(securityRecord.getRecordId());
		return vr;
	}
	
	private ValidationResponse initializeErrorResponse(ValidationCode code, String additionalInfo) {
		ValidationResponse vr = new ValidationResponse(code);
		vr.setAdditionalInformation(additionalInfo);
		return vr;
	}

}
