package jokes.jokes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.entity.JokeCategory;
import jokes.jokes.entity.JokeEntity;
import jokes.jokes.service.JokeExportService;
import jokes.jokes.service.JokeImportService;
import jokes.jokes.service.JokeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JokeController.class)
@ExtendWith(MockitoExtension.class)
class JokeControllerTest {

    @Autowired
    private JokeController jokeController;

    @MockitoBean
    private JokeExportService jokeExportService;

    @MockitoBean
    private JokeService jokeService;

    @MockitoBean
    private JokeImportService jokeImportService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Long ID = 1L;

    private static final String GENERAL_URL = "/api/jokes";
    private static final String EXPORT_URL = "/api/jokes/export/csv";
    private static final String IMPORT_URL = "/api/jokes/import/csv";
    private static final String GET_ALL_URL = "/api/jokes/all";
    private static final String GET_RANDOM_URL = "/api/jokes/random";
    private static final String LIKE_URL = "/api/jokes/" + ID + "/like";
    private static final String TODAY_URL = "/api/jokes/today";
    private static final String TOP_URL = "/api/jokes/top";


    @Test
    void importFromCsvFile() throws Exception {
        var file = new MockMultipartFile("file", "file.csv", "text/csv", "text".getBytes());
        var result = mockMvc.perform(multipart(IMPORT_URL).file(file))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("text/plain;charset=UTF-8", result.getResponse().getContentType());
        assertNotNull(result.getResponse().getContentAsString());
        verify(jokeImportService).importCsv(any());
    }

    @Test
    void exportCsvFile() throws Exception {
        var resource = new ByteArrayResource("test".getBytes());
        when(jokeExportService.exportCsv()).thenReturn(resource);

        var result = mockMvc.perform(get(EXPORT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("application/octet-stream", result.getResponse().getContentType());
        assertNotNull(result.getResponse().getHeader("Content-Disposition"));
        verify(jokeExportService).exportCsv();
    }

    @Test
    void getAll() throws Exception {
        var expectedResponse = Collections.singletonList(new JokeEntity(ID, "joke", JokeCategory.JOB, 1));
        when(jokeService.getAllJokes()).thenReturn(expectedResponse);
        mockMvc.perform(get(GET_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).getAllJokes();
    }

    @Test
    void getRandomByCategory() throws Exception {
        var expectedResponse = new JokeEntity(ID, "joke", JokeCategory.JOB, 5);
        when(jokeService.getRandomJokeByCategory(JokeCategory.JOB)).thenReturn(expectedResponse);
        mockMvc.perform(get(GET_RANDOM_URL)
                        .param("category", JokeCategory.JOB.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).getRandomJokeByCategory(any());
    }

    @Test
    void create() throws Exception {
        var expectedResponse = new JokeEntity(ID, "joke", JokeCategory.JOB, 5);
        var request = new JokeDto("joke", JokeCategory.JOB);
        when(jokeService.create(request)).thenReturn(expectedResponse);
        mockMvc.perform(post(GENERAL_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).create(any());
    }

    @Test
    void update() throws Exception {
        var expectedResponse = new JokeEntity(ID, "joke", JokeCategory.JOB, null);
        var request = new JokeDto("joke", JokeCategory.JOB);
        when(jokeService.update(ID, request)).thenReturn(expectedResponse);
        mockMvc.perform(put(GENERAL_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).update(eq(ID), any());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(GENERAL_URL + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(jokeService).delete(ID);
    }

    @Test
    void like() throws Exception {
        var expectedResponse = new JokeEntity(ID, "joke", JokeCategory.JOB, null);
        var request = new JokeDto("joke", JokeCategory.JOB);
        when(jokeService.like(ID)).thenReturn(expectedResponse);
        mockMvc.perform(patch(LIKE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).like(ID);
    }

    @Test
    void getJokeOfTheDay() throws Exception {
        var expectedResponse = new JokeEntity(ID, "joke", JokeCategory.JOB, 5);
        when(jokeService.getJokeOfTheDay()).thenReturn(expectedResponse);
        mockMvc.perform(get(TODAY_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).getJokeOfTheDay();
    }

    @Test
    void getTopJokes() throws Exception {
        var expectedResponse = Collections.singletonList(new JokeEntity(ID, "joke", JokeCategory.JOB, 5));
        when(jokeService.getTop()).thenReturn(expectedResponse);
        mockMvc.perform(get(TOP_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(jokeService).getTop();
    }
}