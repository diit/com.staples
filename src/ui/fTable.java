package ui;

import javax.swing.JTable;

public class fTable extends JTable{
	public fTable(String[][] dV, String[] columnNames) {
		super(dV,columnNames);
	}

	public fTable() {
		super();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
	   return (column != 0);
	}
}
