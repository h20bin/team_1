import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Character {
    private int health; // 적의 체력

    // 생성자: 적의 위치, 체력, 이미지, 무기를 설정
    public Enemy(int x, int y, int health, BufferedImage sprite, Weapon weapon) {
        super(x, y, sprite, weapon);
        initializeHealth(health);
    }

    private void initializeHealth(int health) {
        this.health = health;
    }

    // 데미지를 입었을 때 체력을 감소시키는 메서드
    public void takeDamage(int damage) {
        reduceHealth(damage);
        checkHealth();
    }

    private void reduceHealth(int damage) {
        health -= damage;
    }

    private void checkHealth() {
        if (health <= 0) {
            handleEnemyDefeat();
        }
    }

    private void handleEnemyDefeat() {
        System.out.println("Enemy defeated!");
        performDefeatActions();
    }

    private void performDefeatActions() {
        // 필요시 적 처치 후 추가 동작 처리 (예: 점수 증가, 애니메이션 등)
    }

    // 이동 메서드: dx, dy를 받아 적의 위치를 변경
    @Override
    public void move(int dx, int dy) {
        updatePosition(dx, dy);
        checkOutOfBounds();
    }

    private void updatePosition(int dx, int dy) {
        y += dy; // 적은 y축으로 아래로만 이동 (dy만 처리)
    }

    private void checkOutOfBounds() {
        if (y > 768) {
            resetPosition();
        }
    }

    private void resetPosition() {
        resetYPosition();
        resetXPosition();
    }

    private void resetYPosition() {
        y = 0; // 화면 상단으로 이동
    }

    private void resetXPosition() {
        x = generateRandomXCoordinate();
    }

    private int generateRandomXCoordinate() {
        return (int) (Math.random() * 512);
    }

    // 렌더링 메서드: 적을 그릴 때 무기와 본체를 그린다
    @Override
    public void render(Graphics g) {
        renderEnemy(g);
    }

    private void renderEnemy(Graphics g) {
        super.render(g);
    }

    // 충돌 영역을 반환하는 메서드: 적의 영역을 40x40으로 설정
    @Override
    public Rectangle getBounds() {
        return calculateBounds();
    }

    private Rectangle calculateBounds() {
        return new Rectangle(getXCoordinate(), getYCoordinate(), getEnemyWidth(), getEnemyHeight());
    }

    private int getXCoordinate() {
        return x;
    }

    private int getYCoordinate() {
        return y;
    }

    private int getEnemyWidth() {
        return 40; // 적의 가로 크기
    }

    private int getEnemyHeight() {
        return 40; // 적의 세로 크기
    }

    // 체력을 반환하는 메서드
    public int getHealth() {
        return retrieveHealth();
    }

    private int retrieveHealth() {
        return health;
    }

    // 체력을 설정하는 메서드
    public void setHealth(int health) {
        assignHealth(health);
    }

    private void assignHealth(int health) {
        this.health = health;
    }

	public Weapon getWeapon() {
		// TODO Auto-generated method stub
		return null;
	}
}
