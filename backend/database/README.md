Database Micro Service
==

## Connect MySQL to the project: 
Currently the project contains database configurations in the src/main/resources/application.properties file for running the project on your local computer/environment. 
src/main/resources/application-docker.properties is also available for running the project inside a Docker Container
In order to run the MySQL Database on your computer, you might need to change some of the settings. 

### MySQL From Local Installation: 
Set the spring.datasource.url to jdbc:mysql://localhost:3306/YOURDB_NAME?autoReconnect=true&useSSL=false and the username and password to whatever you set them during the installation of MySQL

### MySQL From Docker Container: 
You can leave the options the way they are, if you create a MySQL Docker Container with a database name "gdis_db" and the same username and password (see the application.properties file). 

### Create a MySQL Docker Container

[Official Docker MySQL Server container](https://hub.docker.com/r/mysql/mysql-server/)

Follow the guide and use the following command to create and run the Docker Container: 

docker run --name gdis_db -e MYSQL_ROOT_PASSWORD=abcd1234 MYSQL_USER=gdis_db_user MYSQL_PASSWORD=abcd124 MYSQL_DATABASE=gdis_db -p 3306:3306 -d mysql/mysql-server:5.7

Alternatively the Kitematic GUI can be used. You just have to change the name of the container (not actually necessary), the listed environment variables (after -e flag) have to be set under Settings/General and also the port has to be set. 


## Building the project: 
The project is currently set up to run only inside a Docker Container. See the README File in the root directory of the project for more information. 
