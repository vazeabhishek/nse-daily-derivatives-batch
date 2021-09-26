package com.invicto.nddb.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CONTRACT")
public class Contract {
    @Id
    @GeneratedValue(generator = "contract-sequence-generator")
    @GenericGenerator(
            name = "contract-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "contract_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
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
