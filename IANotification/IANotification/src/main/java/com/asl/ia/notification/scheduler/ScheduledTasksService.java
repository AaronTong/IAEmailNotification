package com.asl.ia.notification.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.ia.notification.bean.Application;
import com.asl.ia.notification.bean.Repository;
import com.asl.ia.notification.query.DqlQuery;
import com.asl.ia.notification.scheduler.mail.Email;
import com.asl.ia.notification.scheduler.mail.EmailService;
import com.asl.ia.notification.util.AppProperties;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

@Service
public class ScheduledTasksService {
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasksService.class);
   
	@Autowired
    private DqlQuery query;
    @Autowired
    private AppProperties appProperties;
    @Autowired 
    private EmailService es;
    
	public void mainQuery() throws IOException {
		//String query1 = "SELECT DISTINCT repository FROM IA_Notify_DB";
		
			//Connection conn = this.connect();
			//Statement stmt = conn.createStatement();
			//ResultSet result = stmt.executeQuery(query1);
			
			Set<Repository> repoList = appProperties.getDocumentum().getRepositories();
			
			//search for repository
			repoList.stream().forEach(repo -> {
				try {
				Connection conn = this.connect();
				PreparedStatement subQuery = conn.prepareStatement("SELECT * FROM IA_Notify_DB WHERE repository = ?");
				subQuery.setString(1,repo.getName());
				ResultSet result1 = subQuery.executeQuery();
				Set<Application> appList = new HashSet<>();
				Email mail = new Email();
				//search for application
				while(result1.next()) {
					String repositoryName = result1.getString("repository");
					String to = result1.getString("mail_to");
					mail.setTo(to);
					String cc = result1.getString("mail_cc");
					mail.setCc(cc);
					String subject = result1.getString("mail_subject");
					mail.setSubject(subject);
					
					/**
					 * @Puvadee: Use for what?, Just DQL enough information, use this filed eas_dss_holding for holding name it return for dql result
					 */
					//search for Holding
		        	/*String dirName = String.format("C:\\Documentum\\data\\%s\\%s", repo.getName(),result1.getString("application"));
			        
		        	File file = new File(dirName);
		        	Set<Holding> holdingList = new HashSet<>();
			        Files.walkFileTree(file.toPath(), Collections.emptySet(), 1, new SimpleFileVisitor<Path>() {
			            @Override
			            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			            	
			            	Holding holding = new Holding();
			            	holding.setHoldingName(file.getFileName().toString());
			                holdingList.add(holding);
			                return FileVisitResult.CONTINUE;
			            }
			            
			        });    */    
				
					Application app = new Application();
					app.setName(result1.getString("application"));
					//app.setHoldings(holdingList);
					//set thresold for app
					appList.add(app);
			
					query.run(mail);
				} 
				repo.setApplications(appList);
				}catch (Exception e) {
					log.error(e.getMessage());
				}
				
				
				
			});
			
			/**
			 * @Puvadee this is duplicated work, please check your logic above first
			 */
//			documentum.setRepositories(repoList);
//			for (Iterator<Repository> it = repoList.iterator(); it.hasNext(); ) {
//			        Repository rp = it.next();
//			        for(Iterator<Application> it2 = rp.getApplications().iterator(); it2.hasNext();) {
//			        //System.out.printf("Repo Name: %s\n",rp.getName());
//				        query.setRepoName(rp,it2.next().getName());
//				        try {
//							query.run(mail);
//						} catch (Exception e) {
//							log.error(e.getMessage());
//						}
//			        }
//			}
		
	}
    private Connection connect() {
        // SQLite connection string
        String url = String.format("jdbc:sqlite:%s\\%s", appProperties.getLocalDb().getPath(), appProperties.getLocalDb().getDbName());
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }
        return conn;
    }

}
