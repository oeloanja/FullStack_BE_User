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

            helper.setFrom("billit.project@gmail.com");
            helper.setTo(to);
            helper.setSubject("[BillIt] 임시 비밀번호가 발급되었습니다");

            String content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>임시 비밀번호가 발급되었습니다</title>" +
                    "</head>" +
                    "<body style='margin: 0; padding: 0; font-family: Arial, \"Malgun Gothic\", sans-serif;'>" +
                    "  <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f8f9fa;'>" +
                    "    <tr>" +
                    "      <td align='center' style='padding: 40px 0;'>" +
                    "        <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border: 1px solid #dee2e6;'>" +
                    "          <tr>" +
                    "            <td style='padding: 40px;'>" +
                    "              <h1 style='color: #40E0D0; font-size: 28px; font-weight: bold; margin: 20px 0; text-align: center;'>임시 비밀번호 발급 안내</h1>" +
                    "              <div style='margin: 30px 0; padding: 20px; background-color: #f8f9fa; border-left: 4px solid #40E0D0;'>" +
                    "                <p style='margin: 0;'>안녕하세요. <span style='color: #20B2AA; font-weight: bold;'>BillIt</span> 서비스를 이용해 주셔서 감사합니다.</p>" +
                    "                <p style='margin: 10px 0 0 0;'>회원님의 계정 보안을 위한 임시 비밀번호가 발급되었습니다.</p>" +
                    "              </div>" +
                    "              <div style='background: #40E0D0; color: white; padding: 25px; margin: 30px 0; text-align: center; font-weight: bold; font-size: 24px; letter-spacing: 2px;'>" +
                    tempPassword +
                    "              </div>" +
                    "              <div style='background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404; padding: 15px; margin: 25px 0; font-size: 14px;'>" +
                    "                ⚠️ <strong>보안을 위한 주의사항</strong><br>" +
                    "                • 임시 비밀번호로 로그인 후 반드시 새로운 비밀번호로 변경해 주세요.<br>" +
                    "                • 본 이메일의 임시 비밀번호는 24시간 동안만 유효합니다." +
                    "              </div>" +
                    "              <hr style='border: none; height: 1px; background-color: #dee2e6; margin: 30px 0;'>" +
                    "              <p style='font-size: 13px; color: #666; text-align: center; margin: 0;'>" +
                    "                본 메일은 발신전용이며, 문의사항은 <span style='color: #20B2AA; font-weight: bold;'>고객센터</span>를 이용해 주시기 바랍니다." +
                    "              </p>" +
                    "            </td>" +
                    "          </tr>" +
                    "          <tr>" +
                    "            <td style='padding: 20px 40px; text-align: center; border-top: 1px solid #dee2e6;'>" +
                    "              <p style='font-size: 13px; color: #6c757d; margin: 0;'>&copy; 2024 BillIt. All rights reserved.</p>" +
                    "              <p style='font-size: 13px; color: #6c757d; margin: 10px 0 0 0;'>본 메일은 회원님의 요청으로 발송된 이메일입니다.</p>" +
                    "            </td>" +
                    "          </tr>" +
                    "        </table>" +
                    "      </td>" +
                    "    </tr>" +
                    "  </table>" +
                    "</body>" +
                    "</html>";

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

            String content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>이메일 인증 코드</title>" +
                    "</head>" +
                    "<body style='margin: 0; padding: 0; font-family: Arial, \"Malgun Gothic\", sans-serif;'>" +
                    "  <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f8f9fa;'>" +
                    "    <tr>" +
                    "      <td align='center' style='padding: 40px 0;'>" +
                    "        <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border: 1px solid #dee2e6;'>" +
                    "          <tr>" +
                    "            <td style='padding: 40px;'>" +
                    "              <h1 style='color: #40E0D0; font-size: 28px; font-weight: bold; margin: 20px 0; text-align: center;'>이메일 인증 코드 안내</h1>" +
                    "              <div style='margin: 30px 0; padding: 20px; background-color: #f8f9fa; border-left: 4px solid #40E0D0;'>" +
                    "                <p style='margin: 0;'>안녕하세요. <span style='color: #20B2AA; font-weight: bold;'>BillIt</span> 서비스입니다.</p>" +
                    "                <p style='margin: 10px 0 0 0;'>회원가입을 위한 인증 코드를 안내드립니다.</p>" +
                    "              </div>" +
                    "              <div style='background: #40E0D0; color: white; padding: 25px; margin: 30px 0; text-align: center; font-weight: bold; font-size: 24px; letter-spacing: 2px;'>" +
                    verificationCode +
                    "              </div>" +
                    "              <div style='background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404; padding: 15px; margin: 25px 0;'>" +
                    "                <p style='margin: 0; font-weight: bold;'>⚠️ 인증 코드는 10분간만 유효합니다.</p>" +
                    "                <p style='margin: 10px 0 0 0;'>본인이 요청하지 않은 경우 이 메일을 무시하셔도 됩니다.</p>" +
                    "              </div>" +
                    "              <hr style='border: none; height: 1px; background-color: #dee2e6; margin: 30px 0;'>" +
                    "              <p style='font-size: 13px; color: #666; text-align: center; margin: 0;'>" +
                    "                본 메일은 발신전용이며, 문의사항은 <span style='color: #20B2AA; font-weight: bold;'>고객센터</span>를 이용해 주시기 바랍니다." +
                    "              </p>" +
                    "            </td>" +
                    "          </tr>" +
                    "          <tr>" +
                    "            <td style='padding: 20px 40px; text-align: center; border-top: 1px solid #dee2e6;'>" +
                    "              <p style='font-size: 13px; color: #6c757d; margin: 0;'>&copy; 2024 BillIt. All rights reserved.</p>" +
                    "            </td>" +
                    "          </tr>" +
                    "        </table>" +
                    "      </td>" +
                    "    </tr>" +
                    "  </table>" +
                    "</body>" +
                    "</html>";

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