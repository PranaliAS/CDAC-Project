package com.fullstack.library.management.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication
@ComponentScan(basePackages = {"com.fullstack.library.management.backend"},excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
@ContextConfiguration(classes = SpringBootLibraryManagementApplicationTests.class,locations = "classpath:application-test.properties")
public class SpringBootLibraryManagementApplicationTests
{
    public static void main( String[] args )
    {
        SpringApplication.run(SpringBootLibraryManagementApplicationTests.class,args);
        System.out.println( "Running Application Spring Boot LibraryManagement React Project!" );
    }
}
