package com.arkrud.pokerconsole.UI.scrollabledesktop;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;

import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * This class provides internal frame positioning methods for use by {@link com.tomtessier.scrollabledesktop.DesktopScrollPane DesktopScrollPane}.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0 11-Aug-2001
 */
public class FramePositioning implements DesktopConstants {
	
	/** The desktop scrollpane. */
	private DesktopScrollPane desktopScrollpane;
	
	/** The auto tile. */
	private boolean autoTile; // determines whether to cascade or tile windows

	/**
	 * creates the FramePositioning object.
	 *
	 * @param desktopScrollpane a reference to the DesktopScrollpane object
	 */
	public FramePositioning(DesktopScrollPane desktopScrollpane) {
		this.desktopScrollpane = desktopScrollpane;
	}

	/**
	 * turns autoTile on or off.
	 *
	 * @param autoTile <code>boolean</code> representing autoTile mode. If <code>true</code>, then all new frames are tiled automatically. If <code>false</code>, then all new frames are cascaded
	 *            automatically.
	 */
	public void setAutoTile(boolean autoTile) {
		this.autoTile = autoTile;
		if (autoTile) {
			tileInternalFrames();
		} else {
			cascadeInternalFrames();
		}
	}

	/**
	 * returns the autoTile mode.
	 *
	 * @return <code>boolean</code> representing current autoTile mode
	 */
	public boolean getAutoTile() {
		return autoTile;
	}

	/**
	 * cycles through and cascades all internal frames.
	 */
	public void cascadeInternalFrames() {
		JInternalFrame[] frames = desktopScrollpane.getAllFrames();
		BaseInternalFrame f;
		int frameCounter = 0;
		for (int i = frames.length - 1; i >= 0; i--) {
			f = (BaseInternalFrame) frames[i];
			// don't include iconified frames in the cascade
			if (!f.isIcon()) {
				f.setSize(f.getInitialDimensions());
				f.setLocation(cascadeInternalFrame(f, frameCounter++));
			}
		}
	}

	/**
	 * cascades the given internal frame based upon the current number of internal frames.
	 *
	 * @param f the internal frame to cascade
	 * @return a Point object representing the location assigned to the internal frame upon the virtual desktop
	 */
	public Point cascadeInternalFrame(JInternalFrame f) {
		return cascadeInternalFrame(f, desktopScrollpane.getNumberOfFrames());
	}

	/**
	 * cascades the given internal frame based upon supplied count.
	 *
	 * @param f the internal frame to cascade
	 * @param count the count
	 * @return a Point object representing the location assigned to the internal frame upon the virtual desktop
	 * @count the count to use in cascading the internal frame
	 */
	private Point cascadeInternalFrame(JInternalFrame f, int count) {
		int windowWidth = f.getWidth();
		int windowHeight = f.getHeight();
		Rectangle viewP = desktopScrollpane.getViewport().getViewRect();
		// get # of windows that fit horizontally
		int numFramesWide = (viewP.width - windowWidth) / X_OFFSET;
		if (numFramesWide < 1) {
			numFramesWide = 1;
		}
		// get # of windows that fit vertically
		int numFramesHigh = (viewP.height - windowHeight) / Y_OFFSET;
		if (numFramesHigh < 1) {
			numFramesHigh = 1;
		}
		// position relative to the current viewport (viewP.x/viewP.y)
		// (so new windows appear onscreen)
		int xLoc = viewP.x + X_OFFSET * ((count + 1) - (numFramesWide - 1) * (count / numFramesWide));
		int yLoc = viewP.y + Y_OFFSET * ((count + 1) - numFramesHigh * (count / numFramesHigh));
		return new Point(xLoc, yLoc);
	}

