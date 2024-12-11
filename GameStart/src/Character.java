import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Character {
    protected int x, y;
    protected BufferedImage sprite;
    protected Weapon weapon;

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
