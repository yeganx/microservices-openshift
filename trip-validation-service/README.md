service-validation-service: Using Multiple Java EE 6 Technologies Deployed as an EAR
==============================================================================================

The service structure has been created using `mvn archetype:generate -DarchetypeGroupId=org.jboss.archetype.eap -DarchetypeArtifactId=jboss-javaee6-webapp-ear-archetype -DarchetypeVersion=6.4.0.GA` and modified/cleaned  accordingly to perform all the business logic required by the service.

Composed by 3 different maven modules:

   - web: REST service interface
   - ejb: service enterprise beans
   - ear: packaging

It also contains a configuration folder which keeps the EAP configuration file to run on Openshift. In this project, a set of Jboss system properties have been defined in order to parameterized the endpoints of the other 2 microservices involved. 

These system properties are:

 `    <system-properties> 
        <property name="passport.blacklist.host" value="${env.PASSPORT_BLACKLIST_HOST:passport-blacklist-serv}"/>  
        <property name="passport.blacklist.port" value="${env.PASSPORT_BLACKLIST_PORT:8080}"/>  
        <property name="passport.blacklist.protocol" value="${env.PASSPORT_BLACKLIST_PROTOCOL:http}"/>  
	<property name="passport.blacklist.baseuri" value="${env.PASSPORT_BLACKLIST_BASEURI:/passports/blacklist}"/> 
        <property name="international.security.host" value="${env.INTERNATIONAL_SECURITY_HOST:int-security-service}"/>
        <property name="international.security.port" value="${env.INTERNATIONAL_SECURITY_PORT:8080}"/>
        <property name="international.security.protocol" value="${env.INTERNATIONAL_SECURITY_PROTOCOL:http}"/>
        <property name="international.security.baseuri" value="${env.INTERNATIONAL_SECURITY_BASEURI:/security}"/>
    </system-properties>   `

