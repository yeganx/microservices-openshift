package com.redhat.openshift.hackfest.service;

import java.net.URISyntaxException;

import org.apache.http.HttpStatus;

/**
 * Abstract class to be extended by concrete REST clients
 * @author dsancho Sep 13, 2016
 *
 */
public abstract class AbstractRestClient {

	protected String buildURI(String protocol, String host, String port, String path) throws URISyntaxException {
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(protocol).append("://");
		uriBuilder.append(host).append(":");
		uriBuilder.append(Integer.valueOf(port));
		uriBuilder.append(path);
		return uriBuilder.toString();
	}

	protected boolean isError(int responseStatus) {
		if (responseStatus >= HttpStatus.SC_BAD_REQUEST) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean checkStatus(int responseStatus, int status) {
		if (responseStatus == status) {
			return true;
		} else {
			return false;
		}
	}

	protected String getSystemProperty(String key) {
		return System.getProperty(key);
	}

}
