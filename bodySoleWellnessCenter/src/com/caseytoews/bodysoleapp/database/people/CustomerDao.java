/**
 * Project: BodySoleWellness
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */
package com.caseytoews.bodysoleapp.database.people;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.Dao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.business.CustomerInvoice;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CRUD for DB customer-JSON file
 */
public class CustomerDao extends Dao {

	private static final Logger LOG = LogManager.getLogger();
	private static CustomerDao customerDaoInstance = new CustomerDao();
	private static BodyPackageDao packagedDaoInstance;
	private static BodyServiceDao serviceDaoInstance;
	private static GroupDao groupDaoInstance;
	private FilesReader filesReader;
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * private constructor for singleton instance
	 */
	private CustomerDao() {
		super("customers");
		filesReader = FilesReader.getTheInstance();
		packagedDaoInstance = BodyPackageDao.getTheInstance();
		serviceDaoInstance = BodyServiceDao.getTheInstance();
		groupDaoInstance = GroupDao.getTheInstance();
	}

	public static CustomerDao getTheInstance() {
		return customerDaoInstance;
	}

	public List<Customer> getAllCustomers() throws ApplicationException {
		return filesReader.readCustomers();
	}

	public long getWalkInCustomerID() throws ApplicationException {
		List<Customer> customers = getAllCustomers();
		long id = 0;
		for (Customer c : customers) {
			if (c.getFirstName().equalsIgnoreCase("Walk-In")) {
				id = c.getID();
				break;
			}
		}
		return id;
	}

	public void addCustomerInvoice(CustomerInvoice customerInvoice) throws ApplicationException {
		List<CustomerInvoice> customerInvoices;
		customerInvoices = filesReader.readCustomerInvoices();
		Long customerInvoiceID = customerDaoInstance.getNextID("customerInvoiceID");
		customerInvoice.setCustomerInvoiceID(customerInvoiceID);
		customerInvoices.add(customerInvoice);
		updateCustomerInvoicesFile(customerInvoices);
	}

	public void addCustomer(Customer customer) throws ApplicationException {
		List<Customer> customers;
		try {
			customers = filesReader.readCustomers();
			Long customerID = customerDaoInstance.getNextID("customerID");
			customer.setID(customerID);

			// Capitalize first letter of first name and last name
			String fn = customer.getFirstName();
			String firstName = fn.substring(0, 1).toUpperCase() + fn.substring(1);
			customer.setFirstName(firstName);
			String ln = customer.getLastName();
			String lastName = ln.substring(0, 1).toUpperCase() + ln.substring(1);
			customer.setLastName(lastName);

			customers.add(customer);
			updateCustomersFile(customers);

		} catch (ApplicationException e) {
			LOG.error("ERROR: CustomerDAo() addCustomer()");
			throw new ApplicationException(e);
		}
	}

