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
        // 프레임 크기로 나눈 행, 열 값 계산
        int framesPerRow = spriteSheet.getWidth() / frameWidth;
        int framesPerColumn = spriteSheet.getHeight() / frameHeight;
        int totalFrames = framesPerRow * framesPerColumn;

        // spriteSheet가 로드되지 않았거나 크기가 잘못된 경우 예외 처리
        if (framesPerRow <= 0 || framesPerColumn <= 0 || totalFrames <= 0) {
            throw new IllegalArgumentException("Invalid sprite sheet dimensions or frame size.");
        }

        // 유효한 범위의 frameIndex인지 확인
        if (frameIndex < 0 || frameIndex >= totalFrames) {
            throw new IllegalArgumentException("Invalid frame index: " + frameIndex);
        }

        // 행렬에서 프레임의 위치 계산
        int x = (frameIndex % framesPerRow) * frameWidth;
        int y = (frameIndex / framesPerRow) * frameHeight;

        // 지정된 프레임을 잘라 반환
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
    
    // 프레임 개수를 반환하는 메서드 추가
    public int getFrameCount() {
        int framesPerRow = spriteSheet.getWidth() / frameWidth;
        int framesPerColumn = spriteSheet.getHeight() / frameHeight;
        return framesPerRow * framesPerColumn;
    }
}
