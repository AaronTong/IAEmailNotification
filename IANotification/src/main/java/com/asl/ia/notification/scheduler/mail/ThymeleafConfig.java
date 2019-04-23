package com.asl.ia.notification.scheduler.mail;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfig {
	
	
	@Autowired
	private TemplateEngine tempTemplateEngine;
	
	
	
	private static TemplateEngine templateEngine;
	//@Bean(name = "textTemplateEngine")
	@PostConstruct
	void init() {
		templateEngine = tempTemplateEngine;
		this.textTemplateinit();
	}
    private void textTemplateinit() {
		templateEngine.addTemplateResolver(textTemplateResolver());
	}

    private ITemplateResolver textTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    public String parseText(String templateName,Map<String,Object> content) {
    	Context context = new Context();
    	context.setVariables(content);
    	String text = templateEngine.process(templateName, context);
    	return text;
    }
}
