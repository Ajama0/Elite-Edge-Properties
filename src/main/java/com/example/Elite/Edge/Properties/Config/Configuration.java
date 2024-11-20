package com.example.Elite.Edge.Properties.Config;


import com.example.Elite.Edge.Properties.Enums.PropertyType;
import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Enums.unitType;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import com.example.Elite.Edge.Properties.Model.Tenants;
import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

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

            //property
            Property p1 = new Property("address", PropertyType.RESIDENTIAL, "Avondale Apartments",
                    3000000.00, Boolean.TRUE, "London", "Greater London", "sw3 3kl",
                    LocalDate.of(2014, Month.MARCH, 5),4,
                    "New residence with the best of facilities");

            Property p2 = new Property("123 Main St", PropertyType.COMMERCIAL,
                    "Luxury Apartment", 500000.00, true,
                    "New York", "NY", "10001", LocalDate.now(), 5,
                    "A great property!");

            //units
            Units u1 = new Units("101", unitStatus.VACANT, "5", 2000, 50000.00, unitType.STUDIO,
                    2, 1500);
            u1.setProperty(p1);


            Units u2 = new Units("102", unitStatus.OCCUPIED, "6", 2500, 55000.00, unitType.APARTMENT,
                    3, 2000);

            u2.setProperty(p2);


            //property owner and property relationship
            PropertyOwner o1 = new PropertyOwner("Jacob", "Richardson","jacobR@example.com,",
                    "90 beverly Hills", LocalDate.of(1970, Month.MARCH, 3),
                    "+1(663)273 283" );
            o1.setProperties(List.of(p1, p2));

            PropertyOwner o2 = new PropertyOwner("Harry", "Richardson","HarryR@example.com,",
                    "90 beverly Hills", LocalDate.of(1990, Month.MAY, 9),
                    "+1(663)273 133" );

            o2.setProperties(List.of(p1,p2));
            p1.setPropertyOwners(List.of(o1,o2));
            p2.setPropertyOwners(List.of(o1,o2));


            //tenant
            Tenants t1 = new Tenants("david", "lenn", "davidl@example.com", "0748310820",
                    LocalDate.of(1994, Month.AUGUST,4),
                    "114 avenue rd", "Engineer", 60000.00);

            Tenants t2 = new Tenants("holly", "jen", "holly@example.com", "+1(534)766 372",
                    LocalDate.of(1997, Month.AUGUST,4),
                    "3 retherford way", "Teacher", 80000.00);

            t1.setUnit(u1);
            u1.setTenant(t1);
            t2.setUnit(u2);
            u2.setTenant(t2);




















        };
    }



}
