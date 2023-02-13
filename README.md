# spring-apache-mvel
Spring Boot Apache MVEL


Apache MVEL
https://github.com/apache/camel/tree/main/components/camel-mvel/src/main/java/org/apache/camel/component/mvel


Apache Camel Spring Boot:
https://github.com/apache/camel-spring-boot-examples/blob/main/rest-openapi-springdoc/README.adoc


Save some result to db:
```
        .to("jdbc:dataSource?useHeadersAsParameters=true&template=insert into actualresult (actualPrice, contractPrice, charge, printFlag, priceDifference) values (:?actualPrice, :?contractPrice, :?charge, :?printFlag, :?priceDifference)")
        .end();
 ```
