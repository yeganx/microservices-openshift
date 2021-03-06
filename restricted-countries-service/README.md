restricted-countries-service: Using Multiple Java EE 6 Technologies Deployed as an EAR
==============================================================================================

The service structure has been created using `mvn archetype:generate -DarchetypeGroupId=org.jboss.archetype.eap -DarchetypeArtifactId=jboss-javaee6-webapp-ear-archetype -DarchetypeVersion=6.4.0.GA` and modified/cleaned  accordingly to perform all the business logic required by the service.

Composed by 2 different maven modules:

   - ejb: service enterprise beans
   - ear: packaging

It also contains a configuration folder which keeps the EAP configuration file to run on Openshift. Main configuration changes:

- System properties:

```
<system-properties>  
	<property name="international.security.host" value="${env.INTERNATIONAL_SECURITY_HOST:int-security-service}"/>
	<property name="international.security.port" value="${env.INTERNATIONAL_SECURITY_PORT:8080}"/>
	<property name="international.security.protocol" value="${env.INTERNATIONAL_SECURITY_PROTOCOL:http}"/>
	<property name="international.security.baseuri" value="${env.INTERNATIONAL_SECURITY_BASEURI:/security}"/>
</system-properties>  
```
- Resource adapter:

```
<subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
        <resource-adapters>
            <resource-adapter id="activemq-rar.rar">
                <archive>
                    activemq-rar.rar
                </archive>
                <transaction-support>XATransaction</transaction-support>
                <config-property name="ServerUrl">
                    tcp://${env.AMQ_SERVICE_HOST}-tcp:61616?jms.rmIdFromConnectionId=true
                </config-property>
                <config-property name="UserName">
                    ${env.AMQ_USER}
                </config-property>
                <config-property name="Password">
                    ${env.AMQ_PASSWORD}
                </config-property>
                <connection-definitions>
                    <connection-definition class-name="org.apache.activemq.ra.ActiveMQManagedConnectionFactory" jndi-name="java:/ConnectionFactory" enabled="true" pool-name="ConnectionFactory">
                        <xa-pool>
                            <min-pool-size>1</min-pool-size>
                            <max-pool-size>20</max-pool-size>
                            <prefill>false</prefill>
                            <is-same-rm-override>false</is-same-rm-override>
                        </xa-pool>
                        <recovery>
                            <recover-credential>
                                <user-name>${env.AMQ_USER}</user-name>
                                <password>${env.AMQ_PASSWORD}</password>
                            </recover-credential>
                        </recovery>
                    </connection-definition>
                </connection-definitions>
                <admin-objects>
                    <admin-object class-name="org.apache.activemq.command.ActiveMQQueue" jndi-name="java:/jms/queue/RestrictedCountriesQueue" use-java-context="true" pool-name="RestrictedCountriesQueue">
                        <config-property name="PhysicalName">
                            RestrictedCountriesQueue
                        </config-property>
                    </admin-object>
                </admin-objects>
            </resource-adapter>
        </resource-adapters>
</subsystem>
```

- Ejb => mdbs

```
<mdb>
	<resource-adapter-ref resource-adapter-name="activemq-rar.rar"/>
	<bean-instance-pool-ref pool-name="mdb-strict-max-pool"/>
</mdb>
```
