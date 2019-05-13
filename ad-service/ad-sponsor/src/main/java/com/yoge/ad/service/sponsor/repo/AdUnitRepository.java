package com.yoge.ad.service.sponsor.repo;

import com.yoge.ad.service.sponsor.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {

    Optional<AdUnit> findByPlanIdAndUnitName(Long planId, String unitName);
}
