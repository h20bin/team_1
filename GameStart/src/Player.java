import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Character {
    private static Player instance; // 싱글톤 인스턴스

    private int maxHP;
    private int currentHP;
    private int gold;
    public int weaponNum = 1;
    private double attackSpeed = 1000.0; // 공격 속도 (초당 2발)
    private long lastAttackTime = 0; // 마지막 발사 시간 기록 (밀리초 단위)

    private boolean invincible;  // 무적 상태
    private long invincibleStartTime;  // 무적 시작 시간
    private static final long INVINCIBLE_TIME = 1000;  // 무적 시간 (1초)

    Player() {
        super(0, 0, null, null); // 기본값 설정

        try {
            // 스프라이트 및 무기 데이터 로드
            BufferedImage[] playerSprites = loadSpriteSheet("/Character/body-Sheet"+weaponNum+".png", 72, 72);
            BufferedImage[] wingSprites = loadSpriteSheet("/Character/wing-Sheet"+weaponNum+".png", 72, 72);            
            BufferedImage[] weaponSprites = loadSpriteSheet("/Weapon/weapon-Sheet"+weaponNum+".png", 72, 72);
            BufferedImage[] bulletFrames = loadSpriteSheet("/Weapon/bullet-Sheet"+weaponNum+".png", 6, 4);

            Weapon defaultWeapon = new Weapon(weaponSprites[0], 3, 100, 8, bulletFrames);

            // 초기 상태 설정
            this.sprite = playerSprites[0];
            this.wing = wingSprites;
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
        this.x = 200;
        this.y = 600;
        this.maxHP = 100;
        this.currentHP = maxHP;
        this.invincible = false;  // 초기에는 무적 상태 아님
        this.invincibleStartTime = 0;

        if (weapon != null) {
            weapon.reset(); // 무기 초기화
        }

        // 골드는 초기화하지 않음
    }


    public void takeDamage(int damage, int knockbackDirection) {
        // 무적 상태 체크
        if (isInvincible()) {
            System.out.println("Player is invincible, no damage taken!");
            return;  // 무적 상태면 피해를 받지 않음
        }

        // 피해 처리
        currentHP = Math.max(0, currentHP - damage);
        if (currentHP == 0) {
            System.out.println("Player is dead!");
            gameOver();
        }

        // 넉백 효과 적용 (더 강한 넉백)
        applyKnockback(knockbackDirection);

        // 플레이어가 지면에 떨어졌는지 확인
        if (y > 600) { // 예시로 y > 600이면 게임 종료
            System.out.println("Player has fallen off the ground! Game Over!");
            gameOver();
        }

        // 무적 시간 시작
        invincible = true;
        invincibleStartTime = System.currentTimeMillis();
    }

    private void gameOver() {
        // 게임 오버 처리 로직 (예: 게임 종료)
        System.exit(0);  // 예시로 게임 종료
    }

    private void applyKnockback(int knockbackDirection) {
        int knockbackDistance = 100; // 넉백 거리 강하게 설정

        // 넉백 방향에 따라 x, y 위치 변경
        if (knockbackDirection == -1) {
            x -= knockbackDistance; // 왼쪽으로 넉백
        } else if (knockbackDirection == 1) {
            x += knockbackDistance; // 오른쪽으로 넉백
        } else if (knockbackDirection == -2) {
            y -= knockbackDistance; // 위로 넉백
        } else if (knockbackDirection == 2) {
            y += knockbackDistance; // 아래로 넉백
        }

        // 화면 경계를 벗어나지 않도록 제한
        x = Math.max(0, Math.min(x, 500));  // x 좌표 경계
        y = Math.max(0, Math.min(y, 750));  // y 좌표 경계
    }

    private boolean isInvincible() {
        // 무적 시간이 경과했는지 체크
        if (invincible && System.currentTimeMillis() - invincibleStartTime >= INVINCIBLE_TIME) {
            invincible = false;  // 무적 상태 해제
        }
        return invincible;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    @Override
    public void move(int dx, int dy) {
        // 플레이어 이미지의 너비와 높이를 sprite에서 가져옴
        int playerWidth = sprite.getWidth();
        int playerHeight = sprite.getHeight();

        // 이동 후 새로운 x, y 계산
        x += dx;
        y += dy;

        // 화면 왼쪽 경계를 벗어나지 않도록 제한
        if (x < 0) {
            x = 0;
        }

        // 화면 오른쪽 경계를 벗어나지 않도록 제한
        if (x + playerWidth > 500) {  
            x = 500 - playerWidth;
        }

        // 화면 위쪽 경계를 벗어나지 않도록 제한
        if (y < 0) {
            y = 0;
        }

        // 화면 아래쪽 경계를 벗어나지 않도록 제한
        if (y + playerHeight > 750) {  
            y = 750 - playerHeight;
        }
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
    
    public void shoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= 1000 / attackSpeed) {
            // 총알 발사
            weapon.shoot(x, y);
            lastAttackTime = currentTime; // 마지막 발사 시간 업데이트
        }
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
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

    public void play() {
        // 플레이어가 진행할 다른 기능들을 여기에 구현
    }

	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCurrentHP(int currentHP) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxHP(int maxHP) {
		// TODO Auto-generated method stub
		
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setPosition(int i, int j) {
		// TODO Auto-generated method stub
		
	}
}