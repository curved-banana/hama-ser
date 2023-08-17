package likelion.hamahama.brand.repository;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {


    List<Brand> findAll();

    Optional<Brand> findByBrandId(Long brandId);
    Brand deleteByBrandId(Long brandId);


    Brand findByBrandName(String theName);

    Brand findByBrandEnglishName(String brandEnglishName);

    //추가
    Page<Brand> findAllByLikeUsersIn(List<BrandLike> brandLike, Pageable pageable);

    //카테고리별 브랜드 검색
    List<Brand> findByCategory(Category category);

    // 키워드 검색 후 브랜드 출력
    List<Brand> findByBrandNameContaining(String brandName);

    //Optional<BrandLike> findByUserAndBrand(User user, Brand brand);


}
