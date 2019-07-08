/**
 * Project: BodySoleWellness
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */
package com.caseytoews.bodysoleapp.database.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.Dao;
import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;
import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;
import com.caseytoews.bodysoleapp.domain.product.ProductDetails;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CRUD for DB product-details-JSON file
 *
 */
public class ProductDetailsDao extends Dao {

	private static final Logger LOG = LogManager.getLogger();
	private static ProductDetailsDao productsDaoInstance = new ProductDetailsDao();
	private FilesReader filesReader;
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * private constructor for singleton instance
	 */
	private ProductDetailsDao() {
		super("services");
		filesReader = FilesReader.getTheInstance();

	}

	public static ProductDetailsDao getTheInstance() {
		return productsDaoInstance;
	}

	public ProductDetails getProductDetails() throws ApplicationException {
		JsonNode rootNode;
		ProductDetails productDetails;
		try {
			byte[] jsonData = filesReader.readProducts();
			rootNode = objectMapper.readTree(jsonData);
			productDetails = objectMapper.treeToValue(rootNode, ProductDetails.class);
		} catch (IOException e) {
			LOG.error("ERROR: ProductsDao() getProducts()");
			throw new ApplicationException(e);
		}
		return productDetails;
	}

	public void updateServiceProductDetails(BodyServiceProduct serviceProduct) throws ApplicationException {
		ArrayList<BodyServiceProduct> serviceProducts;
		try {
			ProductDetails products = getProductDetails();
			serviceProducts = products.getServiceProducts();
			Iterator<BodyServiceProduct> iter = serviceProducts.iterator();
			while (iter.hasNext()) {
				if (iter.next().getProductCode().equalsIgnoreCase(serviceProduct.getProductCode())) {
					iter.remove();
					break;
				}
			}
			serviceProducts.add(serviceProduct);
			products.setServiceProducts(serviceProducts);
			updateProductDetailsDB(products);

		} catch (ApplicationException e) {
			LOG.error("Error: ProdcutsDao/updateServiceProducts()");
			throw new ApplicationException(e);
		}
	}

	public void updatePackageProductDetails(BodyPackageProduct packagedProduct) throws ApplicationException {
		ArrayList<BodyPackageProduct> packagedProducts;
		try {
			ProductDetails products = getProductDetails();
			packagedProducts = products.getPackagedProducts();
			Iterator<BodyPackageProduct> iter = packagedProducts.iterator();
			while (iter.hasNext()) {
				if (iter.next().getProductCode().equalsIgnoreCase(packagedProduct.getProductCode())) {
					iter.remove();
					break;
				}
			}
			packagedProducts.add(packagedProduct);
			products.setPackagedProducts(packagedProducts);
			updateProductDetailsDB(products);

		} catch (ApplicationException e) {
			LOG.error("ProdcutsDao/updateServiceProducts() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateProductDetailsDB(ProductDetails products) throws ApplicationException {
		try {
			productsDaoInstance.updateFile(IOConstants.PRODUCTS_FILE, products);

		} catch (Exception e) {
			LOG.error("ERROR: ProductsDao() updateProducts()");
			throw new ApplicationException(e);
		}
	}

	public BodyServiceProduct getServiceProductDetailsByProductCode(String productCode) throws ApplicationException {
		BodyServiceProduct serviceProduct = null;
		ProductDetails products = getProductDetails();
		ArrayList<BodyServiceProduct> serviceProducts = products.getServiceProducts();

		for (BodyServiceProduct sp : serviceProducts) {
			if (productCode.equalsIgnoreCase(sp.getProductCode())) {
				serviceProduct = sp;
			}
		}
		return serviceProduct;
	}

	public BodyPackageProduct getPackageProductDetailsByProductCode(String productCode) throws ApplicationException {
		BodyPackageProduct packagedProduct = null;
		ProductDetails products = getProductDetails();
		ArrayList<BodyPackageProduct> packagedProducts = products.getPackagedProducts();
		for (BodyPackageProduct pp : packagedProducts) {
			if (productCode.equalsIgnoreCase(pp.getProductCode())) {
				packagedProduct = pp;
			}
		}
		return packagedProduct;
	}
}