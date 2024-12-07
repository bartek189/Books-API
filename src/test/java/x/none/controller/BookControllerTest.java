package x.none.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import x.none.model.Book;
import x.none.model.User;
import x.none.repository.AuthorRepository;
import x.none.repository.BookRepository;
import x.none.service.UserPermissionService;
import x.none.util.SecurityUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserPermissionService userPermissionService;

    @MockBean
    private SecurityUtil securityUtil;

    @SpyBean
    private JobExplorer jobExplorer;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void getMessage() throws Exception {
        User user = new User("A", "B");

        Mockito.when(securityUtil.getUser()).thenReturn(user);
        final byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/BooksTest.txt"));
        mvc.perform(multipart("/api/v1/book/import")
                        .file("file", bytes))
                .andExpect(status().isOk());

        Thread.sleep(2000);

        List<Book> books = List.of(new Book(1L, "cq", "fantasy", 16, authorRepository.findById(9L).get()), new Book(2L, "EDL", "poetry", 28, authorRepository.findById(2L).get()));
        Assertions.assertEquals(2, bookRepository.findAll().size());
        assertThat(bookRepository.findAllWithAuthor())
                .usingRecursiveComparison()
                .ignoringFields("author.bookList")
                .isEqualTo(books);
    }


}