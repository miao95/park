package edu.buaa.sem.common;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class BasicMailSendService {

	@Autowired
	JavaMailSender officialMailSender;

	String mailFrom;

	public JavaMailSender getOfficialMailSender() {
		return officialMailSender;
	}

	public void setOfficialMailSender(JavaMailSender officialMailSender) {
		this.officialMailSender = officialMailSender;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void sendSimpleEmailOfficial(String[] to, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setFrom(mailFrom);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		officialMailSender.send(mailMessage);
	}

	/**
	 * 发送HTML邮件
	 * 
	 * @param to
	 *            多个收件人的地址，以数组的方式传入
	 * @param subject
	 *            邮件的主题
	 * @param html
	 *            邮件的HTML内容
	 */
	public void sendHtmlEmailOfficial(String[] to, String subject, String html) {
		MimeMessage mimeMessage = officialMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
				"utf-8");
		try {
			messageHelper.setTo(to);
			messageHelper.setFrom(mailFrom);
			messageHelper.setSubject(subject);
			messageHelper.setText(html, true);
			officialMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送到多个附件的HTML邮件
	 * 
	 * @param to
	 *            多个收件人的地址，以数组的方式传入
	 * @param subject
	 *            邮件的主题
	 * @param html
	 *            邮件的HTML内容
	 * @param attachment
	 *            多个附件的地址
	 */
	public void sendHtmlWithAttachmentEmailOfficial(String[] to,
			String subject, String html, String[] attachment) {
		try {
			MimeMessage mimeMessage = officialMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, true, "utf-8");
			messageHelper.setTo(to);
			messageHelper.setFrom(mailFrom);
			messageHelper.setSubject(subject);
			messageHelper.setText(html, true);
			for (int i = 0; i < attachment.length; i++) {
				File file = new File(attachment[i]);
				messageHelper.addAttachment(
						MimeUtility.encodeWord(file.getName()), file);
			}
			officialMailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
