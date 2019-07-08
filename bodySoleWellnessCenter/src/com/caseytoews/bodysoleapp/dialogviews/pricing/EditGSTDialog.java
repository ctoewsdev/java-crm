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
import com.caseytoews.bodysoleapp.domain.product.ProductDetails;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class EditGSTDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldProductPrice;
	private JButton okButton;
	ProductDetails products;

	public EditGSTDialog(JFrame frame, ProductDetailsDao productsDao) throws ApplicationException {
		super(frame, true);
		products = productsDao.getProductDetails();
		setModal(true);
		setResizable(false);
		setTitle("Edit GST Percentage");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 200);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[45][45][45][45]", "[grow]"));

		JLabel lblPrice = new JLabel("GST:  %");
		lblPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPrice, "cell 1 0,alignx trailing");
		textFieldProductPrice = UiCommon.setEditableTextField();
		String price = UiCommon.TWO_DECIMAL_FORMAT.format(products.getGST());
		textFieldProductPrice.setText(price);
		contentPanel.add(textFieldProductPrice, "cell 2 0,growx");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{

			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (textFieldProductPrice.getText() == null || textFieldProductPrice.getText().isEmpty()) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Percentage must be greater than zero!", false);
						popUp.setVisible(true);
					} else if (Double.parseDouble(textFieldProductPrice.getText()) <= 0) {
						MessagePopUp popUp = new MessagePopUp(frame, "Oops! Percentage must be greater than zero!", false);
						popUp.setVisible(true);
					} else {
						try {
							double temp = Double.parseDouble(textFieldProductPrice.getText());
							ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame,
									"Please Confirm!  The perenctage for GST will be changed to: %" + temp, "Yes. Please Change.",
									"No, do not change.");
							popUp.setVisible(true);
							if (popUp.getActionValue()) {
								products.setGST(Double.parseDouble(textFieldProductPrice.getText()));
								productsDao.updateProductDetailsDB(products);
								EditGSTDialog.this.dispose();
								MessagePopUp confirm = new MessagePopUp(frame, "Edit Complete! The perenctage for GST will be changed to: %" + temp,
										true);
								confirm.setVisible(true);

							} else {

								EditGSTDialog.this.dispose();
							}

						} catch (ApplicationException e) {
							LOG.error("ERORR in EditGSTDialog ()");
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
					EditGSTDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblCancelBtnSpacer = new JLabel("  ");
		buttonPane.add(lblCancelBtnSpacer);
	}
}
