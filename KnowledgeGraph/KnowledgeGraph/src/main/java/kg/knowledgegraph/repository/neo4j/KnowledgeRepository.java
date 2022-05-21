package kg.knowledgegraph.repository.neo4j;

import kg.knowledgegraph.domain.neo4j.Knowledge;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KnowledgeRepository extends Neo4jRepository<Knowledge, Long> {
}
