passport-blacklist-service: Using Multiple Java EE 6 Technologies Deployed as an EAR
==============================================================================================

The service structure has been created using `mvn archetype:generate -DarchetypeGroupId=org.jboss.archetype.eap -DarchetypeArtifactId=jboss-javaee6-webapp-ear-archetype -DarchetypeVersion=6.4.0.GA` and modified/cleaned  accordingly to perform all the business logic required by the service.

Composed by 3 different maven modules:

   - web: REST service interface
   - ejb: service enterprise beans
   - ear: packaging

It also contains a configuration folder which keeps the EAP configuration file to run on Openshift. Main configuration changes:

- Datasource definition:


```
<subsystem xmlns="urn:jboss:domain:datasources:1.2">
    <datasources>
        <datasource jndi-name="java:jboss/datasources/BlacklistDS" enabled="true" use-java-context="true" pool-name="BlacklistDS">
            <connection-url>jdbc:mysql://${env.DATABASE_SERVICE_HOST}:${env.DATABASE_SERVICE_PORT:3306}/${env.MYSQL_DATABASE:blacklist}</connection-url>
            <driver>mysql</driver>
            <security>
              <user-name>${env.MYSQL_USER:passport}</user-name>
              <password>${env.MYSQL_PASSWORD}</password>
            </security>
        </datasource>
        <drivers>
            <driver name="mysql" module="com.mysql">
                <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
            </driver>
        </drivers>
    </datasources>
</subsystem>
```
