package com.example.Elite.Edge.Properties.service;

import com.example.Elite.Edge.Properties.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TenantsService {

    private final TenantRepository tenantRepository;

    public TenantsService(TenantRepository tenantRepository){
        this.tenantRepository = tenantRepository;
    }



}
