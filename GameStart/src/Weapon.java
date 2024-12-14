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

    // 매개변수를 받는 생성자
    public Weapon(BufferedImage sprite, int fireRate, int damage, int bulletSpeed, BufferedImage[] bulletFrames) {
        this.sprite = sprite;
        this.fireRate = fireRate;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.bulletFrames = bulletFrames;
    }

    // 기본 생성자 추가 (기본값 설정)
    public Weapon() {
        this.sprite = null; // 기본 스프라이트 (null로 설정)
        this.fireRate = 1;  // 기본 발사 속도
        this.damage = 10;   // 기본 데미지
        this.bulletSpeed = 5; // 기본 탄환 속도
        this.bulletFrames = new BufferedImage[1]; // 기본 프레임 배열 (1개 크기)
    }

    // 탄환 발사 메서드
    public void shoot(int weaponX, int weaponY) {
        // 무기의 중심 좌표 계산
        int leftBulletX = weaponX + sprite.getWidth() / 4 - bulletFrames[0].getWidth() / 2; // 왼쪽 탄환
        int rightBulletX = weaponX + (3 * sprite.getWidth()) / 4 - bulletFrames[0].getWidth() / 2; // 오른쪽 탄환
        int bulletY = weaponY - bulletFrames[0].getHeight() + 20; // 탄환의 Y 좌표 (무기 상단 바로 위)

        // 탄환 두 개 추가 (왼쪽, 오른쪽)
        bullets.add(new Bullet(leftBulletX, bulletY, bulletSpeed, damage, bulletFrames));
        bullets.add(new Bullet(rightBulletX, bulletY, bulletSpeed, damage, bulletFrames));
    }

    // 무기 및 탄환 렌더링 메서드
    public void render(Graphics g, int x, int y) {
        g.drawImage(sprite, x, y, null);
        for (Bullet bullet : bullets) {
            bullet.render(g);
        }
    }

    // 탄환 업데이트 메서드
    public void updateBullets() {
        bullets.removeIf(bullet -> bullet.getY() < 0); // 화면 밖으로 나간 탄환 제거
        for (Bullet bullet : bullets) {
            bullet.move(); // 각 탄환 이동
        }
    }

    // 발사된 탄환 리스트 반환
    public List<Bullet> getBullets() {
        return bullets;
    }

    // 기본값을 초기화하는 리셋 메서드 (필요시 구현)
    public void reset() {
        bullets.clear(); // 발사된 탄환 리스트 초기화
    }

    // 발사 속도를 줄이는 메서드 (원하는 만큼 구현)
    public int reduceFireRate(double amount) {
        fireRate -= amount;
        return fireRate;
    }
}
