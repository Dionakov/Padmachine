package org.neotouch.neopad.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Sequence
{
	private List<Frame> frames = new ArrayList<>();

	public int addFrame()
	{
		frames.add(new Frame());
		return frames.size() - 1;
	}

	public int length()
	{
		return frames.size();
	}

	public Color getColorAt(int frame, int brow, int bcol)
	{
		return frames.get(frame).getColorAt(brow, bcol);
	}

	public void setColorAt(int frame, int brow, int bcol, Color color)
	{
		frames.get(frame).setColorAt(brow, bcol, color);
	}
}
