package org.neotouch.neopad.model;

import java.awt.Color;

public class Frame
{
	private static final int SQUARE_SIDE = 9;
	private Color[][] buttonColors = new Color[SQUARE_SIDE][SQUARE_SIDE];

	public Frame()
	{
		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				buttonColors[row][col] = Color.BLACK;
			}
		}
	}

	public Color getColorAt(int row, int col)
	{
		return buttonColors[row][col];
	}

	public void setColorAt(int row, int col, Color color)
	{
		buttonColors[row][col] = color;
	}
}
