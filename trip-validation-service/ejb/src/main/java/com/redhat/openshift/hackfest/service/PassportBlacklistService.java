package com.redhat.openshift.hackfest.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

/**
 * Service in charge of validate passport against Blacklist service
 * 
 * @author dsancho Sep 13, 2016
 *
 */
@LocalBean
@Stateless
public class PassportBlacklistService extends AbstractRestClient {

	private static final String PASSPORT_BLACKLIST_HOST = "passport.blacklist.host";
	private static final String PASSPORT_BLACKLIST_PORT = "passport.blacklist.port";
	private static final String PASSPORT_BLACKLIST_PROTOCOL = "passport.blacklist.protocol";
	private static final String PASSPORT_BLACKLIST_BASEURI = "passport.blacklist.baseuri";

	public boolean checkBL(String passport) {

		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(System.getProperty(PASSPORT_BLACKLIST_BASEURI)).append("/").append(passport);

		ClientRequest request;
		try {
			request = new ClientRequest(
					buildURI(getSystemProperty(PASSPORT_BLACKLIST_PROTOCOL), getSystemProperty(PASSPORT_BLACKLIST_HOST),
							getSystemProperty(PASSPORT_BLACKLIST_PORT), pathBuilder.toString()));
			ClientResponse<?> clientResponse = request.get();
			if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_NOT_FOUND)) {
				return true;
			} else if (isError(clientResponse.getStatus())) {
				// TODO throw error (so far, just reject)
			}
		} catch (Exception e) {
			// TODO throw error (so far, just reject)
		}
		return false;

	}

}
