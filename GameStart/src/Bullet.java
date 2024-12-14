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

    // 상태 관리
    private enum BulletState { NORMAL, IMPACT }
    private BulletState state = BulletState.NORMAL;

    public Bullet(int x, int y, int speed, int damage, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.frames = frames;
    }

    public void move() {
        if (state == BulletState.NORMAL) {
            y -= speed; // NORMAL 상태에서는 위쪽으로 이동
        }
    }

    public void render(Graphics g) {
        if (state == BulletState.NORMAL) {
            // NORMAL 상태의 애니메이션 처리
            if (frameCount / animationSpeed < 3) {
                // 초기 프레임 (1~3번 프레임 재생)
                currentFrame = (frameCount / animationSpeed) % 3;
            } else {
                // 이후에는 4번 프레임 고정
                currentFrame = 3;
            }
        } else if (state == BulletState.IMPACT) {
            // IMPACT 상태의 애니메이션 처리 (5~13번 프레임 재생)
            currentFrame = 4 + (frameCount / animationSpeed);
            if (currentFrame >= frames.length) {
                // 마지막 프레임까지 재생되면 소멸 처리
                currentFrame = frames.length - 1;
            }
        }

        // 프레임 그리기
        g.drawImage(frames[currentFrame], x, y, null);
        frameCount++;
    }

    public void onCollision() {
        // 충돌 발생 시 IMPACT 상태로 전환
        if (state == BulletState.NORMAL) {
            state = BulletState.IMPACT;
            frameCount = 0; // 충돌 애니메이션 재생을 위해 초기화
        }
    }

    public boolean isFinished() {
        // IMPACT 상태에서 마지막 프레임까지 재생되면 true 반환
        return state == BulletState.IMPACT && currentFrame >= frames.length - 1;
    }

    public int getDamage() {
        return damage;
    }

    public int getY() {
        return this.y;
    }

    public Rectangle getBounds() {
        // 적의 영역을 40x40으로 설정
        return new Rectangle(x, y, 6, 4);
    }
}
