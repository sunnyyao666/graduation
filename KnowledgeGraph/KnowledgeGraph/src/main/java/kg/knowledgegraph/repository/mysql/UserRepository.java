package kg.knowledgegraph.repository.mysql;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import kg.knowledgegraph.domain.mysql.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
