/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 18, 2018
 * Time: 8:46:26 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.staff;

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.dialogviews.common.EvenOddCellRenderer;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.models.staff.StaffListItem;
import com.caseytoews.bodysoleapp.models.staff.StaffListModel;
import com.caseytoews.bodysoleapp.utility.comparator.CompareStaffByLastName;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class ViewStaffListDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JList<StaffListItem> staffList;
	private StaffListModel staffModel;
	private JButton cancelButton;
	private JScrollPane scrollPane;

	public ViewStaffListDialog(JFrame frame, StaffDao staffDao) throws ApplicationException {
		setBounds(100, 100, 750, 410);
		setTitle("Staff List");
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new MigLayout("fillx", "[650px]", "[500px][41px]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, "cell 0 0,grow");
		contentPanel.setLayout(new MigLayout("fillx", " [575px]", "[40px] [450]"));

		{
			JLabel lblNewLabel = new JLabel("Complete Staff List.");
			lblNewLabel.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx center,aligny center");
		}

		{
			staffModel = new StaffListModel();
			staffList = new JList<>(staffModel);
			staffList.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 15));
			staffList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			staffList.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(staffList);
			scrollPane.setForeground(UiCommon.BLACK);
			contentPanel.add(scrollPane, "cell 0 1,aligny top,grow");

			List<Staff> staffMember = staffDao.getAllStaff();
			staffMember.sort(new CompareStaffByLastName());
			for (Staff staff : staffMember) {
				staffModel.add(staff);
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
					ViewStaffListDialog.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}
	}
}