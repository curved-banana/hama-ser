package likelion.hamahama.brand.repository;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandLikeRepsitory extends JpaRepository<BrandLike, Long> {

    BrandLike findOneByUserAndBrand(User user, Brand brand);
   // BrandLike findByUserAndBrand(User user, Brand brand);
}
