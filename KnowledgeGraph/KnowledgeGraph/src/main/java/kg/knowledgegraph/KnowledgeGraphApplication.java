package kg.knowledgegraph;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import kg.knowledgegraph.domain.mysql.User;
import kg.knowledgegraph.repository.mysql.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author YHT
 **/
@SpringBootApplication
@EnableJpaAuditing
public class KnowledgeGraphApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeGraphApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, PasswordEncoder encoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                init(userRepository, encoder);
            }
        };
    }

    private void init(UserRepository userRepository, PasswordEncoder encoder) {
        User user = userRepository.findByUsername("admin");
        if (user == null) {
            user = new User("admin", encoder.encode("admin123"));
            userRepository.save(user);
        }
    }
}
