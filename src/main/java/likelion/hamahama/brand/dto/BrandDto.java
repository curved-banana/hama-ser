package likelion.hamahama.brand.dto;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private Long brandId;
    private Category category;
    private String brandName;
    private String brandImgUrl;
    private String brandEnglishName;

//    public BrandDto(Category category, String brandName, String brandImgUrl) {
//        this.category = category;
//        this.brandName = brandName;
//        this.brandImgUrl = brandImgUrl;
//    }


    public BrandDto(Brand brand){
        this.brandId = brand.getId();
        this.category = brand.getCategory();
        this.brandName = brand.getBrandName();
        this.brandImgUrl = brand.getBrandImgUrl();
        this.brandEnglishName = brand.getBrandEnglishName();
    }
//    public BrandDto(String brandName, String brandImgUrl) {
//        this.brandName = brandName;
//        this.brandImgUrl = brandImgUrl;
//    }


}
