package org.neotouch.neopad.model;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.neotouch.neopad.Builder;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class ButtonShapeBuilder implements Builder<Shape>
{
	private static final Point2D BUTTONS_OFFSET = new Point2D(30d, 30d);
	private static final Dimension2D NORMAL_BUTTON_SIZE = new Dimension2D(60d,
			60d);
	private static final Dimension2D BUTTON_MARGIN = new Dimension2D(15d, 15d);
	private static final double NORMAL_BUTTON_CURVE_RATIO = 0.10d;
	private static final int NORMAL_BUTTON_COUNT = 64;
	private static final double CONTROL_BUTTON_DIAMETER_RATIO = 0.77d;
	private static final double CONTROL_BUTTON_BORDER_SIZE = 10d;

	private static final double CENTER_BUTTON_CUT_LENGTH = 15d / 20d;
	private final double CONTROL_BUTTON_DIAMETER = NORMAL_BUTTON_SIZE.getWidth()
			* CONTROL_BUTTON_DIAMETER_RATIO;
	private final double SQUARE_SIDE = Math.sqrt(NORMAL_BUTTON_COUNT);

	private final Dimension2D containerSize;
	private final Point2D canvasOrigin;

	public ButtonShapeBuilder()
	{
		this(new Dimension2D(800, 600), new Point2D(0, 0));
	}

	public ButtonShapeBuilder(Dimension2D containerSize, Point2D canvasOrigin)
	{
		this.containerSize = containerSize;
		this.canvasOrigin = canvasOrigin;
	}

	@Override
	public Shape build()
	{
		return build(Button.NORMAL, 0, 0);
	}

	public Shape build(Button type, int row, int col)
	{
		Shape button = null;
		switch (type) {
			case NORMAL:
				button = buildNormal(row, col);
				break;
			case CENTER:
				button = buildCenter(row, col);
				break;
			case CONTROL:
				button = buildControl(row, col);
				break;
			default:
				return null;
		}

		// scale and center
		button = scaleButton(button);
		button = centerButton(button);
		return button;
	}

	private Shape buildNormal(int row, int col)
	{

		if ((row == 4 || row == 5) && (col == 3 || col == 4)) {
			return buildCenter(row, col);
		}

		Point2D buttonPos = new Point2D(BUTTONS_OFFSET.getX() + (col
				* (NORMAL_BUTTON_SIZE.getWidth() + BUTTON_MARGIN.getWidth())),
				BUTTONS_OFFSET.getY() + CONTROL_BUTTON_DIAMETER
						+ CONTROL_BUTTON_BORDER_SIZE
						+ ((row - 1) * (NORMAL_BUTTON_SIZE.getHeight()
								+ BUTTON_MARGIN.getHeight())));

		double curve = NORMAL_BUTTON_CURVE_RATIO
				* NORMAL_BUTTON_SIZE.getWidth();

		RoundRectangle2D.Double button = new RoundRectangle2D.Double(
				buttonPos.getX(), buttonPos.getY(),
				NORMAL_BUTTON_SIZE.getWidth(), NORMAL_BUTTON_SIZE.getHeight(),
				curve, curve);

		return button;
	}

	private Shape buildCenter(int row, int col)
	{

		Point2D shapeOrigin = new Point2D(BUTTONS_OFFSET.getX() + (col
				* (NORMAL_BUTTON_SIZE.getWidth() + BUTTON_MARGIN.getWidth())),
				BUTTONS_OFFSET.getY() + CONTROL_BUTTON_DIAMETER
						+ CONTROL_BUTTON_BORDER_SIZE
						+ ((row - 1) * (NORMAL_BUTTON_SIZE.getHeight()
								+ BUTTON_MARGIN.getHeight())));

		Polygon p = new Polygon();
		List<Point2D> points = new ArrayList<>();

		if (row == 4 && col == 3) {
			points.add(new Point2D(0d, 0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(), 0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(),
					NORMAL_BUTTON_SIZE.getHeight() * CENTER_BUTTON_CUT_LENGTH));
			points.add(new Point2D(
					NORMAL_BUTTON_SIZE.getWidth() * CENTER_BUTTON_CUT_LENGTH,
					NORMAL_BUTTON_SIZE.getHeight()));
			points.add(new Point2D(0d, NORMAL_BUTTON_SIZE.getHeight()));

		} else if (row == 4 && col == 4) {
			points.add(new Point2D(0d, 0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(), 0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(),
					NORMAL_BUTTON_SIZE.getHeight()));
			points.add(new Point2D(
					NORMAL_BUTTON_SIZE.getWidth()
							* (1d - CENTER_BUTTON_CUT_LENGTH),
					NORMAL_BUTTON_SIZE.getHeight()));
			points.add(new Point2D(0d,
					NORMAL_BUTTON_SIZE.getHeight() * CENTER_BUTTON_CUT_LENGTH));
		} else if (row == 5 && col == 3) {
			points.add(new Point2D(0d, 0d));
			points.add(new Point2D(
					NORMAL_BUTTON_SIZE.getWidth() * CENTER_BUTTON_CUT_LENGTH,
					0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(),
					NORMAL_BUTTON_SIZE.getHeight()
							* (1d - CENTER_BUTTON_CUT_LENGTH)));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(),
					NORMAL_BUTTON_SIZE.getHeight()));
			points.add(new Point2D(0d, NORMAL_BUTTON_SIZE.getHeight()));
		} else if (row == 5 && col == 4) {
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth()
					* (1d - CENTER_BUTTON_CUT_LENGTH), 0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(), 0d));
			points.add(new Point2D(NORMAL_BUTTON_SIZE.getWidth(),
					NORMAL_BUTTON_SIZE.getHeight()));
			points.add(new Point2D(0d, NORMAL_BUTTON_SIZE.getHeight()));
			points.add(new Point2D(0d, NORMAL_BUTTON_SIZE.getHeight()
					* (1d - CENTER_BUTTON_CUT_LENGTH)));
		} else {
			throw new IllegalArgumentException(
					"Center button index out of range");
		}

		points.forEach((point) ->
		{
			p.addPoint((int) (point.getX() + shapeOrigin.getX()),
					(int) (point.getY() + shapeOrigin.getY()));
		});

		return p;
	}

	private Shape buildControl(int row, int col)
	{
		Shape btnShape = null;
		if (row == 0) {
			Point2D buttonPosition = new Point2D(
					BUTTONS_OFFSET.getX()
							+ (col * (NORMAL_BUTTON_SIZE.getWidth()
									+ BUTTON_MARGIN.getWidth()))
							+ ((NORMAL_BUTTON_SIZE.getWidth()
									- CONTROL_BUTTON_DIAMETER) / 2d),
					BUTTONS_OFFSET.getY() - ((NORMAL_BUTTON_SIZE.getHeight()
							- CONTROL_BUTTON_DIAMETER) / 2d));
			btnShape = new Ellipse2D.Double(buttonPosition.getX(),
					buttonPosition.getY(), CONTROL_BUTTON_DIAMETER,
					CONTROL_BUTTON_DIAMETER);
		} else {
			Point2D buttonPosition = new Point2D(
					BUTTONS_OFFSET.getX()
							+ SQUARE_SIDE * (NORMAL_BUTTON_SIZE.getWidth()
									+ BUTTON_MARGIN.getWidth())
							+ CONTROL_BUTTON_BORDER_SIZE
							- ((NORMAL_BUTTON_SIZE.getWidth()
									- CONTROL_BUTTON_DIAMETER) / 2),
					BUTTONS_OFFSET.getY()
							+ (row - 1) * (NORMAL_BUTTON_SIZE.getHeight()
									+ BUTTON_MARGIN.getHeight())
							+ CONTROL_BUTTON_DIAMETER
							+ CONTROL_BUTTON_BORDER_SIZE
							+ ((NORMAL_BUTTON_SIZE.getHeight()
									- CONTROL_BUTTON_DIAMETER) / 2));
			btnShape = new Ellipse2D.Double(buttonPosition.getX(),
					buttonPosition.getY(), CONTROL_BUTTON_DIAMETER,
					CONTROL_BUTTON_DIAMETER);
		}

		return btnShape;
	}

	private Dimension2D getUnscaledGridSize()
	{
		return new Dimension2D(
				BUTTONS_OFFSET.getX()
						+ SQUARE_SIDE * (NORMAL_BUTTON_SIZE.getWidth()
								+ BUTTON_MARGIN.getWidth())
						- BUTTON_MARGIN.getWidth()
						+ (2d * CONTROL_BUTTON_BORDER_SIZE)
						+ CONTROL_BUTTON_DIAMETER,
				BUTTONS_OFFSET.getY()
						+ SQUARE_SIDE * (NORMAL_BUTTON_SIZE.getHeight()
								+ BUTTON_MARGIN.getHeight())
						- BUTTON_MARGIN.getHeight()
						+ (2d * CONTROL_BUTTON_BORDER_SIZE)
						+ CONTROL_BUTTON_DIAMETER);
	}

	private double getTargetScale()
	{
		double xScale = (containerSize.getWidth() - canvasOrigin.getX())
				/ getUnscaledGridSize().getWidth();
		double yScale = (containerSize.getHeight() - canvasOrigin.getY())
				/ getUnscaledGridSize().getHeight();
		return Math.min(xScale, yScale);
	}

	private Shape scaleButton(Shape button)
	{
		AffineTransform at = new AffineTransform();
		double scale = getTargetScale();
		if (scale < 1d) {
			at.scale(scale, scale);
		}
		return at.createTransformedShape(button);
	}

	private Shape centerButton(Shape button)
	{
		AffineTransform at = new AffineTransform();
		Dimension2D gridSize = getUnscaledGridSize();
		double scale = getTargetScale();
		if (scale < 1d) {
			gridSize = new Dimension2D(gridSize.getWidth() * scale,
					gridSize.getHeight() * scale);
		}
		Point2D gridPos = new Point2D(
				(containerSize.getWidth() + canvasOrigin.getX()
						- gridSize.getWidth()) / 2d,
				(containerSize.getHeight() + canvasOrigin.getY()
						- gridSize.getHeight()) / 2d);
		at.translate(gridPos.getX(), gridPos.getY());
		return at.createTransformedShape(button);
	}

}
