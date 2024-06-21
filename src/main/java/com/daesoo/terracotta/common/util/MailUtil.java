package com.daesoo.terracotta.common.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailUtil {

	private final JavaMailSender javaMailSender;

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom secureRandom = new SecureRandom();

	public MimeMessage createEmailForm(String email, String authCode) throws MessagingException, UnsupportedEncodingException {
		// 코드를 생성합니다.
		String setFrom = "dev.terracotta@gmail.com";	// 보내는 사람
		String toEmail = email;		// 받는 사람(값 받아옵니다.)
		String title = "[테라코타] 회원가입 인증코드 입니다.";		// 메일 제목		

		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
		message.setSubject(title);		// 제목 설정

		StringBuilder msgOfEmail = new StringBuilder();
		msgOfEmail.append("<div style=\"margin: 20px;\">")
		        .append("<p style=\"font-size: 16px; margin-bottom: 10px;\">아래 코드를 인증란에 입력해주세요</p>")
		        .append("<div style=\"text-align: center; border: 1px solid black; font-family: Verdana; padding: 10px;\">")
		        .append("<h3 style=\"color: blue; margin-bottom: 10px;\">회원가입 인증 코드:</h3>")
		        .append("<div style=\"font-size: 16px;\">")
		        .append("<p style=\"font-weight: bold; margin-bottom: 5px;\">CODE: <span style=\"color: red;\">")
		        .append(authCode)
		        .append("</span></p>")
		        .append("</div>")
		        .append("</div>")
		        .append("</div>");

		message.setFrom(setFrom);		// 보내는 사람 설정
		// 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
		message.setText(msgOfEmail.toString(), "utf-8", "html");

		return message;
	}
	
	public MimeMessage createResetPasswordEmailForm(String email, String authCode) throws MessagingException, UnsupportedEncodingException {
		// 코드를 생성합니다.
		String setFrom = "dev.terracotta@gmail.com";	// 보내는 사람
		String toEmail = email;		// 받는 사람(값 받아옵니다.)
		String title = "[테라코타] 비밀번호 재설정 이메일 입니다.";		// 메일 제목		

		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
		message.setSubject(title);		// 제목 설정

		StringBuilder msgOfEmail = new StringBuilder();
		msgOfEmail.append("<div style=\"margin: 20px;\">")
		        .append("<p style=\"font-size: 16px; margin-bottom: 10px;\">아래 링크를 클릭하면 비밀번호 재설정 페이지로 이동합니다.</p>")
		        .append("<div style=\"text-align: center; border: 1px solid black; font-family: Verdana; padding: 10px;\">")
		        .append("<h3 style=\"color: blue; margin-bottom: 10px;\">아래 링크로 이동해 비밀번호 재설정을 완료하십시오.</h3>")
		        .append("<div style=\"font-size: 16px;\">")
		        .append("<p style=\"font-weight: bold; margin-bottom: 5px;\">LINK: <span style=\"color: red;\">")
		        .append("<a href =\"https://terracotta-nu.vercel.app/password/reset?key=" + authCode + "\">" + "비밀번호 재설정" + "</a>")
		        .append("</span></p>")
		        .append("</div>")
		        .append("</div>")
		        .append("</div>");

		message.setFrom(setFrom);		// 보내는 사람 설정
		// 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
		message.setText(msgOfEmail.toString(), "utf-8", "html");

		return message;
	}

	public String generateAuthenticationCode() {
		StringBuilder stringBuilder = new StringBuilder(6);
		for (int i = 0; i < 6; i++) {
			int randomIndex = secureRandom.nextInt(CHARACTERS.length());
			stringBuilder.append(CHARACTERS.charAt(randomIndex));
		}
		return stringBuilder.toString();
	}
	
	public String generateHardAuthenticationCode() {
		return UUID.randomUUID().toString();
	}

	public String sendEmail(String email) {

		String authCode = generateAuthenticationCode();
		//메일전송에 필요한 정보 설정
		try {
			MimeMessage emailForm = createEmailForm(email.trim(), authCode);
			javaMailSender.send(emailForm);
		}catch(Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("메일 보내는중 에러 발생");
		}
		//실제 메일 전송

		return authCode; //인증 코드 반환
	}
	
	public String sendResetPasswordEmail(String email) {

		String authCode = generateHardAuthenticationCode();
		//메일전송에 필요한 정보 설정
		try {
			MimeMessage emailForm = createResetPasswordEmailForm(email.trim(), authCode);
			javaMailSender.send(emailForm);
		}catch(Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("메일 보내는중 에러 발생");
		}
		//실제 메일 전송

		return authCode; //인증 코드 반환
	}

}
