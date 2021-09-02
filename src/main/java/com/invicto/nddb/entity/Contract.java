package com.invicto.nddb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CONTRACT")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "INSTRUMENT")
    private String instrument;
    @Column(name = "SYMBOL")
    private String symbol;
    @Column(name = "EXPIRY_DT")
    private LocalDate expiryDate;
    @OneToMany(mappedBy = "contract",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<ContractData> contractData;
}
