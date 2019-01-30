package me.aboullaite.controller;

import me.aboullaite.model.User;
import me.aboullaite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public String register(@RequestBody User user) {
		User result = userService.findByEmail(user.getEmail());
		if(result!=null){
			return "Account already exists";
		}else{
			String token = "Authorization: Basic " +
					Base64Utils.encodeToString(
							String.format("%s:%s", user.getEmail(), user.getPassword())
									.getBytes()
					);
			userService.save(user);
			return token;
		}
	}

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String get(@RequestHeader("Authorization") String auth) {
		System.out.println(auth);
		if ((auth != null) && (auth.length() > 6)) {
            byte[] b = Base64.getDecoder().decode(auth);
            String[] info = new String(b).split(":");
            User user = userService.findByEmail(info[0]);
            if(user == null){
                return "not found user";
            }else if(!user.getPassword().equals(info[1])){
                return String.format("password not correct: %s, %s", info[1], user.getPassword());
            }else{
                Date d = new Date();
                return String.valueOf(d.getTime());
            }

        }
		return "not logged in, no token";
	}
}
