/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 1, 2019
 * Time: 11:50:30 AM
 */

package com.caseytoews.bodysoleapp.utility.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.product.ProductDetailsDao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.domain.business.CustomerInvoice;
import com.caseytoews.bodysoleapp.domain.id.ID;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;
import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;
import com.caseytoews.bodysoleapp.domain.product.ProductDetails;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is instantiated if run args "1" is passed
 */
public class InitialFilesCreator {
	private static CustomerDao customerDao;
	private static BodyServiceDao serviceDao;
	private static ProductDetailsDao productsDao;
	private static BodyPackageDao packagedDao;
	private static StaffDao staffDao;
	private static GroupDao groupDao;
	private final ObjectMapper objectMapper = new ObjectMapper();
	final String nameLexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final java.util.Random rand = new java.util.Random();
	final Set<String> identifiers = new HashSet<>();
	Customer customer;

	public InitialFilesCreator() {
		customerDao = CustomerDao.getTheInstance();
		serviceDao = BodyServiceDao.getTheInstance();
		productsDao = ProductDetailsDao.getTheInstance();
		packagedDao = BodyPackageDao.getTheInstance();
		staffDao = StaffDao.getTheInstance();
		groupDao = GroupDao.getTheInstance();
	}

	public void createInitialFiles() throws ApplicationException, JsonGenerationException, JsonMappingException, IOException {

		ID id = new ID(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		// Delete existing files then create new files
		customerDao.deleteFile(IOConstants.NEXTID_FILE);
		JsonNode node = objectMapper.convertValue(id, JsonNode.class);
		customerDao.updateNextIDFile(node);

		ArrayList<Customer> customers = new ArrayList<>();
		customerDao.deleteFile(IOConstants.CUSTOMERS_FILE);
		customerDao.updateFile(IOConstants.CUSTOMERS_FILE, customers);

		ArrayList<BodyService> services = new ArrayList<>();
		serviceDao.deleteFile(IOConstants.SERVICES_FILE);
		serviceDao.updateFile(IOConstants.SERVICES_FILE, services);

		ArrayList<BodyPackage> packages = new ArrayList<>();
		packagedDao.deleteFile(IOConstants.PACKAGED_FILE);
		packagedDao.updateFile(IOConstants.PACKAGED_FILE, packages);

		ArrayList<Group> groups = new ArrayList<>();
		groupDao.deleteFile(IOConstants.GROUPS_FILE);
		groupDao.updateFile(IOConstants.GROUPS_FILE, groups);

		ArrayList<CustomerInvoice> customerInvoices = new ArrayList<>();
		packagedDao.deleteFile(IOConstants.CUSTOMERINVOICES_FILE);
		packagedDao.updateFile(IOConstants.CUSTOMERINVOICES_FILE, customerInvoices);

		// CREATE 3 SPECIFIC AND 22 GENERIC CUSTOMERS
		Customer customerA = new Customer("789-236-9812", "Jennifer", "Aniston", "jenn@email.com");
		Customer customerB = new Customer("674-832-1729", "Adam", "Sandler", "adam@email.com");
		Customer customerC = new Customer("924-673-4714", "Meg", "Ryan", "meg@email.com");
		Customer customerD = new Customer("877-281-7083", "John", "Travolta", "john@email.com");
		Customer customerE = new Customer("546-384-8721", "Walk-In", "Customer", "");

		customerDao.addCustomer(customerA);
		customerDao.addCustomer(customerB);
		customerDao.addCustomer(customerC);
		customerDao.addCustomer(customerD);
		customerDao.addCustomer(customerE);

		// service Products
		ArrayList<BodyServiceProduct> serviceProducts = new ArrayList<>();
		String[] ID = { "F50", "B50", "L75", "A60", "C25", "M30", "G25" };
		String[] name = { "Reflexology 50 mins", "Acupressure 50 mins", "LD Massage 75 mins", "Acupuncture 60 mins", "Fire Cupping 25 mins",
				"Moxibustion 30 mins", "Gua Sha 25 mins" };
		double[] price = { 42.00, 45.00, 69.00, 65.00, 26.00, 26.00, 26.00 };
		for (int i = 0; i < 7; i++) {
			BodyServiceProduct product = new BodyServiceProduct(ID[i], name[i], price[i]);
			serviceProducts.add(product);
		}

		// package Products
		ArrayList<BodyPackageProduct> packagedProducts = new ArrayList<>();
		String[] IDs = { "B11", "B12", "A05", "A11" };
		String[] names = { "Body Package 11", "Body Package 12", "Acupuncture 5", "Acupuncture 11" };
		double[] prices = { 420.00, 450.00, 300.00, 650.00 };
		double[] pricePerPackageService = { 38.18, 37.50, 60.00, 59.09 };
		for (int i = 0; i < 4; i++) {
			BodyPackageProduct product = new BodyPackageProduct(IDs[i], names[i], prices[i], pricePerPackageService[i]);
			packagedProducts.add(product);
		}

		// quantities
		ArrayList<Double> quantities = new ArrayList<>(
				Arrays.asList(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0));

		// update products file
		productsDao.deleteFile(IOConstants.PRODUCTS_FILE);
		ProductDetails products = new ProductDetails(.05, quantities, serviceProducts, packagedProducts);
		productsDao.updateFile(IOConstants.PRODUCTS_FILE, products);

		// Staff
		ArrayList<Staff> staffers = new ArrayList<>();
		staffDao.deleteFile(IOConstants.STAFF_FILE);
		staffDao.updateFile(IOConstants.STAFF_FILE, staffers);
		Staff staff1 = new Staff(1, "604-555-6233", "Kim", "Davidson", "kim@email.com", "09132017", 3467.18);
		Staff staff2 = new Staff(2, "790-423-1290", "Don", "Scott", "don@email.com", "02052018", 2112.33);
		Staff staff3 = new Staff(3, "684-333-1783", "Jane", "Williams", "jane@email.com", "08162016", 4205.41);
		Staff staff4 = new Staff(4, "788-913-2748", "Pam", "Sanders", "pam@email.com", "01122019", 164.51);
		staffDao.addStaff(staff1);
		staffDao.addStaff(staff2);
		staffDao.addStaff(staff3);
		staffDao.addStaff(staff4);
	}
}
