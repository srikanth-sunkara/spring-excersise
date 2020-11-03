package com.gpch.login.repository;

import com.gpch.login.model.GuestEntry;
import com.gpch.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestEntryRepository extends JpaRepository<GuestEntry, Long> {    
    GuestEntry findByUserId(String userId);
}