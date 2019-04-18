package com.asl.ia.notification.scheduler.mail;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.asl.ia.notification.thymeleaf.ThymeleafConfig;

@Service
public class EmailTemplateUtil extends ThymeleafConfig{
	
	public static final String EMAIL_COMPTED_TEMLATE = "mail/mailCompletedTemplate";
	public static final String EMAIL_FAILED_TEMLATE  = "mail/mailFailedTemplate";
	public static final String EMAIL_OVERRUN_TEMLATE = "mail/mailOverrunTemplate";
	public static final String TEMPLATE_ENCODING = "UTF-8";
	
	public String processTemplate(Map<String, Object> aggMap, String templateName) {
		Context context = new Context();
		if (aggMap != null) {
			aggMap.forEach((key, value) -> context.setVariable(key, value));
			return this.templateEngine().process(templateName, context);
		}
		return "";

	}
	
}
