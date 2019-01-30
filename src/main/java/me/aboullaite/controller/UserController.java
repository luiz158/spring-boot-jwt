package me.aboullaite.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import me.aboullaite.model.User;
import me.aboullaite.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sun.misc.BASE64Decoder;

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
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				byte[] b = decoder.decodeBuffer(auth);
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
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
				return "not logged in";
			}

		}
		return "not login, no token";
	}
}
