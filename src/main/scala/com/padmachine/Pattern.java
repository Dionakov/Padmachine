package org.neotouch.neopad.launchpad;

import java.awt.Color;

public class Pattern
{
	private final int SQUARE_SIDE = 9;
	private Button[][] buttons = new Button[SQUARE_SIDE][SQUARE_SIDE];

	private class Button
	{
		public Color color = Color.WHITE;
		public boolean isOn = false;

		public Button(Color color, boolean isOn)
		{
			this.color = color;
			this.isOn = isOn;
		}

		public Button()
		{
			this(Color.WHITE, false);
		}
	}

	public Pattern()
	{
		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				buttons[row][col] = new Button();
			}
		}
	}

	public void setButtonColor(int row, int col, Color color)
	{
		buttons[row][col].color = color;
	}

	public void setButtonOn(int row, int col, boolean isOn)
	{
		buttons[row][col].isOn = isOn;
	}

	public Color getButtonColor(int row, int col)
	{
		return buttons[row][col].color;
	}

	public boolean isButtonOn(int row, int col)
	{
		return buttons[row][col].isOn;
	}
}
