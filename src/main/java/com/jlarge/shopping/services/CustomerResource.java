package com.jlarge.shopping.services;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.jlarge.shopping.domain.Customer;

@Path("/customers")
public class CustomerResource {
	private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
	
	private AtomicInteger idCounter = new AtomicInteger();
	
	@POST
	@Consumes("application/xml")
	public Response createCustomer(Customer customer) {
		int id = idCounter.incrementAndGet();
		customer.setId(id);
		customerDB.put(id, customer);
		return Response.created(URI.create("/customers/" + id)).build();
	}
	
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public Customer getCustomer(int id) {
		Customer customer = customerDB.get(id);
		if (customer == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return customer;
	}
	
	@PUT
	@Consumes("application/xml")
	public void updateCustomer(Customer updated) {
		int id = updated.getId();
		Customer current = customerDB.get(id); 
		if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		customerDB.put(id, updated);
	}
}