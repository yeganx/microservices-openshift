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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.openshift.hackfest.model.TripInformation;
import com.redhat.openshift.hackfest.model.ValidationResponse;
import com.redhat.openshift.hackfest.service.TripValidationService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to validate passports and trip information
 * information
 */
@Path("/trips")
@RequestScoped
public class TripResource {

	@Inject
	TripValidationService tripValidator;

	/**
	 * Ask for an old trip validation request to obtain final result
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/cases/{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValidationResponse getCase(@PathParam("id") Long id) {
		return tripValidator.getPendingCases(id);
	}

	/**
	 * Launch a new trip validation process
	 * @param tripInformation
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public ValidationResponse validateTrip(TripInformation tripInformation) {
		return tripValidator.validate(tripInformation);
	}

}
