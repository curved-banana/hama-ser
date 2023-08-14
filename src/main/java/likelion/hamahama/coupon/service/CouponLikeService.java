package likelion.hamahama.coupon.service;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.repository.CouponLikeRepository;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 즐겨찾기 및 만족-불만족
@Service
@RequiredArgsConstructor
public class CouponLikeService {

    private final CouponLikeRepository couponLikeRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

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

    @Transactional
    public Page<CouponDetailDto> getLikedCoupon(Long userId, Pageable pageable){
        User user = userRepository.findById(userId).get();
        List<CouponLike> couponLikes = couponLikeRepository.findByUser(user);
        Page<Coupon> coupons = couponRepository.findAllByLikeUsersIn(couponLikes,pageable);
        Page<CouponDetailDto> couponDto = coupons.map( c -> new CouponDetailDto(c));
        return couponDto;
    }
}
