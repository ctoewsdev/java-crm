/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 18, 2018
 * Time: 8:46:26 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.customer;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.dialogviews.common.EvenOddCellRenderer;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.models.customer.CustomerListItem;
import com.caseytoews.bodysoleapp.models.customer.CustomerListModel;
import com.caseytoews.bodysoleapp.utility.comparator.CompareByLastName;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class GetCustomerByListDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JList<CustomerListItem> customersList;
	private CustomerListModel customerModel;
	private JButton cancelButton;
	private JScrollPane scrollPane;

	// Create the dialog.
	public GetCustomerByListDialog(JFrame frame, CustomerDao customerDao, FilesReader filesReader) throws ApplicationException {
		setBounds(100, 100, 750, 410);
		setTitle("Get Customer by List");
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new MigLayout("fillx", "[650px]", "[500px][41px]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, "cell 0 0,grow");
		contentPanel.setLayout(new MigLayout("fillx", " [575px]", "[40px] [450]"));

		{
			JLabel lblNewLabel = new JLabel("Please select a customer.");
			lblNewLabel.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx center,aligny center");
		}

		{
			customerModel = new CustomerListModel();
			customersList = new JList<>(customerModel);
			customersList.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 15));
			customersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			customersList.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(customersList);
			scrollPane.setForeground(UiCommon.BLACK);
			scrollPane.setBorder(BorderFactory.createLineBorder(UiCommon.WHITE_MAIN));
			contentPanel.add(scrollPane, "cell 0 1,aligny top,grow");

			List<Customer> customers = customerDao.getAllCustomers();
			// sort customers by last name
			customers.sort(new CompareByLastName());
			for (Customer customer : customers) {
				customerModel.add(customer);
			}
			customersList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent event) {
					if (!event.getValueIsAdjusting()) {
						CustomerListItem listCustomer = customersList.getSelectedValue();
						long ID = listCustomer.getCustomer().getID();

						try {

							GetCustomerByListDialog.this.dispose();
							MessagePopUp popUp = new MessagePopUp(frame, "You have selected \"" + listCustomer.getCustomer().getFirstName() + " "
									+ listCustomer.getCustomer().getLastName() + ".\"", true);
							popUp.setVisible(true);
							MainFrame.updateMainTextFields(customerDao.getCustomerById(ID));

						} catch (ApplicationException e) {
							LOG.error("GetCustomerByListDialog() ");
						}
						return;
					}
				}

			});

		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, "cell 0 1,growx,aligny top");

		{
			cancelButton = UiCommon.setCancelButton("  Cancel  ");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GetCustomerByListDialog.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}
	}
}
