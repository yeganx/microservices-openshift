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
package com.redhat.openshift.hackfest.service;

import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.redhat.openshift.hackfest.model.TripInformation;
import com.redhat.openshift.hackfest.model.ValidationResponse;
import com.redhat.openshift.hackfest.model.ValidationResponse.ValidationCode;

@LocalBean
@Stateless
public class TripValidationService {

	@Inject
	private Logger log;
	
	@Inject
	private PassportBlacklistService passportBlService;
	
	@Inject
	private InternationalSecurityService interSecurityService;

	public ValidationResponse validate(TripInformation tripInformation) {
		log.info("Validation trip request: " + tripInformation);
		ValidationResponse validationResponse = new ValidationResponse(ValidationCode.OK);
		// validate passport in blacklist
		if (passportBlService.checkBL(tripInformation.getPassport())) {
			validationResponse = checkTripInformation(tripInformation);
		} else {
			validationResponse = new ValidationResponse(ValidationCode.REJECTED);
			validationResponse.setAdditionalInformation("Passport is blacklisted");
		}
		log.info("Validation trip response: " + validationResponse);
		return validationResponse;

	}

	public ValidationResponse getPendingCases(Long caseId) {
		log.info("Getting caseId: " + caseId);
		ValidationResponse validationResponse =  interSecurityService.getPendingCases(caseId);
		log.info("Case response: " + validationResponse);
		return validationResponse;
	}

	private ValidationResponse checkTripInformation(TripInformation tripInformation) {
		return interSecurityService.checkTrip(tripInformation);
	}

}
