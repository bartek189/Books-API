package x.none.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import x.none.model.StatusInfo;
import x.none.repository.StatusRepository;
import x.none.repository.UserRepository;
import x.none.util.SecurityUtil;

import java.io.File;

@Component
@Getter
@RequiredArgsConstructor
public class MyJobExecutionListener implements JobExecutionListener {

    private final UserRepository userRepository;

    private final StatusRepository statusRepository;
    private final SecurityUtil securityUtil;


    @Override
    public void beforeJob(JobExecution jobExecution) {

        String username = securityUtil.getUser().getUserName();
        statusRepository.save(new StatusInfo(jobExecution.getId(), jobExecution.getStatus().getBatchStatus().name(), String.valueOf(jobExecution.getStartTime()), username));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String username = securityUtil.getUser().getUserName();

        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            statusRepository.save(new StatusInfo(jobExecution.getId(), jobExecution.getStatus().getBatchStatus().name(), String.valueOf(jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime() + "ms"), username));
        }

        if (jobExecution.getStatus().equals(BatchStatus.FAILED)) {
            statusRepository.save(new StatusInfo(jobExecution.getId(), jobExecution.getStatus().getBatchStatus().name(), String.valueOf(jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime() + "ms"), username));
        }
        String filePath = jobExecution.getJobParameters().getString("fullPathFileName");
        new File(filePath).delete();
    }

}
