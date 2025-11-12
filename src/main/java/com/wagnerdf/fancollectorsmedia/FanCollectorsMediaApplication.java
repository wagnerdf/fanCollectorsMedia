package com.wagnerdf.fancollectorsmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling  //Desativa os serviços programados por periodo, sendo um deles o da pasta JOBS que faz entrada de script no banco de dados
public class FanCollectorsMediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FanCollectorsMediaApplication.class, args);
	}

}
