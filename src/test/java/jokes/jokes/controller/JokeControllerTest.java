package jokes.jokes.controller;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

    private static final String EXPORT_URL = "/api/jokes/export/csv";
    private static final String IMPORT_URL = "/api/jokes/import/csv";


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
}