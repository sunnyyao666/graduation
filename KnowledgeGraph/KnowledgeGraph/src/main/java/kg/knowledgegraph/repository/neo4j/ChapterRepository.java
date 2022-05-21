package kg.knowledgegraph.repository.neo4j;

import kg.knowledgegraph.domain.neo4j.Chapter;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ChapterRepository extends Neo4jRepository<Chapter, Long> {
}
