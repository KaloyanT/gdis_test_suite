# GDIS Test Suite

## Requirements

Back-End:

1. JDK 1.8+
2. Maven for building the JAR files

Front-End:

1. Node.js + NPM

API:

1. Python, PIP and FLASK

Swagger Documentation can be found here (or in the API subfolder):
<https://app.swaggerhub.com/apis/BracketJohn/smart_insurance_api/1.0.0>

Other:

1. Docker

## Start the project/system (Front-End Client + API + Back-End)

Currently the project is configured to run on one machine with every component running inside its own Docker Container.

1. Clone or download the repository with both frond end and back end projects
2. Navigate to the database micro-service directory and write in a terminal/command prompt: "mvn clean package -DskipTests docker:build"
3. Navigate to the importer micro-service directory and write in a terminal/command prompt: "mvn clean package -DskipTests docker:build"
4. Navigate to the exporter micro-service directory and write in a terminal/command prompt: "mvn clean package -DskipTests docker:build"
5. Navigate to the root directory of the project and write in a terminal/command prompt: "docker-compose up --build --force-recreate"
6. The Front-End Client should be accessible at <http://localhost:8080>

### Ports for each micro-service, including Front End and MySQL Database

Front End Port: 8080
API Port: 40042
Database Port: 8081
Exporter Port: 8082
Importer Port: 8083
MySQL Port: 3306