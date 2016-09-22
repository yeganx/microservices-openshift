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
package com.redhat.openshift.hackfest.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
/**
 * Entity that represents an entry in the Passport blacklist
 * @author dsancho Sep 13, 2016
 *
 */
@Entity
@Table(name = "Blacklist")
public class PassportBlacklistEntry implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @Size(min = 1, max = 25)
    //@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String passport;
    
    public PassportBlacklistEntry() {
    }
    
    public PassportBlacklistEntry(String passport) {
    	this.passport = passport;
    }

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	@Override
	public String toString() {
		return "PassportBlacklistEntry [passport=" + passport + "]";
	}
    
}
