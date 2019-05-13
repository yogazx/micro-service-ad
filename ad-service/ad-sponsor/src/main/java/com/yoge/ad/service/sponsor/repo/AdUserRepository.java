package com.yoge.ad.service.sponsor.repo;

import com.yoge.ad.service.sponsor.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    AdUser findAdUserByUsername(String username);
}
