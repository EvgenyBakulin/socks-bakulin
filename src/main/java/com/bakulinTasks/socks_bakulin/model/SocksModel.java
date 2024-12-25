package com.bakulinTasks.socks_bakulin.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class SocksModel {
    @Id
    @GeneratedValue
    private Integer id;
    private String colour;
    private Double cottonContent;
    private Integer quantity;
}
