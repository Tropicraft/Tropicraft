package net.tropicraft.map.feature;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public interface MapFeature {
    CompletableFuture<BufferedImage> render(int zoomLevel, int x0, int y0, int x1, int y1, int width, int height);
}
