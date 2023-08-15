package likelion.hamahama.user.service;

import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class RegisterMail{

    @Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    private final SpringTemplateEngine templateEngine;

    @Autowired
    private final UserRepository userRepository;
    private String ePw;

    @Transactional
    private MimeMessage createReceiveCodeMessage(String code, String email) throws Exception{
        Optional<User> user = userRepository.findByEmail(email);
        String nickname = user.get().getNickname();

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("하마하마");

        message.setText(setReceiveCodeContext(code, nickname), "utf-8", "html");

        message.setFrom(new InternetAddress("hamahama0818@naver.com"));

        return message;
    }
    @Transactional
    private MimeMessage createPasswordChangeMessage(String email) throws Exception{
        Optional<User> user = userRepository.findByEmail(email);
        String nickname = user.get().getNickname();

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("하마하마");

        message.setText(setPasswordChangeContext(nickname), "utf-8", "html");

        message.setFrom(new InternetAddress("hamahama0818@naver.com"));

        return  message;
    }
    @Transactional
    public String setReceiveCodeContext(String code, String nickname){
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("nickname", nickname);
        return templateEngine.process("mail", context);
    }
    @Transactional
    public String setPasswordChangeContext(String nickname){
        Context context = new Context();
        context.setVariable("nickname", nickname);
        return templateEngine.process("passwordChange", context);
    }
    @Transactional
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

    @Transactional
    public String sendReceiveCodeMessage(String email)  throws Exception {
        ePw = createKey();

        MimeMessage message = createReceiveCodeMessage(ePw, email);
        try { //예외처리
            javaMailSender.send(message);
        }catch(MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
    @Transactional
    public void sendPasswordResetUrl(String email)  throws Exception {
        ePw = createKey();

        MimeMessage message = createPasswordChangeMessage(email);
        try { //예외처리
            javaMailSender.send(message);
        }catch(MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }







}
