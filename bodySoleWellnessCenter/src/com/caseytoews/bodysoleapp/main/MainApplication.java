package com.caseytoews.bodysoleapp.main;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.caseytoews.bodysoleapp.utility.loader.InitialFilesCreator;

/**
 * Project: BodySolePOS
 * File: MainApplication.java
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */

public class MainApplication {
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	private static boolean reloadFiles;

	static {
		try {
			configureLogging();
		} catch (ApplicationException e) {
		}
	}

	public static final Logger LOG = LogManager.getLogger();

	private static void configureLogging() throws ApplicationException {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			throw new ApplicationException(String.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME));
		}
	}

	public static void main(String[] args) {
		LOG.debug("Starting app: " + UiCommon.dateNow());

		// run configuration value = "1" to reload files
		reloadFiles = (args.length > 0 && args[0].equals("1")) ? true : false;

		MainApplication mainApp = null;
		try {
			mainApp = new MainApplication();
			mainApp.run();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		LOG.debug("main thread exiting");
	}

	public MainApplication() throws FileNotFoundException, IOException, ApplicationException {

		if (reloadFiles) {
			InitialFilesCreator tester = new InitialFilesCreator();
			tester.createInitialFiles();
		}
	}

	private void run() {
		try {
			launchApplication();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}

	private void launchApplication() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				// set up the look and feel
				try {
					// https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html#primary
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							LOG.debug("Nimbus L&F is set.");
							break;
						}
					}
				} catch (Exception e) {
					LOG.debug("Nimbus is not available so default is being used");
				}

				// launch Main Frame
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}