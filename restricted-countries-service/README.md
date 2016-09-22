restricted-countries-service: Using Multiple Java EE 6 Technologies Deployed as an EAR
==============================================================================================

The service structure has been created using `mvn archetype:generate -DarchetypeGroupId=org.jboss.archetype.eap -DarchetypeArtifactId=jboss-javaee6-webapp-ear-archetype -DarchetypeVersion=6.4.0.GA` and modified/cleaned  accordingly to perform all the business logic required by the service.

Composed by 2 different maven modules:

   - ejb: service enterprise beans
   - ear: packaging

It also contains a configuration folder which keeps the EAP configuration file to run on Openshift. 
