package com.invicto.nddb.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "RUN_BOOK")
public class RunBook {

    @Id
    @GeneratedValue(generator = "runbook-sequence-generator")
    @GenericGenerator(
            name = "runbook-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "runbook_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long runNumber;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Column(name = "END_TIME")
    private LocalDateTime endTime;
    @Column(name = "RECORDS_PROCESSED")
    private Long statsRecordProcessed;

    @OneToMany(mappedBy = "runBook",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<ContractData> contractData;

}
