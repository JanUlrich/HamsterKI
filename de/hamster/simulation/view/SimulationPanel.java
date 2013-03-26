package de.hamster.simulation.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.FilteredImageSource;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import de.hamster.simulation.model.Hamster;
import de.hamster.simulation.model.SimulationModel;
import de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class SimulationPanel extends JPanel implements Observer,
		MouseMotionListener {
	int zoom, oldZoom;

	Image[][] hamster;

	Image[] corn;

	Image wall;

	int width;

	int height;

	int dragStartX, dragStartY, dragX, dragY;

	Cursor defaultCursor;

	SimulationModel model;

	SimulationTools tools;

	public SimulationPanel(SimulationModel model, SimulationTools tools) {
		this.model = model;
		model.addObserver(this);
		this.tools = tools;

		zoom = 3;
		loadImages();
		createCursors();
		addMouseMotionListener(this);
		this.setBackground(new Color(240, 244, 246)); // dibo 230309
	}

	void loadImages() {
		hamster = new Image[Utils.COLORS.length][4];
		Image[] orig = new Image[4];
		orig[0] = Utils.getImage("hamsternorth.png");
		orig[1] = Utils.getImage("hamstereast.png");
		orig[2] = Utils.getImage("hamstersouth.png");
		orig[3] = Utils.getImage("hamsterwest.png");
		for (int i = 0; i < hamster.length; i++) {
			ColorFilter c = new ColorFilter(i);
			for (int j = 0; j < 4; j++) {
				hamster[i][j] = createImage(new FilteredImageSource(
						orig[j].getSource(), c));

			}
		}
		corn = new Image[12];
		for (int i = 0; i < corn.length; i++) {
			corn[i] = Utils.getImage((i + 1) + "Corn32.png");
		}
		wall = Utils.getImage("Wall32.png");

	}

	void createCursors() {
		defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	}

	public int getCellWidth() {
		return 2 * (2 << zoom);
	}

	private void updateSize() {
		if (model.getTerrain().getWidth() == width
				&& model.getTerrain().getHeight() == height && zoom == oldZoom)
			return;
		oldZoom = zoom;
		width = model.getTerrain().getWidth();
		height = model.getTerrain().getHeight();
		setPreferredSize(new Dimension(width * (getCellWidth() + 1) + 5, height
				* (getCellWidth() + 1) + 5));
		if (getParent() != null)
			getParent().doLayout();
	}

	int getX(int col) {
		int centerX = (int) (getSize().getWidth() / 2);
		return centerX + col * (getCellWidth() + 1) - width
				* (getCellWidth() + 1) / 2;
	}

	int getY(int row) {
		int centerY = (int) (getSize().getHeight() / 2);
		return centerY + row * (getCellWidth() + 1) - height
				* (getCellWidth() + 1) / 2;
	}

	public int getCol(int x) {
		int centerX = (int) (getSize().getWidth() / 2);
		int col = (x - centerX + width * (getCellWidth() + 1) / 2)
				/ (getCellWidth() + 1);
		return col;
	}

	public int getRow(int y) {
		int centerY = (int) (getSize().getHeight() / 2);
		int row = (y - centerY + height * (getCellWidth() + 1) / 2)
				/ (getCellWidth() + 1);
		return row;
	}

	boolean withinTerrain(int col, int row) {
		return col >= 0 && row >= 0 && col < width && row < height;
	}

	public void drawImage(Graphics2D g, Image image, int i, int j) {
		g.drawImage(image, getX(i) + 1, getY(j) + 1, getCellWidth(),
				getCellWidth(), this);
	}

	public void drawHamsterAlt(Graphics2D g, int nr, int i, int j, int dir, int color) {
		if (nr >= hamster.length)
			nr = hamster.length - 1;
		drawImage(g, hamster[nr][dir], i, j);
	}
	
	public void drawHamster(Graphics2D g, int nr, int i, int j, int dir, int color) {
		if (color <= -1) {
			color = nr;
		}
		if (color >= hamster.length)
			color = hamster.length - 1;
		drawImage(g, hamster[color][dir], i, j);
	}

	public void drawCorn(Graphics2D g, int i, int j, int count) {
		if (count == 0)
			return;
		count = Math.min(count, 12);
		g.drawImage(corn[count - 1], getX(i) + 1, getY(j) + 1, getCellWidth(),
				getCellWidth(), this);
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		updateSize();
		if (model == null)
			return;
		Graphics2D g = (Graphics2D) graphics;
		for (int i = 0; i < width + 1; i++)
			g.drawLine(getX(i), getY(0), getX(i), getY(height));
		for (int i = 0; i < height + 1; i++)
			g.drawLine(getX(0), getY(i), getX(width), getY(i));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (model.getTerrain().getWall(i, j))
					drawImage(g, wall, i, j);
				else
					drawCorn(g, i, j, model.getTerrain().getCornCount(i, j));
			}
		}
		Hamster h = model.getTerrain().getDefaultHamster();
		drawHamster(g, 0, h.getX(), h.getY(), h.getDir(), Utils.COLOR);
		for (int i = 0; i < model.getHamster().size(); i++) {
			h = (Hamster) model.getHamster().get(i);
			// dibo
			// drawHamster(g, i + 1, h.getX(), h.getY(), h.getDir());
			if (h.getId() != -1) {
				drawHamster(g, h.getId() + 1, h.getX(), h.getY(), h.getDir(), h.getColor());
			}
		}
		tools.paint(g);
	}

	public void zoomIn() {
		zoom++;
		repaint();
	}

	public void zoomOut() {
		if (zoom > 1) {
			zoom--;
			repaint();
		}
	}

	public void update(Observable o, Object arg) {
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		int row = getRow(e.getY());
		int col = getCol(e.getX());
		if (row < 0 || row >= model.getTerrain().getHeight() || col < 0
				|| col >= model.getTerrain().getWidth()) {
			setToolTipText(null);
			return;
		}
		if (model.getTerrain().getWall(col, row)) {
			setToolTipText("<html>"
					+ Utils.getResource("simulation.view.reihe") + ": " + row
					+ "<br>" + Utils.getResource("simulation.view.spalte")
					+ ": " + col + "<br>"
					+ Utils.getResource("simulation.view.mauer") + "</html>");
		} else {
			setToolTipText("<html>"
					+ Utils.getResource("simulation.view.reihe") + ": " + row
					+ "<br>" + Utils.getResource("simulation.view.spalte")
					+ ": " + col + "<br>"
					+ Utils.getResource("simulation.view.koerner") + ": "
					+ model.getTerrain().getCornCount(col, row) + "</html>");
		}
	}
}