	public void updateCustomer(Customer customer) throws ApplicationException {
		List<Customer> customers;
		try {

			customers = filesReader.readCustomers();
			Iterator<Customer> iter = customers.iterator();
			while (iter.hasNext()) {
				if (iter.next().getID() == customer.getID()) {
					iter.remove();
					break;
				}
			}

			// Capitalize first letter of first name and last name
			String fn = customer.getFirstName();
			String firstName = fn.substring(0, 1).toUpperCase() + fn.substring(1);
			customer.setFirstName(firstName);
			String ln = customer.getLastName();
			String lastName = ln.substring(0, 1).toUpperCase() + ln.substring(1);
			customer.setLastName(lastName);

			customers.add(customer);
			updateCustomersFile(customers);

		} catch (ApplicationException e) {
			LOG.error("CustomerDao/updateCustomers() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateCustomersFile(List<Customer> customers) throws ApplicationException {
		try {
			customerDaoInstance.updateFile(IOConstants.CUSTOMERS_FILE, customers);
		} catch (Exception e) {
			LOG.error("ERROR: CustomerDAo() updateCustomersFile()");
			throw new ApplicationException(e);
		}
	}

	public void updateCustomerInvoicesFile(List<CustomerInvoice> customerInvoices) throws ApplicationException {
		try {
			customerDaoInstance.updateFile(IOConstants.CUSTOMERINVOICES_FILE, customerInvoices);
		} catch (Exception e) {
			LOG.error("ERROR: CustomerDAo() updateCustomerInvoicesFile()");
			throw new ApplicationException(e);
		}
	}

	public char validateNewCustomer(Customer customer) throws ApplicationException {

		char validationCode = 'A'; // start as valid
		List<Customer> customers;
		try {
			customers = filesReader.readCustomers();
			if (customers == null) {
				return validationCode;
			}

			// if this is the first customer added to the application
			if (customers.size() == 0) {

				return validationCode;
			} else {
				// test for invalid and change validationCode accordingly
				for (Customer c : customers) {
					String newCustomerName = customer.getFirstName() + customer.getLastName();

					if (newCustomerName.equalsIgnoreCase(c.getFirstName() + c.getLastName()) || customer.getPhone().equals(c.getPhone())) {
						if (newCustomerName.equalsIgnoreCase(c.getFirstName() + c.getLastName()) && customer.getPhone().equals(c.getPhone())) {
							validationCode = 'B';// means both name and phone are duplicate
						} else if (newCustomerName.equalsIgnoreCase(c.getFirstName() + c.getLastName())) {
							validationCode = 'N'; // means only a duplicate name
						} else if (customer.getPhone().equals(c.getPhone())) {
							validationCode = 'P'; // means only a duplicate phone
						}
						break;
					}
				}
			}
		} catch (ApplicationException e) {
			LOG.error("ERROR: CustomerDAo() validateNewCustomer()");
			throw new ApplicationException(e);
		}
		return validationCode;
	}

	public ArrayList<Long> getCustomerServiceIDs(long customerID) throws ApplicationException {
		Customer customer = getCustomerById(customerID);
		ArrayList<Long> serviceIDs = customer.getServiceIDs();
		return serviceIDs;
	}

	public ArrayList<Long> getCustomerPackageIDs(long customerID) throws ApplicationException {
		ArrayList<Long> packageIDs = null;
		Customer customer = getCustomerById(customerID);
		packageIDs = customer.getPackageIDs();
		return packageIDs;
	}

	public Customer getCustomerById(long customerId) throws ApplicationException {
		Customer customer = null;
		List<Customer> customers;
		try {
			customers = filesReader.readCustomers();
			for (Customer c : customers) {
				if (c.getID() == customerId) {
					customer = c;
					break;
				}
			}
		} catch (ApplicationException e) {
			LOG.error("CustomerDao/getCustomerById() ERROR");
			throw new ApplicationException(e);
		}
		return customer;
	}

	public ArrayList<Customer> getCustomerBy4Digits(String fourDigits) throws Exception {
		ArrayList<Customer> matchingCustomers = new ArrayList<>();
		List<Customer> customers;

		try {
			customers = filesReader.readCustomers();
			for (Customer c : customers) {
				String lastFourDigits = c.getPhone().substring(c.getPhone().length() - 4);
				if (lastFourDigits.equals(fourDigits)) {
					matchingCustomers.add(c);
				}
			}
		} catch (ApplicationException e) {
			LOG.error("ERROR: CustomerDAo() getCustomerByPhone()");
			throw new ApplicationException(e);
		}
		return matchingCustomers;
	}

	public void addPackageToCustomer(long packageID, long customerID) throws ApplicationException {
		try {
			Customer customer = getCustomerById(customerID);
			ArrayList<Long> packageIDs = customer.getPackageIDs();
			packageIDs.add(packageID);
			customer.setPackageIDs(packageIDs);
			updateCustomer(customer);
		} catch (Exception e) {
			LOG.error("CustomerDao/addPAckageToCustomer() Error");
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}

	public void addServiceToCustomer(long serviceID, String serviceDate, long customerID) throws ApplicationException {
		Customer customer = getCustomerById(customerID);
		ArrayList<Long> serviceIDs = customer.getServiceIDs();
		serviceIDs.add(serviceID);
		customer.setServiceIDs(serviceIDs);

		// Join Date
		LocalDate LDService = LocalDate.parse(serviceDate, UiCommon.DATETIME_FORMAT_MMddyyyy);

		if (customer.getJoinDate() == null) {
			customer.setJoinDate(serviceDate);
		} else {
			LocalDate LDcurrentJoinDate = LocalDate.parse(customer.getJoinDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
			if (LDService.isBefore(LDcurrentJoinDate)) {
				customer.setJoinDate(serviceDate);
			}
		}
		updateCustomer(customer);
	}

	public double[] getCustomerSales_Balance_Remaining(Customer customer) throws ApplicationException {
		double financials[] = new double[4];
		double totalSales = 0.0;
		double packageBalance = 0.0;
		double bodyRemaining = 0.0;
		double acupunctureRemaining = 0.0;
		if (new File(IOConstants.PACKAGED_FILE).exists()) {

			// get body and acupuncture remaining if customer is in a group
			if (!groupDaoInstance.validateGroupMember(customer.getID())) {
				ArrayList<BodyPackage> customerPackages = packagedDaoInstance.getPackagesByCustomerID(customer.getID());
				for (BodyPackage p : customerPackages) {
					totalSales += p.getTotalCost();
					packageBalance += packagedDaoInstance.getPackagedBalance(p);
				}
				Group group = groupDaoInstance.getGroupByCustomerId(customer.getID());
				double groupRemaining[] = groupDaoInstance.getGroupRemaining(group);
				bodyRemaining = groupRemaining[0];
				acupunctureRemaining = groupRemaining[1];

			} else { // if customer is not in a group,
				ArrayList<BodyPackage> customerPackages = packagedDaoInstance.getPackagesByCustomerID(customer.getID());
				for (BodyPackage p : customerPackages) {
					totalSales += p.getTotalCost();
					packageBalance += packagedDaoInstance.getPackagedBalance(p);
					if (p.getProductCode().startsWith("A")) {
						acupunctureRemaining += packagedDaoInstance.getPackagedRemaining(p);
					} else {
						bodyRemaining += packagedDaoInstance.getPackagedRemaining(p);
					}
				}
			}
		}

		// PROCESS CUSTOMERS SERVICES
		if (new File(IOConstants.SERVICES_FILE).exists()) {
			ArrayList<BodyService> customerServices = serviceDaoInstance.getServicesByCustomerID(customer.getID());
			for (BodyService s : customerServices) {
				if (s.getPackageID() == 0) {
					totalSales += s.getTotalCost();
				}
			}
		}

		BigDecimal TSales = new BigDecimal(totalSales);
		TSales = TSales.setScale(2, RoundingMode.HALF_UP);
		financials[0] = TSales.doubleValue();

		BigDecimal BDBalance = new BigDecimal(packageBalance);
		BDBalance = BDBalance.setScale(2, RoundingMode.HALF_UP);
		financials[1] = BDBalance.doubleValue();

		BigDecimal bodyRemain = new BigDecimal(bodyRemaining);
		bodyRemain = bodyRemain.setScale(2, RoundingMode.HALF_UP);
		financials[2] = bodyRemain.doubleValue();

		BigDecimal acupRemain = new BigDecimal(acupunctureRemaining);
		acupRemain = acupRemain.setScale(2, RoundingMode.HALF_UP);
		financials[3] = acupRemain.doubleValue();

		return financials;
	}

	public void delete(Customer customer) throws ApplicationException {

	}
}