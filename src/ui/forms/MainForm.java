package ui.forms;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import sesion.props.UIPropSheet;
import ui.components.EmptyControlPann;
import ui.components.RichList;
import ui.components.SheetTabbedPane;
import ui.controller.FacadeController;
import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.proxy.MainWindowProxy;

public class MainForm extends JFrame implements MainWindowProxy {
	private static final long serialVersionUID = 1L;
	private static MainForm instance = new MainForm();
	private SheetTabbedPane sheetTabPane;

	// This components reprezent the main panel of the application (without the
	// toolbars and menues)
	// and are used for drag and drop integration
	private RootWindow rootWindow;
	public RootWindowProperties properties;
	private TabWindow controlTPane;
	private View onSelectView;
	private View typesView;
	private ViewMap viewMap;

	private RichList entityContextJList;
	private JPanel onSelectEntJPan;
	private MainMenu menu;
	private JPanel toolbarHostPan;
	private JPanel applicationToolBarPannel;
	private JPanel pluginToolBarPannel;
	private MainToolbar generalToolbar;
	private EditToolbar editToolbar;

	public static MainForm getInstance() {
		return instance;
	}

	private MainForm() {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocation(200, 200);
		getContentPane().setLayout(new BorderLayout());
		addComponents();

	}

	private void addComponents() {
		menu = new MainMenu();
		setJMenuBar(menu);

		// main toolbar pannel
		toolbarHostPan = new JPanel();
		toolbarHostPan.setLayout(new BoxLayout(toolbarHostPan, BoxLayout.X_AXIS));
		getContentPane().add(toolbarHostPan, BorderLayout.NORTH);

		// default tools
		applicationToolBarPannel = new JPanel();
		applicationToolBarPannel.setLayout(new BoxLayout(applicationToolBarPannel, BoxLayout.X_AXIS));
		toolbarHostPan.add(applicationToolBarPannel);

		generalToolbar = new MainToolbar();
		applicationToolBarPannel.add(generalToolbar);

		editToolbar = new EditToolbar();
		applicationToolBarPannel.add(editToolbar);

		// custom tools
		pluginToolBarPannel = new JPanel();
		pluginToolBarPannel.setLayout(new BoxLayout(pluginToolBarPannel, BoxLayout.X_AXIS));
		toolbarHostPan.add(pluginToolBarPannel);

		viewMap = new ViewMap();

		entityContextJList = new RichList();
		entityContextJList.putClientProperty("List.isFileList", Boolean.TRUE);
		typesView = new View("Types", null, entityContextJList);
		viewMap.addView(1, typesView);

		onSelectEntJPan = new EmptyControlPann();
		onSelectView = new View("Properties", null, onSelectEntJPan);
		viewMap.addView(2, onSelectView);

		sheetTabPane = new SheetTabbedPane();
		sheetTabPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

		View sheetTabPaneView = new View("Sheet", null, sheetTabPane);
		viewMap.addView(3, sheetTabPaneView);
		sheetTabPaneView.getViewProperties().setAlwaysShowTitle(false);

		rootWindow = DockingUtil.createRootWindow(viewMap, true);
		properties = new RootWindowProperties();
		properties.getDockingWindowProperties().setCloseEnabled(false);
		properties.getDockingWindowProperties().setUndockEnabled(false);
		properties.getDockingWindowProperties().setMaximizeEnabled(false);
		rootWindow.getRootWindowProperties().addSuperObject(properties);

		controlTPane = new TabWindow(new DockingWindow[] { onSelectView, typesView });
		rootWindow.setWindow(new SplitWindow(true, 1 - UIPropSheet.CONTROL_PAN_WITH_RATIO, sheetTabPaneView, controlTPane));
		// controlTPane.setSelectedTab(1);

		getContentPane().add(rootWindow, BorderLayout.CENTER);

		entityContextJList.addMouseListener(FacadeController.getInstance().getEntityListDND());
		entityContextJList.addMouseMotionListener(FacadeController.getInstance().getEntityListDND());
		entityContextJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public MainMenu getMenu() {
		return menu;
	}

	public MainToolbar getGeneralToolbar() {
		return generalToolbar;
	}

	public JPanel getPluginToolbarHostPane() {
		return pluginToolBarPannel;
	}

	public RichList getEntityContextJList() {
		return entityContextJList;
	}

	public SheetTabbedPane getSheetTabPane() {
		return sheetTabPane;
	}

	public void updateOnSelectPan(AbstractControlJPanel newPan) {
		if (onSelectEntJPan.getComponentCount() == 1 && onSelectEntJPan.getComponent(0) instanceof AbstractControlJPanel) {
			if (((AbstractControlJPanel) onSelectEntJPan.getComponent(0)).equals(newPan))
				return;
			((AbstractControlJPanel) onSelectEntJPan.getComponent(0)).cleanup();
		}

		onSelectEntJPan.removeAll();
		onSelectEntJPan.add(new JScrollPane(newPan), BorderLayout.CENTER);
		onSelectEntJPan.setPreferredSize(newPan.getPreferredSize());
		controlTPane.revalidate();
		controlTPane.repaint();
		onSelectView.repaint();
	}

	public void onSelectPaneToFront() {
		onSelectView.restoreFocus();
	}

	public void createEntPaneToFront() {
		typesView.restoreFocus();
	}
}
