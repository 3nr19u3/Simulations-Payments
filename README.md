# Payments simulation system built in java adopting a microservices architecture

## Payments Application 
<br>

## Architecture: (final goal)
-Authentication Service <br>
-Balance Service <br>
-Payment Service <br>
<br>

## Some Specificitation:
-Registry-service(Eureka Server)<br>
-Config-server<br>
-Api-gateway<br>
<br>

## Some version specificitation:
-Spring boot 3.2.4<br>
-Java 17<br>
-Maven (compile tool)<br>
<br>

### *How works?*
The authentication-service managment the users into applications (using JWT), an user can have only an balance account (balance-service)<br>
this balance-service will saved an register for each user in this register of balance also saved the currency type and value (of balance account)<br>
While the payment-service register an simulated payment making the substraction to balance-service, also validating the value between currencies<br>
<br>
<br>
### *How test this project?*(all endpoints will be available from a service in the next days, after deployment of this application)
1.- You should register as user in http://localhost:9191/api/auth/signup<br>
2.- After that you be an user registered you access into app using the endpoint http://localhost:9191/api/auth/signin<br>
3.- As a next step you should be create a balance account (with a value in an currency type valid) using http://localhost:9191/api/balance<br>
4.- The final test for the process can be make the payment using http://localhost:9191/api/payment<br>
(remember that you should have an balance account registred with balance higher value that payment, otherwise this operation will result in an error.)


![image](https://github.com/3nr19u3/java-microservices/assets/46394434/cd0d15a3-4869-4414-9071-9747bba7f11a)
