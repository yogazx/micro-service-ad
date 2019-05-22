package com.yoge.ad.service.dumps.repo.unitcondition;

import com.yoge.ad.service.common.dump.table.AdUnitItTable;
import com.yoge.ad.service.dumps.entity.unitcondition.AdUnitIt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdUnitItRepository extends JpaRepository<AdUnitIt, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdUnitItTable(a.unitId, a.itTag) from AdUnitIt as a")
    List<AdUnitItTable> find();
}
