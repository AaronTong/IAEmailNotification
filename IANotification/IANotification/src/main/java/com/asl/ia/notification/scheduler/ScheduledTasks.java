package com.asl.ia.notification.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.asl.ia.notification.bean.Application;
import com.asl.ia.notification.bean.Documentum;
import com.asl.ia.notification.bean.Repository;
import com.asl.ia.notification.query.DqlQuery;
import com.asl.ia.notification.scheduler.mail.Email;
import com.asl.ia.notification.scheduler.mail.EmailService;
import com.asl.ia.notification.util.AppProperties;


@Component
public class ScheduledTasks {
		
	@Autowired
	private ScheduledTasksService tasksService;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Scheduled(fixedDelay = 1000)
    public void reportCurrentTime() throws Exception{
//    	mainQuery();
    	//System.out.println("hello");
    	tasksService.mainQuery();
    	
    	/**
    	documentum = appProperties.getDocumentum();
    	Set<Repository> repositories = documentum.getRepositories();
    	System.out.println("Repo Count: "+repositories.size());
    	repositories.stream().forEach(repo -> {
    		System.out.println("Repository: "+ repo.getName());
    		if(null != repo.getApplications() && !repo.getApplications().isEmpty()) {
    			//query = new DqlQuery(repo.getName());
				try {
					query.setRepoName(repo.getName());
					System.out.println("Repository Name: "+ query.getRepoName());
					query.run();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	    repo.getApplications().forEach(app -> {
	    	    	System.out.println("\tapplication: "+ app.getName());
	    	    	if(null != app.getHoldings() && !app.getHoldings().isEmpty()) {
		    	    	app.getHoldings().stream().forEach(hold -> {
		    	    		System.out.println("\t\tholding: " +hold.getHoldingName());
		    	    		
		    	    	});
	    	    	}
	    	    });
    		}
    	});
    	**/
    	//es.sendByMailTemplateSample(null, null);
		//System.out.println("Scheduled");
    }
    
    
}
