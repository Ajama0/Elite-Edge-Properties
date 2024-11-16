package com.example.Elite.Edge.Properties.Model;


import jakarta.persistence.*;

@Entity
public class Lease {

    @Id
    @SequenceGenerator(name = "Lease_sequence",
            sequenceName = "lease_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Lease_sequence"

    )
    private Long id;


    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Units unit;



}
