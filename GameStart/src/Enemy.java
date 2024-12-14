import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Character {
    private int health;  // 적의 체력

    // 생성자: 적의 위치, 체력, 이미지, 무기를 설정
    public Enemy(int x, int y, int health, BufferedImage sprite, Weapon weapon) {
        super(x, y, sprite, weapon);  // 부모 클래스 Character의 생성자 호출
        this.health = health;  // 체력 설정
    }

    // 데미지를 입었을 때 체력을 감소시키는 메서드
    public void takeDamage(int damage) {
        health -= damage;  // 체력 감소
        if (health <= 0) {
            System.out.println("Enemy defeated!");  // 체력이 0 이하일 때 적 처치 메시지 출력
            // 처치된 적에 대한 추가 작업이 필요하다면 여기서 처리 (예: 점수 증가, 애니메이션 등)
        }
    }

    // 이동 메서드: dx, dy를 받아 적의 위치를 변경
    @Override
    public void move(int dx, int dy) {
        y += dy;  // 적은 y축으로 아래로만 이동 (dy만 처리)
        if (y > 768) {  // 화면을 벗어난 적을 다시 화면 상단으로 보내기
            y = 0;
            x = (int) (Math.random() * 512);  // 랜덤 x좌표로 위치 변경
        }
    }

    // 렌더링 메서드: 적을 그릴 때 무기와 본체를 그린다
    @Override
    public void render(Graphics g) {
        super.render(g);  // Character 클래스의 render 메서드를 호출하여 무기와 본체 렌더링
    }

    // 충돌 영역을 반환하는 메서드: 적의 영역을 40x40으로 설정
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);  // 적의 영역 (충돌을 위한 바운딩 박스)
    }

    // 체력을 반환하는 메서드
    public int getHealth() {
        return health;  // 적의 현재 체력 반환
    }

    // 체력을 설정하는 메서드
    public void setHealth(int health) {
        this.health = health;  // 적의 체력 설정
    }
}
