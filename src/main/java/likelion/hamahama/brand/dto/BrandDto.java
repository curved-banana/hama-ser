package likelion.hamahama.brand.dto;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
    private long brandId;
    private Category category;
    private String brandName;
    private String brandImgUrl;

    public BrandDto(Category category, String brandName, String brandImgUrl, boolean favoirteStatus) {
        this.category = category;
        this.brandName = brandName;
        this.brandImgUrl = brandImgUrl;
    }


    public BrandDto(Brand brand){
        this.brandId = brand.getBrandId();
        this.category = brand.getCategory();
        this.brandName = brand.getBrandName();
        this.brandImgUrl = brand.getBrandImgUrl();
        this.favoirteStatus = brand.isFavoriteStatus();
    }
    public BrandDto(String brandName, String brandImgUrl) {
        this.brandName = brandName;
        this.brandImgUrl = brandImgUrl;
    }


}
