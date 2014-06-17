package Main;

import helper.DB;
import helper.customFont;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.miginfocom.swing.MigLayout;
import ui.fFrame;
import ui.fTable;
import debug.debug;

public class Main implements ActionListener, TableModelListener{

	// Graphic Components
	customFont Notera = new customFont("Notera", Font.PLAIN, 32);
	customFont basicTitle = new customFont("basictitlefont", Font.PLAIN, 32);
	customFont H1 = new customFont("Alegreya-Regular", Font.PLAIN, 32);
	customFont code = new customFont("SourceSansPro-Regular", Font.PLAIN, 18);

	// Static Swing Declarations
	JTextArea queryResponse = new JTextArea(14, 50);
	JScrollPane output = new JScrollPane (queryResponse);
	JTable tbl = new JTable();
	//--Login
	JPasswordField txtPass = new JPasswordField(16);
	JTextField txtUser = new JTextField(16);
	//--Front
	JLabel reciptTotal = new JLabel("Total: 0.00");
	JTextArea recipt = new JTextArea(4,25);
	int orderTotal=0;
	ArrayList order = new ArrayList();
	//--Table
	fTable table = new fTable();
	//--products
	JTextField title=new JTextField(10);
	JTextField price=new JTextField(6);
	JTextField id=new JTextField(6);

	// Global Panel
	String VIEW=null;
	CardLayout viewHandler = new CardLayout();
	JPanel _global = new JPanel(viewHandler);

	//Current User
	int currentUser;


	/////////////////////////////////////////////////////
	//                    MAIN                         //
	/////////////////////////////////////////////////////
	public Main() {
		setupLookAndFeel();
		setup();
	}

	/////////////////////////////////////////////////////
	//                    SETUP                        //
	/////////////////////////////////////////////////////
	public void setup(){
		// Window
		fFrame frame = new fFrame("St.Aples Office Depot");
		frame.setVisible(false);

		// Views
		setupViews(frame);		

		// START FINAL
		setView("loginView");
		frame.setVisible(true);

		//TESTING ONLY - NOT FOR PRODUCTION
		txtUser.setText("root");
		txtPass.setText("toor");
	}

	public void setupViews(fFrame frame){
		/* Views 
		 * - Add a view for each JPanel method
		 * - ie. if you have a JPanel called "startupView"
		 * 	then you must name the record in the views list 
		 * 	named "startupView" 
		 */

		_global.add(loginView(frame), "loginView");
		_global.add(frontView(frame), "frontView");
		_global.add(adminView(frame), "adminView");
		_global.add(usersView(frame), "usersView");
		_global.add(productsView(frame), "productsView");
		_global.add(salesView(frame), "salesView");
		_global.add(addProductView(frame), "addProductView");
		_global.add(dropProductView(frame), "dropProductView");

		frame.add(_global);
	}

