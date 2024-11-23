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
            log.info("Attempting to send email to: {}", to);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(to);
            helper.setSubject("[BillIt] 임시 비밀번호가 발급되었습니다");

            String content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>임시 비밀번호가 발급되었습니다</title>" +
                    "<style>" +
                    "  @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap');" +
                    "  body { font-family: 'Noto Sans KR', Arial, sans-serif; line-height: 1.6; background-color: #f8f9fa; margin: 0; padding: 0; }" +
                    "  .container { margin: 40px auto; padding: 40px; max-width: 600px; background-color: #ffffff; border-radius: 16px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                    "  .header { text-align: center; margin-bottom: 40px; }" +
                    "  .title { " +
                    "    color: #40E0D0; " +
                    "    font-size: 28px; " +
                    "    font-weight: 700; " +
                    "    margin: 20px 0; " +
                    "    text-align: center; " +
                    "  }" +
                    "  .content { margin: 30px 0; padding: 0 20px; color: #333333; }" +
                    "  .message-box { " +
                    "    background-color: #f8f9fa; " +
                    "    border-left: 4px solid #40E0D0; " +
                    "    padding: 20px; " +
                    "    margin: 20px 0; " +
                    "    border-radius: 0 8px 8px 0; " +
                    "  }" +
                    "  .password-box { " +
                    "    background: #40E0D0; " +
                    "    color: white; " +
                    "    padding: 25px; " +
                    "    border-radius: 12px; " +
                    "    margin: 30px auto; " +
                    "    text-align: center; " +
                    "    font-weight: 700; " +
                    "    font-size: 24px; " +
                    "    letter-spacing: 2px; " +
                    "  }" +
                    "  .warning { " +
                    "    background-color: #fff3cd; " +
                    "    border: 1px solid #ffeeba; " +
                    "    color: #856404; " +
                    "    padding: 15px; " +
                    "    border-radius: 8px; " +
                    "    margin: 25px 0; " +
                    "    font-size: 14px; " +
                    "  }" +
                    "  .highlight { " +
                    "    color: #20B2AA; " +
                    "    font-weight: 500; " +
                    "  }" +
                    "  .footer { " +
                    "    font-size: 13px; " +
                    "    color: #6c757d; " +
                    "    text-align: center; " +
                    "    margin-top: 40px; " +
                    "    padding-top: 20px; " +
                    "    border-top: 1px solid #dee2e6; " +
                    "  }" +
                    "  .divider { " +
                    "    height: 1px; " +
                    "    background: #dee2e6; " +
                    "    margin: 30px 0; " +
                    "  }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <div class='header'>" +
                    "      <div class='title'>임시 비밀번호 발급 안내</div>" +
                    "    </div>" +
                    "    <div class='content'>" +
                    "      <div class='message-box'>" +
                    "        <p>안녕하세요. <span class='highlight'>BillIt</span> 서비스를 이용해 주셔서 감사합니다.</p>" +
                    "        <p>회원님의 계정 보안을 위한 임시 비밀번호가 발급되었습니다.</p>" +
                    "      </div>" +
                    "      <div class='password-box'>" + tempPassword + "</div>" +
                    "      <div class='warning'>" +
                    "        ⚠️ <strong>보안을 위한 주의사항</strong><br>" +
                    "        • 임시 비밀번호로 로그인 후 반드시 새로운 비밀번호로 변경해 주세요.<br>" +
                    "        • 본 이메일의 임시 비밀번호는 24시간 동안만 유효합니다." +
                    "      </div>" +
                    "      <div class='divider'></div>" +
                    "      <p style='font-size: 13px; color: #666; text-align: center;'>" +
                    "        본 메일은 발신전용이며, 문의사항은 <span class='highlight'>고객센터</span>를 이용해 주시기 바랍니다." +
                    "      </p>" +
                    "    </div>" +
                    "    <div class='footer'>" +
                    "      <p>&copy; 2024 BillIt. All rights reserved.</p>" +
                    "      <p>본 메일은 회원님의 요청으로 발송된 이메일입니다.</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);
            emailSender.send(mimeMessage);

            log.info("Email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }
}