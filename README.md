#  LoadingJr - 2024
## Description
This is an application for publish posts, with a jwt authentication and images upload by AWS S3 using docker compose to deploy the application
## Install Guide
`git clone `

Install maven projects using:  
`mvn clean install`  
Before run the docker compose, make sure to package the maven projects using:  
`mvn clean package`    

Now, you need to build docker images, in the root repository folder type:  
`docker compose build `  
to get the containers running, type:  
`docker compose up -d`  
now everything's should be up and working
## Routes:
 Docs:
```http request
    http://localhost:8080/api/swagger-ui/index.html
```
## Technology used:
+ Spring
+ PostgreSQL
+ Junit 5
+ Mockito
+ Swagger
+ Docker
+ AWS S3
