package likelion.hamahama.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import likelion.hamahama.user.dto.FCMMessageDto;
import likelion.hamahama.user.dto.FcmRequest;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.brand.repository.BrandRepository;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final String API_URL = "https://fcm.googleapis.com//v1/projects/hamahama-sku20230818/messages:send";
    private final ObjectMapper objectMapper;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    public String getAccessToken() throws IOException {
        String firebaseConfigPath = "templates/firebase-admin.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        System.out.println("Access Token: " + googleCredentials.getAccessToken().getTokenValue());
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendMessageTo(String topic, String title, String body) throws IOException, FirebaseMessagingException{
        topicCreate();

        String message = makeMessage(topic, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody =  RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

        //sendWebPush(targetToken, title, body);
    }

//    public void sendWebPush(String targetToken, String title, String body) throws IOException{
//        String request = makeRequest(targetToken, title, body);
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody =  RequestBody.create(request,
//                MediaType.get("application/json; charset=utf-8"));
//        Request request2 = new Request.Builder()
//                .url("http://localhost:8088/notification/send")
//                .post(requestBody)
//                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                .build();
//        Response response2 = client.newCall(request2).execute();
//        System.out.println(response2.body().string());
//    }

    public String makeMessage(String topic, String title, String body) throws JsonProcessingException{
        FCMMessageDto fcmMessageDto = FCMMessageDto.builder()
                .message(FCMMessageDto.Message.builder()
                        .topic(topic)
                        .notification(FCMMessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .build())
                        .build())
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessageDto);
    }

    public void topicCreate() throws FirebaseMessagingException {

        List<String> brandNames = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        List<User> users = userRepository.findAll();

        brands.forEach(brand -> {
            if(brand.isFavoriteStatus()){
                brandNames.add(brand.getBrandName());
            }
        });

        for(int i=0; i<brandNames.size(); i++){
            List<String> registrationTokens = new ArrayList<>();
            String topic = brandNames.get(i);

            users.forEach(user -> {
                if(user.getFavoriteBrands().contains(topic)){
                    registrationTokens.add(user.getFcmToken());
                }
            });

            LinkedHashSet<String> li_hs = new LinkedHashSet<String>(registrationTokens);
            registrationTokens.clear();
            registrationTokens.addAll(li_hs);

            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(registrationTokens, topic);
        }
//        List<String> registrationTokens = Arrays.asList(
//                "f0oNEr_D5vTUKO6341XdcN:APA91bF8UTkXzVsPJIdX8oV-0wpqLbj_jObWX7O1X5ZkG5vlt56Yg4cHHtd2EeETzfNR28ZYelxWEZMGFvOOSHOoUKJnK68WlomKqFJDr2iliEHB3vwEMzAVVWxINxR5dKKHcXzQzwm0",
//                "fm840FzQmULuU2LDf3modu:APA91bG5c5g7jdIeuZpduBsbbW3NNt8O8HA7wJGxKTCP6YuVbsM8QBQYF8xlwmeDI0idBc-tcLmy8OMr1pROMZj3n6miU2lcaR9yKbIP9R0no4MGEiMx4YaKWjG2FXsx2P1sB4w4Xt5r"
//        );
//        String topic = "animal";
//        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(registrationTokens, topic);
//        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");
    }

//    private String makeRequest(String targetToken, String title, String body) throws IOException{
//        FcmRequest fcmRequest = FcmRequest.builder()
//                .token(targetToken)
//                .title(title)
//                .body(body)
//                .build();
//
//        return objectMapper.writeValueAsString(fcmRequest);
//    }

//    public String sendNotificationByToken(FCMMessageDto fcmMessageDto){
//
//        Notification notification = Notification.builder()
//                .setTitle(fcmMessageDto.getTitle())
//                .setBody(fcmMessageDto.getBody())
//                .build();
//
//        Message message = Message.builder()
//                .setToken(fcmMessageDto.getToken())
//                .setNotification(notification)
//                .build();
//
//        try{
//            firebaseMessaging.send(message);
//            System.out.println("message " + message);
//            return "Success Sending Notification";
//        }catch(FirebaseMessagingException e){
//            e.printStackTrace();
//            return "Error Sending Notification";
//        }
//    }

}
