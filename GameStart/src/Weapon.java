import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private BufferedImage sprite; // 무기의 스프라이트 이미지
    private List<Bullet> bullets = new ArrayList<>(); // 발사된 탄환 목록
    private int fireRate; // 발사 속도
    private int damage; // 무기의 데미지
    private int bulletSpeed; // 탄환의 속도
    private BufferedImage[] bulletFrames; // 탄환의 프레임

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
        g.drawImage(sprite, x, y, null); // 무기 이미지 렌더링
        for (Bullet bullet : bullets) { // 발사된 각 탄환을 그리기
            bullet.render(g);
        }
    }

    // 탄환 업데이트 메서드
    public void updateBullets() {
        // 화면 밖으로 나간 탄환을 리스트에서 제거
        bullets.removeIf(bullet -> bullet.getY() < 0);
        // 각 탄환을 이동시키고 업데이트
        for (Bullet bullet : bullets) {
            bullet.move();
        }
    }

    // 발사된 탄환 리스트 반환
    public List<Bullet> getBullets() {
        return bullets; // 발사된 탄환 목록 반환
    }

    // 기본값을 초기화하는 리셋 메서드 (필요시 구현)
    public void reset() {
        bullets.clear(); // 발사된 탄환 리스트 초기화
    }

    // 발사 속도를 줄이는 메서드 (원하는 만큼 구현)
    public int reduceFireRate(double amount) {
        fireRate -= amount; // 발사 속도를 감소시킴
        return fireRate; // 감소된 발사 속도 반환
    }

    // 탄환 속도를 증가시키는 메서드
    public int increaseBulletSpeed(int amount) {
        bulletSpeed += amount; // 탄환 속도를 증가시킴
        return bulletSpeed; // 증가된 속도 반환
    }

    // 무기의 데미지를 증가시키는 메서드
    public int increaseDamage(int amount) {
        damage += amount; // 데미지를 증가시킴
        return damage; // 증가된 데미지 반환
    }

    // 무기 스프라이트를 설정하는 메서드
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite; // 스프라이트 설정
    }

    // 발사 속도를 설정하는 메서드
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate; // 발사 속도 설정
    }

    // 탄환 프레임을 설정하는 메서드
    public void setBulletFrames(BufferedImage[] bulletFrames) {
        this.bulletFrames = bulletFrames; // 탄환 프레임 배열 설정
    }

    // 무기와 탄환 정보를 출력하는 메서드
    public void printWeaponInfo() {
        System.out.println("Weapon Info:");
        System.out.println("Sprite: " + (sprite == null ? "No sprite" : sprite));
        System.out.println("Fire Rate: " + fireRate);
        System.out.println("Damage: " + damage);
        System.out.println("Bullet Speed: " + bulletSpeed);
        System.out.println("Bullet Frames: " + bulletFrames.length);
    }

    // 탄환을 발사하는 메서드 (연속 발사)
    public void shootContinuous(int weaponX, int weaponY) {
        for (int i = 0; i < fireRate; i++) {
            shoot(weaponX, weaponY); // 연속적으로 발사
        }
    }

    // 탄환의 크기를 설정하는 메서드 (프레임 크기 변경)
    public void setBulletFrameSize(int width, int height) {
        for (int i = 0; i < bulletFrames.length; i++) {
            BufferedImage resizedBullet = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            resizedBullet.getGraphics().drawImage(bulletFrames[i], 0, 0, width, height, null);
            bulletFrames[i] = resizedBullet; // 변경된 크기 저장
        }
    }

    // 무기를 충전하는 메서드 (특정 조건에 맞춰 충전 기능 구현 가능)
    public void chargeWeapon() {
        // 예시로 충전 로직을 간단히 추가
        System.out.println("Charging weapon...");
        // 충전 로직을 여기에 구현
    }

    // 탄환을 다 쏘고 난 후 무기의 상태를 리셋하는 메서드
    public void reloadWeapon() {
        System.out.println("Reloading weapon...");
        reset(); // 발사된 탄환 초기화
    }

    // 탄환을 화면에서 랜더링하기 전에 탄환의 상태를 확인하는 메서드
    public void checkBulletState() {
        System.out.println("Checking bullet states...");
        for (Bullet bullet : bullets) {
        
        }
    }

    // 무기의 총알 속도와 데미지를 한 번에 증가시키는 메서드
    public void upgradeWeapon(int speedIncrease, int damageIncrease) {
        bulletSpeed += speedIncrease; // 탄환 속도 증가
        damage += damageIncrease; // 데미지 증가
        System.out.println("Weapon upgraded: Bullet Speed = " + bulletSpeed + ", Damage = " + damage);
    }

    // 무기 초기화 후 출력하는 메서드 (디버그용)
    public void debugWeaponState() {
        System.out.println("Weapon Debug State:");
        System.out.println("Sprite: " + sprite);
        System.out.println("Fire Rate: " + fireRate);
        System.out.println("Damage: " + damage);
        System.out.println("Bullet Speed: " + bulletSpeed);
        System.out.println("Bullet Frames: " + bulletFrames);
    }

    // 탄환 리스트의 크기를 반환하는 메서드
    public int getBulletCount() {
        return bullets.size(); // 발사된 탄환의 개수를 반환
    }

    // 무기의 데미지를 반환하는 메서드
    public int getDamage() {
        return damage; // 데미지 반환
    }

    // 탄환 속도를 반환하는 메서드
    public int getBulletSpeed() {
        return bulletSpeed; // 탄환 속도 반환
    }

    // 발사 속도를 반환하는 메서드
    public int getFireRate() {
        return fireRate; // 발사 속도 반환
    }

    // 무기의 스프라이트 이미지를 반환하는 메서드
    public BufferedImage getSprite() {
        return sprite; // 무기 스프라이트 반환
    }

    // 탄환의 프레임을 반환하는 메서드
    public BufferedImage[] getBulletFrames() {
        return bulletFrames; // 탄환 프레임 배열 반환
    }

    // 발사된 탄환을 초기화하는 메서드
    public void clearBullets() {
        bullets.clear(); // 발사된 탄환 리스트 초기화
        System.out.println("All bullets have been cleared.");
    }

    // 무기 속도를 증가시키는 메서드
    public void increaseWeaponSpeed(int speedIncrease) {
        fireRate += speedIncrease; // 무기 속도 증가
        System.out.println("Weapon speed increased to " + fireRate);
    }
}
