GDIS Test Suite
==

## Project Structure TODO:
1. Create Docker files for each back end micro service
2. Create Docker file for the front end
2. Create a Docker compose file, so everything can be linked together 

The back end project is now split into micro services. Every micro service is a Spring Boot project.

## Requirements

Back  end: 
1. JDK 1.8+
2. Eclipse with Spring Tool Suite Plug-In or Spring Tool Suite IDE. Alternatively everything can be build and run with Maven

Front end: 
1. Node.js + NPM 

API:
1. To start the API and example page, navigate to the TLDirectory gdist_test_suite, then use `docker-compose up --build --force-recreate` to start the service. 
You can then access the API itself via localhost:40042/ (index page), all other Endpoint via /records [POST], /... (see Swagger)


Since only the API micro service should be linked to the frond end, Angular is Routing request to the API micro service only (Port 8080)

## Start the Client + API micro service (project): 
1. Clone or download the repository with both frond end and back end projects
2. Import the root directory of the api micro service project in Eclipse (File/Import/Maven/Existing Maven Projects) and then Run As Java Application. 
Alternatively: cd backend/api, mvn clean install, mvn spring-boot:run. 
3. Start the front end application. cd frontend, npm install, npm run

The front end app will be accessible at localhost:4200
The micro service API, including the example API, is accessible at localhost:4200/api and localhost:8080/api. The example REST GET API is at localhost:4200/api/example
For development purposes the exporter, importer and database micro services can also be started and accessed, if their REST APIs are implemented. For example
the exporter has an example REST GET API accessible at localhost:8082/rest/example

#### Angular's Routing Settings: 
/frontend/proxy.conf.json

### Ports for each micro service 
API Port: 8080
Database Port: 8081
Exporter Port: 8082
Importer Port: 8083