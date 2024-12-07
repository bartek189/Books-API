package x.none.service;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;
import x.none.config.ItemCountListener;
import x.none.model.dto.StatusResponseDto;

@Service
public class StatusService {

    private final JobExplorer jobExplorer;
    private final ItemCountListener countListener;

    private final UserPermissionService userPermissionService;

    public StatusService(JobExplorer jobExplorer,
                         ItemCountListener countListener, UserPermissionService userPermissionService) {

        this.jobExplorer = jobExplorer;
        this.countListener = countListener;
        this.userPermissionService = userPermissionService;
    }

    public StatusResponseDto getStatus(long id) {

        userPermissionService.validateRequest(id);

        JobExecution jobExecution = jobExplorer.getJobExecution(id);

        long duration = System.currentTimeMillis() - jobExecution.getStartTime().getTime();
        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            duration = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        }

        return new StatusResponseDto(jobExecution.getStatus().getBatchStatus().name(), countListener.getCount(), duration);
    }
}
