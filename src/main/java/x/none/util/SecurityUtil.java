package x.none.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import x.none.model.User;
import x.none.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    public User getUser() {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUserName(userName).orElseThrow();
    }
}
