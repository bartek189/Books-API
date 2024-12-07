package x.none.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import x.none.model.dto.JobIdResponseDto;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class BookImportServiceTest {
    private BooksImportService service;
    private JobLauncher jobLauncher;
    private Job job;

    private JobExecution jobExecution;


    @BeforeEach
    public void init() {
        jobLauncher = Mockito.mock(JobLauncher.class);
        job = Mockito.mock(Job.class);
        service = new BooksImportService(jobLauncher, job);
    }

    @Test
    public void testGetMessage() throws JobExecutionException {
        File f = new File("src/test/resources/BooksTest");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", f.getAbsolutePath()).toJobParameters();

        jobExecution = new JobExecution(1L, jobParameters);

        Mockito.when(jobLauncher.run(Mockito.eq(job), Mockito.any())).thenReturn(jobExecution);

        JobIdResponseDto a = service.getMessage(f);
        Assertions.assertEquals(1, a.getJobId());

    }

    @Test
    public void shouldGetMessageThrow() {


        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            File f = new File("src/test/resources/BooksTest");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", f.getAbsolutePath()).toJobParameters();

            jobExecution = new JobExecution(1L, jobParameters);


            Mockito.when(jobLauncher.run(Mockito.eq(job), Mockito.any())).thenThrow(JobInstanceAlreadyCompleteException.class);
            JobIdResponseDto a = service.getMessage(f);


        });

        assertTrue(thrown.getMessage().contains("JobInstanceAlreadyCompleteException"));

    }


}

