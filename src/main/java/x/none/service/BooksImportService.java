package x.none.service;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import x.none.model.dto.JobIdResponseDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class BooksImportService {

    private final JobLauncher jobLauncher;
    private final Job job;

    public BooksImportService(@Qualifier("asyncJobLauncher") JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public JobIdResponseDto getMessage(File file) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", file.getAbsolutePath())
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(job, jobParameters);
            return new JobIdResponseDto(execution.getId());
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }


    public File getFile(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        File tempFile = File.createTempFile(uuid, ".csv");
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile;
    }

}




