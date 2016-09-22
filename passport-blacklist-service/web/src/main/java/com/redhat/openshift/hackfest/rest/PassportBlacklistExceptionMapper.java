package com.redhat.openshift.hackfest.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper to set Response codes when PassportBlacklistRsException is thrown by the service
 * @author dsancho Sep 13, 2016
 *
 */
@Provider
public class PassportBlacklistExceptionMapper  implements ExceptionMapper<PassportBlacklistRsException>{

	@Override
	public Response toResponse(PassportBlacklistRsException exception) {
		return Response.status(exception.getCode()).entity(exception.getMessage()).build();  
	}

}
