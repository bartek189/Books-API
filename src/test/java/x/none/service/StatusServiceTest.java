package x.none.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.boot.test.mock.mockito.MockBean;
import x.none.config.ItemCountListener;
import x.none.model.dto.StatusResponseDto;

import java.util.Date;

@RequiredArgsConstructor
class StatusServiceTest {

    private StatusService service;

    private JobExplorer jobExplorer;
    private ItemCountListener itemCountListener;

    @MockBean
    private UserPermissionService userPermissionService;


    @BeforeEach
    public void init() {
        jobExplorer = Mockito.mock(JobExplorer.class);
        itemCountListener = Mockito.mock(ItemCountListener.class);
        userPermissionService = Mockito.mock(UserPermissionService.class);
        service = new StatusService(jobExplorer, itemCountListener, userPermissionService);
    }

    @Test
    public void shouldGetStatus() {
        Mockito.doNothing().when(userPermissionService).validateRequest(Mockito.anyLong());
        JobExecution jobExecution = Mockito.mock(JobExecution.class);

        Mockito.when(jobExplorer.getJobExecution(1L)).thenReturn(jobExecution);
        Mockito.when(jobExecution.getStartTime()).thenReturn(new Date());
        Mockito.when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        Mockito.when(jobExecution.getEndTime()).thenReturn(new Date());


        StatusResponseDto s = service.getStatus(1L);
        String string = s.getStatus();
        boolean a = string.contains("COMPLETED");
        Assertions.assertTrue(a);
    }
}