package com.example.Elite.Edge.Properties.Model;


import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "Property_Owner")
public class PropertyOwner{



    @Id
    @SequenceGenerator(name = "owner_sequence",
            sequenceName = "owner_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "owner_sequence"

    )
    private Long id;




    @ManyToMany
    @JoinTable(name = "property_0wners_m2m",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    private List<Property> properties;
}
