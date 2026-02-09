package jokes.jokes;

import liquibase.Liquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class JokesApplication {

	@Value("${spring.liquibase.change-log}")
	private String liquibaseChangeLogPath;

	public static void main(String[] args) {
		SpringApplication.run(JokesApplication.class, args);
	}

	// TODO CSV beim Starten importieren damit die DB immer gefüllt ist


	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setShouldRun(true);
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(liquibaseChangeLogPath);
		return liquibase;
	}



}
