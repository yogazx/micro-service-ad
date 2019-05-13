package com.yoge.ad.service.sponsor.repo;

import com.yoge.ad.service.sponsor.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdPlanRepository extends JpaRepository<AdPlan, Long> {

    AdPlan findByUserIdAndPlanName(Long userId, String planName);

    List<AdPlan> findAllByIdInAndUserId(List<Long> idList, Long userId);

    AdPlan findByIdAndUserId(Long id, Long userId);
}
