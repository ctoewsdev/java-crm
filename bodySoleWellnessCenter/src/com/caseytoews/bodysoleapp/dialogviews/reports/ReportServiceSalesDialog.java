/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 18, 2018
 * Time: 8:46:26 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.reports;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.dialogviews.common.EvenOddCellRenderer;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.business.Report;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.models.reports.ReportListItem;
import com.caseytoews.bodysoleapp.models.reports.ReportListModel;
import com.caseytoews.bodysoleapp.utility.comparator.CompareReportByPurchaseDateDesc;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class ReportServiceSalesDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JList<ReportListItem> reportList;
	private ReportListModel reportModel;
	private JButton cancelButton;
	private JScrollPane scrollPane;

	public ReportServiceSalesDialog(JFrame frame, FilesReader filesReader) throws ApplicationException {
		setBounds(100, 100, 500, 350);
		setTitle("Monthly Sales: Single Services");
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new MigLayout("fillx", "[445]", "[300][41px]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, "cell 0 0,grow");
		contentPanel.setLayout(new MigLayout("fillx", " [425]", "[40px] [300]"));

		{
			JLabel lblNewLabel = new JLabel("Monthly Totals for Single Services.");
			lblNewLabel.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx center,aligny center");
		}

		{
			reportModel = new ReportListModel();
			reportList = new JList<>(reportModel);
			reportList.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 15));
			reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			reportList.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(reportList);
			scrollPane.setForeground(UiCommon.BLACK);
			contentPanel.add(scrollPane, "cell 0 1,aligny top,grow");
		}
		{

			// Array of Month/Year Containers
			List<BodyService> allServices = filesReader.readServices();
			ArrayList<Report> monthlySales = new ArrayList<>();

			for (BodyService service : allServices) {

				// dont include package services
				if (service.getPackageID() == 0) {
					LocalDate purchaseDate = LocalDate.parse(service.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
					Report newReport = new Report(purchaseDate, service.getTotalGST(), service.getTotalCost(), 1);

					boolean containerExists = false;

					// check if mmyyyy container exists
					for (Report container : monthlySales) {
						if (newReport.getMonthYearLD().getYear() - container.getMonthYearLD().getYear() == 0) {
							if (newReport.getMonthYearLD().getMonthValue() - container.getMonthYearLD().getMonthValue() == 0) {
								container.setTotalGST(container.getTotalGST() + newReport.getTotalGST());
								container.setTotalCost(container.getTotalCost() + newReport.getTotalCost());
								container.setVisits(container.getVisits() + 1);
								containerExists = true;
								break;
							}
						}
					}
					if (!containerExists) {
						Report newContainer = new Report(newReport.getMonthYearLD(), newReport.getTotalGST(), newReport.getTotalCost(), 1);
						monthlySales.add(newContainer);
					}
				}
			}

			// ADD TO THE MODEL
			List<Report> monthlyReport = monthlySales;
			monthlyReport.sort(new CompareReportByPurchaseDateDesc());
			for (Report report : monthlyReport) {
				reportModel.add(report);
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
					ReportServiceSalesDialog.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}
	}
}
