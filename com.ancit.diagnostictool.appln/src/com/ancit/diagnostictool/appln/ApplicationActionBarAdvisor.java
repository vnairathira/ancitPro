package com.ancit.diagnostictool.appln;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction helpContents;
	private IWorkbenchAction aboutDialog;
	private IWorkbenchAction saveAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("File", IWorkbenchActionConstants.M_FILE);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(saveAction);
		fileMenu.add(exitAction);
		
		MenuManager helpMenu = new MenuManager("Help", IWorkbenchActionConstants.M_HELP);
		helpMenu.add(helpContents);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		helpMenu.add(aboutDialog);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
	
	}
	
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		
		ToolBarManager toolbarManager = new ToolBarManager();
		coolBar.add(toolbarManager);
		
		toolbarManager.add(saveAction);
		super.fillCoolBar(coolBar);
	}
	
	@Override
	protected void makeActions(IWorkbenchWindow window) {
		exitAction = ActionFactory.QUIT.create(window);
		helpContents = ActionFactory.HELP_CONTENTS.create(window);
		aboutDialog = ActionFactory.ABOUT.create(window);
		saveAction = ActionFactory.SAVE.create(window);
	}
	
	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		
		String licenseKey = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\ANCIT", "MAC_ID");
		String[] licenseKeyDetails = licenseKey.split("/");
		ContributionItem item = new ContributionItem() {
			@Override
			public void fill(Composite parent) {
				Label label=new Label(parent,SWT.NONE);
				label.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK));
				Label label1=new Label(parent,SWT.NONE);
			    label1.setText("You have only" + " " + licenseKeyDetails[2] + " " + "days left ");	
				super.fill(parent);
			}
		};
		statusLine.add(item);
	}

}
