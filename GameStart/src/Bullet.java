import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
    private int x, y; // 총알 위치
    private int speed; // 총알 이동 속도
    private int damage; // 총알 데미지

    // 생성자
    public Bullet(int x, int y, int speed, int damage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
    }

    // 총알 이동
    public void move() {
        y -= speed; // 위쪽으로 이동
    }

    // 총알 렌더링
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 5, 10); // 총알을 (x, y)에 그리기
    }

    // 총알의 충돌 영역 반환
    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 10);
    }

    // 총알의 데미지 반환
    public int getDamage() {
        return damage;
    }

    // x 좌표 반환
    public int getX() {
        return x;
    }

    // y 좌표 반환
    public int getY() {
        return y;
    }
}
