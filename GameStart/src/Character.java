import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Character {
    protected int x, y;
    protected BufferedImage sprite;
    protected BufferedImage[] wing;
    protected Weapon weapon;

    private int currentWingFrame = 0; // 현재 Wing 프레임 인덱스
    private long lastFrameTime = 0; // 마지막 프레임 업데이트 시간
    private int animationSpeed = 10; // 애니메이션 속도 (밀리초 단위)

    public Character(int x, int y, BufferedImage sprite, Weapon weapon) {
        initializePosition(x, y);
        initializeSprite(sprite);
        initializeWeapon(weapon);
    }

    private void initializePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void initializeSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    private void initializeWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void render(Graphics g) {
        renderWeapon(g);
        renderBody(g);
        renderWingAnimation(g);
    }

    private void renderWeapon(Graphics g) {
        if (weapon != null) {
            weapon.render(g, x, y - 10);
        }
    }

    private void renderBody(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, null);
        }
    }

    private void renderWingAnimation(Graphics g) {
        if (wing != null && wing.length > 0) {
            updateWingFrame();
            drawWingFrame(g);
        }
    }

    private void updateWingFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= animationSpeed) {
            currentWingFrame = (currentWingFrame + 1) % wing.length;
            lastFrameTime = currentTime;
        }
    }

    private void drawWingFrame(Graphics g) {
        g.drawImage(wing[currentWingFrame], x, y, null);
    }

    public abstract void move(int dx, int dy);

    public int getY() {
        return retrieveY();
    }

    public int getX() {
        return retrieveX();
    }

    private int retrieveY() {
        return this.y;
    }

    private int retrieveX() {
        return this.x;
    }

    public Rectangle getBounds() {
        return generateBounds();
    }

    private Rectangle generateBounds() {
        return new Rectangle(x, y, calculateWidth(), calculateHeight());
    }

    private int calculateWidth() {
        return (sprite != null) ? sprite.getWidth() : 0;
    }

    private int calculateHeight() {
        return (sprite != null) ? sprite.getHeight() : 0;
    }
}
