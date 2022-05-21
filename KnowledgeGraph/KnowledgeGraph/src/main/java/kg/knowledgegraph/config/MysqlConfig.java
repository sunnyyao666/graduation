package kg.knowledgegraph.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * @author YHT
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryMysql", transactionManagerRef = "transactionManagerMysql", basePackages = {"kg.knowledgegraph.repository.mysql"})
public class MysqlConfig {
    private final DataSource mysqlDataSource;
    private final Map<String, Object> vendorProperties;

    @Autowired
    public MysqlConfig(@Qualifier("mysqlDataSource") DataSource mysqlDataSource, @Qualifier("vendorProperties") Map<String, Object> vendorProperties) {
        this.mysqlDataSource = mysqlDataSource;
        this.vendorProperties = vendorProperties;
    }

    @Primary
    @Bean(name = "entityManagerFactoryMysql")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryMysql(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(mysqlDataSource).properties(vendorProperties).packages("kg.knowledgegraph.domain.mysql").persistenceUnit("mysqlPersistenceUnit").build();
    }

    @Bean(name = "entityManagerMysql")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return Objects.requireNonNull(entityManagerFactoryMysql(builder).getObject()).createEntityManager();
    }

    @Bean(name = "transactionManagerMysql")
    @Primary
    PlatformTransactionManager transactionManagerMysql(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryMysql(builder).getObject()));
    }
}
