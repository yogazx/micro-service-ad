package com.yoge.ad.service.dumps.entity;

import com.yoge.ad.service.dumps.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DESC 推广计划表
 *
 * @author You Jia Ge
 * Created Time 2019/5/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_plan")
public class AdPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;
    @Column(length = 20)
    @NotNull
    private Long userId;
    @NotNull
    @Column(length = 48)
    private String planName;
    @NotNull
    @Column(length = 4)
    private Integer planStatus;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private LocalDateTime createTime;
    @NotNull
    private LocalDateTime updateTime;

    public AdPlan(Long userId, String planName, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planStatus = CommonStatus.VALID.getStatus();
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

}
