package likelion.hamahama.admin;

import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/")
public class Controller {

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestParam String email){
        return new ResponseEntity<>(userService.findUserOne(email), HttpStatus.OK);
    }
}
