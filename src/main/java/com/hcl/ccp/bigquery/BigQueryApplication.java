package com.hcl.ccp.bigquery;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class BigQueryApplication {

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${spring.cloud.gcp.config.credentials.location}")
	String credentialJsonPath;

	//@Autowired
	GoogleCredentials credentials;


	public static void main(String[] args) {
		SpringApplication.run(BigQueryApplication.class, args);
	}



 @Bean
  public BigQuery createBigQueryBean(){


/* added logic for query bean */

	 try  {
//		 File credentialsPath = resourceLoader.getResource(credentialJsonPath).getInputStream();
		 InputStream in = resourceLoader.getResource(credentialJsonPath).getInputStream();
//		 FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);
		 credentials = ServiceAccountCredentials.fromStream(in);

		 BigQuery bigQuery =
				 BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
		 return bigQuery;
	 }
	 catch (Exception io){
		System.out.println(" Got IO Exception " );
		io.printStackTrace();
	 }

	 // Instantiate a client.

	 return BigQueryOptions.getDefaultInstance().getService();
  }
}
