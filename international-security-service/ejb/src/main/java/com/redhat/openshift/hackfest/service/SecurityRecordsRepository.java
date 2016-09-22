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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.redhat.openshift.hackfest.model.SecurityRecord;

@Stateless
public class SecurityRecordsRepository {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public SecurityRecord createOrUpdate(SecurityRecord securityRecord, boolean updateOnly) throws Exception {
        log.info("Adding security record: " + securityRecord);
        SecurityRecord oldSecurityRecord = find(securityRecord.getRecordId());
        if (oldSecurityRecord==null && !updateOnly) {
        	em.persist(securityRecord);
            // in order to get the recordId populated back to the entity adn check constraints
            em.flush();
            log.info("Security record added!");
            return securityRecord;
        } else if (updateOnly && oldSecurityRecord!=null){
        	oldSecurityRecord.setAdditionalInformation(securityRecord.getAdditionalInformation());
        	oldSecurityRecord.setCode(securityRecord.getCode());
//        	oldSecurityRecord.setCreationTimestamp(securityRecord.getCreationTimestamp());
//        	oldSecurityRecord.setDateOfExpiry(securityRecord.getDateOfExpiry());
//        	oldSecurityRecord.setDestinationCountry(securityRecord.getDestinationCountry());
//        	oldSecurityRecord.setNationality(securityRecord.getNationality());
//        	oldSecurityRecord.setPassportId(securityRecord.getPassportId());
        	log.info("Security record modifed");
        	return oldSecurityRecord;
        }  else {
        	log.info("Nothing to add since only updates are allowed");
        	return null;
        }
    }
    
    public SecurityRecord find(Long  recordId) throws Exception {
        log.info("Getting security record by id " + (recordId!=null?recordId:"(none)"));
        if (recordId !=null) {
	        SecurityRecord securityRecord = em.find(SecurityRecord.class, recordId);
	        log.info("Result: " + (securityRecord!=null?securityRecord:"(none)"));
	        return securityRecord;
        } else {
        	return null;
        }
    }
    
    public void remove(Long recordId)  throws Exception {
        log.info("Removing security record by id " + recordId);
        SecurityRecord securityRecord = find(recordId);
        if (securityRecord!=null) {
            em.remove(securityRecord);
            log.info("Security record removed");
        } else {
            log.info("Security record does not exist");
        }
    	
    }
}
