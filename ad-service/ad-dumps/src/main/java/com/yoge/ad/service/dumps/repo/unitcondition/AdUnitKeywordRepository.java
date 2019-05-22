package com.yoge.ad.service.dumps.repo.unitcondition;

import com.yoge.ad.service.common.dump.table.AdUnitKeywordTable;
import com.yoge.ad.service.dumps.entity.unitcondition.AdUnitKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdUnitKeywordRepository extends JpaRepository<AdUnitKeyword, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdUnitKeywordTable(a.unitId, a.keyword) from AdUnitKeyword as a")
    List<AdUnitKeywordTable> find();
}
