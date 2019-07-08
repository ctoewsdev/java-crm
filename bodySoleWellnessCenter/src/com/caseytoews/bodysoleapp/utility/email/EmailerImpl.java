/**
 * Project: bodySoleWellnessCenter
 * Date: Mar 12, 2019
 * Time: 10:59:13 AM
 */

package com.caseytoews.bodysoleapp.utility.email;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

/**
 * This class requires an the interface Emailer I have not
 * uploaded due to sensitivity. Requires the following constants:
 * static final String USERNAME = "XXX";
 * static final String PASSWORD = "XXX";
 * static final String SENDER = "XXX@gmail.com";
 * static final String HOST = "smtp.gmail.com";
 * static final String MANAGER = "XXX@gmail.com";
 *
 */
public class EmailerImpl implements Emailer {
	private String recipient;
	private String subject;
	private String invoice;
	private Boolean isBackUp;
	private Boolean isErrorLog;
	private Boolean isInvoice;
	private ArrayList<String> packageOwners = new ArrayList<>();
	private static final Logger LOG = LogManager.getLogger();

	public EmailerImpl(String recipient, String subject, String invoice) {
		super();
		this.recipient = recipient;
		this.subject = subject;
		this.invoice = invoice;
		isBackUp = false;
		isErrorLog = false;
		isInvoice = false;
	}

	public void setIsBackUp() {
		isBackUp = true;
	}

	public void setIsErrorLog() {
		isErrorLog = true;
	}

	public void setIsInvoice() {
		isInvoice = true;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public void setPackageOwnersCC(ArrayList<String> packageOwners) {
		this.packageOwners = packageOwners;
	}

	public String sendEmail() throws ApplicationException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(SENDER));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

			if (isInvoice) {
				message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(MANAGER));
			}

			if (packageOwners.size() > 0) {
				for (String owner : packageOwners) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(owner));
				}
			}

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(invoice);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			if (isBackUp) {

				ArrayList<String> files = new ArrayList<>();
				files.add(IOConstants.CUSTOMERINVOICES_FILE);
				files.add(IOConstants.CUSTOMERS_FILE);
				files.add(IOConstants.NEXTID_FILE);
				files.add(IOConstants.PACKAGED_FILE);
				files.add(IOConstants.PRODUCTS_FILE);
				files.add(IOConstants.SERVICES_FILE);
				files.add(IOConstants.STAFF_FILE);
				files.add(IOConstants.GROUPS_FILE);
				for (String file : files) {
					messageBodyPart = new MimeBodyPart();
					String filename = file;
					DataSource source = new FileDataSource(filename);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename);
					multipart.addBodyPart(messageBodyPart);
				}
			}

			if (isErrorLog) {
				messageBodyPart = new MimeBodyPart();
				String filename = "out.log";
				DataSource source = new FileDataSource("out.log");
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
			}

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			return "Sent message successfully....";

		} catch (MessagingException e) {
			LOG.error("Error in Emailer SendEmail(). Sending to: " + recipient);
			throw new ApplicationException(e);
		}
	}
}