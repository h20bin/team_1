import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Character {
    private int health;

    public Enemy(int x, int y, int health, BufferedImage sprite, Weapon weapon) {
        super(x, y, sprite, weapon);
        this.health = health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("Enemy defeated!");
        }
    }

    @Override
    public void move(int dx, int dy) {
        y += dy; // 아래로 이동
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // Weapon → Body 순으로 렌더링
    }

    @Override
    public Rectangle getBounds() { 
        // 적의 영역을 40x40으로 설정
        return new Rectangle(x, y, 40, 40);
    }

    public int getHealth() { 
        // 적 체력을 반환
        return health;
    }
}
