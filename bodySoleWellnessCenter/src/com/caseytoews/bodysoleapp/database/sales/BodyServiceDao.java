/**
 * Project: BodySoleWellness
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */
package com.caseytoews.bodysoleapp.database.sales;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.Dao;
import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CRUD for DB service-purchase-JSON file
 *
 */
public class BodyServiceDao extends Dao {

	private static final Logger LOG = LogManager.getLogger();

	private static BodyServiceDao serviceDaoInstance = new BodyServiceDao();
	private static FilesReader filesReader;
	private static CustomerDao customerDaoInstance;
	private static BodyPackageDao packagedDaoInstance;
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * private constructor for singleton instance
	 */
	private BodyServiceDao() {
		super("services");
		filesReader = FilesReader.getTheInstance();
		customerDaoInstance = CustomerDao.getTheInstance();
		packagedDaoInstance = BodyPackageDao.getTheInstance();
	}

	public static BodyServiceDao getTheInstance() {
		return serviceDaoInstance;
	}

	public void addService(BodyService service) throws ApplicationException {
		List<BodyService> services;
		try {
			services = filesReader.readServices();
			long serviceID = serviceDaoInstance.getNextID("serviceID");
			service.setPurchaseID(serviceID);
			services.add(service);
			updateServicesFile(services);

			customerDaoInstance.getTheInstance().addServiceToCustomer(serviceID, service.getPurchaseDate(), service.getCustomerID());
			if (service.getPackageID() != 0) {
				packagedDaoInstance.getTheInstance().updatePackagedWithAService(service.getPackageID(), serviceID);
			}

		} catch (ApplicationException e) {
			LOG.error("ServiceDao/addService() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateEditedService(BodyService service) throws ApplicationException {
		List<BodyService> services;
		try {
			services = filesReader.readServices();
			services.add(service);
			updateServicesFile(services);

			customerDaoInstance.getTheInstance().addServiceToCustomer(service.getPurchaseID(), service.getPurchaseDate(), service.getCustomerID());
			if (service.getPackageID() != 0) {
				packagedDaoInstance.getTheInstance().updatePackagedWithAService(service.getPackageID(), service.getPurchaseID());
			}

		} catch (ApplicationException e) {
			LOG.error("CustomerDao/updateCustomers() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateServicesFile(List<BodyService> services) throws ApplicationException {
		try {
			serviceDaoInstance.updateFile(IOConstants.SERVICES_FILE, services);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public BodyService getServiceById(long serviceID) throws ApplicationException {
		BodyService service = null;
		List<BodyService> services;

		try {
			services = filesReader.readServices();
			for (BodyService s : services) {
				if (s.getPurchaseID() == serviceID) {
					service = s;
					break;
				}
			}
		} catch (ApplicationException e) {
			LOG.error("ServiceDao/getServiceById() ERROR");
			throw new ApplicationException(e);
		}
		return service;
	}

	public List<BodyService> getServiceByDate(String date) throws ApplicationException {

		List<BodyService> services = new ArrayList<>();

		try {
			List<BodyService> allServices = filesReader.readServices();
			for (BodyService service : allServices) {
				if (service.getPurchaseDate().equalsIgnoreCase(date)) {
					services.add(service);
				}
			}
		} catch (ApplicationException e) {
			LOG.error("ServiceDao/getServiceById() ERROR");
			throw new ApplicationException(e);
		}
		return services;
	}

	public ArrayList<BodyService> getServicesByCustomerID(long customerId) throws ApplicationException {
		ArrayList<BodyService> customerServices = new ArrayList<>();
		List<BodyService> allServices;
		if (customerId != 0) {

			ArrayList<Long> customerServiceIDs = customerDaoInstance.getTheInstance().getCustomerServiceIDs(customerId);

			if (customerServiceIDs != null) {
				try {

					allServices = filesReader.readServices();
					for (BodyService service : allServices) {
						for (long serviceID : customerServiceIDs) {
							if (serviceID == service.getPurchaseID()) {
								customerServices.add(service);
							}
						}
					}
				} catch (ApplicationException e) {
					LOG.error("ERROR: ServiceDao() getServicesByCustomerID()");
					throw new ApplicationException(e);
				}
			} else {
				LOG.debug("customerId in ServiceDao.getServicesByCustomerID() is null");
			}
		}
		return customerServices;
	}

	public void deleteService(BodyService originalService) throws ApplicationException {

		List<BodyService> services;

		// ***1***remove service from servicesFile***
		services = filesReader.readServices();
		Iterator<BodyService> serviceIter = services.iterator();
		while (serviceIter.hasNext()) {
			if (serviceIter.next().getPurchaseID() == originalService.getPurchaseID()) {
				serviceIter.remove();
				break;
			}
		}
		updateServicesFile(services);

		// ***2***remove service from packaged file if there is a packaged ID with the service***
		if (originalService.getPackageID() != 0) {
			// get all packaged array
			List<BodyPackage> packaged;
			BodyPackage originalPackaged = null;
			packaged = filesReader.readPackaged();
			Iterator<BodyPackage> packageIter = packaged.iterator();

			// find the package
			while (packageIter.hasNext()) {
				originalPackaged = packageIter.next();
				if (originalPackaged.getPurchaseID() == originalService.getPackageID()) {
					// create originalPackaged and remove the packaged from the all packaged array
					packageIter.remove();
					break;
				}
			}
			// search originalPackaged serviceIDs and remove the service id
			List<Long> serviceIDs;
			serviceIDs = originalPackaged.getServices();
			Iterator<Long> servIDIter = serviceIDs.iterator();
			while (servIDIter.hasNext()) {
				if (servIDIter.next() == originalService.getPurchaseID()) {
					servIDIter.remove();
					// set the originalPackaged serviceIDs service long array with the updated array
					ArrayList<Long> editedServices = new ArrayList<>(serviceIDs);
					originalPackaged.setServices(editedServices);
					break;
				}
			}
			// add updated orriginalPackaged to packaged array
			packaged.add(originalPackaged);
			// update packaged file with updated packaged array.
			packagedDaoInstance.getTheInstance().updatePackagedFile(packaged);
		}

		// ***3***remove service from customer file***
		List<Customer> customers;
		customers = filesReader.readCustomers();
		Customer originalCustomer = null;
		Iterator<Customer> customerIter = customers.iterator();
		// remove customer and create originalCustomer
		while (customerIter.hasNext()) {
			originalCustomer = customerIter.next();
			if (originalCustomer.getID() == originalService.getCustomerID()) {
				customerIter.remove();
				break;
			}
		}
		// remove serviceID from originalcustomer's serviceIDs array;
		List<Long> custServiceIDs = originalCustomer.getServiceIDs();
		Iterator<Long> customerServIDsIter = custServiceIDs.iterator();
		while (customerServIDsIter.hasNext()) {
			if (customerServIDsIter.next() == originalService.getPurchaseID()) {
				customerServIDsIter.remove();
				// set the servicesID of originalCustomer with update array
				ArrayList<Long> updatedCustServiceIDs = new ArrayList<>(custServiceIDs);
				originalCustomer.setServiceIDs(updatedCustServiceIDs);
				// add customer to customers array
				customers.add(originalCustomer);
				// Update customers File
				List<Customer> updateCustomers = customers;
				customerDaoInstance.getTheInstance().updateCustomersFile(updateCustomers);
				break;
			}
		}
	}
}