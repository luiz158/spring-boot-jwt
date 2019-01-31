package csye6225.dao;

import csye6225.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
	User save(User user);
    // generate query automatically
	User findByEmail(String email);
}
