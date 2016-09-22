package com.redhat.openshift.hackfest.consumer;

import java.util.Random;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.redhat.openshift.hackfest.model.SecurityRecord;
import com.redhat.openshift.hackfest.service.InternationalSecurityNotificationService;

@MessageDriven(name = "RestrictedQueueMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "RestrictedCountriesQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class RestrictedQueueMDB implements MessageListener {

	@Inject
	Logger log;
	
	@Inject
	InternationalSecurityNotificationService internationalSecurityService;

	@Override
	public void onMessage(Message message) {
		try {
			// FIXME extract keys to constants
			String destinationCountry = ((MapMessage) message).getString("destinationCountry");
			Long recordId = ((MapMessage) message).getLong("recordId");
			// init security record
			SecurityRecord sr = new SecurityRecord(recordId, SecurityRecord.Code.OK,
					"Passport accepted in destination country");
			log.info("Message will be process using restricted procedure for destination " + destinationCountry);
			// sleep randomly for a while, just for this demonstration
			int sleepInSeconds = new Random().nextInt(20);
			log.info("Sleeping " + sleepInSeconds + " seconds");
			try {
				Thread.sleep(sleepInSeconds*1000);
			} catch (InterruptedException e) {
			}
			if (!new Random().nextBoolean()) {
				sr.setCode(SecurityRecord.Code.REJECTED.toString());
				sr.setAdditionalInformation("Passport is not valid in destination country");
			}
			// notify
			log.info("Notifying security record: " + sr);
			if (internationalSecurityService.notifySecurityRecord(sr)) {
				log.info("Notification succed");
			} else {
				log.severe("Could not notify international security about this record");
			}
			
		} catch (JMSException e) {
			log.severe(
					"Impossible to notify back international security service: could not obtain recordId/destinationCountry from message");
		}
	}

}
