package com.yoge.ad.service.sponsor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {

    private Long userId;
    private String username;
    private String token;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
