/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 19, 2018
 * Time: 1:17:27 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.pricing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.product.ProductDetailsDao;
import com.caseytoews.bodysoleapp.dialogviews.common.ActionRequiredPopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class EditPackageProductDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldProductName;
	private JTextField textFieldProductPrice;
	private JTextField textFieldProductWPackagePrice;
	private JButton okButton;
	BodyPackageProduct product;

	// Create the dialog
	public EditPackageProductDialog(JFrame frame, ProductDetailsDao productsDao, BodyPackageProduct product) {
		super(frame, true);
		this.product = product;
		setModal(true);
		setResizable(false);
		setTitle("Edit Package Pricing");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 220);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow][180][40]", "[][][][]"));

		JLabel lblProduct = new JLabel("Service Product:");
		lblProduct.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblProduct, "cell 0 1,alignx trailing");
		textFieldProductName = UiCommon.setEditableTextField();
		textFieldProductName.setText(product.getProductName());
		contentPanel.add(textFieldProductName, "cell 1 1 2 1,growx");

		JLabel lblPrice = new JLabel("Package Price:");
		lblPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPrice, "cell 0 2,alignx trailing");
		textFieldProductPrice = UiCommon.setEditableTextField();
		String price = UiCommon.TWO_DECIMAL_FORMAT.format(product.getProductPrice());
		textFieldProductPrice.setText(price);
		contentPanel.add(textFieldProductPrice, "cell 1 2,growx");

		JLabel lblPricePerServ = new JLabel("Package Service Price:");
		lblPricePerServ.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPricePerServ, "cell 0 3,alignx trailing");
		textFieldProductWPackagePrice = UiCommon.setEditableTextField();
		String priceWPack = UiCommon.TWO_DECIMAL_FORMAT.format(product.getPricePerPackageService());
		textFieldProductWPackagePrice.setText(priceWPack);
		contentPanel.add(textFieldProductWPackagePrice, "cell 1 3,growx");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{

			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String prodName = "";
					prodName = textFieldProductName.getText();

					if (textFieldProductPrice.getText() == null || textFieldProductPrice.getText().isEmpty()) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Price must be greater than zero!", false);
						popUp.setVisible(true);
					} else if (Double.parseDouble(textFieldProductPrice.getText()) <= 0) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Price must be greater than zero!", false);
						popUp.setVisible(true);
					} else if (textFieldProductWPackagePrice.getText() == null || textFieldProductWPackagePrice.getText().isEmpty()) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Price with package service must be greater than zero!", false);
						popUp.setVisible(true);
					} else if (Double.parseDouble(textFieldProductWPackagePrice.getText()) <= 0) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Price with package servicemust be greater than zero!", false);
						popUp.setVisible(true);
					} else if (prodName.length() == 0) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! What is the product name?", false);
						popUp.setVisible(true);
					} else {
						try {
							double prodPrice = Double.parseDouble(textFieldProductPrice.getText());
							double prodPriceWPack = Double.parseDouble(textFieldProductWPackagePrice.getText());
							String tempA = UiCommon.TWO_DECIMAL_FORMAT.format(prodPrice);
							String tempB = UiCommon.TWO_DECIMAL_FORMAT.format(prodPriceWPack);
							ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame, "Please Confirm! The price for package product \"" + prodName
									+ "\" will be: $" + tempA + "\nand the package service price will be: $" + tempB, "Yes. Please Change.",
									"No, do not change.");
							popUp.setVisible(true);
							if (popUp.getActionValue()) {
								product.setProductPrice(prodPrice);
								product.setPricePerPackageService(prodPriceWPack);
								product.setProductName(prodName);
								String prodCode = product.getProductCode();
								String newCode = prodCode.substring(0, 1) + (prodName.substring(prodName.length() - 2));
								product.setProductCode(newCode);

								productsDao.updatePackageProductDetails(product);
								EditPackageProductDialog.this.dispose();
								MessagePopUp confirm = new MessagePopUp(frame, "Edit Complete! The price for package product \"" + prodName
										+ "\" will be: $" + tempA + "\nand the package service price will be: $" + tempB, true);
								confirm.setVisible(true);

							} else {

								EditPackageProductDialog.this.dispose();
							}

						} catch (ApplicationException e) {
							LOG.error("ERORR in EditPackageProductDialog ()");
							e.printStackTrace();
						}
					}
				}
			});

			buttonPane.add(okButton);

			JButton cancelButton = UiCommon.setCancelButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EditPackageProductDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblCancelBtnSpacer = new JLabel("  ");
		buttonPane.add(lblCancelBtnSpacer);
	}
}
