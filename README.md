# spring-boot-webflux
webflux, API functional, mongo

###Compile

mvn clean install

###Execute

mvn spring-boot: run

###Swagger

http://localhost:8080/swagger-ui.html#/

# docker mongo
docker run --name mongo -d -p 27017:27017 mongo

edit mongo host in appplication.properties
