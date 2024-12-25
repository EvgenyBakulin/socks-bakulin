package com.bakulinTasks.socks_bakulin.dto;

import lombok.Data;

@Data
public class SocksSpecificationDTO {
    private String colour;
    private Double cottonContent;
    private Double lessThanCottonContent;
    private Double moreThanCottonContent;
    private Double CottonContentMinor;
    private Double CottonContentMajor;
}
