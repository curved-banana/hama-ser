package likelion.hamahama.coupon.entity;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.BaseTimeEntity;
import likelion.hamahama.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name="coupon_table")
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_id")
    private Long id;

    // 쿠폰이름
    @Column(name="CouponName")
    private String couponName;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    private Category category;

    // 쿠폰코드
    @Column(name="CouponCode")
    private String couponCode;

    private String couponUrl;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    //== 쿠폰 유효기간 ==
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @OneToMany(
            mappedBy = "coupon",
            cascade =  CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CouponLike> likeUsers = new ArrayList<>();

    @Column(name="likeCount")
    @ColumnDefault("0")
    private int likeCount;

    /**추가 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;

    // 일반적으로 사용
    public Coupon(String couponName, Category category, String couponCode, String couponUrl, LocalDate startDate, LocalDate endDate, String description, int likeCount) {
        this.couponName = couponName;
        this.category = category;
        this.couponCode = couponCode;
        this.couponUrl = couponUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.likeCount = likeCount;
    }
}
