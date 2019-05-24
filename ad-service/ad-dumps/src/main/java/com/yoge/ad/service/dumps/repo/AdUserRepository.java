package com.yoge.ad.service.dumps.repo;

import com.yoge.ad.service.dumps.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    AdUser findAdUserByUsername(String username);
}
