package kg.knowledgegraph.repository.mysql;

import kg.knowledgegraph.domain.mysql.StarMP4;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarMP4Repository extends CrudRepository<StarMP4, Long> {
    void deleteByMp4IDAndUser_Id(Long mp4ID, Long userID);

    List<StarMP4> findAllByUser_IdOrderByCreateTimeDesc(Long userID);
}
