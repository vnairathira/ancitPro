package com.ancit.diagnostictool.appln;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		try {
			String licenseKey = null;
			boolean registryKeyExists = Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\ANCIT");
			if (registryKeyExists) {
				licenseKey = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\ANCIT", "MAC_ID");
			}

			String sb = getMACId();

			if (licenseKey == null || licenseKey.isEmpty()) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = new Date();

				Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, "SOFTWARE", "ANCIT");
				Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\ANCIT", "MAC_ID",
						sb + "/" + dateFormat.format(date) + "/" + 30);

			} else {

				String[] split = licenseKey.split("/");
				String date = split[1].toString();
				if (sb.toString().equals(split[0])) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					Date dateformat = formatter.parse(date);

					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Date current = new Date();
					String parse = dateFormat.format(current);
					Date datefo = formatter.parse(parse);

					int diffInDays = (int) ((datefo.getTime() - dateformat.getTime()) / (1000 * 60 * 60 * 24));

					if (diffInDays >= 30) {
						MessageDialog.openWarning(display.getActiveShell(), "Licence Expired",
								"Your trial peroid has expired");
						return IApplication.EXIT_OK;
					} else {
						int days = 30 - diffInDays;
//						MessageDialog.openWarning(display.getActiveShell(), "Licence Expired",
//								"You have only" + " " + days + " " + "days left");
						if (Integer.parseInt(split[2]) != 0) {
							Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\ANCIT", "MAC_ID",
									split[0] + "/" + split[1] + "/" + days);
						} else {
							MessageDialog.openError(display.getActiveShell(), "Licence Expired",
									"Your trial peroid has expired" + licenseKey);
							return IApplication.EXIT_OK;
						}

					}
				} else {
					MessageDialog.openError(display.getActiveShell(), "Invalid Installation",
							"This Installation is intended to work on Machine with MAC ID" + licenseKey);
					return IApplication.EXIT_OK;
				}
			}
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}

	}

	private String getMACId() throws UnknownHostException, SocketException {
		InetAddress ip = InetAddress.getLocalHost();

		NetworkInterface network = NetworkInterface.getByInetAddress(ip);

		byte[] mac = network.getHardwareAddress();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}

		return sb.toString();
	}

	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
