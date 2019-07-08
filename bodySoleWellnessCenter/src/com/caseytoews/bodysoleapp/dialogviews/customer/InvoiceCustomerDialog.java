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
import java.util.ArrayList;
import java.util.List;

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

import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.common.EvenOddCellRenderer;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.models.bodyservice.BodyServiceListItem;
import com.caseytoews.bodysoleapp.models.bodyservice.BodyServiceListModel;
import com.caseytoews.bodysoleapp.utility.comparator.CompareByPurchaseDateDesc;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class InvoiceCustomerDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JList<BodyServiceListItem> servicesList;
	private BodyServiceListModel serviceModel;
	private JButton cancelButton;
	private JScrollPane scrollPane;
	List<BodyService> services;
	private static BodyServiceDao serviceDaoInstance;
	private static Customer customer;

	// Create the dialog.
	public InvoiceCustomerDialog(JFrame frame) throws ApplicationException {
		super(frame, true);
		serviceDaoInstance = BodyServiceDao.getTheInstance();
		customer = MainFrame.getMainFrameCustomer();
		setBounds(100, 100, 850, 410);
		setTitle("Invoice Customer");
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new MigLayout("fillx", "[750px]", "[500][41px]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, "cell 0 0,alignx center,growy");
		contentPanel.setLayout(new MigLayout("fillx", " [700]", "[40px] [450px]"));

		{
			JLabel lblNewLabel = new JLabel("Please select an invoice for " + customer.getFirstName() + " " + customer.getLastName() + ".");
			lblNewLabel.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx center,aligny center");
		}

		{
			serviceModel = new BodyServiceListModel();
			servicesList = new JList<>(serviceModel);
			servicesList.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 15));
			servicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			servicesList.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(servicesList);
			scrollPane.setForeground(UiCommon.BLACK);
			contentPanel.add(scrollPane, "cell 0 1,growx,aligny top");

			ArrayList<BodyService> customerServices = serviceDaoInstance.getServicesByCustomerID(customer.getID());
			customerServices.sort(new CompareByPurchaseDateDesc());

			for (BodyService service : customerServices) {
				serviceModel.add(service);
			}

			servicesList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent event) {
					if (!event.getValueIsAdjusting()) {
						BodyServiceListItem listService = servicesList.getSelectedValue();
						try {

							InvoiceCustomerDialog.this.dispose();
							InvoiceCustomerConfirmation invoiceConfirmation = new InvoiceCustomerConfirmation(frame,
									listService.getService().getPurchaseDate());
							invoiceConfirmation.setVisible(true);

						} catch (ApplicationException e) {
							LOG.error("InvoiceCustomerDialog() ");
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
					InvoiceCustomerDialog.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}
	}

}
