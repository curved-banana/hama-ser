package likelion.hamahama.user.dto;

import com.google.firebase.messaging.FcmOptions;
import com.google.firebase.messaging.Notification;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FCMMessageDto {
    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Message{
        private Notification notification;
        private String topic;
        private Webpush webpush;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Webpush{
        private FcmOptions fcm_options;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class FcmOptions{
        private String link;
    }

}
