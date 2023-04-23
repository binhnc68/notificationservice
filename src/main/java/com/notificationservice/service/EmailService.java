/**
 * 
 */
package com.notificationservice.service;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.notificationservice.model.MessageDTO;

import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author pc
 *
 */
public interface EmailService {
	void sendEmail(MessageDTO messageDTO);

}

@Service
class EmailServiceImpl implements EmailService{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Value("${spring.mail.username}")
	private String from;
	

	@Override
	@Async
	public void sendEmail(MessageDTO messageDTO) {
		try {
			logger.info("Start .. sending email.");
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
			
			// load tempalte email with content
			Context context = new Context();
			context.setVariable("name", messageDTO.getToName());
			context.setVariable("content", messageDTO.getContent());
			String html = templateEngine.process("welcome-email", context);
			logger.info("2.Start .. sending email ToName" + messageDTO.getToName() + ",To: "+ messageDTO.getTo());
			// send email
			helper.setTo(messageDTO.getTo());
			helper.setText(html, true);
			helper.setSubject(messageDTO.getSubject());
			helper.setFrom(from);
			
			javaMailSender.send(message);			
			logger.info("End .. sending email to: " + messageDTO.getTo());
			
		} catch (Exception e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}





