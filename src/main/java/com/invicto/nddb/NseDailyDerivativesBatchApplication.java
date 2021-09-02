package com.invicto.nddb;

import com.invicto.nddb.process.BhavCopyReader;
import com.invicto.nddb.process.BhavCopyRecordProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class NseDailyDerivativesBatchApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(NseDailyDerivativesBatchApplication.class, args);

		if(args.length > 0){
			String pathToFile = args[0];
			File file = new File(pathToFile);
			if(file.exists()){
				log.info("Starting processing file {}",pathToFile);
				BhavCopyReader bhavCopyReader = context.getBean(BhavCopyReader.class);
				BhavCopyRecordProcessor processor = context.getBean(BhavCopyRecordProcessor.class);
				bhavCopyReader.loadBhavCopy(file,processor);
			}
			else {
				log.info("File not found {}",pathToFile);
				System.exit(-1);
			}
		}
		else {
			log.info("No files to process");
			System.exit(-1);
		}
	}

}
