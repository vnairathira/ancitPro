package com.ancit.diagnostictool.appln;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(true);
	}

	@Override
	public boolean preWindowShellClose() {
		return MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Exit Confirmation",
				"Do You Want to QUIT ?");
	}

	@Override
	public void postWindowCreate() {
		// remove unwanted menu entries
		List<String> unwantedItems = Arrays.asList("org.eclipse.ui.openLocalFile", "converstLineDelimitersTo",
				"org.eclipse.ui.cheatsheets.actions.CheatSheetHelpMenuAction",
				"org.eclipse.debug.ui.actions.BreakpointTypesContribution", "ExternalToolsGroup",
				"org.eclipse.ui.externaltools.ExternalToolMenuDelegateMenu", "navigate", "org.eclipse.search.menu",
				"org.eclipse.ui.run", "org.eclipse.ui.navigator", "org.eclipse.ui.actions.showKeyAssistHandler");

		IMenuManager menuManager = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		removeUnwantedItems(unwantedItems, menuManager);

//		/* removes unwanted IDE contribution items from the toolbar */
//		IActionBarConfigurer actionBarConfigurer = getWindowConfigurer().getActionBarConfigurer();
//		IContributionItem[] coolItems1 = actionBarConfigurer.getCoolBarManager().getItems();
//		// for (IContributionItem iContributionItem : coolItems) {
//		// System.out.println("ID :"+iContributionItem.getId());
//		// }
//		for (IContributionItem coolItems : coolItems1) {
//			if (coolItems instanceof ToolBarContributionItem) {
//				ToolBarContributionItem toolbarItem = (ToolBarContributionItem) coolItems;
//				System.out.println("\ttoolBarItem Id: " + toolbarItem.getId());
//
//				if (toolbarItem.getId().equals("org.eclipse.ui.WorkingSetActionSet")
//						|| toolbarItem.getId().equals("org.eclipse.ui.edit.text.actionSet.annotationNavigation")
//						|| toolbarItem.getId().equals("org.eclipse.ui.edit.text.actionSet.navigation")) 
//				{
//					toolbarItem.getToolBarManager().removeAll();
//				}
//			}
//		}
//
//		actionBarConfigurer.getCoolBarManager().update(true);

	}

	private void removeUnwantedItems(final List<String> unwantedItems, final IMenuManager menuManager) {
		IContributionItem[] items = menuManager.getItems();

		for (IContributionItem item : items) {

			if (item instanceof IMenuManager) {
				removeUnwantedItems(unwantedItems, (IMenuManager) item);
			}

			if (unwantedItems.contains(item.getId())) {
				menuManager.remove(item);
			}
		}
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();

		IWorkbenchWindowConfigurer workbenchWindowConfigurer = getWindowConfigurer();
		IActionBarConfigurer actionBarConfigurer = workbenchWindowConfigurer.getActionBarConfigurer();
		IMenuManager menuManager = actionBarConfigurer.getMenuManager();
		IContributionItem[] menuItems = menuManager.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			IContributionItem menuItem = menuItems[i];

			// Hack to remove the Run menu - it seems you cannot do this using the
			// "org.eclipse.ui.activities" extension
			if ("org.eclipse.ui.run".equals(menuItem.getId()) || "org.eclipse.ui.navigator".equals(menuItem.getId())) {
				menuManager.remove(menuItem);
			}
		}

		menuManager.update(true);
	}
}
