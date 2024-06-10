package com.service.apigateway.filter;

import com.service.apigateway.util.JwtUtil;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory <AuthenticationFilter.Config>{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                LOGGER.info("Si pasa el filtro!");
                //check the token exist in the request
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    throw new RuntimeException("missing authorization header");

                String authenticationHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authenticationHeader != null && authenticationHeader.startsWith("Bearer "))
                    authenticationHeader = authenticationHeader.substring(7);
                try{
                    //call to authentication service(auth-service) to validate token and continue in the flux
                    //also another way to validate token is...with the creation of JwtUtilClass with the validatetoken method
                    //(this option include add the jwt dependencies into gateway service)
                    jwtUtil.validateToken(authenticationHeader);

                }catch(Exception e){
                    //throw a exception
                    LOGGER.error(e.toString());
                    throw new RuntimeException("unauthorized access to application");
                }

            }
            LOGGER.info("Result {}",routeValidator.isSecured.test(exchange.getRequest()));
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }
}
