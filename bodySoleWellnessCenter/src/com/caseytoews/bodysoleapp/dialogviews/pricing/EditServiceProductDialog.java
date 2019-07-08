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
import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class EditServiceProductDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldProductName;
	private JTextField textFieldProductPrice;
	private JButton okButton;
	BodyServiceProduct product;

	public EditServiceProductDialog(JFrame frame, ProductDetailsDao productsDao, BodyServiceProduct product) {
		super(frame, true);
		this.product = product;
		setModal(true);
		setResizable(false);
		setTitle("Edit Service Pricing");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 200);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow][180][40]", "[][][]"));

		JLabel lblProduct = new JLabel("Service Product:");
		lblProduct.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblProduct, "cell 0 1,alignx trailing");
		textFieldProductName = UiCommon.setEditableTextField();
		textFieldProductName.setText(product.getProductName());
		contentPanel.add(textFieldProductName, "cell 1 1 2 1,growx");

		JLabel lblPrice = new JLabel("Service Price:");
		lblPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPrice, "cell 0 2,alignx trailing");
		textFieldProductPrice = UiCommon.setEditableTextField();
		String price = UiCommon.TWO_DECIMAL_FORMAT.format(product.getProductPrice());
		textFieldProductPrice.setText(price);
		contentPanel.add(textFieldProductPrice, "cell 1 2,growx");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{

			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (textFieldProductPrice.getText() == null || textFieldProductPrice.getText().isEmpty()) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Price must be greater than zero!", false);
						popUp.setVisible(true);
					} else if (Double.parseDouble(textFieldProductPrice.getText()) <= 0) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Price must be greater than zero!", false);
						popUp.setVisible(true);
					} else {
						try {
							double temp = Double.parseDouble(textFieldProductPrice.getText());
							ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame,
									"Please Confirm!  The price for service product \"" + product.getProductName() + "\"will be changed to: $" + temp,
									"Yes. Please Change.", "No, do not change.");
							popUp.setVisible(true);
							if (popUp.getActionValue()) {
								product.setProductPrice(Double.parseDouble(textFieldProductPrice.getText()));
								productsDao.updateServiceProductDetails(product);
								EditServiceProductDialog.this.dispose();
								MessagePopUp confirm = new MessagePopUp(frame, "Edit Complete! The price for service product \""
										+ product.getProductName() + "\" has been changed to: $" + temp, true);
								confirm.setVisible(true);

							} else {

								EditServiceProductDialog.this.dispose();
							}

						} catch (ApplicationException e) {
							LOG.error("ERORR in EditServiceProductDialog ()");
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
					EditServiceProductDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblCancelBtnSpacer = new JLabel("  ");
		buttonPane.add(lblCancelBtnSpacer);
	}
}
