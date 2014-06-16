package ui;

import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

public class ProductBtnUI extends BasicButtonUI {
	/*
	 * Partial Implementation
	 * 
	 * ISSUES:
	 * - Not drawing text
	 * - Unable to call custom functions
	 */
	String price = "noPriceSet",title = "noTitleSet";
	
    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        
        //Draw Title and Price
        g.drawString(title, 0, 0);
        
    }
    
    public void setTitle(String title){
    	this.title = title;
    }
    
    public void setPrice(String price){
    	this.price = price;
    	
    }
}
