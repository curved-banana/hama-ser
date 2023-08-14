package likelion.hamahama.brand.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brand_table")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Brand {

    // 브랜드 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="brand_id")
    private long Id;

    // 카테고리(enum으로 선언하기)
    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private Category category;

    // 브랜드 이름
    @Column(name="brand_name")
    private String brandName;

    // 브랜드 이미지 URL
    @Column(name="brand_img_url")
    private String brandImgUrl;

    @Column(name="favorites_status")
    @ColumnDefault("0")
    private boolean favoriteStatus;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Coupon> coupons;

    public Brand(){

    }

    public Brand(Category category, String brandName, String brandImgUrl) {
        this.category = category;
        this.brandName = brandName;
        this.brandImgUrl = brandImgUrl;
    }
//    @Builder
//    public Brand(long brandId, Category category, String brandName, String brandImgUrl) {
//        this.Id = brandId;
//        this.category = category;
//        this.brandName = brandName;
//        this.brandImgUrl = brandImgUrl;
//    }


}
