package com.mineaurion.api.query;

import com.mineaurion.api.library.model.query.Access;
import com.mineaurion.api.library.model.query.Server;
import com.mineaurion.api.library.model.query.Version;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    Server mockServer = new Server(
            "Test Name",
            new Version("1.0.0", "1.0.0"),
            "skyblock",
            new Access(false, false, false),
            "dns.example.com"
    );

    @Test
    public void getAllQueryWithoutQueryParams() throws Exception {
        Mockito.when(queryService.findAll(Sort.Direction.ASC, "id")).thenReturn(Arrays.asList(mockServer));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/query").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Mockito.verify(queryService, Mockito.times(1)).findAll(Sort.Direction.ASC, "id");
    }

}
