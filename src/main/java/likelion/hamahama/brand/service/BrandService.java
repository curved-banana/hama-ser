package likelion.hamahama.brand.service;

import likelion.hamahama.brand.dto.BrandDto;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.brand.repository.BrandLikeRepsitory;
import likelion.hamahama.brand.repository.BrandRepository;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    @Autowired
    private final BrandRepository brandRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BrandLikeRepsitory brandLikeRepsitory;

    @Transactional
    public List<Brand> findAll(){
        return brandRepository.findAll();
    }

    @Transactional
    public Optional<Brand> findBrandById(Long brandId){

        return brandRepository.findById(brandId);
    }

    // 카테고리에 맞는 브랜드 불러오기
    @Transactional
    public List<Brand> findByCategory(Category category){
        return brandRepository.findByCategory(category);
    }

    // 브랜드이름에서 키워드 검색 후 결과 리스트 출력
    @Transactional
    public List<Brand> findByKeyword(String keyword){
        return brandRepository.findByBrandNameContaining(keyword);
    }

//    // DTO 받아와서 브랜드 저장하기
//    @Transactional
//    public void saveBrand(BrandDto brandDto){
//        Brand tempBrand = new Brand(brandDto.getCategory(),
//                brandDto.getBrandName(),
//                brandDto.getBrandImgUrl());
//
//        brandRepository.save(tempBrand);
//    }

    // DTO를 받아와서 브랜드 업데이트
    @Transactional
    public void updateBrand(BrandDto brandDto, Long brandId){

        Optional<Brand> tempBrand = brandRepository.findById(brandId);

        tempBrand.get().setBrandName(brandDto.getBrandName());
        tempBrand.get().setCategory(brandDto.getCategory());
        tempBrand.get().setBrandImgUrl(brandDto.getBrandImgUrl());

        brandRepository.save(tempBrand.get());
    }

    // 브랜드 ID 기반으로 삭제
    @Transactional
    public void deleteById(long theId){
        brandRepository.deleteById(theId);
    }

    //===============================
    // 브랜드 즐겨찾기
//    @Transactional
//    public void createBrandFavorite(User user, Brand brand){
//        BrandLike createdBrandLike = new BrandLike(user, brand);
//        brandLikeRepsitory.save(createdBrandLike);
//    }

    @Transactional
    public void createBrandFavorite(User user, Brand brand){

        BrandLike brandLike = BrandLike.builder()
                .user(user)
                .brand(brand)
                .build();
        brandLikeRepsitory.save(brandLike);
    }

    @Transactional
    public void deleteBrandFavorite(User user, Brand brand){
        BrandLike brandLike = brandLikeRepsitory.findOneByUserAndBrand(user,brand);
        brandLikeRepsitory.delete(brandLike);

    }
    // ================ 마이페이지에서 브랜드 즐겨찾기 불러오기 =======================
    /** 여기서는 브랜드아이디로 받아와도 되지 않나?*/
//    @Transactional
//    public Page<BrandDto> getLikedBrand(Long userId, Pageable pageable){
//        User user = userRepository.findById(userId).get();
//        List<BrandLike> brandLikes = brandLikeRepsitory.findByUser(user);
//        Page<Brand> brands = brandRepository.findAllByLikeUsersIn(brandLikes, pageable);
//        Page<BrandDto> brandDto = brands.map(c-> new BrandDto(c));
//        return brandDto;
//    }

    @Transactional
    public List<Brand> getLikedBrand(Long userId){
        List<Brand> brands = new ArrayList<>();
        User user = userRepository.findById(userId).get();
        List<Long> userIdList = brandLikeRepsitory.findByUserId(user.getId());
        userIdList.forEach(brandId-> {
            Optional<Brand> brand = brandRepository.findById(brandId);
            brands.add(brand.get());
        });

        return brands;
    }
}
