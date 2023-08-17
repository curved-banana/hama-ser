package likelion.hamahama.brand.repository;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandLikeRepsitory extends JpaRepository<BrandLike, Long> {

    BrandLike findOneByUserAndBrand(User user, Brand brand);
   // BrandLike findByUserAndBrand(User user, Brand brand);
    List<BrandLike> findByUser(User user);
//    List<BrandLike> findByUser(User user);

    List<Long> findByUserId(Long UserId);

    Optional<List<BrandLike>> findByBrand(Brand brand);

//    BrandLike deleteByUserIdAndBrandId(Long userId, Long brandId);
}
