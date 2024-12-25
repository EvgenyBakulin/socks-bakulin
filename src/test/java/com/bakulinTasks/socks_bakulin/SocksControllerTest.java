package com.bakulinTasks.socks_bakulin;

import com.bakulinTasks.socks_bakulin.dto.SocksInfoDTO;
import com.bakulinTasks.socks_bakulin.repository.SocksRepository;
import com.bakulinTasks.socks_bakulin.service.SocksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.bakulinTasks.socks_bakulin.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SocksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SocksService socksService;
    @BeforeEach
    public void setUp() {
        DTO1.setId(ID1);
        DTO1.setColour(COLOUR1);
        DTO1.setCottonContent(COTTON1);
        DTO1.setQuantity(QUANTITY1);
        DTO2.setId(ID2);
        DTO2.setColour(COLOUR1);
        DTO2.setCottonContent(COTTON1);
        DTO2.setQuantity(QUANTITY1+QUANTITY1);
        DTO3.setId(ID2);
        DTO3.setColour(COLOUR1);
        DTO3.setCottonContent(COTTON1);
        DTO3.setQuantity(QUANTITY1-QUANTITY3);
        MODEL1.setId(ID1);
        MODEL1.setColour(COLOUR1);
        MODEL1.setCottonContent(COTTON1);
        MODEL1.setQuantity(QUANTITY1);
        MODEL2.setId(ID2);
        MODEL2.setColour(COLOUR1);
        MODEL2.setCottonContent(COTTON1);
        MODEL2.setQuantity(QUANTITY1);
        MODEL3.setId(ID2);
        MODEL3.setColour(COLOUR1);
        MODEL3.setCottonContent(COTTON1);
        MODEL3.setQuantity(QUANTITY1+QUANTITY1);
        MODEL4.setId(ID2);
        MODEL4.setColour(COLOUR1);
        MODEL4.setCottonContent(COTTON1);
        MODEL4.setQuantity(QUANTITY1-QUANTITY3);
        MODEL5.setId(ID1);
        MODEL5.setColour(COLOUR2);
        MODEL5.setCottonContent(COTTON2);
        MODEL5.setQuantity(QUANTITY2);
        INFO1.setQuantity(QUANTITY1);
        INFO1.setColour(COLOUR1);
        INFO1.setCottonContent(COTTON1);
        INFO2.setQuantity(QUANTITY3);
        INFO2.setColour(COLOUR1);
        INFO2.setCottonContent(COTTON1);
        INFO3.setCottonContent(COTTON2);
        INFO3.setColour(COLOUR2);
        INFO3.setQuantity(QUANTITY2);
        DTO4.setQuantity(QUANTITY2);
        DTO4.setColour(COLOUR2);
        DTO4.setCottonContent(COTTON2);
        DTO4.setId(ID1);
    }
    @Test
    void testMethodAddSocksInStorage() throws Exception {
        when(socksService.addInStorage(any(SocksInfoDTO.class))).thenReturn(DTO2);
        mockMvc.perform(post("http://localhost:8081/api/socks/income").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(INFO1)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.colour").value("Серый"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cottonContent").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").isNumber());
    }
    @Test
    void testMethodGetSocksFromStorage() throws Exception {
        when(socksService.getFromStorage(any(SocksInfoDTO.class))).thenReturn(DTO3);
        mockMvc.perform(post("http://localhost:8081/api/socks/outcome").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(INFO2)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.colour").value("Серый"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cottonContent").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").isNumber());
    }

    @Test
    void testMethodUpdateById() throws Exception {
        when(socksService.updateInStorage(anyInt(),any(SocksInfoDTO.class))).thenReturn(DTO4);
        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:8081/api/socks/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(INFO3)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.colour").value("Чёрный"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cottonContent").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").isNumber());
    }

    }

