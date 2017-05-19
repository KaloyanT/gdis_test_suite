Database Micro Service
==

## Connect MySQL to the project: 
Currently the project contains database configurations in the src/main/resources/application.properties file. 
In order to run the MySQL Database on your computer, you might need to change some of the settings. 

### MySQL From Local Installation: 
Set the spring.datasource.url to jdbc:mysql://localhost:3306/YOURDB_NAME?autoReconnect=true&useSSL=false and the username and password to whatever you set them during the installation of MySQL

### MySQL From Docker Container: 
You can leave the options the way they are, if you create a MySQL Docker Container with a database name "gdis_db" and the same username and password. 

Note: The Address which is set for the MySQL Database in the properties file is actually the IP Address of the Docker VM

### Create a MySQL Docker Container

[Official Docker MySQL Server container](https://hub.docker.com/r/mysql/mysql-server/)

Follow the guide and use the following command to create and run the Docker Container: 

docker run --name gdis_db -e MYSQL_ROOT_PASSWORD=abcd1234 MYSQL_USER=gdis_db_user MYSQL_PASSWORD=abcd124 MYSQL_DATABASE=gdis_db -p 3306:3306 -d mysql/mysql-server:5.7

Alternatively the Kitematic GUI can be used. You just have to change the name of the container (not actually necessary), the listed environment variables (after -e flag) have to be set under Settings/General and also the port has to be set. 

## Database Schema: 

Currently, with the annotations contained in the Java Classes for the Models, Hibernate creates the following SQL Schema: 

Tables: 
contracts
customers
customers_insured_by
customers_owned_contracts
extend_contract
new_contract
product