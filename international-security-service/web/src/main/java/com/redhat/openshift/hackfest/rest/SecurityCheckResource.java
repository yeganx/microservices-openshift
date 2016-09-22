/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.openshift.hackfest.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.openshift.hackfest.model.Passport;
import com.redhat.openshift.hackfest.model.SecurityRecord;
import com.redhat.openshift.hackfest.service.SecurityProcedureService;
import com.redhat.openshift.hackfest.service.SecurityRecordsRepository;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/security")
@RequestScoped
public class SecurityCheckResource {

	@Inject
	SecurityProcedureService secProcService;

	@Inject
	SecurityRecordsRepository recordsRepository;

	@POST
	@Path("/{country}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SecurityRecord checkPassportInDestinationCountry(@PathParam("country") String country, Passport passport)
			throws SecurityCheckRsException {
		try {
			return secProcService.check(country, passport);
		} catch (Exception e) {
			throw new SecurityCheckRsException("Unexpected error", 500);
		}
	}

	@GET
	@Path("/records/{recordId}")
	@Produces(MediaType.APPLICATION_JSON)
	public SecurityRecord getRecord(@PathParam("recordId") String recordId) throws SecurityCheckRsException {
		SecurityRecord securityRecord = null;
		try {
			securityRecord = recordsRepository.find(Long.valueOf(recordId));
		} catch (Exception e) {
			throw new SecurityCheckRsException("Unexpected error", 500);
		}
		if (securityRecord == null) {
			throw new SecurityCheckRsException("Record not found", 404);
		}
		return securityRecord;

	}

	@PUT
	@Path("/records/{recordId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SecurityRecord modifyRecord(@PathParam("recordId") String recordId, SecurityRecord securityRecord)
			throws SecurityCheckRsException {
		SecurityRecord secRecord = null;
		if (recordId != null && securityRecord != null && securityRecord.getRecordId() != null
				&& recordId.equals(String.valueOf(securityRecord.getRecordId()))) {
			try {

				secRecord = recordsRepository.createOrUpdate(securityRecord, true);
			} catch (Exception e) {
				throw new SecurityCheckRsException("Unexpected error", 500);
			}
		} else {
			throw new SecurityCheckRsException("Invalid input parameters", 400);
		}
		if (secRecord != null) {
			return secRecord;
		} else {
			throw new SecurityCheckRsException("Record not found", 404);
		}
	}

	@DELETE
	@Path("/records/{recordId}")
	public void removeRecord(@PathParam("recordId") String recordId) throws SecurityCheckRsException {
		try {
			recordsRepository.remove(Long.valueOf(recordId));
		} catch (Exception e) {
			throw new SecurityCheckRsException("Unexpected error", 500);
		}
	}

}
