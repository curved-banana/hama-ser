package likelion.hamahama.admin.controller;

import likelion.hamahama.admin.service.AdminService;
import likelion.hamahama.coupon.dto.CouponDto;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.coupon.service.CouponService;
import likelion.hamahama.user.repository.UserRepository;
import likelion.hamahama.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final UserService userService;
    private final UserRepository userRepository;
    private  final AdminService adminService;

    @RequestMapping("/admin")
    private String main() {
        return "admin/main";
    }

    //================ 쿠폰 관리

    //쿠폰 목록 조회 -> 닉네임 회원이메일 등록된 쿠폰명만 출력
    @GetMapping("/admin/coupons")
    private String couponList(Model model){
        List<Coupon> couponList = adminService.findAll_coupon();
        model.addAttribute("couponlist", couponList);

        return "admin/coupons";
    }
    //쿠폰 정보 수정 - form : admin/coupons/{couponId}/edit
    //@GetMapping("")

    //쿠폰 정보 수정 - post
    //@PostMapping("")
    //쿠폰 정보 삭제
    @GetMapping("admin/coupons/{couponId}/delete")
    public String deleteCoupon(@PathVariable("couponId") Long couponId){
        adminService.deleteById(couponId);

        return "redirect:/admin/coupons";
    }

   //==================== 브랜드 관리  =================
    //브랜드 등록

}
