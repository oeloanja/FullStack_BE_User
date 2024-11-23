package com.billit.user_service.user.service;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendPasswordResetEmail(String to, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[BillIt] 비밀번호 재설정");
            message.setText("귀하의 임시 비밀번호는 " + resetToken + " 입니다.\n"
                    + "로그인 후 반드시 비밀번호를 변경해주세요.");

            emailSender.send(message);
        } catch (MailException e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }
}