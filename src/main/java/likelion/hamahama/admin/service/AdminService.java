package likelion.hamahama.admin.service;

import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final CouponRepository couponRepository;

    // 쿠폰 전체 조회
    public List<Coupon> findAll_coupon() {
        return couponRepository.findAll();
    }
    // ID 기반으로 쿠폰 삭제
    @Transactional
    public void deleteById(long theId) {
        couponRepository.deleteById(theId);
    }


}
