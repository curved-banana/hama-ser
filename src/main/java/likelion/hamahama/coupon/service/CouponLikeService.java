package likelion.hamahama.coupon.service;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.repository.CouponLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 즐겨찾기 및 만족-불만족
@Service
@RequiredArgsConstructor
public class CouponLikeService {
    private final CouponLikeRepository couponLikeRepository;
    // ============ 즐겨찾기 ====================
    @Transactional
    public void createCouponLike(User user, Coupon coupon){
        CouponLike createdCouponLike = new CouponLike(user,coupon);
        couponLikeRepository.save(createdCouponLike);
    }
    @Transactional
    public void deleteCouponLike(User user, Coupon coupon){
        CouponLike couponLike = couponLikeRepository.findOneByUserAndCoupon(user,coupon);
        couponLike.dislike();
        couponLikeRepository.delete(couponLike);
    }
    // =============후기 작성 시 만족.불만족에 따라서 likeCount 업다운( 인기순 )==================
    @Transactional
    public void satisfied(Long couponId){
        couponLikeRepository.increaseLikeCount(couponId);
    }
    @Transactional
    public void unsatisfied(Long couponId){
        couponLikeRepository.decreaseLikeCount(couponId);
    }

    //==========마이페이지 즐겨찾기 한 쿠폰 가져오기 ============//


}
