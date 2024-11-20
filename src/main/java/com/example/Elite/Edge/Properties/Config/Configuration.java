package com.example.Elite.Edge.Properties.Config;


import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private final leaseRepository leaseRepo;
    private final paymentsRepository paymentsRepo;
    private final propertyOwnerRepository propertyOwnerRepo;
    private final propertyRepository propertyRepo;
    private final tenantsRepository tenantsRepo;
    private final unitRepository unitRepo;

    @Autowired
    public Configuration(leaseRepository leaseRepo, paymentsRepository paymentsRepo,
                         propertyOwnerRepository propertyOwnerRepo,
                         propertyRepository propertyRepo,
                         tenantsRepository tenantsRepo,
                         unitRepository unitRepo

                         ){
        this.leaseRepo = leaseRepo;
        this.paymentsRepo = paymentsRepo;
        this.propertyOwnerRepo = propertyOwnerRepo;
        this.propertyRepo = propertyRepo;
        this.tenantsRepo = tenantsRepo;
        this.unitRepo = unitRepo;
    }

    @Bean
    CommandLineRunner commandLineRunner(leaseRepository leaseRepo, paymentsRepository paymentsRepo,
                                        propertyOwnerRepository propertyOwnerRepo,
                                        propertyRepository propertyRepo,
                                        tenantsRepository tenantsRepo, unitRepository unitRepo){

        return args -> {

        };
    }



}
