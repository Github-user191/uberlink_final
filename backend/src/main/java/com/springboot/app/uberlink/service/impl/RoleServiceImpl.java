package com.springboot.app.uberlink.service.impl;

import com.springboot.app.uberlink.model.ERole;
import com.springboot.app.uberlink.model.Role;
import com.springboot.app.uberlink.repository.RoleRepository;
import com.springboot.app.uberlink.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;


    @Override
    public Role findByName(ERole name) {
        return roleRepository.findByName(name);
    }
}
