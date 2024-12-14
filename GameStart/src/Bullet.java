import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet {
    private int x, y;
    private int speed;
    private int damage;
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private int frameCount = 0;
    private int animationSpeed = 4; // 애니메이션 속도 조정

    public Bullet(int x, int y, int speed, int damage, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.frames = frames;
    }

    public void move() {
        y -= speed; // 위쪽으로 이동
    }

    public void render(Graphics g) {
        currentFrame = (frameCount / animationSpeed) % frames.length;
        g.drawImage(frames[currentFrame], x, y, null);
        frameCount++;
    }

    public int getDamage() {
        return damage;
    }

	public int getY() {
		// TODO Auto-generated method stub
		return this.y;
	}

	public Rectangle getBounds() { 
	    // 적의 영역을 40x40으로 설정
	    return new Rectangle(x, y, 6, 4);
	}
}
