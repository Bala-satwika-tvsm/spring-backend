package com.tvse.callrecordingsbackend;

import com.tvse.callrecordingsbackend.repository.CallRecordingsRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CallrecordingsbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallrecordingsbackendApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(CallRecordingsRepo cr) {
//		return args -> cr.fetchCallRecords();
//	}

}
