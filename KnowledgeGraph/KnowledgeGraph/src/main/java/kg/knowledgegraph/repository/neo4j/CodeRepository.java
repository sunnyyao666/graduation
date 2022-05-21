package kg.knowledgegraph.repository.neo4j;

import kg.knowledgegraph.domain.neo4j.Code;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CodeRepository extends Neo4jRepository<Code, Long> {
}
