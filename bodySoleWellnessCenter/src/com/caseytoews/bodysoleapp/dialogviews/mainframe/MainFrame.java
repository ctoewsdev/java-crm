
package com.caseytoews.bodysoleapp.dialogviews.mainframe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.controller.UiController;
import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.models.bodypackage.BodyPackageTableModel;
import com.caseytoews.bodysoleapp.models.bodyservice.BodyServiceTableModel;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame {
	public static final Logger LOG = LogManager.getLogger();
	private static JFrame frame;

	private static final long serialVersionUID = 1L;
	private static Customer mainFrameCustomer = new Customer();
	private static CustomerDao customerDaoInstance;
	private static GroupDao groupDaoInstance;
	private static final int JMENU_MARGIN_HEIGHT = 1;
	private static final int CUSTOMER_MARGIN_WIDTH = 21;
	private static final int SERVICE_MARGIN_WIDTH = 33;
	private static final int PACKAGE_MARGIN_WIDTH = 28;
	private static final int REPORT_MARGIN_WIDTH = 32;
	private static final int STAFFING_MARGIN_WIDTH = 24;
	private static final int PRICING_MARGIN_WIDTH = 30;
	private static final String MENU_FONT = "Book Antiqua";

	private static final int MENU_FONT_SIZE = 18;

	private JPanel contentPane;
	private static JTextField textFieldName;
	private static JTextField textFieldPhone;
	private static JTextField textFieldBalance;
	private static JTextField textFieldTotalSales;
	private static JTextField textFieldEmail;
	private static JTextField textFieldJoinDate;
	private static JTextField textFieldAcupunctureRemaining;
	private static JTextField textFieldBodyRemaining;
	private static JMenu menuCustomer;
	private static JMenu menuService;
	private static JMenu menuPackage;
	private static JMenu menuGetReports;
	private static JMenu menuStaffing;
	private static JMenu menuServicesPricing;
	private static JMenuItem mntmAddNewCustomer;
	private static JMenuItem mntmGetWalkInCustomer;
	private static JMenuItem mntmGetCustomerByPhone;
	private static JMenuItem mntmGetCustomerList;
	private static JMenuItem mntmInvoiceCustomer;
	private static JMenuItem mntmSellService;
	private static JMenuItem mntmSellCombo1_5;
	private static JMenuItem mntmSellCombo2_0;
	private static JMenuItem mntmSellPackage;
	private static JMenuItem mntmPackageSales;
	private static JMenuItem mntmServiceSales;
	private static JMenuItem mntmPackageServiceSales;
	private static JMenuItem mntmCloseShop;
	private static JMenuItem mntmAddStaff;
	private static JMenuItem mntmEditStaff;
	private static JMenuItem mntmViewStaff;
	private static JMenuItem mntmEditPricing;
	private static JMenuItem mntmViewPricing;
	private static JMenuItem mntmEditGST;
	private static JTable servicesTable;
	private static JTable packagesTable;
	private static BodyServiceTableModel sTableModel;
	private static BodyPackageTableModel pTableModel;
	private static TableColumn pTable_id;
	private static TableColumn pTable_purchaser;
	private static TableColumn pTable_date;
	private static TableColumn pTable_packageName;
	private static TableColumn pTable_price;
	private static TableColumn pTable_gst;
	private static TableColumn pTable_cost;
	private static TableColumn pTable_balance;
	private static TableColumn pTable_remaining;
	private static TableColumn pTable_staff;
	private static TableColumn sTable_id;
	private static TableColumn sTable_packageID;
	private static TableColumn sTable_date;
	private static TableColumn sTable_service;
	private static TableColumn sTable_price;
	private static TableColumn sTable_qty;
	private static TableColumn sTable_subtotal;
	private static TableColumn sTable_gst;
	private static TableColumn sTable_cost;
	private static TableColumn sTable_staff;
	private static JButton btnEditThisCustomer;
	private static JButton btnAddToGroup;
	private static JComboBox<String> groupComboBox;
	private JLabel lblFeet;

	// Create the frame.
	public MainFrame() throws ApplicationException, IOException {

		setResizable(false);
		frame = this;
		customerDaoInstance = CustomerDao.getTheInstance();
		groupDaoInstance = GroupDao.getTheInstance();
		LOG.debug("Creating the MainFrame");

		URL iconURL = getClass().getResource("/images/logoframe.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		modifyUIManager();
		createUI();
		addEventHandlers();
	}

	public static Customer getMainFrameCustomer() {
		return mainFrameCustomer;
	}

	private static void setMainFrameCustomer(Customer c) {
		mainFrameCustomer.setID(c.getID());
		mainFrameCustomer.setPhone(c.getPhone());
		mainFrameCustomer.setFirstName(c.getFirstName());
		mainFrameCustomer.setLastName(c.getLastName());
		mainFrameCustomer.setEmail(c.getEmail());
		mainFrameCustomer.setJoinDate(c.getJoinDate());
		mainFrameCustomer.setTotalSales(c.getTotalSales());
		mainFrameCustomer.setServiceIDs(c.getServiceIDs());
		mainFrameCustomer.setPackageIDs(c.getPackageIDs());
		mainFrameCustomer.setCurrentPackageID(c.getCurrentPackageID());
	}

	private void modifyUIManager() {

		// MAIN COLORS
		UIManager.put("text", UiCommon.WHITE_MAIN);// MainFrame body text color

		UIManager.put("control", UiCommon.PURPLE_MAIN); // MainFrame Body Color

		// MENU
		UIManager.put("nimbusBlueGrey", UiCommon.MENU_BAR_BACKGROUND); // menu bar background
		UIManager.put("nimbusBase", UiCommon.MAIN_MENU_SELECT); // menu bar select

		// JTABLE COLORS
		UIManager.put("Table.background", UiCommon.WHITE_MAIN); // main row background color
		UIManager.put("Table.alternateRowColor", UiCommon.PURPLE_LITE);// alternate row background color

		// Button
		UIManager.put("nimbusFocus", UiCommon.WHITE_MAIN);// border around button

		// COMBOBOX
		UIManager.put("ComboBox:\"ComboBox.listRenderer\".background", UiCommon.DARK_GREY);//

		UIManager.put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", UiCommon.PURPLE_MAIN);//
		UIManager.put("ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", UiCommon.WHITE_MAIN);//

		// TOOL TIP
		UIManager.put("ToolTip:background", UiCommon.DARK_GREY);// \

	}

	private void createUI() throws ApplicationException {
		LOG.debug("Creating the UI in MainFrame.");
		setTitle("Body & Sole Wellness Center Langley (1.4)");

		setSize(1230, 800);
		setLocationRelativeTo(null);

		// build main content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(
				new MigLayout("", "[40][][300px][60px][90px][140px][300px][150px][20px][10px]", "[16px][][][][][30px][260px][40px][130px]"));

		// **************
		// ***MENU BAR***
		// **************
		JMenuBar menuBar = new JMenuBar();
		menuBar.setEnabled(false);
		menuBar.setBackground(Color.BLACK);
		menuBar.setBorder(new LineBorder(new Color(73, 25, 73), 5));
		setJMenuBar(menuBar);

		// JMenu objects
		menuCustomer = new JMenu("CUSTOMER");
		menuCustomer.setIcon(UiCommon.CUSTOMER_ICON);
		menuService = new JMenu("SERVICE");
		menuService.setIcon(UiCommon.SERVICE_ICON);
		menuPackage = new JMenu("PACKAGE");
		menuPackage.setIcon(UiCommon.PACKAGE_ICON);
		menuGetReports = new JMenu("REPORTS");
		menuGetReports.setIcon(UiCommon.REPORTS_ICON);
		menuServicesPricing = new JMenu("PRICING");
		menuServicesPricing.setIcon(UiCommon.PRICING_ICON);
		menuStaffing = new JMenu("STAFFING");
		menuStaffing.setIcon(UiCommon.STAFF_ICON);

		// JMenu properties
		menuCustomer.setFont(new Font(MENU_FONT, Font.BOLD, MENU_FONT_SIZE));
		menuCustomer.setMargin(new Insets(JMENU_MARGIN_HEIGHT, CUSTOMER_MARGIN_WIDTH, JMENU_MARGIN_HEIGHT, CUSTOMER_MARGIN_WIDTH));
		menuCustomer.setOpaque(true);
		menuCustomer.setForeground(UiCommon.MENU_TEXT);
		menuCustomer.setBackground(UiCommon.GREEN_MAIN);

		menuService.setFont(new Font(MENU_FONT, Font.BOLD, MENU_FONT_SIZE));
		menuService.setMargin(new Insets(JMENU_MARGIN_HEIGHT, SERVICE_MARGIN_WIDTH, JMENU_MARGIN_HEIGHT, SERVICE_MARGIN_WIDTH));
		menuService.setOpaque(true);
		menuService.setForeground(UiCommon.MENU_TEXT);
		menuService.setBackground(UiCommon.GREEN_MAIN);

		menuPackage.setFont(new Font(MENU_FONT, Font.BOLD, MENU_FONT_SIZE));
		menuPackage.setMargin(new Insets(JMENU_MARGIN_HEIGHT, PACKAGE_MARGIN_WIDTH, JMENU_MARGIN_HEIGHT, PACKAGE_MARGIN_WIDTH));
		menuPackage.setOpaque(true);
		menuPackage.setForeground(UiCommon.MENU_TEXT);
		menuPackage.setBackground(UiCommon.GREEN_MAIN);

		menuGetReports.setFont(new Font(MENU_FONT, Font.BOLD, MENU_FONT_SIZE));
		menuGetReports.setMargin(new Insets(JMENU_MARGIN_HEIGHT, REPORT_MARGIN_WIDTH, JMENU_MARGIN_HEIGHT, REPORT_MARGIN_WIDTH));
		menuGetReports.setOpaque(true);
		menuGetReports.setForeground(UiCommon.MENU_TEXT);
		menuGetReports.setBackground(UiCommon.GREEN_MAIN);

		menuStaffing.setFont(new Font(MENU_FONT, Font.BOLD, MENU_FONT_SIZE));
		menuStaffing.setMargin(new Insets(JMENU_MARGIN_HEIGHT, STAFFING_MARGIN_WIDTH, JMENU_MARGIN_HEIGHT, STAFFING_MARGIN_WIDTH));
		menuStaffing.setOpaque(true);
		menuStaffing.setForeground(UiCommon.MENU_TEXT);
		menuStaffing.setBackground(UiCommon.GREEN_MAIN);

		menuServicesPricing.setFont(new Font(MENU_FONT, Font.BOLD, MENU_FONT_SIZE));
		menuServicesPricing.setMargin(new Insets(JMENU_MARGIN_HEIGHT, PRICING_MARGIN_WIDTH, JMENU_MARGIN_HEIGHT, PRICING_MARGIN_WIDTH));
		menuServicesPricing.setOpaque(true);
		menuServicesPricing.setForeground(UiCommon.MENU_TEXT);
		menuServicesPricing.setBackground(UiCommon.GREEN_MAIN);

		JLabel lblNewLabel_menuHeadSpacer = new JLabel("");
		lblNewLabel_menuHeadSpacer.setBackground(UiCommon.PURPLE_MAIN);
		lblNewLabel_menuHeadSpacer.setOpaque(true);
		menuBar.add(lblNewLabel_menuHeadSpacer);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator_5.setOpaque(true);
		menuBar.add(separator_5);

		// add JMenu objects and vertical separators

		// CUSTOMER
		menuBar.add(menuCustomer);

		mntmGetWalkInCustomer = new JMenuItem("Get Walk-In Customer");
		mntmGetWalkInCustomer.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuCustomer.add(mntmGetWalkInCustomer);

		mntmGetCustomerByPhone = new JMenuItem("Get Customer By Phone");
		mntmGetCustomerByPhone.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuCustomer.add(mntmGetCustomerByPhone);

		mntmGetCustomerList = new JMenuItem("Get Customer By List");
		mntmGetCustomerList.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuCustomer.add(mntmGetCustomerList);

		mntmAddNewCustomer = new JMenuItem("Add New Customer");
		mntmAddNewCustomer.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuCustomer.add(mntmAddNewCustomer);

		mntmInvoiceCustomer = new JMenuItem("Invoice Customer");
		mntmInvoiceCustomer.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuCustomer.add(mntmInvoiceCustomer);

		JSeparator separator = new JSeparator();
		separator.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator.setOpaque(true);

		menuBar.add(separator);

		// SERVICE
		menuBar.add(menuService);

		mntmSellService = new JMenuItem("Sell Service");
		mntmSellService.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuService.add(mntmSellService);

		mntmSellCombo1_5 = new JMenuItem("Sell Combo 1.5");
		mntmSellCombo1_5.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuService.add(mntmSellCombo1_5);

		mntmSellCombo2_0 = new JMenuItem("Sell Combo 2.0");
		mntmSellCombo2_0.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuService.add(mntmSellCombo2_0);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator_1.setOpaque(true);
		menuBar.add(separator_1);

		// PACKAGE
		menuBar.add(menuPackage);

		mntmSellPackage = new JMenuItem("Sell Package");
		mntmSellPackage.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuPackage.add(mntmSellPackage);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator_2.setOpaque(true);
		menuBar.add(separator_2);

		// REPORTS
		menuBar.add(menuGetReports);

		mntmServiceSales = new JMenuItem("Single Services");
		mntmServiceSales.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuGetReports.add(mntmServiceSales);

		mntmPackageSales = new JMenuItem("Packages");
		mntmPackageSales.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuGetReports.add(mntmPackageSales);

		mntmPackageServiceSales = new JMenuItem("Package Services");
		mntmPackageServiceSales.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuGetReports.add(mntmPackageServiceSales);

		mntmCloseShop = new JMenuItem("Close Shop");
		mntmCloseShop.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuGetReports.add(mntmCloseShop);

		JSeparator separator_3c = new JSeparator();
		separator_3c.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator_3c.setOpaque(true);
		menuBar.add(separator_3c);

		// STAFFING
		menuBar.add(menuStaffing);

		mntmAddStaff = new JMenuItem("Add New Staff");
		mntmAddStaff.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuStaffing.add(mntmAddStaff);

		mntmEditStaff = new JMenuItem("Edit Staff");
		mntmEditStaff.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuStaffing.add(mntmEditStaff);

		mntmViewStaff = new JMenuItem("View Staff List");
		mntmViewStaff.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuStaffing.add(mntmViewStaff);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator_4.setOpaque(true);
		menuBar.add(separator_4);

		// PRICING
		menuBar.add(menuServicesPricing);

		mntmEditPricing = new JMenuItem("Edit Pricing");
		mntmEditPricing.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuServicesPricing.add(mntmEditPricing);

		mntmViewPricing = new JMenuItem("View Pricing List");
		mntmViewPricing.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuServicesPricing.add(mntmViewPricing);

		mntmEditGST = new JMenuItem("Edit GST");
		mntmEditGST.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 14));
		menuServicesPricing.add(mntmEditGST);

		JSeparator separator_10 = new JSeparator();
		separator_10.setBackground(UiCommon.MENU_VERTICAL_SEPARATOR);
		separator_10.setOpaque(true);
		menuBar.add(separator_10);

		// ***********************
		// ***MAIN CONTENT PANE***
		// ***********************

		// Pane Title
		JLabel lblTitle = new JLabel("Customer Detailed Report    ");
		lblTitle.setIcon(UiCommon.LOGO_RECOLOR);
		lblTitle.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 24));
		contentPane.add(lblTitle, "cell 1 0 3 1,alignx left,aligny center");

		// Edit customer
		btnEditThisCustomer = new JButton("Edit Current Customer");
		btnEditThisCustomer.setForeground(UiCommon.WHITE_MAIN);
		btnEditThisCustomer.setBackground(UiCommon.PURPLE_MAIN);
		btnEditThisCustomer.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 14));
		contentPane.add(btnEditThisCustomer, "cell 6 0 2 1,alignx right,aligny center");

		// Name
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblName, "cell 1 2,alignx left");
		textFieldName = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldName, "flowx,cell 2 2,alignx center,aligny center, growx");

		// Phone
		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblPhone, "cell 4 2,alignx right");
		textFieldPhone = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldPhone, "cell 5 2,growx");

		// Total Sales
		JLabel lblTotalSales = new JLabel("Total Customer Sales:");
		lblTotalSales.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblTotalSales, "cell 6 2,alignx trailing,aligny center");
		textFieldTotalSales = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldTotalSales, "cell 7 2,growx");

		// Email
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblEmail, "cell 1 3 2 1,aligny center");
		textFieldEmail = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldEmail, "cell 1 3, growx");

		// Join Date
		JLabel lblJoinDate = new JLabel("Join Date:");
		lblJoinDate.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblJoinDate, "cell 4 3,alignx trailing,aligny center");
		textFieldJoinDate = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldJoinDate, "cell 5 3,growx");

		// Balance
		JLabel lblBalance = new JLabel("Packages Balance:");
		lblBalance.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblBalance, "cell 6 3,alignx right");
		textFieldBalance = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldBalance, "cell 7 3,growx");

		// Group
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblGroup, "flowx,cell 1 4 2 1,aligny center");
		groupComboBox = new JComboBox<>();
		groupComboBox.setFont(UiCommon.DIALOG_FONT);
		contentPane.add(groupComboBox, "cell 2 4, growx");

		// ADD TO GROUP
		btnAddToGroup = new JButton("Add Group Member");
		btnAddToGroup.setForeground(UiCommon.WHITE_MAIN);
		btnAddToGroup.setBackground(UiCommon.PURPLE_MAIN);
		btnAddToGroup.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 14));
		contentPane.add(btnAddToGroup, "cell 1 5 2 1,alignx left,aligny center");

		// SERVICES TABLE TITLE
		JLabel labelServiceHistory = new JLabel("Service Purchase History");
		labelServiceHistory.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 17));
		labelServiceHistory.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelServiceHistory, "cell 4 5 2 1,alignx right,aligny bottom");

		// Remaining
		JLabel lblRemaining = new JLabel("Packages Remaining Body:");
		lblRemaining.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblRemaining, "cell 6 4,alignx trailing");
		textFieldBodyRemaining = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldBodyRemaining, "cell 7 4,growx");

		lblFeet = new JLabel("Acup:");
		lblFeet.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, UiCommon.UI_FONT_SIZE));
		contentPane.add(lblFeet, "cell 7 4");
		textFieldAcupunctureRemaining = UiCommon.setNotEditableTextField();
		contentPane.add(textFieldAcupunctureRemaining, "cell 7 4, growx");

		// ********************
		// ***SERVICES TABLE***
		// ********************

		sTableModel = new BodyServiceTableModel(getMainFrameCustomer());
		servicesTable = new JTable(sTableModel);
		servicesTable.setShowHorizontalLines(true);
		servicesTable.setForeground(Color.BLACK);
		servicesTable.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 14));
		servicesTable.setRowHeight(20);
		servicesTable.getTableHeader().setReorderingAllowed(false);
		servicesTable.getTableHeader().setResizingAllowed(false);
		servicesTable.setFocusable(false);
		servicesTable.setRowSelectionAllowed(false);
		servicesTable.setModel(sTableModel);

		// Center Header, cells, and set font
		TableCellRenderer rendererHeader = servicesTable.getTableHeader().getDefaultRenderer();
		JLabel header = (JLabel) rendererHeader;
		header.setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer centerRend = new DefaultTableCellRenderer();
		centerRend.setHorizontalAlignment(JLabel.CENTER);
		servicesTable.setDefaultRenderer(String.class, centerRend);

		setServiceListTableColumns();

		JScrollPane servicesTableScrollPane = new JScrollPane(servicesTable);
		servicesTableScrollPane.setBackground(UiCommon.WHITE_MAIN);
		contentPane.add(servicesTableScrollPane, "cell 1 6 7 1,grow");

		// ********************
		// ***PACKAGES TABLE***
		// ********************
		JLabel labelPackageHistory = new JLabel("Package Purchase History");
		labelPackageHistory.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 17));
		labelPackageHistory.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelPackageHistory, "cell 1 7 7 1,alignx center,aligny bottom");

		pTableModel = new BodyPackageTableModel(getMainFrameCustomer());
		packagesTable = new JTable(pTableModel);
		packagesTable.setShowHorizontalLines(true);
		packagesTable.setForeground(Color.BLACK);
		packagesTable.setFont(new Font(UiCommon.UI_FONT, Font.BOLD, 14));
		packagesTable.setRowHeight(20);
		packagesTable.getTableHeader().setReorderingAllowed(false);
		packagesTable.getTableHeader().setResizingAllowed(false);
		packagesTable.setFocusable(false);
		packagesTable.setRowSelectionAllowed(false);
		packagesTable.setModel(pTableModel);

		// Center Header, cells, and set font
		TableCellRenderer rendererFromHeader = packagesTable.getTableHeader().getDefaultRenderer();
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		packagesTable.setDefaultRenderer(String.class, centerRenderer);

		setPackageListTableColumns();

		JScrollPane packagesTableScrollPane = new JScrollPane(packagesTable);
		packagesTableScrollPane.setBackground(UiCommon.WHITE_MAIN);
		contentPane.add(packagesTableScrollPane, "cell 1 8 7 1,grow");

	}

	private void addEventHandlers() {
		LOG.debug("Adding the Event Handlers");
		new UiController(this);
		addWindowListener(new UiController.MainFrameWindowEventHandler());

		btnEditThisCustomer.addActionListener(new UiController.EditCustomerItemHandler());

		// Group
		btnAddToGroup.addActionListener(new UiController.AddGroupMemberItemHandler());

		// Customer
		mntmGetWalkInCustomer.addActionListener(new UiController.GetWalkInCustomerItemHandler());
		mntmAddNewCustomer.addActionListener(new UiController.AddCustomerItemHandler());
		mntmGetCustomerByPhone.addActionListener(new UiController.GetCustomerByPhoneItemHandler());
		mntmGetCustomerList.addActionListener(new UiController.GetCustomerByListItemHandler());
		mntmInvoiceCustomer.addActionListener(new UiController.InvoiceCustomerItemHandler());

		// Service
		mntmSellService.addActionListener(new UiController.SellServiceItemHandler());
		mntmSellCombo1_5.addActionListener(new UiController.SellCombo1_5ItemHandler());
		mntmSellCombo2_0.addActionListener(new UiController.SellCombo2_0ItemHandler());
		// Package
		mntmSellPackage.addActionListener(new UiController.SellPackageItemHandler());

		// Reports
		mntmPackageSales.addActionListener(new UiController.ReportPackageItemHandler());
		mntmServiceSales.addActionListener(new UiController.ReportServiceItemHandler());
		mntmPackageServiceSales.addActionListener(new UiController.ReportPackageServiceItemHandler());
		mntmCloseShop.addActionListener(new UiController.CloseShopItemHandler());

		// Staff
		mntmAddStaff.addActionListener(new UiController.AddStaffItemHandler());
		mntmEditStaff.addActionListener(new UiController.EditStaffItemHandler());
		mntmViewStaff.addActionListener(new UiController.ViewStaffItemHandler());

		// Pricing
		mntmEditPricing.addActionListener(new UiController.EditPricingItemHandler());
		mntmViewPricing.addActionListener(new UiController.ViewPricingItemHandler());
		mntmEditGST.addActionListener(new UiController.EditGSTItemHandler());

		// Services table
		servicesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					long id = 0;

					int selectedRow = servicesTable.getSelectedRow();
					Object value = servicesTable.getValueAt(selectedRow, 0);
					if (value != null) {

						id = Long.parseLong(servicesTable.getValueAt(selectedRow, 0).toString());
						new UiController.EditServiceItemHandler(id);
					}
				}
			}
		});
		// Packages table
		packagesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					long id = 0;

					int selectedRow = packagesTable.getSelectedRow();
					Object value = packagesTable.getValueAt(selectedRow, 0);
					if (value != null) {

						id = Long.parseLong(packagesTable.getValueAt(selectedRow, 0).toString());
						new UiController.EditBodyPackageItemHandler(id);
					}
				}
			}
		});
	}

	// REFRESH MAINFRAME CUSTOMER DATA
	public static void updateMainTextFields(Customer customer) throws ApplicationException {
		setMainFrameCustomer(customer);
		updateServicesList();
		updatePackagesList();
		textFieldPhone.setText(customer.getPhone());

		// total Sales, balance, and body and acupuncture remaining
		double financials[] = customerDaoInstance.getCustomerSales_Balance_Remaining(customer);
		textFieldTotalSales.setText(UiCommon.TWO_DECIMAL_FORMAT.format(financials[0]));
		textFieldBalance.setText(UiCommon.TWO_DECIMAL_FORMAT.format(financials[1]));
		textFieldBodyRemaining.setText(UiCommon.ONE_DECIMAL_FORMAT.format(financials[2]));
		textFieldAcupunctureRemaining.setText(UiCommon.ONE_DECIMAL_FORMAT.format(financials[3]));

		textFieldEmail.setText(customer.getEmail());
		// Date
		if (customer.getJoinDate() != null) {
			LocalDate joinDate = LocalDate.parse(customer.getJoinDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
			textFieldJoinDate.setText(joinDate.format(UiCommon.DATE_FORMAT_UI));
		} else {
			textFieldJoinDate.setText("No Service Yet");
		}

		textFieldName.setText(customer.getFirstName() + " " + customer.getLastName());

		// update group combo box
		groupComboBox.removeAllItems();
		if (!groupDaoInstance.validateGroupMember(mainFrameCustomer.getID())) {
			ArrayList<Long> members = groupDaoInstance.getGroupByCustomerId(mainFrameCustomer.getID()).getMembers();

			for (long s : members) {
				Customer c = customerDaoInstance.getCustomerById(s);
				String member = c.getFirstName() + " " + c.getLastName() + " (" + c.getPhone() + ")";
				groupComboBox.addItem(member);
			}
		} else {
			groupComboBox.addItem("N/A");
		}

	}

	private static void updateServicesList() throws ApplicationException {
		sTableModel = new BodyServiceTableModel(getMainFrameCustomer());
		servicesTable.setModel(sTableModel);
		setServiceListTableColumns();
	}

	private static void updatePackagesList() throws ApplicationException {
		pTableModel = new BodyPackageTableModel(getMainFrameCustomer());
		packagesTable.setModel(pTableModel);
		setPackageListTableColumns();
	}

	private static void setPackageListTableColumns() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		pTable_id = packagesTable.getColumnModel().getColumn(0);
		pTable_purchaser = packagesTable.getColumnModel().getColumn(1);
		pTable_date = packagesTable.getColumnModel().getColumn(2);
		pTable_packageName = packagesTable.getColumnModel().getColumn(3);
		pTable_price = packagesTable.getColumnModel().getColumn(4);
		pTable_gst = packagesTable.getColumnModel().getColumn(5);
		pTable_cost = packagesTable.getColumnModel().getColumn(6);
		pTable_balance = packagesTable.getColumnModel().getColumn(7);
		pTable_remaining = packagesTable.getColumnModel().getColumn(8);
		pTable_staff = packagesTable.getColumnModel().getColumn(9);
		pTable_id.setCellRenderer(centerRenderer);
		pTable_purchaser.setCellRenderer(centerRenderer);
		pTable_date.setCellRenderer(centerRenderer);
		pTable_packageName.setCellRenderer(centerRenderer);
		pTable_price.setCellRenderer(centerRenderer);
		pTable_gst.setCellRenderer(centerRenderer);
		pTable_cost.setCellRenderer(centerRenderer);
		pTable_balance.setCellRenderer(centerRenderer);
		pTable_remaining.setCellRenderer(centerRenderer);
		pTable_staff.setCellRenderer(centerRenderer);
		pTable_id.setPreferredWidth(20);
		pTable_purchaser.setPreferredWidth(50);
		pTable_date.setPreferredWidth(40);
		pTable_packageName.setPreferredWidth(140);
		pTable_price.setPreferredWidth(30);
		pTable_gst.setPreferredWidth(30);
		pTable_cost.setPreferredWidth(30);
		pTable_balance.setPreferredWidth(30);
		pTable_remaining.setPreferredWidth(30);
		pTable_staff.setPreferredWidth(90);
	}

	private static void setServiceListTableColumns() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		//// Service ID", "Package ID", "Date", "Service", "Price", "Qty", "GST", "Total Cost", "Paid" "Staff"
		sTable_id = servicesTable.getColumnModel().getColumn(0);
		sTable_packageID = servicesTable.getColumnModel().getColumn(1);
		sTable_date = servicesTable.getColumnModel().getColumn(2);
		sTable_service = servicesTable.getColumnModel().getColumn(3);
		sTable_price = servicesTable.getColumnModel().getColumn(4);
		sTable_qty = servicesTable.getColumnModel().getColumn(5);
		sTable_subtotal = servicesTable.getColumnModel().getColumn(6);
		sTable_gst = servicesTable.getColumnModel().getColumn(7);
		sTable_cost = servicesTable.getColumnModel().getColumn(8);
		sTable_staff = servicesTable.getColumnModel().getColumn(9);

		sTable_id.setCellRenderer(centerRenderer);
		sTable_packageID.setCellRenderer(centerRenderer);
		sTable_date.setCellRenderer(centerRenderer);
		sTable_service.setCellRenderer(centerRenderer);
		sTable_price.setCellRenderer(centerRenderer);
		sTable_qty.setCellRenderer(centerRenderer);
		sTable_subtotal.setCellRenderer(centerRenderer);
		sTable_gst.setCellRenderer(centerRenderer);
		sTable_cost.setCellRenderer(centerRenderer);
		sTable_staff.setCellRenderer(centerRenderer);

		sTable_id.setPreferredWidth(10);
		sTable_packageID.setPreferredWidth(30);
		sTable_date.setPreferredWidth(40);
		sTable_service.setPreferredWidth(120);
		sTable_price.setPreferredWidth(20);
		sTable_qty.setPreferredWidth(15);
		sTable_subtotal.setPreferredWidth(20);
		sTable_gst.setPreferredWidth(20);
		sTable_cost.setPreferredWidth(30);
		sTable_staff.setPreferredWidth(90);
	}
}
