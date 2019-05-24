package com.yoge.ad.service.dumps.repo;

import com.yoge.ad.service.common.dump.table.AdUnitTable;
import com.yoge.ad.service.dumps.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdUnitTable(a.id, a.unitStatus, a.positionType, a.planId) from AdUnit as a where a.unitStatus = :unitStatus")
    List<AdUnitTable> findByUnitStatus(@Param("unitStatus") Integer unitStatus);
}
