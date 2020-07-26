package dev.kodice.games.ludo.util;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring-Security aware class to resolve username when using @CreatedBy and @LastModifiedBy on JPA entities
 **/
public class UsernameAuditableProvider implements AuditorAware<String> {

    /**
     * @return the current username if available in SecurityContextHolder or 'SYSTEM' in other case
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = (authentication == null || !authentication.isAuthenticated()) ? "SYSTEM" : authentication.getName();

        return Optional.of(userName);
    }

}

