import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {
    private BufferedImage spriteSheet;
    private int frameWidth;
    private int frameHeight;

    public SpriteSheet(String resourcePath, int frameWidth, int frameHeight) throws IOException {
        var inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        this.spriteSheet = ImageIO.read(inputStream);
        if (this.spriteSheet == null) {
            throw new IOException("Failed to load sprite sheet: " + resourcePath);
        }
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public BufferedImage getFrame(int frameIndex) {
        int framesPerRow = spriteSheet.getWidth() / frameWidth;
        int totalFrames = framesPerRow * (spriteSheet.getHeight() / frameHeight);

        if (frameIndex < 0 || frameIndex >= totalFrames) {
            throw new IllegalArgumentException("Invalid frame index: " + frameIndex);
        }

        int x = (frameIndex % framesPerRow) * frameWidth;
        int y = (frameIndex / framesPerRow) * frameHeight;

        return spriteSheet.getSubimage(x, y, frameWidth, frameHeight);
    }

    public BufferedImage[] getAllFrames() {
        int totalFrames = (spriteSheet.getWidth() / frameWidth) * (spriteSheet.getHeight() / frameHeight);
        BufferedImage[] frames = new BufferedImage[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            frames[i] = getFrame(i);
        }
        return frames;
    }
}
