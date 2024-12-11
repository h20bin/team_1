import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private BufferedImage sprite;
    private List<Bullet> bullets = new ArrayList<>();
    private int fireRate;
    private int damage;
    private int bulletSpeed;
    private BufferedImage[] bulletFrames;

    public Weapon(BufferedImage sprite, int fireRate, int damage, int bulletSpeed, BufferedImage[] bulletFrames) {
        this.sprite = sprite;
        this.fireRate = fireRate;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.bulletFrames = bulletFrames;
    }

    public void shoot(int x, int y) {
        bullets.add(new Bullet(x, y, bulletSpeed, damage, bulletFrames));
    }

    public void render(Graphics g, int x, int y) {
        g.drawImage(sprite, x, y, null);
        for (Bullet bullet : bullets) {
            bullet.render(g);
        }
    }

    public void updateBullets() {
        bullets.removeIf(bullet -> bullet.getY() < 0);
        for (Bullet bullet : bullets) {
            bullet.move();
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }


	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public int reduceFireRate(double amount) {
		// TODO Auto-generated method stub
		return fireRate;
	}
}
