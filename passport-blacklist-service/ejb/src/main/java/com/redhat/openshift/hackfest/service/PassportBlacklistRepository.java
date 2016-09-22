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

import com.redhat.openshift.hackfest.model.PassportBlacklistEntry;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class PassportBlacklistRepository {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void add(String passport) throws Exception {
        log.info("Adding passport to blacklist " + passport);
        if (find(passport)==null) {
        	em.persist(new PassportBlacklistEntry(passport));
            log.info("Passport added!");
        } else {
        	log.info("Passport was already included in blacklist");
        }
    }
    
    public PassportBlacklistEntry find(String  passport) throws Exception {
        log.info("Getting passport from blacklist " + passport);
        PassportBlacklistEntry passportEntry = em.find(PassportBlacklistEntry.class, passport);
        log.info("Result: " + (passportEntry!=null?passportEntry:"(none)"));
        return passportEntry;
    }
    
    public void remove(String passport)  throws Exception {
        log.info("Removing passport from blacklist " + passport);
        PassportBlacklistEntry passportEntry = find(passport);
        if (passportEntry!=null) {
            em.remove(passportEntry);
            log.info("Passport removed from blacklist");
        } else {
            log.info("Passport does not exist in blacklist");
        }
    	
    }
}
