package com.redhat.openshift.hackfest.consumer;

import java.util.Random;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.redhat.openshift.hackfest.model.Passport;
import com.redhat.openshift.hackfest.model.SecurityRecord;
import com.redhat.openshift.hackfest.service.InternationalSecurityNotificationService;

@MessageDriven(name = "NormalQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "NormalProcedureQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class NormalQueueMDB implements MessageListener {

	@Inject
	Logger log;
	
	@Inject
	InternationalSecurityNotificationService internationalSecurityService;
	
	@Override
	public void onMessage(Message message) {
		// init security record
		SecurityRecord sr = new SecurityRecord();
		try {
			// FIXME extract keys to constants
			String passportId = ((MapMessage) message).getString("passport");
			String nationality = ((MapMessage) message).getString("nationality");
			String dateOfExpiry = ((MapMessage) message).getString("dateOfExpiry");
			Passport passport = new Passport(passportId, nationality, dateOfExpiry);
			String destinationCountry = ((MapMessage) message).getString("destinationCountry");
			Long recordId = ((MapMessage) message).getLong("recordId");
			sr.setRecordId(recordId);
			log.info("Message will be process using normal procedure for destination " +destinationCountry);
			// sleep randomly for a while, just for this demonstration
			int sleepInSeconds = new Random().nextInt(20);
			log.info("Sleeping " + sleepInSeconds + " seconds");
			Thread.sleep(sleepInSeconds*1000);
			int monthsOfValidalityRequired = getMonths(destinationCountry);
			if (!passport.checkDateOfExpriry(monthsOfValidalityRequired)) {
				sr.setCode(SecurityRecord.Code.REJECTED.toString());
				sr.setAdditionalInformation("Passport date of expiry is not valid in destination country");
			} else {
				sr.setCode(SecurityRecord.Code.OK.toString());
				sr.setAdditionalInformation("Passport accepted in destination country");
			}
			log.info(sr.getAdditionalInformation());

		} catch (Exception e) {
			sr.setCode(SecurityRecord.Code.ERROR.toString());
			sr.setAdditionalInformation("Validation error: Normal procedure could not be performed");
			log.severe(sr.getAdditionalInformation());
		} 
		try {
			internationalSecurityService.notifySecurityRecord(sr);
		} catch (Exception e1) {
			log.severe("Could not update record");
		}
	}

	private int getMonths(String destinationCountry) {
		// very simple algorithm for the demonstration
		return destinationCountry.length();
	}

}
