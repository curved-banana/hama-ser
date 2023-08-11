package likelion.hamahama.user.service;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import likelion.hamahama.user.dto.SignRequest;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public List<User> findAll(){

        return userRepository.findAll();
    }

    public User findUserOne(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BadCredentialsException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

        return user;
    }

    public void updateUser(String email, SignRequest request){

        User user = userRepository.findByEmail(email).orElseThrow(()->
                new BadCredentialsException("존재하지 않는 이메일입니다."));

        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());

        System.out.println(user.getName());
        System.out.println(user.getNickname());
        System.out.println(user.getEmail());

        userRepository.save(user);

    }

    public void deleteUser(String email){

        userRepository.deleteByEmail(email);

    }
}
