package com.arkrud.pokerconsole.UI;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class UpperCaseDocument extends PlainDocument {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean upperCase = true;

	  public void setUpperCase(boolean flag) {
	    upperCase = flag;
	  }

	  public void insertString(int offset, String str, AttributeSet attSet)
	      throws BadLocationException {
	    if (upperCase)
	      str = str.toUpperCase();
	    super.insertString(offset, str, attSet);
	  }

	}
