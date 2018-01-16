package org.neotouch.neopad.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.neotouch.neopad.model.AbstractModel;
import org.neotouch.neopad.model.GuiLaunchpadModel;
import org.neotouch.neopad.model.SequenceModel;

import javafx.geometry.Point2D;

// TODO
public final class SequencePanel extends JPanel implements ViewController
{
	JScrollPane pane = new JScrollPane();
	JPanel innerPane = new JPanel();
	JPanel controlBar = new JPanel();
	SequenceModel sequenceModel = null;
	GuiLaunchpadModel launchpadModel = null;

	public SequencePanel()
	{
		pane.setViewportView(innerPane);
		innerPane.setLayout(new FlowLayout());
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		add(controlBar, BorderLayout.NORTH);

		controlBar.setLayout(new FlowLayout());
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (arg0 instanceof GuiLaunchpadModel) {
			refresh();
		}
	}

	private void refresh()
	{
		Point2D selected = launchpadModel.getSelectedButton();
		if (selected == null) {
			return;
		}

		innerPane.removeAll();

		for (int i = 0; i < sequenceModel.getSequenceLength(
				(int) selected.getX(), (int) selected.getY(), 0); i++) {
			innerPane.add(new JButton("" + i));
		}

		repaint();
		revalidate();
	}

	@Override
	public void addModel(AbstractModel model)
	{
		if (model instanceof SequenceModel) {
			this.sequenceModel = (SequenceModel) model;
			model.addObserver(this);
		} else if (model instanceof GuiLaunchpadModel) {
			this.launchpadModel = (GuiLaunchpadModel) model;
			model.addObserver(this);
		}
	}
}
