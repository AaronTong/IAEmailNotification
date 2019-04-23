package com.asl.ia.notification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.asl.ia.notification.bean.*;
import com.asl.ia.notification.query.DqlQuery;
import com.asl.ia.notification.scheduler.ScheduledTasks;
import com.asl.ia.notification.scheduler.mail.Email;
import com.asl.ia.notification.scheduler.mail.EmailService;
import com.asl.ia.notification.util.AppProperties;
import java.sql.*;


@SpringBootApplication
@Component
@EnableConfigurationProperties
@EnableScheduling
public class IaNotificationApplication implements ApplicationRunner{
	
	
	List<Repository> repoList;
	
	@Autowired
	private EmailService emailService;
	@Autowired
    private AppProperties appProperties;
	@Autowired
    private DqlQuery query;
	public static void main(String[] args) {
		SpringApplication.run(IaNotificationApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		//mainQuery();
	}
	
	

}
