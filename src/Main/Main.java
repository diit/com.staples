package Main;

import helper.DB;
import helper.customFont;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

	// Global Panel
	CardLayout viewHandler = new CardLayout();
	JPanel _global = new JPanel(viewHandler);


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
		setLayout("loginView");
		frame.setVisible(true);
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
		_global.add(tableView(frame), "tableView");

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

		JPanel panel = new JPanel();
		JLabel test = new JLabel("QUERY TEST POC");
		JButton next = new JButton("QUERY");
		next.setActionCommand("next");
		next.addActionListener(this);

		panel.add(test);
		panel.add(next);
		return panel;
	}

	public JPanel frontView(fFrame frame){
		// Size Frame
		frame.setSize(800, 400);

		JPanel panel = new JPanel();
		JLabel test = new JLabel("QUERY TEST POC:");
		queryResponse.setForeground(Color.BLUE);
		queryResponse.setFont(code);
		JButton next = new JButton("table");
		next.addActionListener(this);

		panel.add(test);
		panel.add(next);
		panel.add(output);
		return panel;
	}

	public JPanel tableView(fFrame frame){
		JPanel panel = new JPanel();
		//TODO: Create Abstraction method for loading data (possibly integrate with that of <169:17>)
		
		// Create columns names
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
		tbl=new JTable(DV,columnNames);

		//Add table event listener
		tbl.getModel().addTableModelListener(this);

		panel.add(tbl);
		return panel;
	}

	/////////////////////////////////////////////////////
	//                LAYOUT SWITCHER                  //
	/////////////////////////////////////////////////////
	public void setLayout(String view){
		viewHandler.show(_global,view);
		debug.notify("view -> "+view);
	}

	/////////////////////////////////////////////////////
	//             SWING ACTION LISTENER               //
	/////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Route Controller Actions to correct methods
		String cmd = arg0.getActionCommand();
		debug.notify(cmd);

		if(cmd.equals("next")){
			//RUN QUERY AND CREATE LOCAL VARIABLE

			//TEST:1 (insert) id=>0	

			DB.query("products", DB.insert, 7 ,new LinkedHashMap<String, String>(){{
				put("name", "scooter");
				put("price", "9.00");
			}});

			DB.query("products", DB.insert, 8 ,new LinkedHashMap<String, String>(){{
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


			setLayout("frontView");
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

			setLayout("tableView");
		}

	}
	
	/////////////////////////////////////////////////////
	//             TABLE CHANGE LISTENER               //
	/////////////////////////////////////////////////////
	@Override
	public void tableChanged(TableModelEvent e) {
		//debug.notify("at row: "+e.getFirstRow()+", Column:"+ e.getColumn()+", UPDATE:"+DV[e.getFirstRow()][e.getColumn()]);
		debug.notify("at row: "+e.getFirstRow()+", Column:"+ e.getColumn());

		//TODO: Create query or abstraction method to update data	
	}

	/////////////////////////////////////////////////////
	//                     MAIN                        //
	/////////////////////////////////////////////////////
	public static void main(String[] args) {
		debug.notify("APP_STARTED");
		new Main();
	}

}
