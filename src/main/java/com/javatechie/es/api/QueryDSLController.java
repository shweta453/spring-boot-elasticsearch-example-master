package com.javatechie.es.api;

import com.javatechie.es.api.model.Customer;
import com.javatechie.es.api.repository.CustomerRepository;
import com.javatechie.es.api.service.QueryDSLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryDSLController {

	@Autowired
	private QueryDSLService service;

	@Autowired
	CustomerRepository repository;

	@GetMapping("/serachMultiField/{firstname}/{age}")
	public List<Customer> serachByMultiField(@PathVariable String firstname, @PathVariable int age) {
		return service.searchMultiField(firstname, age);
	}

	@GetMapping("/customSearch/{firstName}")
	public List<Customer> getCustomerByField(@PathVariable String firstName) {
		return service.getCustomerSerachData(firstName);
	}

	@GetMapping("/search/{text}")
	public List<Customer> doMultimatchQuery(@PathVariable String text) {
		return service.multiMatchQuery(text);
	}

	@GetMapping("/get/{text}")
	public List<Customer> getAll(@PathVariable String text){
		return service.getAll(text);
	}

}
