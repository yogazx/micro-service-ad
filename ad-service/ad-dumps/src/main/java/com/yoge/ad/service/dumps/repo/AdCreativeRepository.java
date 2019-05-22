package com.yoge.ad.service.dumps.repo;

import com.yoge.ad.service.common.dump.table.AdCreativeTable;
import com.yoge.ad.service.dumps.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdCreativeRepository extends JpaRepository<Creative, Long> {

    @Query("select new com.yoge.ad.service.common.dump.table.AdCreativeTable(c.id, c.name, c.type, c.materialType, c.height, c.width, c.auditStatus, c.url) from Creative as c ")
    List<AdCreativeTable> find();
}
