package x.none.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import x.none.service.UserPermissionService;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class StatusControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserPermissionService userPermissionService;


    @SpyBean
    private JobExplorer jobExplorer;


    @Test
    void getStatus() throws Exception {
        Mockito.doNothing().when(userPermissionService).validateRequest(Mockito.anyLong());
        JobExecution jobExecution = Mockito.mock(JobExecution.class);

        Mockito.when(jobExplorer.getJobExecution(1L)).thenReturn(jobExecution);
        Mockito.when(jobExecution.getStartTime()).thenReturn(new Date());
        Mockito.when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        Mockito.when(jobExecution.getEndTime()).thenReturn(new Date());

        String result = mvc.perform(get("/api/v1/imports/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        boolean containsStatus = result.contains("COMPLETED");
        boolean containsProcessedRows = result.contains("0");


        Assertions.assertTrue(containsStatus);
        Assertions.assertTrue(containsProcessedRows);


    }
}