package com.example.securitystudy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securitystudy.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
    public Role findByRoleName(String name);
}
