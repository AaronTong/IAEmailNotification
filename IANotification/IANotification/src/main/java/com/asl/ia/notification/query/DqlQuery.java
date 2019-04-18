package com.asl.ia.notification.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.asl.ia.notification.bean.Repository;
import com.asl.ia.notification.scheduler.mail.Email;
import com.asl.ia.notification.scheduler.mail.EmailService;
import com.asl.ia.notification.util.AppProperties;
import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service

public class DqlQuery {
	//private Map<String, Object> contents;
	@Autowired
	private EmailService es;
	Repository repo;
	String appName = "";
	String outputFile = "";
	public void setRepoName(Repository repo,String appName) {
		this.repo = repo;
		this.appName = appName;
		this.outputFile = outputFile;
	}
	public Repository getRepoName() {
		return this.repo;
	}
	
	public void run() throws Exception{
		 IDfSessionManager sessMgr = null;
		 IDfSession sess = null;
		try {
			System.out.println( "Starting to connect ..." );

            sessMgr = createSessionManager( );
            addIdentity( sessMgr, repo.getUser(), repo.getPassword());
            sess = sessMgr.getSession(this.repo.getName());
            System.out.println( "Successfully connected to the server.");
            queryDocumentum(sess);
		}catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}finally {
			if(null != sess) {
				sessMgr.release(sess);
			}
		}
	}
	
	private void queryDocumentum(IDfSession sess) throws Exception {
	    IDfQuery query = new DfQuery();
	    String currentHolding;
	    String currentAiusCount;
	    String currentStatus;
	    //query for completed ingestion
	    String queryStr= String.format("select * from dm_document where owner_name = 'Administrator' and owner_permit = '7'");
	    
	    query.setDQL(queryStr);
	    IDfCollection coll = query.execute(sess,IDfQuery.DF_EXEC_QUERY);
	    Email email = new Email();
		email.setTo("aarontongwh@gmail.com");
		email.setFrom("aarontongwh@gmail.com");
		email.setSubject("Ingestion completed");
		email.setContent("complete ingestion of pacckage");
		Map<String,Object> contents = null;
		contents = new HashMap();
		Date date = new Date();
		contents.put("asofdate", date);
		contents.put("application", appName);
		ArrayList<Map> holdingsList = new ArrayList<>();
	    while(coll.next())
	    {

			Map<String, Object> holdMap = null;
			holdMap = new HashMap<>();
	    	currentHolding = coll.getValue("r_object_id").toString();
	    	currentAiusCount = coll.getValue("owner_permit").toString();
	    	currentStatus = coll.getValue("group_permit").toString();
			//holdMap.put(currentHolding,currentAiusCount);
			holdMap.put("aius", currentAiusCount);
			holdMap.put("status", currentStatus);
			holdingsList.add(holdMap);
			
			 
			 
	 
	    }
	    contents.put("holdings",holdingsList);
	    es.sendByMailTemplate(email, contents, null);
	    coll.close();
	    String stringToFile =es.prepareSuccessTextFileByTemplate(contents, null);
	    System.out.println(stringToFile);
	    
	    /**
	    String queryStr= String.format("select eas_dss_holding,sum(eas_sip_aiu_count) as aius,eas_phase  \r\n" + 
	    		"from eas_aip \r\n" + 
	    		"where eas_phase_code == 'COM' \r\n" + 
	    		"and eas_dss_application = ${'appname from sqlite'}\r\n" + 
	    		"and eas_commit_date=${asofdate}\r\n" + 
	    		"group by  eas_dss_holding\r\n" + 
	    		"");
	    
	    query.setDQL(queryStr);
	    IDfCollection coll = query.execute(sess,IDfQuery.DF_EXEC_QUERY);
	    Email email = new Email();
		email.setTo("aarontongwh@gmail.com");
		email.setFrom("aarontongwh@gmail.com");
		email.setSubject("Ingestion completed");
		email.setContent("complete ingestion of pacckage");
		Map<String,Object> contents = null;
		contents = new HashMap();
		Date date = new Date();
		contents.put("asofdate", date);
		contents.put("application", appName);
		ArrayList<Map> holdingsList = new ArrayList<>();
	    while(coll.next())
	    {

			Map<String, Object> holdMap = null;
			holdMap = new HashMap<>();
	    	currentHolding = coll.getValue("eas_dss_holding").toString();
	    	currentAiusCount = coll.getValue("sum(eas_sip_aiu_count)").toString();
	    	currentStatus = coll.getValue("eas_phase").toString();
			holdMap.put("holdingname", currentHolding);
			holdMap.put("aius", currentAiusCount);
			holdMap.put("status", currentStatus);
			holdingsList.add(holdMap);
			
			 
			 
	 
	    }
	    contents.put("holdings",holdingsList);
	    es.sendByMailTemplate(email, contents, null);
	    coll.close();
	    String stringToFile =es.prepareSuccessTextFileByTemplate(contents, null);
	    System.out.println(stringToFile);
	    
	    //query for failed ingestion
	    
	    queryStr= String.format("select eas_dss_holding,sum(eas_sip_aiu_count) as aius,eas_phase  \r\n" + 
	    		"from eas_aip \r\n" + 
	    		"where eas_phase_code != 'COM' \r\n" +
	    		"and eas_phase_code != 'REC' \r\n" +
	    		"and eas_phase_code != 'WING' \r\n" +
	    		"and eas_phase_code != 'ING' \r\n" +
	    		"and eas_phase_code != 'WCOM' \r\n" +
	    		"and eas_dss_application = ${'appname from sqlite'}\r\n" + 
	    		"and eas_commit_date=${asofdate}\r\n" + 
	    		"group by  eas_dss_holding\r\n" + 
	    		"");
	    
	    query.setDQL(queryStr);
	    
	    coll = query.execute(sess,IDfQuery.DF_EXEC_QUERY);
	    
	    email = new Email();
		email.setTo("aarontongwh@gmail.com");
		email.setFrom("aarontongwh@gmail.com");
		email.setSubject("Ingestion incomplete");
		email.setContent("Incomplete ingestion of pacckage");
		contents = null;
		contents = new HashMap();
		date = new Date();
		contents.put("asofdate",date);
		contents.put("application", appName);
		holdingsList = new ArrayList<>();
	    while(coll.next())
	    {

			Map<String, Object> holdMap = null;
			holdMap = new HashMap<>();
	    	currentHolding = coll.getValue("eas_dss_holding").toString();
	    	currentAiusCount = coll.getValue("sum(eas_sip_aiu_count)").toString();
	    	currentStatus = coll.getValue("eas_phase").toString();
			holdMap.put("holdingname", currentHolding);
			holdMap.put("aius", currentAiusCount);
			holdMap.put("status", currentStatus);
			holdingsList.add(holdMap);
			
			 
			 
	 
	    }
	    
	    contents.put("holdings",holdingsList);
	    es.sendFailedMailByTemplate(email, contents, null);
	    coll.close();
	    stringToFile =es.prepareFailedTextFileByTemplate(contents, null);
	    System.out.println(stringToFile);
	    
	    //query for overrun ingestions
	    queryStr= String.format("select eas_dss_holding,sum(eas_sip_aiu_count) as aius,eas_phase   from eas_aip where eas_phase_code != 'COM' and eas_phase_code != 'REJ' and eas_phase_code != 'INV' and eas_phase_code != 'PUR' and eas_phase_code != 'PRU' and eas_dss_application = ${'appname from sqlite'}and eas_commit_date=${asofdate}group by  eas_dss_holding");
	    
	    query.setDQL(queryStr);
	    
	    coll = query.execute(sess,IDfQuery.DF_EXEC_QUERY);
	    
	    email = new Email();
		email.setTo("aarontongwh@gmail.com");
		email.setFrom("aarontongwh@gmail.com");
		email.setSubject("Ingestion incomplete");
		email.setContent("Incomplete ingestion of pacckage");
		contents = null;
		contents = new HashMap();
		date = new Date();
		contents.put("asofdate", date);
		contents.put("application", appName);
		holdingsList = new ArrayList<>();
	    while(coll.next())
	    {

			Map<String, Object> holdMap = null;
			holdMap = new HashMap<>();
	    	currentHolding = coll.getValue("eas_dss_holding").toString();
	    	currentAiusCount = coll.getValue("sum(eas_sip_aiu_count)").toString();
	    	currentStatus = coll.getValue("eas_phase").toString();
			holdMap.put("holdingname", currentHolding);
			holdMap.put("aius", currentAiusCount);
			holdMap.put("status", currentStatus);
			holdingsList.add(holdMap);
			
			 
			 
	 
	    }
	    contents.put("holdings",holdingsList);
	    es.sendOverrunMailByTemplate(email, contents, null);
	    coll.close();
	    stringToFile =es.prepareOverrunTextFileByTemplate(contents, null);
	    System.out.println(stringToFile);
	    **/
	}
	private IDfSessionManager createSessionManager( ) 
	            throws Exception {
	        IDfClientX clientX = new DfClientX( );
	        IDfClient localClient = clientX.getLocalClient( );
	        IDfSessionManager sessMgr = localClient.newSessionManager( );

	        System.out.println( "Created session manager." );

	        return sessMgr;
	}
	
	private void addIdentity( final IDfSessionManager sm, 
            final String username, final String password ) 
            throws Exception {
        IDfClientX clientX = new DfClientX( );

        IDfLoginInfo li = clientX.getLoginInfo( );
        li.setUser( username );
        li.setPassword( password );

        // check if session manager already has an identity.
        // if yes, remove it.
        if( sm.hasIdentity( this.repo.getName() ) ) {
            sm.clearIdentity( this.repo.getName() );

            System.out.println( "Cleared identity on :" + this.repo.getName() );
        }

        sm.setIdentity( this.repo.getName(), li );

        System.out.println( "Set up identity for the user." );
        
    }
	
	public static void readTemplate(String fileTempName,String outputFileName,String application, String date,String holding,String holdKey, String holdValue) throws IOException
	{
		// Create new file
		FileWriter fw=new FileWriter(outputFileName);
		
		// Next step is to read the content of template.txt file
				String applicationVar="{application}";
				String dateVar="{asofdate}";
				String holdingVar = "{holdings}";
				String key = "{holdings.key}";
				String value = "{holdings.value}";
				String inputTemplate = fileTempName;
				FileReader fr=new FileReader(inputTemplate);
				BufferedReader br=new BufferedReader(fr);
				
				String currentLine=br.readLine();
				while( currentLine!= null)
				{
					
					// Merging template data and values from dataFile.
					if(currentLine.contains(applicationVar))
					{
						// Replacing variables data with the values recieved from dataFile.txt
						currentLine=currentLine.replace(applicationVar, application);
					}
					if(currentLine.contains(dateVar))
					{
						// Replacing variables data with the values recieved from dataFile.txt
						currentLine=currentLine.replace(dateVar, date);
					}
					if(currentLine.contains(holdingVar))
					{
						// Replacing variables data with the values recieved from dataFile.txt
						currentLine=currentLine.replace(holdingVar, holding);
					}
					if(currentLine.contains(key))
					{
						// Replacing variables data with the values recieved from dataFile.txt
						currentLine=currentLine.replace(key, holdKey);
					}
					if(currentLine.contains(value))
					{
						// Replacing variables data with the values recieved from dataFile.txt
						currentLine=currentLine.replace(value, holdValue);
					}
					
					// Writing updated/merged data into new text file.
					fw.write(currentLine+"\n");
					currentLine=br.readLine();
				}
				
				// Once all the updated data written on new file, close the file.
				fw.close();
	}
}

