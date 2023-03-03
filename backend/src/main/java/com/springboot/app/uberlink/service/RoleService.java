package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.model.ERole;
import com.springboot.app.uberlink.model.Role;


public interface RoleService {
    Role findByName(ERole name);
}
