package com.billit.user_service.common.service;

import com.billit.user_service.common.exception.CustomException;
import com.billit.user_service.common.exception.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}