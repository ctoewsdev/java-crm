/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 18, 2018
 * Time: 8:46:26 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.pricing;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.product.ProductDetailsDao;
import com.caseytoews.bodysoleapp.dialogviews.common.EvenOddCellRenderer;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;
import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;
import com.caseytoews.bodysoleapp.domain.product.ProductDetails;
import com.caseytoews.bodysoleapp.models.pricing.bodypackage.PackagePricingListModel;
import com.caseytoews.bodysoleapp.models.pricing.bodypackage.PackagedPricingListItem;
import com.caseytoews.bodysoleapp.models.pricing.bodyservice.ServicePricingListItem;
import com.caseytoews.bodysoleapp.models.pricing.bodyservice.ServicePricingListModel;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class ViewProductsListDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JList<PackagedPricingListItem> packageProducts;
	private JList<ServicePricingListItem> serviceProducts;
	private ProductDetails allProducts;
	private PackagePricingListModel packageProductModel;
	private ServicePricingListModel serviceProductModel;
	private JButton cancelButton;
	private JScrollPane scrollPane;
	private JTextField textFieldGST;

	public ViewProductsListDialog(JFrame frame, ProductDetailsDao productsDao) throws ApplicationException {
		allProducts = productsDao.getProductDetails();
		setBounds(100, 100, 600, 510);
		setTitle("Product List");
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new MigLayout("fillx", "[650px]", "[450px][41px]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, "cell 0 0,grow");
		contentPanel.setLayout(new MigLayout("fillx", " [540px]", "[40px][40px] [245][40px] [170]"));

		{
			JLabel lblgst = new JLabel("GST %");
			lblgst.setFont(UiCommon.DIALOG_FONT);
			lblgst.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblgst, "flowx,cell 0 0,alignx center,aligny center");
		}

		{
			textFieldGST = new JTextField();
			textFieldGST = UiCommon.setCurrencyField();
			textFieldGST.setEditable(false);
			contentPanel.add(textFieldGST, "cell 0 0");
			textFieldGST.setColumns(10);
			String temp = Double.toString(allProducts.getGST());
			textFieldGST.setText(temp);
		}

		{
			JLabel lblNewLabel = new JLabel("Complete Service Products List.");
			lblNewLabel.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 1,alignx center,aligny center");
		}

		{
			serviceProductModel = new ServicePricingListModel();
			serviceProducts = new JList<>(serviceProductModel);
			serviceProducts.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 15));
			serviceProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			serviceProducts.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(serviceProducts);
			scrollPane.setForeground(UiCommon.BLACK);
			contentPanel.add(scrollPane, "cell 0 2,aligny top,grow");

			List<BodyServiceProduct> services = allProducts.getServiceProducts();
			for (BodyServiceProduct service : services) {
				serviceProductModel.add(service);
			}
		}
		{
			JLabel lblNewLabelb = new JLabel("Complete Package Products List.");
			lblNewLabelb.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblNewLabelb.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabelb, "cell 0 3,alignx center,aligny center");
		}

		{
			packageProductModel = new PackagePricingListModel();
			packageProducts = new JList<>(packageProductModel);
			packageProducts.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 15));
			packageProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			packageProducts.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(packageProducts);
			scrollPane.setForeground(UiCommon.BLACK);
			contentPanel.add(scrollPane, "cell 0 4,aligny top,grow");

			List<BodyPackageProduct> packages = allProducts.getPackagedProducts();
			for (BodyPackageProduct pack : packages) {
				packageProductModel.add(pack);
			}
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, "cell 0 1,growx,aligny top");

		{
			cancelButton = UiCommon.setCancelButton("  Cancel  ");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ViewProductsListDialog.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}
	}
}
