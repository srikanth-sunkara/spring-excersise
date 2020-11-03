package com.gpch.login.service;

import com.gpch.login.model.GuestEntry;
import com.gpch.login.model.Role;
import com.gpch.login.model.User;
import com.gpch.login.repository.GuestEntryRepository;
import com.gpch.login.repository.RoleRepository;
import com.gpch.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class GuestEntryService {

    //private UserRepository userRepository;
    private GuestEntryRepository guestEntryRepository;
    

    @Autowired
    public GuestEntryService(UserRepository userRepository,
    		GuestEntryRepository guestEntryRepository) {
     //   this.userRepository = userRepository;
        this.guestEntryRepository = guestEntryRepository;        
    }

 
    public GuestEntry findUserByUserId(String id) {
        return guestEntryRepository.findByUserId(id);
    }

    public GuestEntry saveEntry(GuestEntry entry) {
    	entry.setApproved(false);
    	
        return guestEntryRepository.save(entry);
    }

    public List<GuestEntry> findAll() {
    	return guestEntryRepository.findAll();
    }
    
    
}