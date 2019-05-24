package com.yoge.ad.service.dumps.repo;

import com.yoge.ad.service.common.dump.table.AdPlanTable;
import com.yoge.ad.service.dumps.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdPlanRepository extends JpaRepository<AdPlan, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdPlanTable(a.id, a.userId, a.planStatus, a.startDate, a.endDate) from AdPlan as a where a.planStatus = :planStatus")
    List<AdPlanTable> findByPlanStatus(@Param("planStatus") Integer planStatus);
}
