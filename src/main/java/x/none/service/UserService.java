package x.none.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import x.none.model.ERole;
import x.none.model.Role;
import x.none.model.User;
import x.none.model.dto.UserDto;
import x.none.repository.RoleRepository;
import x.none.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public User saveNewUser(UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        User user = modelMapper.map(userDto, User.class);
        roleRepository.findByName(ERole.ROLE_USER).ifPresentOrElse(role -> user.setRoles(Set.of(role)), () -> {
            Role role = roleRepository.save(new Role(ERole.ROLE_USER));
            user.setRoles(Set.of(role));
        });
        return repository.save(user);
    }
}
