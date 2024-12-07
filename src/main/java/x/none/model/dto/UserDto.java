package x.none.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import x.none.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private long id;
    private String userName;
    private String password;

    public UserDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.password = user.getPassword();
    }
}
