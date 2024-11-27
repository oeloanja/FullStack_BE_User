package com.billit.user_service.common.service;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendPasswordResetEmail(String to, String tempPassword) {
        try {
            log.info("이메일 발송 시작. 수신자: {}", to);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("billit.project@gmail.com"); // 발신자 이메일 추가
            helper.setTo(to);
            helper.setSubject("[BillIt] 임시 비밀번호가 발급되었습니다");

            String content = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>임시 비밀번호가 발급되었습니다</title>
                        <style>
                            body { font-family: 'Arial', sans-serif; line-height: 1.6; }
                            .container { max-width: 600px; margin: 20px auto; padding: 20px; }
                            .header { text-align: center; margin-bottom: 30px; }
                            .content { margin: 20px 0; }
                            .password-box { 
                                background: #f8f9fa; 
                                padding: 15px; 
                                margin: 20px 0; 
                                text-align: center; 
                                font-size: 20px; 
                                font-weight: bold;
                                border-radius: 5px;
                            }
                            .footer { 
                                margin-top: 30px; 
                                padding-top: 20px; 
                                border-top: 1px solid #ddd; 
                                font-size: 12px; 
                                color: #666; 
                                text-align: center;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h2>임시 비밀번호 발급 안내</h2>
                            </div>
                            <div class="content">
                                <p>안녕하세요. BillIt 서비스입니다.</p>
                                <p>요청하신 임시 비밀번호가 발급되었습니다.</p>
                                <div class="password-box">
                                    %s
                                </div>
                                <p>보안을 위해 로그인 후 반드시 비밀번호를 변경해 주시기 바랍니다.</p>
                                <p>본 임시 비밀번호는 24시간 동안만 유효합니다.</p>
                            </div>
                            <div class="footer">
                                <p>본 메일은 발신전용이며, 문의사항은 고객센터를 이용해 주시기 바랍니다.</p>
                                <p>&copy; 2024 BillIt. All rights reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """, tempPassword);

            helper.setText(content, true);

            log.debug("이메일 전송 시도");
            emailSender.send(mimeMessage);
            log.info("이메일 전송 성공");

        } catch (Exception e) {
            log.error("이메일 전송 실패. 에러: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public void sendVerificationEmail(String to, String verificationCode) {
        try {
            log.info("인증 이메일 발송 시작. 수신자: {}", to);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("billit.project@gmail.com");
            helper.setTo(to);
            helper.setSubject("[BillIt] 이메일 인증을 완료해주세요");

            String content = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>이메일 인증 코드</title>
                    <style>
                        body { font-family: 'Arial', sans-serif; line-height: 1.6; }
                        .container { max-width: 600px; margin: 20px auto; padding: 20px; }
                        .header { text-align: center; margin-bottom: 30px; }
                        .content { margin: 20px 0; }
                        .verification-box { 
                            background: #f8f9fa; 
                            padding: 15px; 
                            margin: 20px 0; 
                            text-align: center; 
                            font-size: 24px; 
                            font-weight: bold;
                            letter-spacing: 2px;
                            border-radius: 5px;
                        }
                        .warning { 
                            color: #dc3545; 
                            font-weight: bold; 
                            margin: 20px 0; 
                        }
                        .footer { 
                            margin-top: 30px; 
                            padding-top: 20px; 
                            border-top: 1px solid #ddd; 
                            font-size: 12px; 
                            color: #666; 
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>이메일 인증 코드 안내</h2>
                        </div>
                        <div class="content">
                            <p>안녕하세요. BillIt 서비스입니다.</p>
                            <p>회원가입을 위한 인증 코드를 안내드립니다.</p>
                            <div class="verification-box">
                                %s
                            </div>
                            <p class="warning">인증 코드는 10분간만 유효합니다.</p>
                            <p>본인이 요청하지 않은 경우 이 메일을 무시하셔도 됩니다.</p>
                        </div>
                        <div class="footer">
                            <p>본 메일은 발신전용이며, 문의사항은 고객센터를 이용해 주시기 바랍니다.</p>
                            <p>&copy; 2024 BillIt. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """, verificationCode);

            helper.setText(content, true);

            log.debug("인증 이메일 전송 시도");
            emailSender.send(mimeMessage);
            log.info("인증 이메일 전송 성공");

        } catch (Exception e) {
            log.error("인증 이메일 전송 실패. 에러: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }
}