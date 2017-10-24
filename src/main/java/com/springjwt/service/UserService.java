package com.springjwt.service;

import com.springjwt.model.User;

public interface UserService {
	User save(User user);

	User findByEmail(String email);

}
