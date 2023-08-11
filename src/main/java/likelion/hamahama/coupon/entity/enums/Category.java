package likelion.hamahama.coupon.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    식당("Category_food","식당"),
    카페("Category_cafe","카페"),
    영화("Category_movie","영화"),
    놀이공원("Category_shopping","놀이공원"),
    게임("Category_culture","게임"),
    옷("Category_cloth","옷"),
    신발("Category_shoes","신발"),
    화장품("Category_cosmetics","화장품");

    private final String key;
    private final String title;
}

