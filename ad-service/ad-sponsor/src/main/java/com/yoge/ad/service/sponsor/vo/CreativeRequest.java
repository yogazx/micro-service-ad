package com.yoge.ad.service.sponsor.vo;

import com.yoge.ad.service.sponsor.constant.CommonStatus;
import com.yoge.ad.service.sponsor.entity.Creative;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreativeRequest {

    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Long size;
    private Integer duration;
    private Long userId;
    private String url;

    public Creative convertToEntity() {
        Creative creative = new Creative();
        creative.setName(name);
        creative.setType(type);
        creative.setMaterialType(materialType);
        creative.setHeight(height);
        creative.setWidth(width);
        creative.setSize(size);
        creative.setDuration(duration);
        creative.setAuditStatus(CommonStatus.VALID.getStatus());
        creative.setUserId(userId);
        creative.setUrl(url);
        creative.setCreateTime(LocalDateTime.now());
        creative.setUpdateTime(creative.getCreateTime());
        return creative;
    }

    public boolean isValidate() {
        return StringUtils.isNotBlank(name)
                && type != null
                && materialType != null
                && height != null
                && width != null
                && size != null
                && duration != null
                && userId != null
                && url != null;
    }
}