	/**
	 * tiles internal frames upon the desktop. <BR>
	 * <BR>
	 * Based upon the following tiling algorithm: <BR>
	 * <BR>
	 * - take the sqroot of the total frames rounded down, that gives the number of columns. <BR>
	 * <BR>
	 * - divide the total frames by the # of columns to get the # of rows in each column, and any remainder is distributed amongst the remaining rows from right to left) <BR>
	 * <BR>
	 * eg) <BR>
	 * 1 frame, remainder 0, 1 row<BR>
	 * 2 frames, remainder 0, 2 rows<BR>
	 * 3 frames, remainder 0, 3 rows<BR>
	 * 4 frames, remainder 0, 2 rows x 2 columns <BR>
	 * 5 frames, remainder 1, 2 rows in column I, 3 rows in column II<BR>
	 * 10 frames, remainder 1, 3 rows in column I, 3 rows in column II, 4 rows in column III <BR>
	 * 16 frames, 4 rows x 4 columns <BR>
	 * <BR>
	 * <BR>
	 * Pseudocode: <BR>
	 * <BR>
	 * <code>
	 *     while (frames) { <BR>
	 *           numCols  = (int)sqrt(totalFrames); <BR>
	 *           numRows = totalFrames / numCols; <BR>
	 *           remainder = totalFrames % numCols <BR>
	 *           if ((numCols-curCol) <= remainder) { <BR>
	 *                   numRows++; // add an extra row for this column <BR>
	 *           } <BR>
	 *     }  </code><BR>
	 */
	public void tileInternalFrames() {
		// Rectangle viewP = desktopScrollpane.getViewport().getViewRect();
		int totalNonIconFrames = 0;
		JInternalFrame[] rawFrames = desktopScrollpane.getAllFrames();
		HashMap<Integer, JInternalFrame> frameMap = new HashMap<Integer, JInternalFrame>();
		int n = 0;
		while (n < rawFrames.length) {
			String fileSystemPath = UtilMethodsFactory.getConfigPath().substring(1, UtilMethodsFactory.getConfigPath().length()) + "Images/" + rawFrames[n].getName();
			// Replace backslash with forward slash when it show up while opening read only console in the part of the path
			fileSystemPath = fileSystemPath.replace("\\", "/");
			String fileName = fileSystemPath.split("/")[fileSystemPath.split("/").length - 1].split("\\.")[0];
			int theIndesOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(fileName);
			int sequenceNumber = Integer.valueOf(fileName.substring(0, theIndesOfFirstLiteral));
			frameMap.put(sequenceNumber - 1, rawFrames[n]);
			n++;
		}
		List<JInternalFrame> frameslist = new ArrayList<JInternalFrame>();
		for (Map.Entry<Integer, JInternalFrame> entry : frameMap.entrySet()) {
			JInternalFrame frame = entry.getValue();
			frameslist.add(frame);
			if (!frame.isIcon()) { // don't include iconified frames...
				totalNonIconFrames++;
			}
		}
		JInternalFrame[] frames = frameslist.stream().toArray(JInternalFrame[]::new);
		//Collections.reverse(Arrays.asList(frames));
		int curCol = 0;
		int curRow = 0;
		int i = 0;
		if (totalNonIconFrames > 0) {
			// compute number of columns and rows then tile the frames
			int numRows = (int) Math.sqrt(totalNonIconFrames);
			if (numRows > 2) {
				numRows = 2;
			}
			// int frameHeight = viewP.height / numRows;
			for (curRow = 0; curRow < numRows; curRow++) {
				int numCols = totalNonIconFrames / numRows;
				int remainder = totalNonIconFrames % numRows;
				if ((numRows - curRow) <= remainder) {
					numCols++; // add an extra row for this guy
				}
				// int frameWidth = viewP.width / numCols;
				for (curCol = 0; curCol < numCols; curCol++) {
					while (frames[i].isIcon()) { // find the next visible frame
						i++;
					}
					frames[i].setBounds(curCol * 430, curRow * 440, 430, 440);
					i++;
				}
			}
		}
	}


}