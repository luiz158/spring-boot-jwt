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
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		return "Hello";
	}

//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public User registerUser(@RequestBody User user) {
//
//		return userService.save(user);
//	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestBody User user) {
        String token = "Authorization: Basic " +
				Base64Utils.encodeToString(
						String.format("%s:%s", user.getEmail(), user.getPassword())
								.getBytes()
				);
		userService.save(user);
		return token;
	}

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String get(@RequestHeader("Authorization") String auth) {
		System.out.println(auth);
		if ((auth != null) && (auth.length() > 6)) {

			//auth = auth.substring(6, auth.length());
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				byte[] b = decoder.decodeBuffer(auth);
				String[] info = new String(b).split(":");
				User user = userService.findByEmail(info[0]);
				System.out.println(user.getEmail());
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
				return "not login";
			}

		}
		return "not login, no token";
	}


//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String login(@RequestBody User login) throws ServletException {
//
//		String jwtToken = "";
//
//		if (login.getEmail() == null || login.getPassword() == null) {
//			throw new ServletException("Please fill in username and password");
//		}
//
//		String email = login.getEmail();
//		String password = login.getPassword();
//
//		User user = userService.findByEmail(email);
//
//		if (user == null) {
//			throw new ServletException("User email not found.");
//		}
//
//		String pwd = user.getPassword();
//
//		if (!password.equals(pwd)) {
//			throw new ServletException("Invalid login. Please check your name and password.");
//		}
//
//		jwtToken = Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(new Date())
//				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();
//
//
//		return jwtToken;
//	}
}
