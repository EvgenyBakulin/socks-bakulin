package com.bakulinTasks.socks_bakulin.repository;
import com.bakulinTasks.socks_bakulin.dto.SocksSpecificationDTO;
import com.bakulinTasks.socks_bakulin.model.SocksModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SocksRepository extends JpaRepository<SocksModel,Integer>, JpaSpecificationExecutor<SocksModel> {
    Optional<SocksModel>findSocksModelByColourIgnoreCaseAndCottonContent(String colour, Double cottonContent);
    List<SocksModel> findSocksModelsByColourIgnoreCaseAndCottonContent(String colour, Double cottonContent);

}
