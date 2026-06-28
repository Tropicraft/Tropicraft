package net.tropicraft.map;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import net.tropicraft.map.feature.MapFeature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

public class MapPanel extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private static final int TILE_SIZE = 256;

    private final MapController controller;
    private MapFeature feature;

    private RenderedTileMap tileMap;
    private int zoomLevel;

    private final Int2ObjectMap<RenderedTileMap> cascadedTileMaps = new Int2ObjectRBTreeMap<>();

    private int lastMouseX;
    private int lastMouseY;

    public MapPanel(MapController controller, MapFeature feature) {
        this.controller = controller;
        this.feature = feature;

        controller.register(this);

        zoomLevel = controller.zoomLevel();
        tileMap = createTileMap(zoomLevel);

        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void setFeature(MapFeature feature) {
        this.feature = feature;
        tileMap = createTileMap(zoomLevel);
        cascadedTileMaps.clear();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        tileMap.resize(controller.panX(), controller.panY(), getWidth(), getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - lastMouseX;
        int deltaY = e.getY() - lastMouseY;
        lastMouseX = e.getX();
        lastMouseY = e.getY();
        controller.pan(deltaX, deltaY);
    }

    public void mapMoved() {
        tileMap.move(controller.panX(), controller.panY());
        repaint();
    }

    public void mapZoomed() {
        cascadedTileMaps.put(zoomLevel, tileMap);
        zoomLevel = controller.zoomLevel();
        tileMap.cancelTasks();
        tileMap = createTileMap(zoomLevel);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = e.getUnitsToScroll() < 0 ? -1 : 1;
        controller.zoom(e.getX(), e.getY(), amount);
    }

    private RenderedTileMap createTileMap(int zoomLevel) {
        RenderedTileMap tileMap = new RenderedTileMap(TILE_SIZE, (tileX, tileY) -> {
            int x0 = tileX * TILE_SIZE;
            int y0 = tileY * TILE_SIZE;
            return feature.render(zoomLevel, x0, y0, x0 + TILE_SIZE - 1, y0 + TILE_SIZE - 1, TILE_SIZE, TILE_SIZE);
        });
        tileMap.setRepaintListener(this::repaint);
        tileMap.resize(controller.panX(), controller.panY(), getWidth(), getHeight());
        return tileMap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;

        if (tileMap.isReady()) {
            cascadedTileMaps.clear();
        } else {
            // Pessimistically draw all tiles from former zoom levels - but they should get cleared out soon enough
            for (Int2ObjectMap.Entry<RenderedTileMap> entry : cascadedTileMaps.int2ObjectEntrySet()) {
                int zoomLevel = entry.getIntKey();
                RenderedTileMap tileMap = entry.getValue();
                drawTileMap(graphics, tileMap, controller.zoomLevel() - zoomLevel);
            }
        }

        drawTileMap(graphics, tileMap, 0);
    }

    private void drawTileMap(Graphics2D graphics, RenderedTileMap tileMap, int relativeZoom) {
        int drawSize = scaleByZoom(TILE_SIZE, relativeZoom);
        RenderedTileMap.Frame frame = tileMap.frame();
        for (int tileY = frame.minY(); tileY <= frame.maxY(); tileY++) {
            for (int tileX = frame.minX(); tileX <= frame.maxX(); tileX++) {
                BufferedImage image = tileMap.get(tileX, tileY);
                if (image == null) {
                    continue;
                }
                int x = scaleByZoom(tileX * TILE_SIZE, relativeZoom) - controller.panX();
                int y = scaleByZoom(tileY * TILE_SIZE, relativeZoom) - controller.panY();
                if (x < -drawSize || y < -drawSize || x >= getWidth() || y >= getHeight()) {
                    continue;
                }
                graphics.drawImage(image, x, y, drawSize, drawSize, null);
            }
        }
    }

    private static int scaleByZoom(int coordinate, int relativeZoom) {
        if (relativeZoom > 0) {
            return coordinate >> relativeZoom;
        } else {
            return coordinate << -relativeZoom;
        }
    }
}
