package x.none.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x.none.exception.UnauthorizedException;
import x.none.model.StatusInfo;
import x.none.repository.StatusRepository;
import x.none.util.SecurityUtil;


@RequiredArgsConstructor
@Service
public class UserPermissionService {

    private final StatusRepository statusRepository;
    private final SecurityUtil securityUtil;

    public void validateRequest(long id) {

        String user = securityUtil.getUser().getUserName();

        StatusInfo statusInfo = statusRepository.findFirstByJobId(id).orElseThrow();

        if (!statusInfo.getUserName().equals(user)) {
            throw new UnauthorizedException();
        }
    }
}
