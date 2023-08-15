package likelion.hamahama.brand.entity;

import likelion.hamahama.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="favorites_brand_table")
public class BrandLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

//    public BrandLike(User user, Brand brand){
//        this.user = user;
//        this.brand = brand;
//        user.getLikeBrand().add(this);
//        brand.getLikeUsers().add(this);
//        brand.updateFavoriteStatus();
//    }
//    public void disLike(){
//        this.user.getLikeBrand().remove(this);
//        this.user = null;
//        this.brand.getLikeUsers().remove(this);
//        this.brand = null;
//        this.brand.updateFavoriteStatus();
//    }
}
