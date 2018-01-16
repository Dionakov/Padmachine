package org.neotouch.neopad.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Observable;

import javax.swing.JPanel;

import org.neotouch.neopad.model.AbstractModel;
import org.neotouch.neopad.model.GuiLaunchpadModel;
import org.neotouch.neopad.model.LaunchpadDeviceModel;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

@SuppressWarnings("serial")
public final class LaunchpadPanel extends JPanel implements ViewController
{
	GuiLaunchpadModel buttonGrid = null;
	LaunchpadControlBar controlBar = new LaunchpadControlBar();

	public LaunchpadPanel()
	{
		super();

		controlBar.setLayout(new FlowLayout());

		setLayout(new BorderLayout());
		add(controlBar, BorderLayout.NORTH);
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(300, 300));

		addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				onMousePressedOrDragged(e);
			}

		});

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				onMousePressedOrDragged(e);
			}
		});
	}

	private void onMousePressedOrDragged(MouseEvent e)
	{
		Point2D buttonPos = buttonGrid
				.coordsToButtonPos(new Point2D(e.getX(), e.getY()));

		if (buttonPos != null) {
			int row = (int) buttonPos.getX();
			int col = (int) buttonPos.getY();

			buttonGrid.setButtonColor(row, col, controlBar.getCurrentColor());
		}

		buttonGrid.setSelectedButton(buttonPos);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		buttonGrid.rebuild(new Dimension2D(getWidth(), getHeight()),
				new Point2D(0, controlBar.getHeight()));
		buttonGrid.draw(g2);
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if (!(o instanceof GuiLaunchpadModel)) {
			return;
		}

		repaint();
		revalidate();
	}

	@Override
	public void addModel(AbstractModel model)
	{
		if (model instanceof GuiLaunchpadModel) {
			buttonGrid = (GuiLaunchpadModel) model;
			model.addObserver(this);

			controlBar.addModel(model);
		} else if (model instanceof LaunchpadDeviceModel) {
			controlBar.addModel(model);
		}
	}
}
