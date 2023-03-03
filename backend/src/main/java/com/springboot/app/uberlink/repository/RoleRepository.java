package com.springboot.app.uberlink.repository;


import com.springboot.app.uberlink.model.ERole;
import com.springboot.app.uberlink.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(ERole name);
}
