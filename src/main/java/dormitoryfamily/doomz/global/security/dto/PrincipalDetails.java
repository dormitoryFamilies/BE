package dormitoryfamily.doomz.global.security.dto;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements OAuth2User {

    private final Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getAuthority().toString();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return member.getName();
    }

    public String getEmail() {
        return member.getEmail();
    }
}
