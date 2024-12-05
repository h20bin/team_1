import java.awt.*;

public class Player {
    private static Player instance;
    private int x, y; // 플레이어의 위치
    int maxHP;
    int currentHP;
    private int gold;
    Weapon weapon;
    public double maxSpeed;
    public double attackCycle;

    private Player() {
        this.x = 180; // 초기 위치 설정
        this.y = 500; // 초기 위치 설정
        this.maxHP = 100;
        this.currentHP = maxHP;
        this.gold = 0;
        this.weapon = new Weapon(1, 10, 3); // 초기 무기 설정
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void attack() {
        if (weapon != null) {
            weapon.shoot();
        }
    }

    public void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
        if (currentHP == 0) {
            System.out.println("Player is dead!");
        }
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public int getGold() {
        return gold;
    }

    public int getCurrentHP() {
        return currentHP; // 현재 HP 반환
    }

    public int getX() {
        return x; // x 좌표 반환
    }

    public int getY() {
        return y; // y 좌표 반환
    }

    public void move(int dx, int dy) {
        this.x += dx; // x 좌표 변경
        this.y += dy; // y 좌표 변경
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40); // 플레이어의 위치와 크기 반환
    }

    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 40, 40); // x, y 위치에 플레이어 그리기
    }
    
    public void reset() {
        this.x = 180; // 초기 위치 설정
        this.y = 500; // 초기 위치 설정
        this.maxHP = 100;
        this.currentHP = maxHP;
        this.gold = 0;
        this.weapon = new Weapon(1, 10, 3);
    }
}
