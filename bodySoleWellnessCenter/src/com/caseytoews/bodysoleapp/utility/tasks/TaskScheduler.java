/**
 * Project: bodySoleWellnessCenter
 * Date: Mar 17, 2019
 * Time: 8:27:56 PM
 */

package com.caseytoews.bodysoleapp.utility.tasks;

import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.utility.email.Emailer;
import com.caseytoews.bodysoleapp.utility.email.EmailerImpl;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

public class TaskScheduler {
	public static final Logger LOG = LogManager.getLogger();

	public TaskScheduler() {
		super();
	}

	public static void startBackupDataFilesThread() {
		Timer timer = new Timer();
		Date date = new Date();
		long period = 1000L * 60L * 60L * 24L;

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				LocalDate now = LocalDate.now();
				String today = now.format(UiCommon.DATE_FORMAT_UI);
				EmailerImpl emailer = new EmailerImpl(Emailer.MANAGER, "Daily Backups for " + today, "Daily Backups for " + today);
				emailer.setIsBackUp();
				try {
					emailer.sendEmail();
				} catch (ApplicationException e) {
					LOG.error("Schedule back up email error in class DailyBackup");
					e.printStackTrace();
				}
				EmailerImpl caseyLog = new EmailerImpl(Emailer.IT, "Daily Error Log: " + today, "Daily Error Log: " + today);
				caseyLog.setIsErrorLog();
				try {
					caseyLog.sendEmail();
				} catch (ApplicationException e) {
					LOG.error("TaskScheduler () DailyBackup");
					e.printStackTrace();
				}
			}

		}, date, period);
	}
}
