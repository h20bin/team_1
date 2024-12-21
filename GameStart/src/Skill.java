import java.util.ArrayList;
import java.util.List;

public class Skill {
    private Weapon weapon;
    private int weaponID;
    private List<Bullet> bullets = new ArrayList<>(); // 발사된 탄환 목록
    private int skilltype;

    public Skill(Weapon weapon, int weaponID, int skilltype) {
        this.weapon = weapon;
        this.weaponID = weaponID;
        this.skilltype = skilltype;
    }

    // 삼각함수를 사용한 대각선 발사 패턴
    public void pattern1() {
        int centerX = 100; // 무기 소유자의 X 좌표
        int centerY = 50; // 무기 소유자의 Y 좌표
        int speed = 20;

        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            double dx = Math.cos(angle) * speed;
            double dy = Math.sin(angle) * speed;

            // Weapon의 bullets 리스트에 추가
            weapon.getBullets().add(new Bullet(centerX, centerY, dy, 25, weapon.getBulletFrames(), weapon, dx));
        }
    }

    // 원형 발사 패턴
    public void pattern2() {
        int centerX = 150;
        int centerY = 120;
        int speed = 15;

        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            double dx = Math.cos(angle) * speed;
            double dy = Math.sin(angle) * speed;

            // Weapon의 bullets 리스트에 추가
            weapon.getBullets().add(new Bullet(centerX, centerY, dy, 25, weapon.getBulletFrames(), weapon, dx));
        }
    }

    // 나선형 발사 패턴
    public void pattern3() {
        int centerX = 300;
        int centerY = 120;
        int speed = 10;

        for (int i = 0; i < 20; i++) {
            double angle = Math.toRadians(i * 18);
            double dx = Math.cos(angle) * speed;
            double dy = Math.sin(angle) * speed;

            weapon.getBullets().add(new Bullet(centerX, centerY, dy, 25, weapon.getBulletFrames(), weapon, dx));
            speed += 1;
        }
    }

    // 양옆으로 퍼지는 패턴
    public void pattern4() {
        int centerX = 350;
        int centerY = 50;
        int speed = 20;

        for (int i = -3; i <= 3; i++) {
            double angle = Math.toRadians(i * 20);
            double dx = Math.cos(angle) * speed;
            double dy = Math.sin(angle) * speed;

            weapon.getBullets().add(new Bullet(centerX, centerY, dy, 25, weapon.getBulletFrames(), weapon, dx));
        }
    }

    public void shooting() {
        switch (this.skilltype) {
            case 1 -> {
                if (this.weaponID == 4) pattern1();
                if (this.weaponID == 5) pattern3();
            }
            case 2 -> {
                if (this.weaponID == 4) pattern2();
                if (this.weaponID == 5) pattern4();
            }
        }
    }
}
