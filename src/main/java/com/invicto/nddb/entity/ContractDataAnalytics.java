package com.invicto.nddb.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ContractDataAnalytics {
    @Id
    @GeneratedValue(generator = "contract-analytics-sequence-generator")
    @GenericGenerator(
            name = "contract-analytics-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "contract_analytics_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private double deltaVolumeP;
    private double deltaOiP;
    private double deltaCloseP;
    private String signal;
    private LocalDate analyticsDate;
    @ManyToOne(targetEntity = Contract.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Contract contract;
}
