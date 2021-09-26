package com.invicto.nddb.process;

import com.invicto.nddb.entity.RunBook;
import com.invicto.nddb.model.EquityDerivativeCsvRecord;
import com.invicto.nddb.repository.RunBookRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@Slf4j
@Component
public class BhavCopyReader {

    private DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd-MMM-yyyy")
            .toFormatter(Locale.ENGLISH);

    private RunBookRepo runBookRepo;

    @Autowired
    public BhavCopyReader(RunBookRepo runBookRepo) {
        this.runBookRepo = runBookRepo;
    }

    @Transactional
    public void loadBhavCopy(File file, BhavCopyRecordProcessor processor) {
        long recordCounter = 0;
        RunBook runBook = new RunBook();
        runBook.setStartTime(LocalDateTime.now());
        runBookRepo.save(runBook);
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().withAllowMissingColumnNames().parse(in);
            for (CSVRecord record : records) {
                EquityDerivativeCsvRecord edcsvRecord = new EquityDerivativeCsvRecord();
                edcsvRecord.setInstrument(record.get("INSTRUMENT"));
                edcsvRecord.setSymbol(record.get("SYMBOL"));
                edcsvRecord.setExpiryDt(record.get("EXPIRY_DT"), formatter);
                edcsvRecord.setStrikePrice(record.get("STRIKE_PR"));
                edcsvRecord.setOptionTYpe(record.get("OPTION_TYP"));
                edcsvRecord.setOpen(record.get("OPEN"));
                edcsvRecord.setHigh(record.get("HIGH"));
                edcsvRecord.setLow(record.get("LOW"));
                edcsvRecord.setClose(record.get("CLOSE"));
                edcsvRecord.setSettle(record.get("SETTLE_PR"));
                edcsvRecord.setContracts(record.get("CONTRACTS"));
                edcsvRecord.setValInLakh(record.get("VAL_INLAKH"));
                edcsvRecord.setOi(record.get("OPEN_INT"));
                edcsvRecord.setCoi(record.get("CHG_IN_OI"));
                edcsvRecord.setTimestamp(record.get("TIMESTAMP"), formatter);
                if(edcsvRecord.getInstrument().contentEquals("FUTSTK"))
                processor.process(edcsvRecord,runBook);
                recordCounter ++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
