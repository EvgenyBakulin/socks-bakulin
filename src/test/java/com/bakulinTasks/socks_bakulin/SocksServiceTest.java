package com.bakulinTasks.socks_bakulin;

import com.bakulinTasks.socks_bakulin.dto.SocksDTO;
import com.bakulinTasks.socks_bakulin.dto.SocksInfoDTO;
import com.bakulinTasks.socks_bakulin.model.SocksModel;
import com.bakulinTasks.socks_bakulin.repository.SocksRepository;
import com.bakulinTasks.socks_bakulin.service.implementation.SockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.bakulinTasks.socks_bakulin.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SocksServiceTest {
    @Mock
    private SocksRepository socksRepository;
    @InjectMocks
    private SockServiceImpl sockService;
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
        INFO2.setCottonContent(COTTON1);}

    /**Тестирование метода добавления записи в базу**/

    @Test
    void methodAddInBaseWhenEqualsSocksIsAbsent(){
        when(socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(anyString(),anyDouble())).thenReturn(Optional.empty());
        when(socksRepository.save(any(SocksModel.class))).thenReturn(MODEL1);
        SocksDTO result = sockService.addInStorage(INFO1);
        assertEquals(result,DTO1);
    }

    @Test
    void methodAddInBaseWhenEqualsSocksIsPresent(){
        when(socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(anyString(),anyDouble())).thenReturn(Optional.of(MODEL2));
        when(socksRepository.save(any(SocksModel.class))).thenReturn(MODEL3);
        SocksDTO result = sockService.addInStorage(INFO1);
        assertEquals(result,DTO2);
    }
    /**Тестирование метода выдачи носков со склада**/
    @Test
    void methodGetFromBaseWhenRemainQuantityGreaterThatZero(){
        when(socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(anyString(),anyDouble())).thenReturn(Optional.of(MODEL2));
        when(socksRepository.save(any(SocksModel.class))).thenReturn(MODEL2);
        SocksDTO result = sockService.getFromStorage(INFO1);
        assertEquals(result,DTO3);
    }
     @Test
    void methodGetFromBaseWhenRemainQuantityIsZero(){
        when(socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(anyString(),anyDouble())).thenReturn(Optional.of(MODEL2));
        SocksDTO result = sockService.getFromStorage(INFO1);
        assertNull(result);
    }
    /**Тестирование метода изменения записи в базе**/
    @Test
    void methodUpdateInBaseWithoutEqualsRows(){
        INFO3.setCottonContent(COTTON2);
        INFO3.setColour(COLOUR2);
        INFO3.setQuantity(QUANTITY2);
        DTO4.setQuantity(QUANTITY2);
        DTO4.setColour(COLOUR2);
        DTO4.setCottonContent(COTTON2);
        DTO4.setId(ID1);
        LIST.add(MODEL5);
        LIST.add(MODEL6);

        when(socksRepository.findById(ID1)).thenReturn(Optional.of(MODEL1));
        when(socksRepository.save(any(SocksModel.class))).thenReturn(MODEL5);
        when(socksRepository.save(any(SocksModel.class))).thenReturn(MODEL5);
        SocksDTO result = sockService.updateInStorage(ID1,INFO3);
        assertEquals(result,DTO4);
    }

    @Test
    void methodUpdateInBaseWithEqualsRows(){
        INFO3.setCottonContent(COTTON2);
        INFO3.setColour(COLOUR2);
        INFO3.setQuantity(QUANTITY2);
        DTO4.setQuantity(QUANTITY2+QUANTITY1);
        DTO4.setColour(COLOUR2);
        DTO4.setCottonContent(COTTON2);
        DTO4.setId(ID1);
        MODEL6.setId(ID2);
        MODEL6.setQuantity(QUANTITY1);
        MODEL6.setColour(COLOUR2);
        MODEL6.setCottonContent(COTTON2);
        MODEL7.setId(ID1);
        MODEL7.setQuantity(QUANTITY1+QUANTITY2);
        MODEL7.setColour(COLOUR2);
        MODEL7.setCottonContent(COTTON2);

        when(socksRepository.findById(ID1)).thenReturn(Optional.of(MODEL1));
        when(socksRepository.findSocksModelsByColourIgnoreCaseAndCottonContent(anyString(),anyDouble())).thenReturn(LIST);
        when(socksRepository.save(any(SocksModel.class))).thenReturn(MODEL7);
        SocksDTO result = sockService.updateInStorage(ID1,INFO3);
        assertEquals(result,DTO4);
    }
}
