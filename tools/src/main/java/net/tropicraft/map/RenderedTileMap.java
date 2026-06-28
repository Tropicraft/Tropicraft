package net.tropicraft.map;

import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class RenderedTileMap {
    private final int tileSize;
    private final Renderer renderer;

    private Tile[] tiles = new Tile[0];
    private Frame frame = Frame.EMPTY;

    private Runnable repaintListener = () -> {
    };

    public RenderedTileMap(int tileSize, Renderer renderer) {
        this.tileSize = tileSize;
        this.renderer = renderer;
    }

    public void setRepaintListener(Runnable repaintListener) {
        this.repaintListener = repaintListener;
    }

    public Frame frame() {
        return frame;
    }

    public @Nullable BufferedImage get(int x, int y) {
        if (!frame.contains(x, y)) {
            return null;
        }
        return tiles[frame.index(x, y)].getImage();
    }

    public void move(int x, int y) {
        Frame oldFrame = frame;
        frame = new Frame(
                Math.floorDiv(x, tileSize), Math.floorDiv(y, tileSize),
                frame.width, frame.height
        );

        for (int tileY = frame.minY(); tileY <= frame.maxY(); tileY++) {
            for (int tileX = frame.minX(); tileX <= frame.maxX(); tileX++) {
                int index = frame.index(tileX, tileY);
                if (!oldFrame.contains(tileX, tileY)) {
                    tiles[index].cancelTask();
                    tiles[index] = loadTile(tileX, tileY);
                }
            }
        }
    }

    public void resize(int x, int y, int width, int height) {
        Frame oldFrame = frame;
        Tile[] oldTiles = tiles;

        frame = new Frame(
                Math.floorDiv(x, tileSize), Math.floorDiv(y, tileSize),
                Mth.positiveCeilDiv(width, tileSize) + 1, Mth.positiveCeilDiv(height, tileSize) + 1
        );
        tiles = new Tile[frame.width * frame.height];

        for (int tileY = frame.minY(); tileY <= frame.maxY(); tileY++) {
            for (int tileX = frame.minX(); tileX <= frame.maxX(); tileX++) {
                int index = frame.index(tileX, tileY);
                if (oldFrame.contains(tileX, tileY)) {
                    tiles[index] = oldTiles[oldFrame.index(tileX, tileY)];
                } else {
                    Tile oldTile = tiles[index];
                    if (oldTile != null) {
                        oldTile.cancelTask();
                    }
                    tiles[index] = loadTile(tileX, tileY);
                }
            }
        }
    }

    private Tile loadTile(int x, int y) {
        CompletableFuture<BufferedImage> future = renderer.render(x, y);
        future.thenRun(repaintListener);
        return new Tile(future);
    }

    public boolean isReady() {
        for (Tile tile : tiles) {
            if (!tile.image.isDone()) {
                return false;
            }
        }
        return true;
    }

    public void cancelTasks() {
        for (Tile tile : tiles) {
            tile.cancelTask();
        }
    }

    public record Frame(int minX, int minY, int width, int height) {
        public static final Frame EMPTY = new Frame(0, 0, 0, 0);

        public int maxX() {
            return minX + width - 1;
        }

        public int maxY() {
            return minY + height - 1;
        }

        public boolean contains(int x, int y) {
            int relativeX = x - minX;
            int relativeY = y - minY;
            return relativeX >= 0 && relativeY >= 0 && relativeX < width && relativeY < height;
        }

        private int index(int x, int y) {
            int indexX = Math.floorMod(x, width);
            int indexY = Math.floorMod(y, height);
            return indexX + indexY * width;
        }
    }

    private record Tile(CompletableFuture<@Nullable BufferedImage> image) {
        public @Nullable BufferedImage getImage() {
            return image.getNow(null);
        }

        public void cancelTask() {
            image.complete(null);
        }
    }

    @FunctionalInterface
    public interface Renderer {
        CompletableFuture<BufferedImage> render(int tileX, int tileY);
    }
}
