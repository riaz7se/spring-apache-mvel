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


```
from("direct:start")
  .setHeader("product1", simple("${body.product1}"))
  .setHeader("product2", simple("${body.product2}"))
  .to("sql:SELECT expression FROM expressions WHERE id = 1?dataSource=dataSource")
  .choice()
    .when(mvel("${body.printFlag == 'N'}"))
      .setHeader("priceDifference", mvel("${body}"))
      .log("Price difference between product1 and product2: ${header.priceDifference}")
    .end()
  .to("direct:end");

```
