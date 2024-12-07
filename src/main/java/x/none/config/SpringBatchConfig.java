package x.none.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import x.none.model.dto.BookDto;

import javax.sql.DataSource;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SpringBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MyJobExecutionListener listener;

    @Bean
    public Job asyncJob(FlatFileItemReader<BookDto> reader, DataSource dataSource) {
        return jobBuilderFactory
                .get("Asynchronous Processing JOB")
                .listener(listener)
                .incrementer(new RunIdIncrementer())
                .flow(asyncManagerStep(reader, dataSource))
                .end()
                .build();
    }

    @Bean
    public Step asyncManagerStep(FlatFileItemReader<BookDto> reader, DataSource dataSource) {
        return stepBuilderFactory
                .get("Asynchronous Processing : Read -> Process -> Write ")
                .<BookDto, Future<BookDto>>chunk(100)
                .reader(reader)
                .writer(asyncWriter(dataSource))
                .processor(itemProcessor())
                .listener(itemCountListener())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public ItemCountListener itemCountListener() {
        return new ItemCountListener();
    }


    @Bean
    public ItemProcessor<BookDto, Future<BookDto>> itemProcessor() {
        return (bookDto) -> {
            CompletableFuture<BookDto> future = new CompletableFuture<>();
            future.complete(bookDto);
            return future;
        };
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(64);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(64);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("MultiThreaded-");
        return executor;
    }

    @Bean
    public JobLauncher asyncJobLauncher(JobRepository jobRepository, TaskExecutor taskExecutor) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        return jobLauncher;
    }

    @Bean
    public AsyncItemWriter<BookDto> asyncWriter(DataSource dataSource) {
        AsyncItemWriter<BookDto> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(itemWriter(dataSource));
        return asyncItemWriter;
    }

    @Bean
    public ItemWriter<BookDto> itemWriter(DataSource dataSource) {
        return (bookDtos) -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            for (BookDto b : bookDtos) {
                jdbcTemplate.update("INSERT INTO BOOK (ID, TITLE, CATEGORY, PRICE,AUTHOR_ID) VALUES (?, ?, ?, ?,?)",
                        b.getId(), b.getTitle(), b.getCategory(), b.getPrice(), b.getAuthorId());
            }
        };
    }


    @Bean
    @StepScope
    public FlatFileItemReader<BookDto> asyncReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFIle) {
        return new FlatFileItemReaderBuilder<BookDto>()
                .linesToSkip(1)
                .name("Reader")
                .resource(new FileSystemResource(new File(pathToFIle)))
                .delimited()
                .names("id", "title", "category", "price", "authorId")
                .lineMapper(new DefaultLineMapper<BookDto>() {
                    {
                        setLineTokenizer(new DelimitedLineTokenizer() {
                            {
                                setDelimiter(",");
                                setNames("id", "title", "category", "price", "authorId");
                            }
                        });
                        setFieldSetMapper(new BeanWrapperFieldSetMapper<BookDto>() {
                            {
                                setTargetType(BookDto.class);
                            }
                        });
                    }
                })
                .build();
    }


}
