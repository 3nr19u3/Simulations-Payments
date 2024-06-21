# Payments simulation

## Payments Application 
<br>

## Architecture: (final goal)
-Authentication Service (To authenticate) <br>
-Balance Service (To Register an account with balance(coins), Here also you can be create currencies and established their values)<br>
-Payment Service (This service process the payments validate the balance in account of the user and another points) <br>
<br>

## Some Specificitation:
-Registry-service(Eureka Server)<br>
-Config-server(include the model of .yml file to work with private repository)<br>
-Api-gateway(With the Authenticacion filter for some services)<br>
<br>

## Some version specificitation:
-Spring boot 3<br>
-Java 17<br>
-Maven<br>
<br>

### *How works?*
The authentication-service<br>
Managment the users into applications (using JWT), an user can have only ONE balance account (balance-service)<br>
this balance-service will saved an register for each user in this register of balance also saved the currency type and value (of balance account)<br>
While the payment-service register an simulated payment making the substraction to balance-service, also validating the value between currencies<br>
<br>
<br>
### *How test this project?*
<br>
(all endpoints will be available from a service in the next days, after deployment of this application)
<br>
1.- Download the source code<br>
2.- Create the auth_db, balance_db and payment_db Schemas in your local environment (you can be use an tool to managment databases graphic way as MysqkWorkbrench) <br>
3.- Configure the file setup of the services(include .yml.example files to guide you)<br>
4.- Create a Config-server repository(here will be all your configuration as jwt-sercret jwt-espiration-time in others), this repository will contains three files (for each service).<br>
5.- When you ended the configuration, you should can start with the launch the project (recommended registry-service, config-server,api-gateway,auth-service, balance-service, payment-service (in order))<br>
6.- Once the project is up and running<br>
7.- You must create an account in http://localhost:9191/api/auth/signup<br>
8.- Also you must create at leats an currency in http://localhost:9191/api/currency<br>
9.- After can create an balance account using the endpoint http://localhost:9191/api/balance (this account will belong to the user autenticAte)<br>
10.- The final test for the process can be make the payment using http://localhost:9191/api/payment(include multiples validations to the make the rigth process)<br>
<br>

![Untitled-2024-03-23-2008](https://github.com/3nr19u3/java-microservices/assets/46394434/99974881-f9c0-46c4-b393-87df8f64ab35)

<br>
