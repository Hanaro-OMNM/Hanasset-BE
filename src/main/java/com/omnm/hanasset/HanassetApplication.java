package com.omnm.hanasset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HanassetApplication {

	public static void main(String[] args) { SpringApplication.run(HanassetApplication.class, args); }

}
