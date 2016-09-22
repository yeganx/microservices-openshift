package com.redhat.openshift.hackfest.service;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.redhat.openshift.hackfest.model.SecurityRecord;

/**
 * Service in charge of validate passport against Blacklist service
 * @author dsancho Sep 13, 2016
 *
 */
@LocalBean
@Stateless
public class InternationalSecurityNotificationService extends AbstractRestClient {

	private static final String INT_SECURITY_HOST = "international.security.host";
	private static final String INT_SECURITY_PORT = "international.security.port";
	private static final String INT_SECURITY_PROTOCOL = "international.security.protocol";
	private static final String INT_SECURITY_BASEURI = "international.security.baseuri";
	
	private static final String RECORDS_PATH = "/records/";
	
	@Inject
	Logger log;
	
	public boolean notifySecurityRecord(SecurityRecord securityRecord) {
		log.info("Notifying international security service record " + securityRecord.getRecordId());
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(System.getProperty(INT_SECURITY_BASEURI)).append(RECORDS_PATH).append(securityRecord.getRecordId());
		ClientRequest request;
		try {
			request = buildRequest(pathBuilder.toString());
			request.body(MediaType.APPLICATION_JSON_TYPE, securityRecord);
			ClientResponse<SecurityRecord> clientResponse = request.put(SecurityRecord.class);
			if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_OK)) {
				log.info("Returned ok from notification");
				return true;
			} else {
				log.severe("Returned error status from notification");
				return false;
			}
		} catch (Exception e) {
			// TODO throw error (so far, just reject)
			log.severe("Error in notification: " + e.getMessage()!=null?e.getMessage():"(none)");
			return false;
		}
	}
	
	private ClientRequest buildRequest(String path) throws URISyntaxException {
		return new ClientRequest(
				buildURI(getSystemProperty(INT_SECURITY_PROTOCOL), getSystemProperty(INT_SECURITY_HOST),
						getSystemProperty(INT_SECURITY_PORT), path)).accept(MediaType.APPLICATION_JSON); 
	}
}
