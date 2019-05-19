package com.faujor.service.mail.impl;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.AppointMail;
import com.faujor.service.mail.MailCommonService;
import com.faujor.utils.PDF.PDFUtils2;

@Service("mailCommonService")
public class MailCommonServiceImpl implements MailCommonService {

	@Autowired
	private JavaMailSender mailSender;// 框架自带的

	@Value("${spring.mail.username}") // 发送人的邮箱 比如155156641XX@163.com
	private String from;

	@Override
	@Async // 异步调用,outlook邮件发送
	public boolean sendOutlookMail(AppointMail appoint, List<AppoMate> list) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "mail.top-china.com");
    //	properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.starttls.enable", "false");
		properties.setProperty("mail.smtp.auth", "false");
		Session session = Session.getInstance(properties);
//		Session session = Session.getDefaultInstance(properties, new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication("srm", "top123");
//			}
//		});
		String from = "srm@top-china.com";
		String to = appoint.getSupplierEmail();
		if (StringUtils.isEmpty(to))
			return false;
//		String reciTo = "1002436465@qq.com";
		String status = appoint.getStatus();

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from)); // 发送者邮箱
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));// 目标邮箱
//			message.setRecipient(Message.RecipientType.CC, new InternetAddress(reciTo));// 抄送对象
			message.setSubject("预约单发送");

			// 发送附件
			// 创建复合消息体
			Multipart multipart = new MimeMultipart();

			StringBuilder sb = new StringBuilder();
			sb.append("尊敬的合作伙伴：您好！\n\n您的预约单");
			sb.append(appoint.getAppointCode());
			if ("已发布".equals(status)) {
				sb.append("已经发布，详细信息请查看附件。在此感谢您的合作！\n\n");
				// 添加附件
				BodyPart filePart = new MimeBodyPart();
				// 开发地址
				// 生成PDF文档
				String pdfPath = new PDFUtils2().generationPDF(appoint, list);
				if (StringUtils.isEmpty(pdfPath))
					return false;
				FileDataSource source = new FileDataSource(pdfPath);
				filePart.setDataHandler(new DataHandler(source));
				filePart.setFileName(source.getName());
				multipart.addBodyPart(filePart);
			} else {
				sb.append("已经作废。在此感谢您的合作！\n\n");
			}
			sb.append("脱普企业集团");

			// 添加文本内容
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(sb.toString());
			multipart.addBodyPart(textPart);
			// 绑定消息对象
			message.setContent(multipart, "text/html;charset=utf-8");
			// 发送邮件
			Transport.send(message);
			System.out.println("发送成功！");

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void sendQQMail(String title, String url, String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from); // 发送人的邮箱
		message.setSubject(title); // 标题
		message.setTo(email); // 发给谁 对方邮箱
		message.setText(url); // 内容
		mailSender.send(message); // 发送
	}

}
