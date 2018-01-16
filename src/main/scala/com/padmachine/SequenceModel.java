package org.neotouch.neopad.model;

import java.awt.Color;

public final class SequenceModel extends AbstractModel
{
	private static final int SQUARE_SIDE = 9;
	private Sequence[][][] buttonSequences = new Sequence[SQUARE_SIDE][SQUARE_SIDE][SQUARE_SIDE];

	public SequenceModel()
	{
		super();

		for (int page = 0; page < SQUARE_SIDE; page++) {
			for (int row = 0; row < SQUARE_SIDE; row++) {
				for (int col = 0; col < SQUARE_SIDE; col++) {
					buttonSequences[page][row][col] = new Sequence();
				}
			}
		}
	}

	public int addFrame(int row, int col, int page)
	{
		setChanged();
		notifyObservers();
		return buttonSequences[page][row][col].addFrame();
	}

	public int getSequenceLength(int row, int col, int page)
	{
		return buttonSequences[page][row][col].length();
	}

	public Color getColorAt(int row, int col, int page, int frame, int brow,
			int bcol)
	{
		return buttonSequences[page][row][col].getColorAt(frame, brow, bcol);
	}

	public void setColorAt(int row, int col, int page, int frame, int brow,
			int bcol, Color color)
	{
		buttonSequences[page][row][col].setColorAt(frame, brow, bcol, color);
		setChanged();
		notifyObservers();
	}
}
