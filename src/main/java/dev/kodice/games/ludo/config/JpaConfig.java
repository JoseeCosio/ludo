package dev.kodice.games.ludo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.kodice.games.ludo.util.UsernameAuditableProvider;;;

@Configuration
@EnableJpaRepositories(basePackages = "dev.kodice.games.ludo.repository")
@EnableJpaAuditing(auditorAwareRef = "usernameAuditableProvider")
public class JpaConfig {

	@Bean("usernameAuditableProvider")
	AuditorAware<String> usernameAuditableProvider() {
		return new UsernameAuditableProvider();
	}

}