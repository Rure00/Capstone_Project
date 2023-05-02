package com.capstone.server;

import com.capstone.server.read_excel.ExcelRead;
import com.capstone.server.read_excel.ExcelReadOption;
import com.capstone.server.read_excel.UploadLineData;
import com.capstone.server.repository.RoadwayRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
