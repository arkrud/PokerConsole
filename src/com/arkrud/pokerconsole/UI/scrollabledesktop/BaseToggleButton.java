package com.arkrud.pokerconsole.UI.scrollabledesktop;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JToggleButton;

// TODO: Auto-generated Javadoc
/**
 * This class creates a base toggle button. A
 * {@link com.tomtessier.scrollabledesktop.BaseInternalFrame BaseInternalFrame}
 * object is associated with every instance of this class.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  11-Aug-2001
 */

public class BaseToggleButton extends JToggleButton
            implements DesktopConstants, FrameAccessorInterface {

      /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The associated frame. */
	private BaseInternalFrame associatedFrame;
      
      /** The default color. */
      private Color defaultColor;


    /**
     * creates the BaseToggleButton.
     *
     * @param title the title of the button
     */
      public BaseToggleButton(String title) {

            super(title);

            setButtonFormat();
            setToolTipText(title);

            defaultColor = getForeground();

      }

      /**
       * Sets the button format.
       */
      private void setButtonFormat() {
            Font buttonFont = getFont();
            setFont(new Font(buttonFont.getFontName(),
                             buttonFont.getStyle(),
                             buttonFont.getSize()-1));
            setMargin(new Insets(0,0,0,0));
      }

     /**
      *  sets the associated frame.
      *
      * @param associatedFrame the BaseInternalFrame object to associate with
      * the menu item
      */
      @Override
	public void setAssociatedFrame(BaseInternalFrame associatedFrame) {
            this.associatedFrame = associatedFrame;
      }

     /**
      *  returns the associated frame.
      *
      * @return the BaseInternalFrame object associated with this menu item
      */
      @Override
	public BaseInternalFrame getAssociatedFrame() {
            return associatedFrame;
      }

     /**
       *  flags the contents as "changed" by setting the foreground color to
       * {@link
       * com.tomtessier.scrollabledesktop.DesktopConstants#CONTENTS_CHANGED_COLOR
       * CONTENTS_CHANGED_COLOR}.
       * Used to notify the user when the contents of an inactive internal frame
       * have changed.
       *
       * @param changed <code>boolean</code> indicating whether contents have
       * changed
       */
      public void flagContentsChanged(boolean changed) {
            if (changed) {
                  setForeground(CONTENTS_CHANGED_COLOR);
            }
            else {
                  setForeground(defaultColor);
            }
      }


}