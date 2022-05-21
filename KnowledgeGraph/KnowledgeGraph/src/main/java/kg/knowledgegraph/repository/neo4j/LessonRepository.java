package kg.knowledgegraph.repository.neo4j;

import kg.knowledgegraph.domain.neo4j.Lesson;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LessonRepository extends Neo4jRepository<Lesson, Long> {
    Lesson findByName(String name);
}
