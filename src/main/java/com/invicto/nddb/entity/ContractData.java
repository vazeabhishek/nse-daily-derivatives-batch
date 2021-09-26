package com.invicto.nddb.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "CONTRACT_DATA")
public class ContractData {

    @Id
    @GeneratedValue(generator = "contract-data-sequence-generator")
    @GenericGenerator(
            name = "contract-data-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "contract_data_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
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
