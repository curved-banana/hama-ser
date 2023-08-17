package likelion.hamahama.coupon.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    식당("Category_food","식당"),
    카페("Category_cafe","카페"),
    여행("Category_trip","여행"),
    취미("Category_hobby","취미"),
    옷("Category_cloth","옷"),
    신발("Category_shoes","신발"),
    화장품("Category_cosmetics","화장품");

    private final String key;
    private final String title;
}

