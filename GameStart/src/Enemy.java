import java.awt.*;

public class Enemy {
    private int x, y; // 적의 위치
    private int health; // 적의 체력
    private int speed; // 적의 이동 속도

    // 생성자
    public Enemy(int x, int y, int health, int speed) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
    }

    // 적 이동
    public void move() {
        y += speed; // 아래로 이동
    }

    // 적 데미지 처리
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("Enemy defeated!");
        }
    }

    // 적 렌더링
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 30, 30); // 적을 (x, y)에 그리기
    }

    // 적의 충돌 영역 반환
    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }

    // 적의 체력 반환
    public int getHealth() {
        return health;
    }
}
