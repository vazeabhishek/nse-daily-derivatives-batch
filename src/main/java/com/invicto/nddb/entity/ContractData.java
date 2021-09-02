package com.invicto.nddb.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "CONTRACT_DATA")
public class ContractData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "OPEN")
    private double open;
    @Column(name = "HIGH")
    private double high;
    @Column(name = "LOW")
    private double low;
    @Column(name = "CLOSE")
    private double close;
    @Column(name = "VOLUME")
    private double volume;
    @Column(name = "OPEN_INT")
    private double openInterest;
    @Column(name = "COLLECTION_DATE")
    private LocalDate collectionDate;

    @ManyToOne(targetEntity = Contract.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Contract contract;

    @ManyToOne(targetEntity = RunBook.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private RunBook runBook;
}
