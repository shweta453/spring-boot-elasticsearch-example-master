package com.javatechie.es.api.service;

import com.javatechie.es.api.model.Customer;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryDSLService {

	@Autowired
	private ElasticsearchTemplate template;

	public List<Customer> searchMultiField(String firstname, int age) {
		QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("firstname", firstname))
				.must(QueryBuilders.matchQuery("age", age));
		NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
		List<Customer> customers = template.queryForList(nativeSearchQuery, Customer.class);
		return customers;
	}

	public List<Customer> getCustomerSerachData(String input) {
		String search = ".*" + input + ".*";
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withFilter(QueryBuilders.regexpQuery("firstname", search)).build();
		List<Customer> customers = template.queryForList(searchQuery, Customer.class);
		return customers;

	}

	public List<Customer> multiMatchQuery(String text) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(text)
				.field("firstname").field("lastname").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)).build();
		List<Customer> customers = template.queryForList(searchQuery, Customer.class);
		return customers;
	}


	public List<Customer> getAll(String text) {

		QueryBuilder query = QueryBuilders.boolQuery()
				.should(
						QueryBuilders.queryStringQuery(text)
								.lenient(true)
								.field("firstname")
								.field("lastname")
				).should(QueryBuilders.queryStringQuery("*" + text + "*")
						.lenient(true)
						.field("firstname")
						.field("lastname"));

		NativeSearchQuery build = new NativeSearchQueryBuilder()
				.withQuery(query)
				.build();

		List<Customer> userses = template.queryForList(build, Customer.class);

		return userses;
	}
}
