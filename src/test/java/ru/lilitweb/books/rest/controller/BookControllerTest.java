package ru.lilitweb.books.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.rest.dto.BookDto;
import ru.lilitweb.books.service.BookService;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    BookService bookService;

    @Test
    public void index() throws Exception {
        Book book = getTestBook();

        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        when(bookService.search(Optional.empty(), pageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(book)));
        MvcResult mvcResult = this.mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
    }

    @Test
    public void viewBook() throws Exception {
        String id = "1";
        Book book = getTestBook();
        book.setId(id);
        when(bookService.getById(id)).thenReturn(Optional.of(book));
        this.mvc.perform(get("/api/v1/book/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void update_ifSuccess() throws Exception {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        BookDto bookDto = new BookDto(
                id,
                "Руслан и Людмила new",
                "author new",
                2011,
                "Описание new",
                genres);
        when(bookService.getById(id)).thenReturn(Optional.of(getTestBook()));
        this.mvc.perform(MockMvcRequestBuilders.post("/api/v1/book/" + id).
                accept(MediaType.APPLICATION_JSON_UTF8).
                param("title", bookDto.getTitle()).
                param("author", bookDto.getAuthor()).
                param("year", String.valueOf(bookDto.getYear())).
                param("description", bookDto.getDescription()).
                param("genres", bookDto.getGenres().toArray(new String[0]))
        ).
                andExpect(status().isOk());
    }

    private Book getTestBook() {
        List<Genre> genres = Arrays.asList(new Genre("Поэзия"), new Genre("Ужастик"));
        Author author = new Author("some book author");
        String id = "1";
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);

        book.setGenres(genres);
        return book;
    }

    @Test
    public void deleteBook() throws Exception {
        String id = "1";

        this.mvc.perform(delete("/api/v1/book/" + id))
                .andExpect(status().isOk());

        verify(bookService, atLeastOnce()).delete(id);
    }

    @Test
    public void create() throws Exception {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        BookDto bookDto = new BookDto(
                id,
                "Руслан и Людмила new",
                "author new",
                2011,
                "Описание new",
                genres);

        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/api/v1/book").
                contentType(MediaType.APPLICATION_JSON_UTF8).

                param("title", bookDto.getTitle()).
                param("author", bookDto.getAuthor()).
                param("year", String.valueOf(bookDto.getYear())).
                param("description", bookDto.getDescription()).
                param("genres", bookDto.getGenres().toArray(new String[0]))
        )
                //.andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("", content);
    }
}
