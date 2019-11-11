package com.ancit.diagnostictool.appln;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class ANCITLaunchScreen extends ViewPart {

	public ANCITLaunchScreen() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lblNewLabel.setImage(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/Logo.jpg").createImage());
		IActionBars bars = getViewSite().getActionBars();
		String copyright = "\u00a9";
		bars.getStatusLineManager().setMessage(null, "Copyright " + copyright + " 2019 ANCiT. All rights reserved ");
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
