# Spring Cloud Config Server with Any Database  
[![build-status](https://travis-ci.org/cynicLT/spring-cloud-config-server-data.svg?branch=master)](https://travis-ci.org/cynicLT/spring-cloud-config-server-data)

Allows to load configuration data from any database.

Based on [spring-cloud-config-server-mongodb](https://github.com/spring-cloud-incubator/spring-cloud-config-server-mongodb)

# Quick Start
Configure pom.xml, like this:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
	    <artifactId>spring-cloud-config-server-data</artifactId>	    
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Create a standard Spring Boot application, like this:
```java
@SpringBootApplication
@EnableDataConfigServer
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
To customize propety source loading - define bean in configuration:
```java
@Bean
DataRepository  dataRepository(){
    // Your custom implementation
}
```
Default implementation is empty:
```java
@Bean
@ConditionalOnMissingBean(name = "dataRepository")
DataRepository emptyDataRepository() {
    return (profiles, labels) -> new ArrayList<>();
}
```