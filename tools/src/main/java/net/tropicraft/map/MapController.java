package net.tropicraft.map;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class MapController {
    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 3;

    private final List<MapPanel> maps = new ArrayList<>();

    private int panX;
    private int panY;
    private int zoomLevel = MIN_ZOOM;

    public void register(MapPanel map) {
        maps.add(map);
    }

    public void pan(int deltaX, int deltaY) {
        panX -= deltaX;
        panY -= deltaY;
        for (MapPanel map : maps) {
            map.mapMoved();
        }
    }

    public void zoom(int pivotX, int pivotY, int amount) {
        int newZoomLevel = Mth.clamp(zoomLevel + amount, MIN_ZOOM, MAX_ZOOM);
        if (zoomLevel == newZoomLevel) {
            return;
        }
        zoomLevel = newZoomLevel;
        if (amount > 0) {
            panX = panX - pivotX >> amount;
            panY = panY - pivotY >> amount;
        } else if (amount < 0) {
            panX = (panX << -amount) + pivotX;
            panY = (panY << -amount) + pivotY;
        }
        for (MapPanel map : maps) {
            map.mapZoomed();
        }
    }

    public int panX() {
        return panX;
    }

    public int panY() {
        return panY;
    }

    public int zoomLevel() {
        return zoomLevel;
    }
}
