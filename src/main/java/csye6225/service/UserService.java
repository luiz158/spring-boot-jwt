package csye6225.service;

import csye6225.model.User;

public interface UserService {
	User save(User user);

	User findByEmail(String email);

}
