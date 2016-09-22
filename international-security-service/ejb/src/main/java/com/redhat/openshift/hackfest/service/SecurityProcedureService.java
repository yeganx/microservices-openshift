package com.redhat.openshift.hackfest.service;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.redhat.openshift.hackfest.model.Passport;
import com.redhat.openshift.hackfest.model.SecurityRecord;

@Stateless
public class SecurityProcedureService {

	@Inject
	Logger log;
	
	@Inject
	SecurityRecordsRepository recordsRepository;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory qcf;

	@Resource(mappedName = "java:/jms/queue/RestrictedCountriesQueue")
	private Queue restrictedCountriesQueue;

	@Resource(mappedName = "java:/jms/queue/NormalProcedureQueue")
	private Queue normalProcedureQueue;
	
	// just for this demonstration, restricted countries are those starting by an "a" or finishing by an "n"
	private static final Pattern RESTRICTED_COUNTRIES_PATTERN = Pattern.compile("(^[Aa].*)|(.*[nN]$)");
	
	public SecurityRecord check(String country, Passport passport) throws Exception {
		log.info("Checking passport " + passport.getPassportId() + " for country " + country);
		if (country != null && !"".equals(country) && passport!=null && passport.validate()) {
			// if countries are the same, simple date validation
			if (country.equalsIgnoreCase(passport.getNationality())) {
				String message = "";
				SecurityRecord securityRecord = new SecurityRecord(passport, country, SecurityRecord.Code.OK, message);
				if (!passport.checkDateOfExpriry()) {
					message = "Passport date of expiry is not valid anymore";
					securityRecord = new SecurityRecord(passport, country, SecurityRecord.Code.REJECTED, message);
					log.severe(message);
				} else {
					log.info(message);
				}
				return recordsRepository.createOrUpdate(securityRecord, false);
			} else {
				// other cases, send to queues
				Matcher matcher = RESTRICTED_COUNTRIES_PATTERN.matcher(country);
				boolean sent = true;
				String suffix = "";
				// init record as PENDING
				SecurityRecord securityRecord = new SecurityRecord(passport, country, SecurityRecord.Code.PENDING, "");
				securityRecord = recordsRepository.createOrUpdate(securityRecord, false);
				if (matcher.matches())  {
					sent = sendMessagesToQueue(passport, country, securityRecord.getRecordId(), restrictedCountriesQueue);
					suffix = " Restricted countries";
				} else {
					sent = sendMessagesToQueue(passport, country, securityRecord.getRecordId(), normalProcedureQueue);
					suffix = " Normal procedure";
				}
				String message = "Validation pending: ";
				if (!sent) {
					message = "Validation error: ";
					securityRecord.setCode(SecurityRecord.Code.ERROR.toString());
				}
				securityRecord.setAdditionalInformation(message + suffix);
				securityRecord = recordsRepository.createOrUpdate(securityRecord, true);
				log.info(securityRecord.getAdditionalInformation());
				return securityRecord;
			}
		} else {
			String message = "Required information is missing";
			SecurityRecord securityRecord = new SecurityRecord(passport, country, SecurityRecord.Code.ERROR, message);
			recordsRepository.createOrUpdate(securityRecord, false);
			log.severe(message);
			return securityRecord;
		}
		
	}

	private boolean sendMessagesToQueue(Passport passport, String destinationCountry, Long recordId, Queue queue) {
		Connection connection = null;
		Session session = null;
		try {
			connection = qcf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = null;
			messageProducer = session.createProducer(queue);
			// FIXME extract keys to constants
			MapMessage mapMessage = session.createMapMessage();
			mapMessage.setString("passport", passport.getPassportId());
			mapMessage.setString("nationality", passport.getNationality());
			mapMessage.setString("dateOfExpiry", passport.getDateOfExpiry());
			mapMessage.setString("destinationCountry", destinationCountry);
			mapMessage.setLong("recordId", recordId);

			messageProducer.send(mapMessage);
			log.info("Message '" + mapMessage + "' sent");
			return true;
		} catch (JMSException e) {
			log.severe("Error sending message:" + e.getMessage());
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
					if (session != null) {
						session.close();
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
