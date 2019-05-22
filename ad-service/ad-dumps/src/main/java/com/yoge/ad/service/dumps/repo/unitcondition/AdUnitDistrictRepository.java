package com.yoge.ad.service.dumps.repo.unitcondition;

import com.yoge.ad.service.common.dump.table.AdUnitDistrictTable;
import com.yoge.ad.service.dumps.entity.unitcondition.AdUnitDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdUnitDistrictRepository extends JpaRepository<AdUnitDistrict, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdUnitDistrictTable(a.unitId, a.province, a.city) from AdUnitDistrict as a")
    List<AdUnitDistrictTable> find();
}
