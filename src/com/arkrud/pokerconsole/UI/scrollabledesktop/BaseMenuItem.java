package com.arkrud.pokerconsole.UI.scrollabledesktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

// TODO: Auto-generated Javadoc
/**
 * This class creates a generic base menu item. ActionListener, mnemonic,
 * keyboard shortcut, and title are set via the constructor.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  29-Jan-2001
 */

public class BaseMenuItem extends JMenuItem  {


    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * creates the BaseMenuItem.
	 *
	 * @param listener the action listener to assign
	 * @param itemTitle the title of the item
	 * @param mnemonic the mnemonic used to access the menu
	 * @param shortcut the keyboard shortcut used to access the menu.
	 *      -1 indicates no shortcut.
	 */
      public BaseMenuItem(ActionListener listener,
                             String itemTitle,
                             int mnemonic,
                             int shortcut) {

            super(itemTitle, mnemonic);


            // set the alt-Shortcut accelerator
            if (shortcut != -1) {
                  setAccelerator(
                        KeyStroke.getKeyStroke(
                                shortcut, ActionEvent.ALT_MASK));
            }

            setActionCommand(itemTitle);
            addActionListener(listener);

      }

}