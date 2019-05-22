package com.yoge.ad.service.dumps.repo.unitcondition;

import com.yoge.ad.service.common.dump.table.AdCreativeUnitTable;
import com.yoge.ad.service.dumps.entity.unitcondition.CreativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreativeUnitRepository extends JpaRepository<CreativeUnit, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdCreativeUnitTable(a.id, a.unitId) from CreativeUnit as a")
    List<AdCreativeUnitTable> find();
}
