package likelion.hamahama.brand.controller;

import likelion.hamahama.brand.dto.BrandDto;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.service.BrandService;
import likelion.hamahama.coupon.entity.Coupon;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BrandController {
    private BrandService brandService;

    public BrandController(BrandService theBrandService){
        brandService = theBrandService;
    }

    // 모든 브랜드 조회
    @GetMapping("/brands")
    public List<Brand> findAll_brand() {
        return brandService.findAll_brand();
    }

    // 브랜드 상세 조회
    @GetMapping("/brands/{brandId}")
    public BrandDto getBrand(@PathVariable long brandId){
        BrandDto theBrandDTO = new BrandDto(brandService.findBrandById(brandId));

        if(theBrandDTO == null){
            throw new RuntimeException("브랜드가 발견 되지 않았습니다 - " + brandId);
        }

        return theBrandDTO;
    }

    // 브랜드 생성
    @PostMapping("/brands")
    public void addBrand(@RequestBody BrandDto theBrandDTO){
        theBrandDTO.setBrandId(0);

        brandService.saveBrand(theBrandDTO);
    }

    // 브랜드 수정
    @PutMapping("/brands/{brandId}")
    public void updateBrand(@PathVariable long brandId, @RequestBody BrandDto theBrandDTO){
        brandService.updateBrand(theBrandDTO, brandId);
    }

    // 브랜드 삭제
    @DeleteMapping("/brands/{brandId}")
    public String deleteBrand(@PathVariable long brandId){
        Brand tempBrand = brandService.findBrandById(brandId);

        if(tempBrand == null){
            throw new RuntimeException("브랜드가 발견 되지 않았습니다 - " + brandId);
        }

        List<Coupon> coupons = tempBrand.getCoupons();

        for(Coupon tempCoupon : coupons){
            tempCoupon.setBrand(null);
        }

        brandService.deleteById(brandId);

        return "브랜드가 삭제 되었습니다 - " + brandId;
    }
}
