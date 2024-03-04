package dormitoryfamily.doomz.global.security;

import java.util.Optional;

import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationAuditAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        Member memberPrincipal = ((PrincipalDetails) authentication.getPrincipal()).getMember();
        return Optional.ofNullable(memberPrincipal.getId());
    }
}
