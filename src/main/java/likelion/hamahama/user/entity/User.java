package likelion.hamahama.user.entity;


import likelion.hamahama.coupon.entity.CouponLike;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_table")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = true, unique = true)
    private String nickname;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    private String password;

    private String provider; // 소셜 로그인 계정 구분

    @Column(name="fcm_token")
    private String fcmToken;

    @Convert(converter = StringListConverter.class)
    private List<String> favoriteBrands = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public User update(String name, String email){
        this.name = name;
        this.email = email;

        return this;
    }
    @OneToMany(
            mappedBy ="user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CouponLike> likeCoupons = new ArrayList<>();

}
