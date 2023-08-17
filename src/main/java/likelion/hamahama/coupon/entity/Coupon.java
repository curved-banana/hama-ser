package likelion.hamahama.coupon.entity;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.BaseTimeEntity;
import likelion.hamahama.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name="coupon_table")
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_id")
    private Long couponId;

    // 쿠폰이름
    @Column(name="CouponName")
    private String couponName;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;

    // 쿠폰코드
    @Column(name="CouponCode")
    private String couponCode;

    private String couponUrl;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    //== 쿠폰 유효기간 ==
    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

//<<<<<<< Updated upstream
//    @OneToMany(
//            mappedBy = "coupon",
//            cascade =  CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<CouponLike> likeUsers = new ArrayList<>();
//=======
//>>>>>>> Stashed changes

    @Column(name="popularity")
    @ColumnDefault("0")
    private int popularity;

    /**추가 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;


     //일반적으로 사용
    public Coupon(String couponName ,String couponCode, String couponUrl, String startDate, String endDate, String description, int popularity) {

        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponUrl = couponUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;

        this.popularity = popularity;
    }
}
