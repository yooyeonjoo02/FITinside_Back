package com.team2.fitinside.coupon.service;

import com.team2.fitinside.coupon.dto.CouponEmailRequestDto;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponEmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(CouponEmailRequestDto dto) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 메일을 받을 수신자 설정
            mimeMessageHelper.setTo(dto.getAddress());

            // 메일의 제목 설정
            mimeMessageHelper.setSubject("FITinside 쿠폰 메일");

            // 발신자 설정
            mimeMessageHelper.setFrom("chm20060@gmail.com", "FITinside 관리자");

            // 메일의 내용 설정
            mimeMessageHelper.setText(dto.getTemplate(), true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_DATA);
        }
    }
}
