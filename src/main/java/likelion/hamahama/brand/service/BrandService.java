package likelion.hamahama.brand.service;

import likelion.hamahama.brand.dto.BrandDto;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.brand.repository.BrandLikeRepsitory;
import likelion.hamahama.brand.repository.BrandRepository;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandLikeRepsitory brandLikeRepsitory;

    public List<Brand> findAll_brand(){
        return brandRepository.findAll();
    }

    public Brand findBrandById(long theId){
        return brandRepository.findById(theId);
    }
    // DTO 받아와서 브랜드 저장하기
    @Transactional
    public void saveBrand(BrandDto brandDto){
        Brand tempBrand = new Brand(brandDto.getCategory(),
                brandDto.getBrandName(),
                brandDto.getBrandImgUrl());

        brandRepository.save(tempBrand);
    }

    // DTO를 받아와서 브랜드 업데이트
    @Transactional
    public void updateBrand(BrandDto brandDto, long brandId){

        Brand tempBrand = brandRepository.findById(brandId);

        tempBrand.setBrandName(brandDto.getBrandName());
        tempBrand.setCategory(brandDto.getCategory());
        tempBrand.setBrandImgUrl(brandDto.getBrandImgUrl());

        brandRepository.save(tempBrand);
    }

    // 브랜드 ID 기반으로 삭제
    @Transactional
    public void deleteById(long theId){
        brandRepository.deleteById(theId);
    }

    //===============================
    // 브랜드 즐겨찾기
    @Transactional
    public void createBrandFavorite(User user, Brand brand){
        BrandLike createdBrandLike = new BrandLike(user, brand);
        brandLikeRepsitory.save(createdBrandLike);
    }
    @Transactional
    public void deleteBrandFavorite(User user, Brand brand){
        BrandLike brandLike = brandLikeRepsitory.findOneByUserAndBrand(user,brand);
        brandLike.disLike();
        brandLikeRepsitory.delete(brandLike);

    }

}
