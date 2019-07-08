/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 17, 2018
 * Time: 5:04:12 PM
 */

package com.caseytoews.bodysoleapp.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.domain.business.CustomerInvoice;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * read JSON database files
 */
public class FilesReader {
	private static final Logger LOG = LogManager.getLogger();
	private static FilesReader theInstance = new FilesReader();
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Private Singleton Default Constructor
	 */
	private FilesReader() {
		super();
	}

	public static FilesReader getTheInstance() {
		return theInstance;
	}

	public byte[] readIDs() throws ApplicationException {
		byte[] jsonData;
		try {
			jsonData = Files.readAllBytes(Paths.get(IOConstants.NEXTID_FILE));
		} catch (IOException e) {
			LOG.error("FilesReader/readIDs() error catch");
			throw new ApplicationException(e);
		}
		return jsonData;
	}

	public List<Customer> readCustomers() throws ApplicationException {

		List<Customer> customers = null;
		try {

			customers = objectMapper.readValue(new File(IOConstants.CUSTOMERS_FILE),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Customer.class));
		}

		catch (Exception e) {
			LOG.error("FilesReader/readCustomers() error catch");
			throw new ApplicationException(e);
		}
		return customers;
	}

	public List<CustomerInvoice> readCustomerInvoices() throws ApplicationException {

		List<CustomerInvoice> customerInvoices = null;
		try {
			customerInvoices = objectMapper.readValue(new File(IOConstants.CUSTOMERINVOICES_FILE),
					objectMapper.getTypeFactory().constructCollectionType(List.class, CustomerInvoice.class));
		} catch (Exception e) {
			LOG.error("FilesReader/readCustomerInvoices() error catch");
			throw new ApplicationException(e);
		}
		return customerInvoices;
	}

	public List<BodyService> readServices() throws ApplicationException {

		List<BodyService> services = null;
		try {

			services = objectMapper.readValue(new File(IOConstants.SERVICES_FILE),
					objectMapper.getTypeFactory().constructCollectionType(List.class, BodyService.class));
		} catch (Exception e) {
			LOG.error("FilesReader/readServices() error catch");
			throw new ApplicationException(e);
		}
		return services;
	}

	public List<BodyPackage> readPackaged() throws ApplicationException {

		List<BodyPackage> packaged = null;
		try {

			packaged = objectMapper.readValue(new File(IOConstants.PACKAGED_FILE),
					objectMapper.getTypeFactory().constructCollectionType(List.class, BodyPackage.class));
		} catch (Exception e) {
			LOG.error("FilesReader/readPackaged() error catch");
			throw new ApplicationException(e);
		}
		return packaged;
	}

	public byte[] readProducts() throws ApplicationException {

		byte[] jsonData;
		try {
			jsonData = Files.readAllBytes(Paths.get(IOConstants.PRODUCTS_FILE));
		} catch (IOException e) {
			LOG.error("FilesReader/readProducts() error catch");
			throw new ApplicationException(e);
		}
		return jsonData;
	}

	public List<Staff> readStaff() throws ApplicationException {

		List<Staff> staff = null;
		try {

			staff = objectMapper.readValue(new File(IOConstants.STAFF_FILE),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Staff.class));
		} catch (Exception e) {
			LOG.error("FilesReader/readStaff() error catch");
			throw new ApplicationException(e);
		}
		return staff;
	}

	public List<Group> readGroups() throws ApplicationException {

		List<Group> groups = null;
		try {
			groups = objectMapper.readValue(new File(IOConstants.GROUPS_FILE),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Group.class));
		} catch (Exception e) {
			LOG.error("FilesReader/readGroups() error catch");
			throw new ApplicationException(e);
		}
		return groups;
	}
}