package kg.knowledgegraph.repository.mysql;

import kg.knowledgegraph.domain.mysql.StarPPT;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarPPTRepository extends CrudRepository<StarPPT, Long> {
    void deleteByPptIDAndUser_Id(Long PPTId, Long userID);

    List<StarPPT> findAllByUser_IdOrderByCreateTimeDesc(Long userID);
}
