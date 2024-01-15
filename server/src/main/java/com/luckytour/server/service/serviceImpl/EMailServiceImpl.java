package com.luckytour.server.service.serviceImpl;

import com.luckytour.server.common.constant.EMailTemplate;
import com.luckytour.server.service.EMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author yujin
 */
@Service
@Slf4j
public class EMailServiceImpl implements EMailService {
	@Autowired
	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String from;

	public EMailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	/**
	 * 发送简单邮件
	 *
	 * @param from    发送者
	 * @param to      接收者
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	@Override
	public Boolean sendSimpleMail(String from, String to, String subject, String content) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		// 发送者
		simpleMailMessage.setFrom(from);
		// 接收者
		simpleMailMessage.setTo(to);
		// 邮件主题
		simpleMailMessage.setSubject(subject);
		// 邮件内容
		simpleMailMessage.setText(content);
		// 发送邮件
		try {
			javaMailSender.send(simpleMailMessage);
			log.debug("邮件发送成功:from: {} to: {}", from, to);
			return true;
		} catch (Exception e) {
			log.error("邮件发送失败:from: {} to: {}", from, to);
			log.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 发送验证码邮件
	 *
	 * @param to   接收者
	 * @param code 验证码
	 */
	@Override
	public Boolean sendVerificationCode(String to, String code) {
		if (sendSimpleMail(from, to, EMailTemplate.VERIFICATION_CODE.getSubject(), EMailTemplate.VERIFICATION_CODE.fillOneParam(code))) {
			log.info("验证码邮件发送成功:from: {} to: {} ,with code {}", from, to, code);
			return true;
		} else {
			log.error("验证码邮件发送失败:from: {} to: {} ,with code {}", from, to, code);
			return false;
		}
	}

	/**
	 * 发送欢迎邮件
	 *
	 * @param to   接收者
	 * @param name 用户名
	 */
	public Boolean sendGreetings(String to, String name) {
		if (sendSimpleMail(from, to, EMailTemplate.GREETINGS.getSubject(), EMailTemplate.GREETINGS.fillOneParam(name))) {
			log.debug("欢迎邮件发送成功:from: {} === to: {} ,with name {}", from, to, name);
			return true;
		} else {
			log.error("欢迎邮件发送失败:from: {} === to: {} ,with name {}", from, to, name);
			return false;
		}
	}

}
