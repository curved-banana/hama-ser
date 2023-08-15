package likelion.hamahama.brand.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brand_table")
public class Brand {

    // 브랜드 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="brand_id")
    private Long id;

    // 카테고리(enum으로 선언하기)
    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private Category category;

    // 브랜드 이름
    @Column(name="brand_name")
    private String brandName;

    @Column(name="brand_english_name")
    private String brandEnglishName;

    // 브랜드 이미지 URL
    @Column(name="brand_img_url")
    private String brandImgUrl;

    @Column(name="favorites_status")
    @ColumnDefault("0")
    private boolean favoriteStatus;

    @OneToMany(mappedBy = "brand",  cascade = CascadeType.ALL)
    private List<Coupon> coupons;

    /**추가*/
    @OneToMany(
            mappedBy = "brand",
            cascade =  CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BrandLike> likeUsers = new ArrayList<>();
    /**========================*/
//    public Brand(){
//    }

    public Brand(Category category, String brandName, String brandImgUrl) {
        this.category = category;
        this.brandName = brandName;
        this.brandImgUrl = brandImgUrl;
    }
    /** (추가) 즐겨찾기 상태 변화 */
    public void updateFavoriteStatus(){
        if(!likeUsers.isEmpty()){
            favoriteStatus = true;
        } else{
            favoriteStatus = false;
        }
    }



}
