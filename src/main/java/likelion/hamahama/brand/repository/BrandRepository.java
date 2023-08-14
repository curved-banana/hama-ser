package likelion.hamahama.brand.repository;

import likelion.hamahama.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {


    List<Brand> findAll();

    Brand findById(long theId);

    Brand findByBrandName(String theName);
}
