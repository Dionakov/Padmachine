package org.neotouch.neopad.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.neotouch.neopad.model.AbstractModel;
import org.neotouch.neopad.model.GuiLaunchpadModel;
import org.neotouch.neopad.model.LaunchpadDeviceModel;
import org.neotouch.neopad.resource.PatternFile;

@SuppressWarnings("serial")
public final class LaunchpadControlBar extends JPanel implements ViewController
{
	private JButton randomColorsButton = new JButton("Random colors");
	private JButton currentColorButton = new JButton("Color");
	private JButton savePatternButton = new JButton("Save pattern");
	private JButton launchpadPreviewButton = new JButton("Preview");
	private Color currentColor = Color.BLACK;
	private GuiLaunchpadModel buttonGrid = null;
	private LaunchpadDeviceModel device = null;

	public LaunchpadControlBar()
	{
		setLayout(new FlowLayout());
		add(randomColorsButton);

		currentColorButton.setBackground(currentColor);
		add(currentColorButton);
		add(savePatternButton);
		add(launchpadPreviewButton);
		randomColorsButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (buttonGrid != null) {
					buttonGrid.setRandomColors();
				}
			}

		});

		currentColorButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setCurrentColor(JColorChooser.showDialog(null, "Pick a color",
						currentColor));
			}

		});

		savePatternButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				File patternFile = new File("resources/patterns/"
						+ JOptionPane.showInputDialog(
								"Please enter file name :", "newPattern1")
						+ ".npat");
				try {
					new PatternFile(patternFile)
							.savePattern(buttonGrid.exportPattern());
				} catch (FileNotFoundException
						| UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

		launchpadPreviewButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						if (row == 0 && col == 8) {
							continue;
						}

						Color c = buttonGrid.getButtonColor(row, col);
						device.setButtonLedOn(row, col, !c.equals(Color.BLACK));

						device.setButtonColor(row, col, c);
					}
				}
			}

		});
	}

	public void setCurrentColor(Color c)
	{
		this.currentColor = c;
		this.currentColorButton.setBackground(currentColor);
	}

	public Color getCurrentColor()
	{
		return this.currentColor;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		// nothing to do.
	}

	@Override
	public void addModel(AbstractModel model)
	{
		if (model instanceof GuiLaunchpadModel) {
			buttonGrid = (GuiLaunchpadModel) model;
			buttonGrid.addObserver(this);
		} else if (model instanceof LaunchpadDeviceModel) {
			device = (LaunchpadDeviceModel) model;
			device.addObserver(this);
		}
	}
}
