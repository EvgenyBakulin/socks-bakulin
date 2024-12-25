package com.bakulinTasks.socks_bakulin.service;

import com.bakulinTasks.socks_bakulin.dto.SocksDTO;
import com.bakulinTasks.socks_bakulin.dto.SocksInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SocksService {
    SocksDTO addInStorage(SocksInfoDTO info);
    SocksDTO getFromStorage(SocksInfoDTO info);
    List<SocksDTO> readUsersFromExcel(MultipartFile inputStream);
    SocksDTO updateInStorage(Integer id, SocksInfoDTO info);
    Map<Integer,List<SocksDTO>> getSocksQuantityWithFilter(String colour, Double cottonContent, Double lessThanCottonContent, Double moreThanCottonContent);
}
