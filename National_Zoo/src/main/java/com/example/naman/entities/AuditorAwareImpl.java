package com.example.naman.entities;

import java.util.Objects;
import java.util.Optional;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

public class AuditorAwareImpl implements  AuditorAware<String> {

	/**
	 * Override the getCurrentAuditor Method for Enable Auditing.
	 */

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(Objects.isNull(authentication)) {
			return Optional.of("System");
		}

		if(authentication.getPrincipal() instanceof User user) {
			return Optional.ofNullable(user.getUsername().toString());
		} else {
			return Optional.of(authentication.getName());
		}

	}

}
