package com.invicto.nddb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "RUN_BOOK")
public class RunBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
