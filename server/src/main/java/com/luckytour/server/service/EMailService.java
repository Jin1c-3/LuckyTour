package com.luckytour.server.service;


/**
 * 邮件发送服务
 */
public interface EMailService {
	Boolean sendSimpleMail(String from, String to, String subject, String content);

	Boolean sendVerificationCode(String to, String code);

	Boolean sendGreetings(String to, String name);


}
