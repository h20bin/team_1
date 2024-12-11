import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Character {
    private static Player instance; // 싱글톤 인스턴스

    private int maxHP;
    private int currentHP;
    private int gold;

    private Player() {
        super(0, 0, null, null); // 기본값 설정

        try {
            // 스프라이트 및 무기 데이터 로드
            BufferedImage[] playerSprites = loadSpriteSheet("/Character/body-Sheet1.png", 72, 72);
            BufferedImage[] weaponSprites = loadSpriteSheet("/Weapon/weapon-Sheet1.png", 72, 72);
            BufferedImage[] bulletFrames = loadSpriteSheet("/Weapon/bullet-Sheet1.png", 6, 4);

            Weapon defaultWeapon = new Weapon(weaponSprites[0], 3, 10, 5, bulletFrames);

            // 초기 상태 설정
            this.sprite = playerSprites[0];
            this.weapon = defaultWeapon;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load player or weapon assets.");
        }

        // 초기 상태 기본값
        reset();
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void reset() {
        // 초기화 로직
        this.x = 100;
        this.y = 100;
        this.maxHP = 100;
        this.currentHP = maxHP;
        this.gold = 0;
        if (weapon != null) {
            weapon.reset(); // 무기 초기화
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

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getGold() {
        return gold;
    }

    public void reduceAttackCycle(double amount) {
        if (weapon != null) {
            weapon.reduceFireRate(amount);
        }
    }

    public void increaseMaxHP(int d) {
        maxHP += d;
        currentHP = maxHP;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    // 유틸리티 메서드: 스프라이트 시트를 로드합니다.
    private BufferedImage[] loadSpriteSheet(String resourcePath, int frameWidth, int frameHeight) throws IOException {
        return new SpriteSheet(resourcePath, frameWidth, frameHeight).getAllFrames();
    }

	public void increaseMaxHP(double d) {
		this.maxHP += d;
	}
}
