package org.neotouch.neopad.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Random;

import org.neotouch.neopad.launchpad.Pattern;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class GuiLaunchpadModel extends AbstractModel
{
	private final int SQUARE_SIDE = 9;
	private final Point2D EMPTY_BUTTON_POS = new Point2D(0, 8);
	private Shape[][] buttonShapes = new Shape[SQUARE_SIDE][SQUARE_SIDE];
	private Color[][] buttonColors = new Color[SQUARE_SIDE][SQUARE_SIDE];
	private Point2D selectedButton = null;
	private ButtonShapeBuilder shapeBuilder;

	public GuiLaunchpadModel()
	{
		this(new Dimension2D(800, 600), new Point2D(0, 0));
	}

	public GuiLaunchpadModel(Dimension2D containerSize, Point2D canvasOrigin)
	{
		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				buttonColors[row][col] = Color.BLACK;
			}
		}
		rebuild(containerSize, canvasOrigin);
	}

	public void rebuild(Dimension2D containerSize, Point2D canvasOrigin)
	{
		shapeBuilder = new ButtonShapeBuilder(containerSize, canvasOrigin);

		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {

				if (row == EMPTY_BUTTON_POS.getX()
						&& col == EMPTY_BUTTON_POS.getY()) {
					continue;
				}

				Button type = null;
				if (row == 0 || col == 8) {
					type = Button.CONTROL;
				} else {
					type = Button.NORMAL;
				}

				buttonShapes[row][col] = shapeBuilder.build(type, row, col);
			}
		}
	}

	public Point2D coordsToButtonPos(Point2D coords)
	{
		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				if (row == EMPTY_BUTTON_POS.getX()
						&& col == EMPTY_BUTTON_POS.getY()) {
					continue;
				}
				if (buttonShapes[row][col].contains(coords.getX(),
						coords.getY())) {
					return new Point2D(row, col);
				}
			}
		}

		return null;
	}

	public void importPattern(Pattern p)
	{
		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				if (p.isButtonOn(row, col)) {
					buttonColors[row][col] = p.getButtonColor(row, col);
				} else {
					buttonColors[row][col] = Color.WHITE;
				}
			}
		}

		setChanged();
		notifyObservers();
	}

	public Pattern exportPattern()
	{
		Pattern p = new Pattern();
		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				if (buttonColors[row][col] != Color.BLACK) {
					p.setButtonColor(row, col, buttonColors[row][col]);
					p.setButtonOn(row, col, true);
				}
			}
		}

		setChanged();
		notifyObservers();

		return p;
	}

	public void draw(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(3));

		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {

				if (row == EMPTY_BUTTON_POS.getX()
						&& col == EMPTY_BUTTON_POS.getY()) {
					continue;
				}

				g2.setColor(buttonColors[row][col]);
				g2.fill(buttonShapes[row][col]);

				if (new Point2D(row, col).equals(selectedButton)) {
					g2.setColor(Color.RED);
				} else {
					g2.setColor(Color.BLACK);
				}

				g2.draw(buttonShapes[row][col]);
			}
		}
	}

	public void setButtonColor(int row, int col, Color color)
	{
		buttonColors[row][col] = color;
		setChanged();
		notifyObservers();
	}

	public Color getButtonColor(int row, int col)
	{
		return buttonColors[row][col];
	}

	public void setRandomColors()
	{
		Random rand = new Random();
		for (Color[] inner : buttonColors) {
			for (Color c : inner) {
				c = new Color(rand.nextFloat(), rand.nextFloat(),
						rand.nextFloat());
			}
		}

		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				setButtonColor(row, col, new Color(rand.nextFloat(),
						rand.nextFloat(), rand.nextFloat()));
			}
		}
	}

	public Point2D getSelectedButton()
	{
		return selectedButton;
	}

	public void setSelectedButton(Point2D selectedButton)
	{
		this.selectedButton = selectedButton;
		setChanged();
		notifyObservers();
	}
}
