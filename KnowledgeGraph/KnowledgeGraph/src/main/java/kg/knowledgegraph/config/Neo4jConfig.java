package kg.knowledgegraph.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author YHT
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "kg.knowledgegraph.repository.neo4j")
@EnableTransactionManagement
public class Neo4jConfig {

    @Value("${spring.neo4j.uri}")
    private String uri;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;

    @Bean("configurationNeo4j")
    public org.neo4j.ogm.config.Configuration configuration() {
        return new org.neo4j.ogm.config.Configuration.Builder().
                uri(uri).credentials(username, password).
                withBasePackages("kg.knowledgegraph.repository.neo4j").
                build();
    }

    @Bean
    public SessionFactory sessionFactory(@Qualifier("configurationNeo4j") org.neo4j.ogm.config.Configuration configuration) {
        return new SessionFactory(configuration, "kg.knowledgegraph.domain.neo4j");
    }

    @Bean("transactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
        return new Neo4jTransactionManager(sessionFactory);
    }
}
