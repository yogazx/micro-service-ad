package com.yoge.ad.service.common.dump.table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdCreativeTable {

    private Long id;
    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Integer auditStatus;
    private String adUrl;

    public AdCreativeTable(Long id, String name, Integer type, Integer materialType, Integer height, Integer width, Integer auditStatus, String adUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.materialType = materialType;
        this.height = height;
        this.width = width;
        this.auditStatus = auditStatus;
        this.adUrl = adUrl;
    }
}
