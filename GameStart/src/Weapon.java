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

    public void shoot(int weaponX, int weaponY) {
        // 무기의 중심 좌표 계산
        int leftBulletX = weaponX + sprite.getWidth() / 4 - bulletFrames[0].getWidth() / 2; // 왼쪽 탄환
        int rightBulletX = weaponX + (3 * sprite.getWidth()) / 4 - bulletFrames[0].getWidth() / 2; // 오른쪽 탄환
        int bulletY = weaponY - bulletFrames[0].getHeight() + 20; // 탄환의 Y 좌표 (무기 상단 바로 위)

        // 탄환 두 개 추가 (왼쪽, 오른쪽)
        bullets.add(new Bullet(leftBulletX, bulletY, bulletSpeed, damage, bulletFrames));
        bullets.add(new Bullet(rightBulletX, bulletY, bulletSpeed, damage, bulletFrames));
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
