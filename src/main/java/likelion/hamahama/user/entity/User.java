package likelion.hamahama.user.entity;


import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.coupon.entity.CouponLike;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @Column(name = "nickname", nullable = true, unique = true)
    private String nickname;


    @Column(name = "email", nullable = true, unique = true)
    private String email;

    private String password;

    private String provider; // 소셜 로그인 계정 구분

    @Column(name="fcm_token")
    private String fcmToken;

    @Column(name="fcm_status")
    private Boolean fcmStatus;

    @Convert(converter = StringListConverter.class)
    private List<String> favoriteBrands = new ArrayList<>();


//    //@ElementCollection(fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<String> roles = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(
            mappedBy ="user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
//    @Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size = 10)
    private List<CouponLike> likeCoupons = new ArrayList<>();

/**추가 */
    @OneToMany(
            mappedBy ="user",
            fetch =  FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BrandLike> likeBrand = new ArrayList<>();
}
