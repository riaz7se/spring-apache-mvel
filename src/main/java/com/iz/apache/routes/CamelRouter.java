package com.iz.apache.routes;

import com.iz.model.PriceClass;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

    @Autowired
    private Environment env;

    @Value("${camel.servlet.mapping.context-path}")
    private String contextPath;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .port(env.getProperty("server.port", "8080"))
                .contextPath(contextPath.substring(0, contextPath.length() - 2))
                // turn on openapi api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "1.0.0");


        rest("/test").description("Test description")
                .produces("application/json")
                .get().outType(String.class)
                .responseMessage().code(200).message("OK....ok  ").endResponseMessage()
                .to("bean:testService?method=testService");
        ;

        from("direct:update-user")
                .to("bean:testService?method=testService")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
                .setBody(constant(""));

//        rest("/users").description("User REST service")
//                .consumes("application/json")
//                .produces("application/json")
//
//                .get().description("Find all users").outType(PriceClass[].class)
//                .responseMessage().code(200).message("All users successfully returned").endResponseMessage()
//                .to("bean:userService?method=findUsers")
    }
}