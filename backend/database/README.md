Database Micro service
==

## TODO
1. In order to use spring-boot-starter-data-jpa (commented in the pom.xml file) the database settings have to be entered in the src/main/resources/application.properties file. 
They are needed in order to use the annotations with the entities. And the annotations are needed for the Javax Persistence. In order to use the javax persistence however,
the project has to be configured to use MongoDB since Javax Persistence is usually used with a SQL DB. More on that in the link below. 

[Javax Persistence + MongoDB]  (http://www.developer.com/java/data/how-to-manage-data-persistence-with-mongodb-and-jpa.html)