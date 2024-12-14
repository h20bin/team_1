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
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.weapon = weapon;
    }

    public void render(Graphics g) {
        // Weapon 먼저 그리기
        if (weapon != null) {
            weapon.render(g, x, y - 10); // 무기의 위치는 캐릭터를 기준으로
        }

        // Body 그리기
        g.drawImage(sprite, x, y, null);
        
        // Wing 애니메이션 그리기
        if (wing != null && wing.length > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameTime >= animationSpeed) {
                currentWingFrame = (currentWingFrame + 1) % wing.length; // 프레임 순환
                lastFrameTime = currentTime; // 마지막 프레임 시간 갱신
            }

            g.drawImage(wing[currentWingFrame], x, y, null);
        }
    }

    public abstract void move(int dx, int dy);

	public int getY() {
		// TODO Auto-generated method stub
		return this.x;
	}

	public int getX() {
		// TODO Auto-generated method stub
		return this.y;
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
