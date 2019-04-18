package com.asl.ia.notification.scheduler.mail;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class overrunEmailTemplateUtil {
	public static final String EMAIL_TEMLATE = "mailOverrunTemplate";
	
	@Autowired
	private TemplateEngine tempTemplateEngine;

	private static TemplateEngine templateEngine;

	@PostConstruct
	void init() {
		templateEngine = tempTemplateEngine;
	}

	public String getProcessedHtml(Map<String, Object> model, String templateName) {

		Context context = new Context();

		if (model != null) {
			model.forEach((key,value) -> context.setVariable(key, value));
			return templateEngine.process(templateName, context);
		}
		return "";

	}
}
