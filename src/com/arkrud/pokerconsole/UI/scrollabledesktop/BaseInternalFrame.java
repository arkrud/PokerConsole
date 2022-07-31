package com.arkrud.pokerconsole.UI.scrollabledesktop;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;

// TODO: Auto-generated Javadoc
/**
 * This class provides a custom internal frame. Each internal frame
 * is assigned an associated toggle button and an optional radio button
 * menu item. These buttons reside in the
 * {@link com.tomtessier.scrollabledesktop.DesktopResizableToolBar
 * DesktopResizableToolBar} and
 * {@link com.tomtessier.scrollabledesktop.DesktopMenu DesktopMenu}.
 * classes respectively.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @author <a href="mailto:francesco.furfari@guest.cnuce.cnr.it">Francesco Furfari</a>
 * @version 1.0  9-Aug-2001
 */

public class BaseInternalFrame extends JInternalFrame {

      /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The associated button. */
	private JToggleButton associatedButton;
      
      /** The associated menu button. */
      private JRadioButtonMenuItem associatedMenuButton;

      /** The is closable. */
      private boolean isClosable;
      
      /** The initial width. */
      private int initialWidth;
      
      /** The initial height. */
      private int initialHeight;

     /**
      *  creates the BaseInternalFrame.
      *
      * @param title the string displayed in the title bar of the internal frame
      * @param icon the ImageIcon displayed in the title bar of the internal frame
      * @param frameContents the contents of the internal frame
      * @param isClosable determines whether the frame is closable
      */
      public BaseInternalFrame(String title,
                              ImageIcon icon, JPanel frameContents,
                              boolean isClosable) {

            super(title, // title
                  true, //resizable
                  isClosable, //closable
                  true, //maximizable
                  true);//iconifiable

            this.isClosable = isClosable;

            setBackground(Color.white);
            setForeground(Color.blue);

            if (icon != null) {
                  setFrameIcon(icon);
            }

            // add the window contents
            getContentPane().add(frameContents);
            pack();

            saveSize();

            setVisible(true); // turn the frame on
      }

      /**
       * Save size.
       */
      private void saveSize() {
            initialWidth = getWidth();
            initialHeight = getHeight();
      }

      /**
       * constructor provided for compatibility with JInternalFrame.
       */
      public BaseInternalFrame() {
            super();
            saveSize();
      }
      
      /**
       * constructor provided for compatibility with JInternalFrame.
       *
       * @param title the title
       */
      public BaseInternalFrame(String title) {
            super(title);
            saveSize();
      }
      
      /**
       * constructor provided for compatibility with JInternalFrame.
       *
       * @param title the title
       * @param resizable the resizable
       */
      public BaseInternalFrame(String title, boolean resizable) {
            super(title, resizable);
            saveSize();
      }
      
      /**
       * constructor provided for compatibility with JInternalFrame.
       *
       * @param title the title
       * @param resizable the resizable
       * @param closable the closable
       */
      public BaseInternalFrame(String title, boolean resizable, boolean closable) {
            super(title, resizable, closable);
            //this.isClosable = isClosable;
            saveSize();
      }
      
      /**
       * constructor provided for compatibility with JInternalFrame.
       *
       * @param title the title
       * @param resizable the resizable
       * @param closable the closable
       * @param maximizable the maximizable
       */
      public BaseInternalFrame(String title, boolean resizable, boolean closable,
                              boolean maximizable) {
            super(title, resizable, closable, maximizable);
            //this.isClosable = isClosable;
            saveSize();
      }
      
      /**
       * constructor provided for compatibility with JInternalFrame.
       *
       * @param title the title
       * @param resizable the resizable
       * @param closable the closable
       * @param maximizable the maximizable
       * @param iconifiable the iconifiable
       */
      public BaseInternalFrame(String title, boolean resizable, boolean closable,
                              boolean maximizable, boolean iconifiable) {
            super(title, resizable, closable, maximizable, iconifiable);
            //this.isClosable = isClosable;
            saveSize();
      }


     /**
      *  sets the associated menu button.
      *
      * @param associatedMenuButton the menu button to associate with
      * the internal frame
      */
      public void setAssociatedMenuButton(JRadioButtonMenuItem associatedMenuButton) {
            this.associatedMenuButton = associatedMenuButton;
      }
     
     /**
      *  returns the associated menu button.
      *
      * @return the JRadioButtonMenuItem object associated with this internal frame
      */
      public JRadioButtonMenuItem getAssociatedMenuButton() {
            return associatedMenuButton;
      }

     /**
      *  sets the associated toggle button.
      *
      * @param associatedButton the toggle button to associate with
      * the internal frame
      */
      public void setAssociatedButton(JToggleButton associatedButton) {
            this.associatedButton = associatedButton;
      }
     
     /**
      *  returns the associated toggle button.
      *
      * @return the JToggleButton object associated with this internal frame
      */
      public JToggleButton getAssociatedButton() {
            return associatedButton;
      }

     /**
       *  returns the initial dimensions of this internal frame. Necessary so that
       * internal frames can be restored to their default sizes when the cascade
       * frame positioning mode is chosen in
       * {@link com.tomtessier.scrollabledesktop.FramePositioning FramePositioning}.
       *
       * @return the Dimension object representing the initial dimensions of
       * this internal frame
       */
      public Dimension getInitialDimensions() {
            return new Dimension(initialWidth, initialHeight);
      }

     /**
       *  returns the toString() representation of this object. Useful for
       * debugging purposes.
       *
       * @return the toString() representation of this object
       */
      @Override
	public String toString() {
            return "BaseInternalFrame: " + getTitle();
      }


     /**
      *  selects the current frame, along with any toggle and menu
      * buttons that may be associated with it.
      */
      public void selectFrameAndAssociatedButtons() {

            // select associated toolbar button
            if (associatedButton != null) {
                  associatedButton.setSelected(true);
                  ((BaseToggleButton)associatedButton).
                        flagContentsChanged(false);
            }

            // select menu button
            if (associatedMenuButton != null) {
                  associatedMenuButton.setSelected(true);
            }

            try {
                  setSelected(true);
                  setIcon(false);  // select and de-iconify the frame
            } catch (java.beans.PropertyVetoException pve) {
                  System.out.println(pve.getMessage());
            }

            setVisible(true); // and make sure the frame is turned on

      }


     /**
       *  saves the size of the current internal frame for those frames whose
       * initial width and heights have not been set. Called when the internal
       * frame is added to the JDesktopPane.
       * <BR><BR>
       * Manually-built internal frames won't display properly without this.
       * <BR><BR>
       * Fix by <a href="mailto:francesco.furfari@guest.cnuce.cnr.it">Francesco Furfari</a>
       *
       */
      @Override
	public void addNotify() {
            super.addNotify();
            if (initialWidth == 0 && initialHeight == 0) {
                  saveSize();
            }
      }

}