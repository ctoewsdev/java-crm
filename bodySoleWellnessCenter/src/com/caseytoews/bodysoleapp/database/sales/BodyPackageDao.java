
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
 * CRUD for DB package-purchase-details-JSON file
 *
 */
public class BodyPackageDao extends Dao {

	private static final Logger LOG = LogManager.getLogger();

	private static BodyPackageDao packagedDaoInstance = new BodyPackageDao();
	private static CustomerDao customerDaoInstance;
	private static BodyServiceDao serviceDaoInstance;
	private static FilesReader filesReader;
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * private constructor for singleton instance
	 */
	private BodyPackageDao() {
		super("packaged");
		customerDaoInstance = CustomerDao.getTheInstance();
		serviceDaoInstance = BodyServiceDao.getTheInstance();
		filesReader = FilesReader.getTheInstance();

	}

	public static BodyPackageDao getTheInstance() {
		return packagedDaoInstance;
	}

	public void addPackaged(BodyPackage packaged) throws ApplicationException {
		List<BodyPackage> allPackaged;
		try {
			allPackaged = filesReader.readPackaged();
			long purchaseID = packagedDaoInstance.getNextID("packagedID");
			packaged.setPurchaseID(purchaseID);
			allPackaged.add(packaged);
			updatePackagedFile(allPackaged);
			customerDaoInstance.getTheInstance().addPackageToCustomer(packaged.getPurchaseID(), packaged.getCustomerID());

		} catch (ApplicationException e) {
			LOG.error("PackagedDao/addPackaged() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updatePackagedWithAService(long packagedID, long serviceID) throws ApplicationException {
		BodyPackage packaged = getPackagedByPackagedId(packagedID);
		ArrayList<Long> services = packaged.getServices();
		services.add(serviceID);
		List<BodyPackage> allPackaged = filesReader.readPackaged();
		Iterator<BodyPackage> iter = allPackaged.iterator();
		while (iter.hasNext()) {
			if (iter.next().getPurchaseID() == packagedID) {
				iter.remove();
				break;
			}
		}
		allPackaged.add(packaged);
		updatePackagedFile(allPackaged);
	}

	public void updatePackagedFile(List<BodyPackage> packaged) throws ApplicationException {
		try {
			System.out.println("inside updated packaged file method");
			packagedDaoInstance.updateFile(IOConstants.PACKAGED_FILE, packaged);
		} catch (Exception e) {
			LOG.error("ERROR: PackagedDao() updatePackagedFile()");
			throw new ApplicationException(e);
		}
	}

	public void updateEditedBodyPackage(BodyPackage updatedPackage) throws ApplicationException {
		List<BodyPackage> bodyPacks;
		try {
			bodyPacks = filesReader.readPackaged();
			bodyPacks.add(updatedPackage);
			System.out.println("updated package: " + updatedPackage.toString());

			updatePackagedFile(bodyPacks);
			customerDaoInstance.getTheInstance().addPackageToCustomer(updatedPackage.getPurchaseID(), updatedPackage.getCustomerID());

		} catch (ApplicationException e) {
			LOG.error("CustomerDao/updateCustomers() ERROR");
			throw new ApplicationException(e);
		}
	}

	public ArrayList<BodyPackage> getPackagesByCustomerID(long customerID) throws ApplicationException {
		ArrayList<BodyPackage> customerPackaged = new ArrayList<>();
		List<BodyPackage> allPackaged;

		if (customerID != 0) {
			ArrayList<Long> customerPackagedIDs = customerDaoInstance.getTheInstance().getCustomerPackageIDs(customerID);
			if (customerPackagedIDs != null) {
				try {
					allPackaged = filesReader.readPackaged();
					for (BodyPackage p : allPackaged) {
						for (long packagedID : customerPackagedIDs) {
							if (packagedID == p.getPurchaseID()) {
								customerPackaged.add(p);
							}
						}
					}
				} catch (ApplicationException e) {
					LOG.error("ERROR: PackagedDao() getPackagesByCustomerID()");
					throw new ApplicationException(e);
				}
			} else {
				return null;
			}
		}
		return customerPackaged;
	}

	public BodyPackage getPackagedByPackagedId(long packagedID) throws ApplicationException {
		BodyPackage packaged = null;
		List<BodyPackage> allPackaged;
		try {
			allPackaged = filesReader.readPackaged();
			for (BodyPackage p : allPackaged) {
				if (p.getPurchaseID() == packagedID) {
					packaged = p;
				}
			}
		} catch (ApplicationException e) {
			LOG.error("ERROR: PackagedDao() getPackagedByPackagedId()");
			throw new ApplicationException(e);
		}
		return packaged;
	}

	public double getPackagedBalance(BodyPackage packaged) throws ApplicationException {
		double expenses = 0.0;
		double packageCost = packaged.getTotalCost();
		ArrayList<Long> serviceIDs = packaged.getServices();
		if (serviceIDs.size() > 0) {
			for (Long id : serviceIDs) {
				BodyService service = serviceDaoInstance.getServiceById(id);
				expenses += service.getTotalCost();
			}
		}
		return packageCost - expenses;
	}

	public double getPackagedRemaining(BodyPackage packaged) throws ApplicationException {
		String maxServices = packaged.getProductCode().substring(1, 3);
		double maxNumOfServices = Double.parseDouble(maxServices);
		double consumed = 0.0;

		ArrayList<Long> serviceIDs = packaged.getServices();
		if (serviceIDs.size() > 0) {
			for (long id : serviceIDs) {
				BodyService service = serviceDaoInstance.getServiceById(id);
				consumed += service.getQuantity();
			}
		}
		return maxNumOfServices - consumed;
	}

	public List<BodyPackage> getPackagesByDate(String date) throws ApplicationException {

		List<BodyPackage> packaged = new ArrayList<>();

		try {
			List<BodyPackage> allPackages = filesReader.readPackaged();
			for (BodyPackage pack : allPackages) {
				if (pack.getPurchaseDate().equalsIgnoreCase(date)) {
					packaged.add(pack);
				}
			}
		} catch (ApplicationException e) {
			LOG.error("BodyPackageDao/getPackagesByDate() ERROR");
			throw new ApplicationException(e);
		}
		return packaged;
	}

	public void deleteBodyPackage(BodyPackage packaged) throws ApplicationException {

		List<BodyPackage> bodyPacks;
		// ***1***remove package from packagedFile***
		bodyPacks = filesReader.readPackaged();
		Iterator<BodyPackage> iter = bodyPacks.iterator();
		while (iter.hasNext()) {
			if (iter.next().getPurchaseID() == packaged.getPurchaseID()) {
				iter.remove();
				break;
			}
		}
		updatePackagedFile(bodyPacks);

		// ***2***remove package from customer file***
		// delete customer55555
		List<Customer> customers;
		customers = filesReader.readCustomers();
		Customer originalCustomer = null;
		Iterator<Customer> customerIter = customers.iterator();
		// remove customer and create originalCustomer
		while (customerIter.hasNext()) {
			originalCustomer = customerIter.next();
			if (originalCustomer.getID() == packaged.getCustomerID()) {
				customerIter.remove();
				break;
			}
		}

		// remove packagedID from originalcustomer's packagedIDs array;
		List<Long> custPackagedIDs = originalCustomer.getPackageIDs();
		Iterator<Long> iter2 = custPackagedIDs.iterator();
		while (iter2.hasNext()) {
			if (iter2.next() == packaged.getPurchaseID()) {
				iter2.remove();
				// set the servicesID of originalCustomer with update array
				ArrayList<Long> updatedCustPackagedIDs = new ArrayList<>(custPackagedIDs);
				originalCustomer.setPackageIDs(updatedCustPackagedIDs);
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