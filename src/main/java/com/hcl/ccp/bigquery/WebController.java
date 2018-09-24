/*
 *  Copyright 2017 original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hcl.ccp.bigquery;

import com.google.api.Service;
import com.google.api.services.bigquery.BigqueryRequest;
import com.google.cloud.bigquery.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebController {

	private static final Log LOGGER = LogFactory.getLog(WebController.class);

	private BigQuery bigQuery;
	private Dataset dataset = null;

	String datasetName = "pubsubdataset";




	public WebController(BigQuery bigQuery) {
	this.bigQuery = bigQuery;
	}

	@PostMapping("/data/view")
	public ResponseEntity<List<Message>> createTopic(@RequestParam("dataset") String dataset) {

		try {
			DatasetInfo datasetInfo = DatasetInfo.newBuilder(datasetName).build();
			this.dataset = this.bigQuery.getDataset(datasetName);
			LOGGER.info("Dataset Retrieved " + this.dataset.getDatasetId().getDataset());
			System.out.printf("Dataset %s Retrieved %n", this.dataset.getDatasetId().getDataset());
			QueryJobConfiguration conf = QueryJobConfiguration.newBuilder(" SELECT * FROM pubsubdataset.incomingevents ").build();
		    Iterable<FieldValueList>  rows = this.dataset.getBigQuery().query(conf).iterateAll();
			List<Message>  messages = new ArrayList<Message>();
		    for(FieldValueList singleRow : rows){
		    	System.out.print( " id : " + singleRow.get(0).getStringValue());
				System.out.print( " Title : " + singleRow.get(1));
				System.out.print( " Type : " + singleRow.get(2));
				Message message = new Message();
				message.setId(singleRow.get("id").getStringValue());
				message.setTitle(singleRow.get("title").getStringValue());
				message.setType(singleRow.get("type").getStringValue());
				//message.setSummary(singleRow.get("type").getStringValue());
				messages.add(message);
			}
			return new ResponseEntity<List<Message>>(messages,HttpStatus.OK);

		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		return new ResponseEntity<List<Message>>(HttpStatus.BAD_REQUEST);
	}


}
