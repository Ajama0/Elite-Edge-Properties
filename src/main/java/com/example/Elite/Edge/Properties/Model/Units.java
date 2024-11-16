package com.example.Elite.Edge.Properties.Model;


import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Enums.unitType;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Units {


    @Id
    @SequenceGenerator(name = "units_sequence",
            sequenceName = "units_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "units_sequence"

    )
    private Long id;

    @Column(name = "unit_number", nullable = false)
    private String unitNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_status", nullable = false)
    private unitStatus unitStatus;

    @Column(name = "Floor_number", nullable = false)
    private String FloorNumber;

    @Column(name = "rent_price")
    private double rentprice;

    @Column(name = "unit_value")
    private double unitvalue;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false)
    private unitType unitType; //whether it is an apartment, an office, studio etc

    @Column(name = "number_of_rooms")
    private Integer numberofrooms;

    @Column(name = "deposit_amount")
    private double deposit;


    /**
     * Entity relationships
     * property->units 1:M  -- bi-directional
     * landlord -> units 1:M  bi-directional
     * units -> Tenants 1:1
     * units->lease 1:M bi-directional, non-owning side is units as it doesnt need to hold store
     * informatoin about a lease
     */

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "Landlord_id")
    private Landlord landlord;

    @OneToOne
    @JoinColumn(name = "tenant_id")
    private Tenants tenant;   //remove this, the tenant cant exist without a unit_id, hence should be the owning side and make it bidrectional

    @OneToMany(mappedBy = "units")
    private List<Lease> lease;











}
