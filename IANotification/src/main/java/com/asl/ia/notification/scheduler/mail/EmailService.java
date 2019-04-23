package com.asl.ia.notification.scheduler.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.activation.FileTypeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;
	@Autowired
	private EmailTemplateUtil emailTemplateUtil;
	
	@Autowired
	private failedEmailTemplateUtil failTemplate;
	
	@Autowired
	private overrunEmailTemplateUtil overrunTemplate;
	
	@Autowired
	private ThymeleafConfig textConfig;
	public EmailService() {
		
	}
	
	public void sendSimpleMessage(final Email mail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(mail.getSubject());
		message.setText(mail.getContent());
		message.setTo(mail.getTo());
		message.setFrom(mail.getFrom());

		emailSender.send(message);
	}

	//Just for reference sample
	public void sendByMailTemplateSample(final Email mail, final Map<String, Object> contents) {

		Map<String, Object> model = new HashMap<>();
		String templateName = "mailTemplate";
		model.put("asofdate", new Date());
		model.put("application", "Infocast");
		
		ArrayList<Map> holdingsList = new ArrayList<>();

		Map<String, Object> holdMap = null;
		holdMap = new HashMap<>();
		holdMap.put("holdingname", "ICDailyStmt");
		holdMap.put("aius", "23311");
		holdingsList.add(holdMap);

		holdMap = new HashMap<>();
		holdMap.put("holdingname", "ICCorpActLtr");
		holdMap.put("aius", "12003");
		holdingsList.add(holdMap);
	
		model.put("holdings", holdingsList);
		
		String resultMail = emailTemplateUtil.getProcessedHtml(model, templateName);

		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(mail.getFrom());
			messageHelper.setTo(mail.getTo());
			messageHelper.setSubject(mail.getSubject());
			messageHelper.setText(resultMail, true);
		};
		emailSender.send(messagePreparator);
		System.out.println(resultMail);
	}

	public void sendByMailTemplate(final Email mail, final Map<String, Object> contents, String emailTemplate) {
		String template = EmailTemplateUtil.EMAIL_TEMLATE; //Default template
		if( null != emailTemplate)
			template = emailTemplate;
		String resultMail = emailTemplateUtil.getProcessedHtml(contents, template);
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(mail.getFrom());
			messageHelper.setTo(mail.getTo());
			messageHelper.setSubject(mail.getSubject());
			messageHelper.setText(resultMail, true);
		};
		emailSender.send(messagePreparator);
		//System.out.println(resultMail);
	}
	
	public void sendFailedMailByTemplate(final Email mail, final Map<String, Object> contents, String emailTemplate) {
		String template = failedEmailTemplateUtil.EMAIL_TEMLATE; //Default template
		if( null != emailTemplate)
			template = emailTemplate;
		String resultMail = failTemplate.getProcessedHtml(contents, template);
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(mail.getFrom());
			messageHelper.setTo(mail.getTo());
			messageHelper.setSubject(mail.getSubject());
			messageHelper.setText(resultMail, true);
		};
		emailSender.send(messagePreparator);
		//System.out.println(resultMail);
	}
	
	public void sendOverrunMailByTemplate(final Email mail, final Map<String, Object> contents, String emailTemplate) {
		String template = failedEmailTemplateUtil.EMAIL_TEMLATE; //Default template
		if( null != emailTemplate)
			template = emailTemplate;
		String resultMail = overrunTemplate.getProcessedHtml(contents, template);
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(mail.getFrom());
			messageHelper.setTo(mail.getTo());
			messageHelper.setSubject(mail.getSubject());
			messageHelper.setText(resultMail, true);
		};
		emailSender.send(messagePreparator);
		//System.out.println(resultMail);
	}
	public String prepareSuccessTextFileByTemplate(final Map<String, Object> contents) {
		String result = textConfig.parseText("fileCompletedTemplate",contents);
		return result;
	}
	public String	prepareOverrunTextFileByTemplate(final Map<String, Object> contents) {
		String result = textConfig.parseText("fileOverrunTemplate",contents);
		return result;
	}
	public String prepareFailedTextFileByTemplate(final Map<String, Object> contents) {
		String result = textConfig.parseText("fileFailedTemplate",contents);
		return result;
	}
}
