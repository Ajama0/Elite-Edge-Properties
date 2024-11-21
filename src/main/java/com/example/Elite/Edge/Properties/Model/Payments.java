package com.example.Elite.Edge.Properties.Model;

import com.example.Elite.Edge.Properties.Enums.paymentStatus;
import com.example.Elite.Edge.Properties.Model.Lease;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Payments {

    @Id

    @SequenceGenerator(name = "payment_sq",
        sequenceName = "", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "payment_sq"
    )
    private Long id;

    //user
    @Column(name = "payment_reference", nullable = false, unique = true)
    private String paymentReference; // Token from payment gateway

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "status", nullable = false)
    private paymentStatus status; // e.g., SUCCESS, FAILED, PENDING

    @Column(name = "card_last_four", nullable = true)
    private String cardLastFour; // Only store last four digits, if needed

    @ManyToOne
    @JoinColumn(name = "lease_id", nullable = false)
    private Lease lease; // Payment associated with a lease


    public Payments(String paymentReference, Double amount, paymentStatus status,
                    String cardLastFour) {
        this.paymentReference = paymentReference;
        this.amount = amount;
        this.status = status;
        this.cardLastFour = cardLastFour;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public Double getAmount() {
        return amount;
    }

    public paymentStatus getStatus() {
        return status;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public Lease getLease() {
        return lease;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setStatus(paymentStatus status) {
        this.status = status;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }
}
