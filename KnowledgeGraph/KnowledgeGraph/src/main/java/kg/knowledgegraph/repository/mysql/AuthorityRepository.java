package kg.knowledgegraph.repository.mysql;

import kg.knowledgegraph.domain.mysql.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
