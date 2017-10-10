# user-registration

Simple example how to make web applicaiton to store/display some dummy data.

# Technologies:
Java 1.8
Spring Boot
JPA / Hibernate
H2
Junit
Lombok
Maven
Angular
Twitter Bootstrap

# Generate WAR file
mvn clean package

# Running
1) copy war file into Tomcat's webapp folder and run: sh catalina.sh run
2) from IDE run Application class as usual main class

Visit http://localhost:8080/user-registration/

# Application structure
src/main/java - contains all server-side related code
src/main/frontend - contains all frontend related code
src/main/resources - contains different application properties

# Requirements for local run
Lombok plugin (in case of running it from IDE)
NodeJS
npm
copyfiles (npm uses it)
Tomcat

# Build application
Server side
Maven: mvn clean package

Frontend side
NPM: npm run build (will copy optimized sources from 'frontend' folder into 'src/main/resources/static')

NOTE: 
1) to run frontend from command line navigate to 'frontend' folder and execute 'npm start' in terminal
2) to data stored in in-memory database visit following url: http://localhost:8080/user-registration/console
(there is no password, so just hit connect button)
