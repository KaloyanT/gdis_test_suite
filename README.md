GDIS Test Suite
==

Spring Boot Project (back end) + Angular 2 Project (front end)

At the moment our project basically consists of two individual projects. Maven is not used to connect
both of them. It is also possible to connect both projects with Maven, so Spring Boot can serve 
the front end files. For now however, the front end will be updated only through the APIs and the front end will route the requests to the back end. 

##Requirements

1. JDK 1.8+
2. Eclipse with Spring Tool Suite Plug-In or Spring Tool Suite IDE. Alternatively everything can be build and run with Maven
3. NodeJS + NPM 

##Start the project: 
1. Clone or download the repository with both projects
2. Import the root directory of the back end project in Eclipse (File/Import/Maven/Existing Maven Projects) and then Run As Java Application. 
Alternatively: cd backend, mvn clean install, mvn spring-boot:run. 
3. Start the front end application. cd frontend, npm install, npm run

The front end app will be accessible at localhost:4200
The back end APIs, including the example APIs, are accessible at localhost:4200/api/ and localhost:8080/api/. The example REST GET API is at localhost:4200/api/example

###Angular's Routing Settings: 
/frontend/proxy.conf.json

[Spring Boot + Angular with Maven] https://dzone.com/articles/angular-2-and-spring-boot-development-environment

[Spring Boot + Angular without Maven] https://blog.jdriven.com/2016/12/angular2-spring-boot-getting-started/