package me.aboullaite.controller;

import me.aboullaite.model.User;
import me.aboullaite.service.UserService;
import me.aboullaite.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Base64;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public StringResponse register(@RequestBody User user, HttpServletResponse response) {
		User result = userService.findByEmail(user.getEmail());
		if(result!=null){
			response.setStatus(HttpStatus.CONFLICT.value());
			return new StringResponse("Account already exists");
		}else{
			String token = "Authorization Token: " +
					Base64Utils.encodeToString(
							String.format("%s:%s", user.getEmail(), user.getPassword())
									.getBytes()
					);
			user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
			userService.save(user);
			return new StringResponse(token);
		}
	}

	@RequestMapping(value="/", method = RequestMethod.GET)
	@ResponseBody
	public StringResponse get(@RequestHeader("Authorization") String auth, HttpServletResponse response) {
		if ((auth != null) && (auth.length() > 0)) {
            byte[] b = Base64.getDecoder().decode(auth);
            String[] info = new String(b).split(":");
            User user = userService.findByEmail(info[0]);
            if(user == null){
				response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new StringResponse("user not found");
			}else if(!PasswordUtil.verifyPassword(info[1],user.getPassword())){
				response.setStatus(HttpStatus.BAD_REQUEST.value());
                String res = String.format("password not correct: %s, %s", info[1], user.getPassword());
                return new StringResponse(res);
            }else{
                LocalDateTime d = LocalDateTime.now();
                return new StringResponse(d.toString());
            }

        }
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		return new StringResponse("not logged in, no token");
	}
	private class StringResponse {

		private String response;

		public StringResponse(String s) {
			this.response = s;
		}

		public String getResponse() {
			return response;
		}
	}
}