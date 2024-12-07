package x.none.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import x.none.model.StatusInfo;
import x.none.model.User;
import x.none.repository.StatusRepository;
import x.none.util.SecurityUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserPermissionServiceTest {


    @Mock
    private StatusRepository statusRepository;

    private UserPermissionService userPermissionService;

    private SecurityUtil securityUtil;

    @BeforeEach
    public void init() {
        statusRepository = Mockito.mock(StatusRepository.class);
        securityUtil = Mockito.mock(SecurityUtil.class);
        userPermissionService = new UserPermissionService(statusRepository, securityUtil);
    }

    @Test
    public void shouldCompleted() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        User user = new User("A", "B");


        Mockito.when(securityUtil.getUser()).thenReturn(user);

        StatusInfo info = new StatusInfo(1L, "COMPLETED", "0", "A");

        Mockito.when(statusRepository.findFirstByJobId(1L)).thenReturn(Optional.of(info));

        StatusInfo statusInfo = statusRepository.findFirstByJobId(1L).get();

        userPermissionService.validateRequest(1L);

        Assertions.assertEquals(1, statusInfo.getJobId());
        Assertions.assertEquals("COMPLETED", statusInfo.getStatus());
        Assertions.assertEquals("0", statusInfo.getDuration());
        Assertions.assertEquals("A", statusInfo.getUserName());


    }

    @Test
    public void shouldThrowUnauthorizedException() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            Authentication authentication = Mockito.mock(Authentication.class);
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            User user = new User("A", "B");
            Mockito.when(securityUtil.getUser()).thenReturn(user);
            StatusInfo info = new StatusInfo(1L, "COMPLETED", "0", "A");


            Authentication authentication2 = Mockito.mock(Authentication.class);
            SecurityContext securityContext2 = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext2.getAuthentication()).thenReturn(authentication2);
            SecurityContextHolder.setContext(securityContext2);
            User user2 = new User("B", "B");


            Mockito.when(securityUtil.getUser()).thenReturn(user2);


            Mockito.when(statusRepository.findFirstByJobId(2L)).thenReturn(Optional.of(info));


            userPermissionService.validateRequest(2L);

        });
        assertEquals("UNAUTHORIZED", thrown.getMessage());


    }

}

