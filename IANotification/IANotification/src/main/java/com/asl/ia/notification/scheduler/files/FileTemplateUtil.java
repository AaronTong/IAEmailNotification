package com.asl.ia.notification.scheduler.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.asl.ia.notification.thymeleaf.ThymeleafConfig;


/**
 * @author puvadeeiednuch
 *
 */


@Service
public class FileTemplateUtil extends ThymeleafConfig{
	
	public static final String FILE_COMPLATED_TEMLATE = "text/fileCompletedTemplate";
	public static final String FILE_FAILED_TEMLATE	  = "text/fileFaliedTemplate";
	public static final String FILE_OVERRUN_TEMLATE   = "text/fileOverrunTemplate";
	public static final String TEMPLATE_ENCODING = "UTF-8";

	private static final Logger log = LoggerFactory.getLogger(FileTemplateUtil.class);
	
	
	public void createOutputFile(final String repository, final String application,
			final Map<String, Object> contentMap, final String templateName) {
		String fileName = "";
		String content = processTemplate(contentMap, templateName);
		String tmpTemplateName = templateName.substring(templateName.indexOf("/") + 1);
		if (tmpTemplateName.equalsIgnoreCase("fileCompletedTemplate")) {
			fileName = "IA_Completed_" + repository + "_" + application + "_"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		} else if (tmpTemplateName.equalsIgnoreCase("fileFaliedTemplate")) {
			fileName = "IA_Failed_" + repository + "_" + application + "_"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		} else if (tmpTemplateName.equalsIgnoreCase("fileCompletedTemplate")) {
			fileName = "IA_Overrun_" + repository + "_" + application + "_"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		} else {
			System.out.println("Exit First: "+ fileName);
			return;
		}
		try {
			System.out.println("fileName: "+ fileName);
			Files.write(Paths.get("C:\\out\\" + fileName + ".txt"), content.getBytes());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public String processTemplate(Map<String, Object> aggMap, String templateName) {
		Context context = new Context();
		if (aggMap != null) {
			aggMap.forEach((key, value) -> context.setVariable(key, value));
			return this.templateEngine().process(templateName, context);
		}
		return "";

	}


}
