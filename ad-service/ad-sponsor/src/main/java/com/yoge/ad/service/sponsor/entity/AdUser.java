package com.yoge.ad.service.sponsor.entity;

import com.yoge.ad.service.sponsor.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_user")
public class AdUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 128, nullable = false)
    private String username;
    @Column(length = 256, nullable = false)
    private String token;
    @NotNull
    private Integer userStatus;
    @NotNull
    private LocalDateTime createTime;
    @NotNull
    private LocalDateTime updateTime;

    public AdUser(String username, String token) {
        this.username = username;
        this.token = token;
        this.userStatus = CommonStatus.VALID.getStatus();
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }
}
