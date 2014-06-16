package Main;

import helper.DB;
import helper.customFont;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import ui.ProductBtnUI;
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
	JTable table;

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
		String[] columnNames = {"ID","name","Password"};
		Object[][] data = {};
		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 380));
		table.setFillsViewportHeight(true);
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
		String[] columnNames = {"First Name",
				"Last Name",
				"Age",
				"Adress",
		"Operation"};
		Object[][] data = {
				{"Kathy", "Smith",
					"Snowboarding", new Integer(5), new Boolean(false)},
					{"John", "Doe",
						"Rowing", new Integer(3), new Boolean(true)},
						{"Sue", "Black",
							"Knitting", new Integer(2), new Boolean(false)},
							{"Jane", "White",
								"Speed reading", new Integer(20), new Boolean(true)},
								{"Joe", "Brown",
									"Pool", new Integer(10), new Boolean(false)}
		};
		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 380));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		topPanel.add(lblProducts);
		midPanel.add(scrollPane);
		botPanel.add(btnBack);
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
		String[] columnNames = {"First Name",
				"Last Name",
				"Age",
				"Adress",
		"Operation"};
		Object[][] data = {
				{"Kathy", "Smith",
					"Snowboarding", new Integer(5), new Boolean(false)},
					{"John", "Doe",
						"Rowing", new Integer(3), new Boolean(true)},
						{"Sue", "Black",
							"Knitting", new Integer(2), new Boolean(false)},
							{"Jane", "White",
								"Speed reading", new Integer(20), new Boolean(true)},
								{"Joe", "Brown",
									"Pool", new Integer(10), new Boolean(false)}
		};
		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 380));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		topPanel.add(lblOrders);
		midPanel.add(scrollPane);
		botPanel.add(btnBack);
		panel.add(topPanel,BorderLayout.PAGE_START);
		panel.add(midPanel,BorderLayout.CENTER);
		panel.add(botPanel,BorderLayout.PAGE_END);
		return panel;
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
		// Create columns names
		String columnNames[] = { "ID", "Name", "Password" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("products", DB.find);
		String DV[][] = new String[tmpData.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DB.query("users", DB.find).entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get("name");
			DV[i][2]=tmpData.get(i).get("password");
			i++;
		}
		table=new JTable(DV,columnNames);
		//((Object) tbl).fireTableDataChanged();
		table.repaint(); //Required to update data
		table.revalidate();
		debug.notify("TABEL LOAD COMPLETE");
	}
	public void loadProducts(){
		// Create columns names
		String columnNames[] = { "ID", "Name", "Price" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("products", DB.find);
		String DV[][] = new String[tmpData.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DB.query("products", DB.find).entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get("name");
			DV[i][2]=tmpData.get(i).get("price");
			i++;
		}
		table=new JTable(DV,columnNames);
		//((Object) tbl).fireTableDataChanged();
		table.repaint(); //Required to update data

	}
	public void loadSales(){
		// Create columns names
		String columnNames[] = { "ID", "UID", "PID" };

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("products", DB.find);
		String DV[][] = new String[tmpData.entrySet().toArray().length][3];
		int i=0;

		// Add data to table-data
		for(Object c: DB.query("sales", DB.find).entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get("UID");
			DV[i][2]=tmpData.get(i).get("PID");
			i++;
		}
		table=new JTable(DV,columnNames);
		//((Object) tbl).fireTableDataChanged();
		table.repaint(); //Required to update data

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
		}else if(cmd.equals("BACK")){
			setView("adminView");
		}

		if(cmd.equals("next")){
			//PRELIM TEST SUITE

			//TEST:1 (insert) id=>0	

			DB.query("products", DB.insert, 1 ,new LinkedHashMap<String, String>(){{
				put("name", "scooter");
				put("price", "9.00");
			}});

			DB.query("products", DB.insert, 2 ,new LinkedHashMap<String, String>(){{
				put("name", "hat");
				put("price", "58.00");
			}});

			//TEST:1 (findFirst) id=>0	
			LinkedHashMap p1 = DB.query("products", DB.findFirst, 0);
			LinkedHashMap p2 = DB.query("products", DB.findFirst, 1);
			LinkedHashMap p3 = DB.query("products", DB.findFirst, 3);

			//TEST:2 (drop) id=>1
			DB.query("products", DB.drop, 1);

			//TEST:3 (find)
			queryResponse.append("============= PRODUCTS =============\n");
			for(Object c: DB.query("products", DB.find).entrySet().toArray()){
				if(!c.toString().contains("e=}")){
					queryResponse.append(
							c.toString()+"\n"
							);
				}
			}

			queryResponse.append("=============== USERS ===============\n");
			for(Object c: DB.query("users", DB.find).entrySet().toArray()){
				if(!c.toString().contains("e=}")){
					queryResponse.append(
							c.toString()+"\n"
							);
				}
			}

			queryResponse.append("=============== SALES ===============\n");
			HashMap<Integer, LinkedHashMap<String, String>> sales = DB.query("sales", DB.find);
			int count=0;
			for(Object c: sales.entrySet().toArray()){
				if(!c.toString().contains("e=}")){
					queryResponse.append(
							c.toString()+"\n"+
									"PRODUCT: "+DB.query("products", DB.findFirst, Integer.parseInt(sales.get(count).get("pid"))).get("name")+"\n"+
									"USER: "+DB.query("users", DB.findFirst, Integer.parseInt(sales.get(count).get("uid"))).get("name")+"\n"
							);
				}
				count++;
			}


			setView("frontView");
		}else if(cmd.equals("table")){
			// TEST ENV:3
			// Create columns names
			String columnNames[] = { "ID", "Name", "Price" };

			// Load some data
			HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query("products", DB.find);
			String DV[][] = new String[tmpData.entrySet().toArray().length][3];
			int i=0;

			// Add data to table-data
			for(Object c: DB.query("products", DB.find).entrySet().toArray()){
				DV[i][0]=Integer.toString(i);
				DV[i][1]=tmpData.get(i).get("name");
				DV[i][2]=tmpData.get(i).get("price");
				i++;
			}
			tbl=new JTable(DV,columnNames);
			//((Object) tbl).fireTableDataChanged();
			tbl.repaint(); //Required to update data

			setView("tableView");
		}

	}

	/////////////////////////////////////////////////////
	//             TABLE CHANGE LISTENER               //
	/////////////////////////////////////////////////////
	@Override
	public void tableChanged(TableModelEvent e) {
		//debug.notify("at row: "+e.getFirstRow()+", Column:"+ e.getColumn()+", UPDATE:"+DV[e.getFirstRow()][e.getColumn()]);
		debug.notify("at row: "+e.getFirstRow()+", Column:"+ e.getColumn());

		DB.query("products", DB.drop, e.getFirstRow()); //TEST CODE
		tbl=drawTbl("products");
		//TODO: Create query or abstraction method to update data

		tbl.repaint(); //Reload data
	}

	public JTable drawTbl(String model){ //TODO: Move to DB helper class under DB.getTable(DB.QUERY());
		// Create columns names
		String columnNames[] = { "ID", "name", "price" }; //TODO: Integrate with column name index in db (DB.getColumns(model);

		// Load some data
		HashMap<Integer, LinkedHashMap<String, String>> tmpData = DB.query(model, DB.find);
		String DV[][] = new String[tmpData.entrySet().toArray().length][3]; //TODO: Create standard data model for active tables 
		int i=0;

		// Add data to table-data
		for(Object c: tmpData.entrySet().toArray()){
			DV[i][0]=Integer.toString(i);
			DV[i][1]=tmpData.get(i).get(columnNames[1]);
			DV[i][2]=tmpData.get(i).get(columnNames[2]);
			i++;
		}
		tbl=new JTable(DV,columnNames);
		tbl.repaint();
		return tbl;
	}

	/////////////////////////////////////////////////////
	//                     MAIN                        //
	/////////////////////////////////////////////////////
	public static void main(String[] args) {
		debug.notify("APP_STARTED");
		new Main();
	}

}
