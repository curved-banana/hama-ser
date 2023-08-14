package likelion.hamahama.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class RegisterMail{

    @Autowired
    private final JavaMailSender javaMailSender;
    //private final SpringTemplateEngine templateEngine;
    private String ePw;

    private MimeMessage createMessage(String code, String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("하마하마 회원가입 이메일 인증");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1 style='margin-bottom:55px;'> 안녕하세요, '하마하마'입니다</h1>";
        msgg += "<p> 00님, 안녕하세요.</p>";
        msgg += "<p>하마하마를 이용해주셔서 감사합니다.<p>";
        msgg += "<p>고객님께서 인증번호를 요청하셨습니다.<p>";
        msgg += "<p>아래 코드를 이용하여 인증을 해주시면 서비스 이용이 가능합니다.<p>";
        msgg += "<div style='border:none; text-align:center;" +
                "font-family:verdana; background-color:#EFF9FF; '>";
        msgg += "<div style='margin-top:30px; font-size:130%'>";
        msgg += "인증번호 <strong>";
        msgg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");

        message.setFrom(new InternetAddress("junho308917@naver.com"));

        return  message;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    public String sendSimpleMessage(String email)  throws Exception {
        ePw = createKey();

        MimeMessage message = createMessage(ePw, email);
        try { //예외처리
            javaMailSender.send(message);
        }catch(MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }







}
