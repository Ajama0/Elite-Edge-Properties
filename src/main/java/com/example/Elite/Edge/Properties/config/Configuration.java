package com.example.Elite.Edge.Properties.config;


import com.example.Elite.Edge.Properties.constants.*;
import com.example.Elite.Edge.Properties.model.*;
import com.example.Elite.Edge.Properties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private final LeaseRepository leaseRepo;
    private final PaymentRepository paymentsRepo;
    private final PropertyOwnerRepository propertyOwnerRepo;
    private final PropertyRepository propertyRepo;
    private final TenantRepository tenantsRepo;
    private final UnitRepository unitRepo;

    @Autowired
    public Configuration(LeaseRepository leaseRepo, PaymentRepository paymentsRepo,
                         PropertyOwnerRepository propertyOwnerRepo,
                         PropertyRepository propertyRepo,
                         TenantRepository tenantsRepo,
                         UnitRepository unitRepo

                         ){
        this.leaseRepo = leaseRepo;
        this.paymentsRepo = paymentsRepo;
        this.propertyOwnerRepo = propertyOwnerRepo;
        this.propertyRepo = propertyRepo;
        this.tenantsRepo = tenantsRepo;
        this.unitRepo = unitRepo;
    }

    @Bean
    CommandLineRunner commandLineRunner(LeaseRepository leaseRepo, PaymentRepository paymentsRepo,
                                        PropertyOwnerRepository propertyOwnerRepo,
                                        PropertyRepository propertyRepo,
                                        TenantRepository tenantsRepo, UnitRepository unitRepo){

        return args -> {

            //property
            Property p1 = new Property("address", PropertyType.RESIDENTIAL, "Avondale Apartments",
                    3000000.00, Boolean.TRUE, "London", "Greater London", "sw3 3kl",
                    LocalDate.of(2014, Month.MARCH, 5),4,
                    "New residence with the best of facilities", Status.ACTIVE);

            Property p2 = new Property("123 Main St", PropertyType.COMMERCIAL,
                    "Luxury Apartment", 500000.00, true,
                    "New York", "NY", "10001", LocalDate.now(), 5,
                    "A great property!", Status.ACTIVE);

            propertyRepo.saveAll(List.of(p1,p2));

            //units
            Units u1 = new Units("101", UnitStatus.VACANT, "5", 2000, 50000.00, UnitType.STUDIO,
                    2, 1500.00);
            u1.setProperty(p1);


            Units u2 = new Units("102", UnitStatus.OCCUPIED, "6", 2500, 55000.00, UnitType.APARTMENT,
                    3, 2000.00);

            u2.setProperty(p2);

            Units u3 = new Units("37", UnitStatus.VACANT, "3", 6000, 900000.00, UnitType.OFFICE, 5,
                    5000.00);
            u3.setProperty(p2);


            unitRepo.saveAll(List.of(u1,u2,u3));


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

            propertyOwnerRepo.saveAll(List.of(o1,o2));


            //tenant
            Tenants t1 = new Tenants("david", "lenn", "davidl@example.com", "0748310820",
                    LocalDate.of(1994, Month.AUGUST,4),
                    "114 avenue rd", "Engineer", 60000.00, Status.ACTIVE);

            Tenants t2 = new Tenants("holly", "jen", "holly@example.com", "+1(534)766 3721",
                    LocalDate.of(1997, Month.AUGUST,4),
                    "3 retherford way", "Teacher", 80000.00, Status.ACTIVE);

            Tenants t3 = new Tenants("louis", "oakley", "louisO@gmail,com", "+1(543)987 1341", LocalDate.of(1980, Month.DECEMBER, 2),
                    "Longlane ave", "Doctor", 200000.00, Status.ACTIVE);



            t1.setUnit(u1);
            u1.setTenant(t1);
            t2.setUnit(u2);
            u2.setTenant(t2);
            t3.setUnit(u3);

            tenantsRepo.saveAll(List.of(t1,t2,t3));

            //leases
            Lease l1 = new Lease(LocalDate.of(2024, Month.SEPTEMBER, 19),
                    LocalDate.of(2024, Month.DECEMBER, 22),2000.00, 1500.00,60,"Agreement.pdf",
                    Status.ACTIVE);

            Lease l2 = new Lease(LocalDate.of(2024, Month.NOVEMBER, 19),
                    LocalDate.of(2024, Month.DECEMBER, 22),2500.00, 2000.00,60,"Agreement.pdf",
                    Status.ACTIVE);

            //extra 10 leases for unit 1 -> property 1






            l1.setUnit(u1);
            l1.setTenants(t1);
            u1.setLease(List.of(l1));
            t1.setLease(l1);

            l2.setUnit(u2);
            l2.setTenants(t2);
            u2.setLease(List.of(l2));
            t2.setLease(l2);

            leaseRepo.saveAll(List.of(l1,l2));
            //delete leases associated to a unit when tenant leaves(cascadeType.ALL)




            //payments
            Payments paym1 = new Payments("#TX-1BF332", 2000.00, PaymentStatus.SUCCESS,
                    "8654");

            Payments paym2 = new Payments("#TX-3CFF21", 2000.00, PaymentStatus.PENDING, "3361");

            paym1.setLease(l1);
            paym2.setLease(l1);
            l1.setPayments(List.of(paym1, paym2));
            paymentsRepo.saveAll(List.of(paym1, paym2));


           //All instances saved to the db




































        };
    }



}