	public void setupLookAndFeel(){
		try 
		{
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			debug.notify("THEME_GOOD");
		} 
		catch (Exception e) 
		{
			debug.notify("THEME_BAD");
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////
	//                    VIEWS                        //
	/////////////////////////////////////////////////////
	public JPanel loginView(fFrame frame){
		// Size Frame
		frame.setSize(400, 400);
		MigLayout layout = new MigLayout(
				"", // Layout Constraints
				"260[][]", // Column constraints
				"160[][][]"); // Row constraints
		JPanel panel = new JPanel(layout);
		JLabel lblLogin = new JLabel("LOGIN");
		JLabel lblUser = new JLabel("USERNAME:");
		JLabel lblPass = new JLabel("PASSWORD:");
		JButton btnLogin = new JButton (" LOGIN ");
		btnLogin.setActionCommand("LOGIN");
		btnLogin.addActionListener(this);
		JButton btnNew = new JButton (" CREATE NEW ACCOUNT ");
		btnNew.setActionCommand("NEW");
		btnNew.addActionListener(this);
		panel.add(lblLogin, "span");
		panel.add(lblUser);
		panel.add(txtUser, "wrap");
		panel.add(lblPass);
		panel.add(txtPass, "wrap");
		panel.add(btnLogin);
		panel.add(btnNew);
		return panel;
	}
	public JPanel frontView(fFrame frame){
		// Size Frame
		frame.setSize(800, 500);
		JPanel panel = new JPanel();
		MigLayout layout = new MigLayout(
				"", // Layout Constraints
				"[]550[][]", // Column constraints
				"[][]"); // Row constraints
		JPanel topPanel = new JPanel(layout); 
		JPanel midPanel = new JPanel(new GridLayout(4,4,10,10)); 
		JPanel botPanel = new JPanel(new GridLayout(4,1));
		JLabel lblProducts = new JLabel("PRODUCTS");
		JButton btnAdmin = new JButton ("ADMIN");
		btnAdmin.setActionCommand("ADMIN");
		btnAdmin.addActionListener(this);
		JButton btnLogout = new JButton ("LOGOUT");
		btnLogout.setActionCommand("LOGOUT");
		btnLogout.addActionListener(this);

		//Draw Products based on DB content
		HashMap<Integer, LinkedHashMap<String, String>> dbr = DB.query("products", DB.find);
		for(int i=0;i<dbr.size();i++){
			JButton temp = new JButton(dbr.get(i).get("name").toString()+" - "+dbr.get(i).get("price").toString());
			temp.setActionCommand(dbr.get(i).get("name").toString());
			temp.addActionListener(this);

			//ProductBtnUI style = new ProductBtnUI();
			//style.setTitle(dbr.get(i).get("name").toString());
			//style.setPrice(dbr.get(i).get("price").toString());
			//temp.setUI(style);

			midPanel.add(temp);
		}

		//Draw Empty Receipt
		JLabel reciptTitle = new JLabel("Current Order");
		JScrollPane reciptArea = new JScrollPane(recipt);
		JPanel inner = new JPanel(new GridLayout(1,2));
		JButton checkout = new JButton("Order");
		checkout.addActionListener(this);

		inner.add(reciptTotal);
		inner.add(checkout);

		botPanel.add(reciptTitle);
		botPanel.add(reciptArea);
		botPanel.add(inner);

		//END
		topPanel.add(lblProducts);
		topPanel.add(btnAdmin);
		topPanel.add(btnLogout);
		panel.add(topPanel,BorderLayout.PAGE_START);
		panel.add(midPanel,BorderLayout.CENTER);
		panel.add(botPanel,BorderLayout.SOUTH);
		return panel;
	}
	public JPanel adminView(fFrame frame){
		// Size Frame
		frame.setSize(100, 100);
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel((new GridLayout(1,1,1,1)));
		MigLayout layout = new MigLayout(
				"", // Layout Constraints
				"170[]20[]20[]170", // Column constraints
				"180[]190"); // Row constraints
		JPanel midPanel = new JPanel(layout);
		JPanel botPanel = new JPanel((new GridLayout(1,1,1,1)));
		JLabel lblAdmin = new JLabel ("ADMINISTRATION");
		JButton btnUsers = new JButton (" USERS ");
		btnUsers.setActionCommand("USERS");
		btnUsers.addActionListener(this);
		JButton btnProducts = new JButton (" PRODUCTS ");
		btnProducts.setActionCommand("PRODUCTS");
		btnProducts.addActionListener(this);
		JButton btnOrders = new JButton (" ORDERS ");
		btnOrders.setActionCommand("SALES");
		btnOrders.addActionListener(this);
		JButton btnBack = new JButton ("BACK");
		btnBack.setActionCommand("adminBACK");
		btnBack.addActionListener(this);
		topPanel.add(lblAdmin);
		midPanel.add(btnUsers);
		midPanel.add(btnProducts);
		midPanel.add(btnOrders);
		botPanel.add(btnBack);
		panel.add(topPanel,BorderLayout.PAGE_START);
		panel.add(midPanel,BorderLayout.CENTER);
		panel.add(botPanel,BorderLayout.PAGE_END);
		return panel;
	}
	public JPanel usersView(fFrame frame){
		// Size Frame
		frame.setSize(800, 500);
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel((new GridLayout(1,1,1,1)));
		JPanel midPanel = new JPanel((new GridLayout(1,1,1,1)));
		JPanel botPanel = new JPanel((new GridLayout(1,1,1,1)));
		JLabel lblUsers = new JLabel("USERS");
		lblUsers.setHorizontalAlignment(JLabel.CENTER);
		JButton btnBack = new JButton("BACK");
		lblUsers.setHorizontalAlignment(JLabel.CENTER);
		btnBack.setActionCommand("BACK");
		btnBack.addActionListener(this);

		/////////// Table ///////////
		String columnNames[] = { "ID", "Name", "Password" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("users", DB.find);
		final String DV[][] = new String[tmpData.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DB.query("users", DB.find).entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get("name");
			DV[i][2]=tmpData.get(i).get("password");
			i++;
		}
		table=new fTable(DV,columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 380));
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(this);

		JScrollPane scrollPane = new JScrollPane(table);
		topPanel.add(lblUsers);
		midPanel.add(scrollPane);
		botPanel.add(btnBack);
		panel.add(topPanel,BorderLayout.PAGE_START);
		panel.add(midPanel,BorderLayout.CENTER);
		panel.add(botPanel,BorderLayout.PAGE_END);
		return panel;
	}
	public JPanel productsView(fFrame frame){
		// Size Frame
		frame.setSize(800, 500);
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel((new GridLayout(1,1,1,1)));
		JPanel midPanel = new JPanel((new GridLayout(1,1,1,1)));
		JPanel botPanel = new JPanel((new GridLayout(1,1,1,1)));
		JLabel lblProducts = new JLabel("PRODUCTS");
		lblProducts.setHorizontalAlignment(JLabel.CENTER);
		JButton btnBack = new JButton("BACK");
		btnBack.setActionCommand("BACK");
		btnBack.addActionListener(this);

		/////////// Table ///////////
		String columnNames[] = { "ID", "Name", "Price" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("products", DB.find);
		final String DV[][] = new String[tmpData.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DB.query("products", DB.find).entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get("name");
			DV[i][2]=tmpData.get(i).get("price");
			i++;
		}
		table=new fTable(DV,columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 380));
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(this);

		JPanel inner = new JPanel(new BorderLayout());
		JButton add = new JButton("Add Product");
		add.addActionListener(this);
		JButton drop = new JButton("Delete Product");
		drop.addActionListener(this);
		inner.add(add, BorderLayout.WEST);
		inner.add(btnBack, BorderLayout.CENTER);
		inner.add(drop, BorderLayout.EAST);

		JScrollPane scrollPane = new JScrollPane(table);
		topPanel.add(lblProducts);
		midPanel.add(scrollPane);
		botPanel.add(inner);
		panel.add(topPanel,BorderLayout.PAGE_START);
		panel.add(midPanel,BorderLayout.CENTER);
		panel.add(botPanel,BorderLayout.PAGE_END);
		return panel;
	}
	public JPanel salesView(fFrame frame){
		// Size Frame
		frame.setSize(800, 500);
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel((new GridLayout(1,1,1,1)));
		JPanel midPanel = new JPanel((new GridLayout(1,1,1,1)));
		JPanel botPanel = new JPanel((new GridLayout(1,1,1,1)));
		JLabel lblOrders = new JLabel("ORDERS");
		lblOrders.setHorizontalAlignment(JLabel.CENTER);
		JButton btnBack = new JButton("BACK");
		btnBack.setActionCommand("BACK");
		btnBack.addActionListener(this);

		/////////// Table ///////////
		String columnNames[] = { "ID", "UID", "PID" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("sales", DB.find);
		final String DV[][] = new String[tmpData.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DB.query("sales", DB.find).entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get("uid");
			DV[i][2]=tmpData.get(i).get("pid");
			i++;
		}
		table=new fTable(DV,columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 380));
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(this);

		JScrollPane scrollPane = new JScrollPane(table);
		topPanel.add(lblOrders);
		midPanel.add(scrollPane);
		botPanel.add(btnBack);
		panel.add(topPanel,BorderLayout.PAGE_START);
		panel.add(midPanel,BorderLayout.CENTER);
		panel.add(botPanel,BorderLayout.PAGE_END);
		return panel;
	}

	public JPanel addProductView(fFrame frame){
		JPanel tp = new JPanel(new GridLayout(4,1));
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		submit.setActionCommand("addProduct");
		submit.addActionListener(this);

		tp.add(title);
		tp.add(price);
		tp.add(submit);
		tp.add(cancel);
		return tp;
	}
	public JPanel dropProductView(fFrame frame){
		JPanel tp = new JPanel(new GridLayout(3,1));
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		submit.setActionCommand("dropProduct");
		submit.addActionListener(this);

		tp.add(id);
		tp.add(submit);
		tp.add(cancel);
		return tp;
	}

	/////////////////////////////////////////////////////
	//                LAYOUT SWITCHER                  //
	/////////////////////////////////////////////////////
	public void setView(String view){
		viewHandler.show(_global,view);
		VIEW=view;
		debug.notify("view -> "+view);
	}

	/////////////////////////////////////////////////////
	//                MISC OPERATIONS                  //
	/////////////////////////////////////////////////////
	//Login
	public boolean login(){
		HashMap<Integer, LinkedHashMap<String, String>> db1 = DB.query("users", DB.find);
		for(int i=0;i<db1.size();i++){
			if(db1.get(i).get("name").equals(txtUser.getText())&db1.get(i).get("password").equals(txtPass.getText())){
				debug.notify("AUTH::SUCCESS");
				currentUser=i;
				return true;
			}
		}
		return false;
	}

	//Front-end Order
	public void addToOrder(String name){
		recipt.append(name+" - ");

		HashMap<Integer, LinkedHashMap<String, String>> db1 = DB.query("products", DB.find);
		for(int i=0;i<db1.size();i++){
			if(db1.get(i).get("name").equals(name)){
				recipt.append(db1.get(i).get("price"));
				orderTotal+=(int)Double.parseDouble(db1.get(i).get("price"));
				break;
			}
		}
		recipt.append("\n");
		order.add(name);

		reciptTotal.setText("Total: "+orderTotal);
	}
	public void order(){
		for (final Object p : order){
			DB.query("sales", DB.insert, DB.query("sales", DB.find).size(),new LinkedHashMap<String, String>(){{
				put("uid", String.valueOf(currentUser));
				put("pid", String.valueOf(p));
			}});
		}
		order.clear();
		recipt.setText("");
		reciptTotal.setText("");
	}

	//Back-End Edit
	public void loadUsers(){
		/////////// Table ///////////
		String columnNames[] = { "ID", "NAME", "PASSWORD" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> DBR = DB.query("users", DB.find);
		final String DV[][] = new String[DBR.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DBR.entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=DBR.get(i).get("name");
			DV[i][2]=DBR.get(i).get("password");
			i++;
		}
		table=new fTable(DV,columnNames);
		table.repaint();
	}
	public void loadProducts(){
		/////////// Table ///////////
		String columnNames[] = { "ID", "NAME", "PASSWORD" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> DBR = DB.query("products", DB.find);
		final String DV[][] = new String[DBR.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DBR.entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=DBR.get(i).get("name");
			DV[i][2]=DBR.get(i).get("password");
			i++;
		}
		table=new fTable(DV,columnNames);
		table.repaint();
	}
	public void loadSales(){
		/////////// Table ///////////
		String columnNames[] = { "ID", "UID", "PID" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> salesDB = DB.query("sales", DB.find);
		final String DV[][] = new String[salesDB.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: salesDB.entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=salesDB.get(i).get("uid");
			DV[i][2]=salesDB.get(i).get("pid");
			i++;
		}
		table=new fTable(DV,columnNames);
		table.repaint();
	}

	/////////////////////////////////////////////////////
	//             SWING ACTION LISTENER               //
	/////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Route Controller Actions to correct methods
		String cmd = arg0.getActionCommand();
		debug.notify(cmd);

		//View Switcher
		if(cmd.equals("LOGIN")){
			if(login())setView("frontView");
		}else if(VIEW.equals("frontView")){
			if(cmd.equals("Order")){
				order();
			}else if(cmd.equals("ADMIN")){
				setView("adminView");
			}else if(cmd.equals("LOGOUT")){
				setView("loginView");
			}else{
				addToOrder(cmd);
			}
		}else if(VIEW.equals("adminView")){
			if(cmd.equals("USERS")){
				loadUsers();
				setView("usersView");
			}else if(cmd.equals("PRODUCTS")){
				loadProducts();
				setView("productsView");
			}else if(cmd.equals("SALES")){
				loadSales();
				setView("salesView");
			}else if(cmd.equals("adminBACK")){
				setView("frontView");
			}
		}else if(VIEW.equals("productsView")){
			if(cmd.equals("Add Product")){
				setView("addProductView");
			}else if(cmd.equals("Delete Product")){
				setView("dropProductView");
			}
		}else if(cmd.equals("addProduct")){/*
			if(cmd.equals("Cancel")){
				setView("productsView");
			}else{
				DB.query("products", DB.insert, DB.query("products", DB.find).size() ,new LinkedHashMap<String, String>(){{
					put("name", title.getText());
					put("price", price.getText());
				}});
				setView("productsView");
			}*/
		}else if(cmd.equals("dropProduct")){/*
			if(cmd.equals("Cancel")){
				setView("productsView");
			}else if(){
				DB.query("products", DB.drop, Integer.parseInt(id.getText()));
				setView("productsView");
			}*/
		}else if(cmd.equals("BACK")){
			setView("adminView");
		}
	}

	/////////////////////////////////////////////////////
	//             TABLE CHANGE LISTENER               //
	/////////////////////////////////////////////////////
	@Override
	public void tableChanged(TableModelEvent e) {
		debug.notify("updated");
	}

	/////////////////////////////////////////////////////
	//                     MAIN                        //
	/////////////////////////////////////////////////////
	public static void main(String[] args) {
		debug.notify("APP_STARTED");
		new Main();
	}

}
