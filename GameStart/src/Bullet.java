import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet {
    private int x, y;
    private double speed_y;
    private double speed_x;
    private int damage;
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private int frameCount = 0;
    private int animationSpeed = 4; // 애니메이션 속도 조정
    private Weapon weapon;
    private Character owner;
    // 상태 관리
    private enum BulletState { NORMAL, IMPACT, EXIT };
    private enum BulletType { wea1, wea2, wea3,ball1,ball2,ball3,ball4};
    private BulletState state = BulletState.NORMAL;
    private BulletType type = BulletType.wea1;
    public Bullet(int x, int y, double dy, int damage, BufferedImage[] frames, Weapon weapon, double dx ) {
        this.x = x;
        this.y = y;
        this.speed_x = dx;
        this.speed_y = dy;
        this.damage = damage;
        this.frames = frames;
        this.weapon = weapon;
        this.owner = weapon.getOwner();
        setBulletType();
    }
    
    public Character getOwner() {
        return this.weapon.getOwner();
    }

    public void setBulletType() {
    	if (this.weapon.weaponID == 1) {
    		type = BulletType.wea1;
    		this.animationSpeed = 3;
    	}
    	else if(this.weapon.weaponID == 2){
    		type = BulletType.wea2;
    		this.animationSpeed = 1;
    	}
    	else if(this.weapon.weaponID == 3){
    		type = BulletType.wea3;
    		this.animationSpeed = 2;
    	}
    }

    public void move() {
        if (state == BulletState.NORMAL) {
            x += speed_x; // 수평 이동
            y -= speed_y; // 수직 이동
        }
    }


    public void render(Graphics g) {
        if (frames == null || frames.length == 0 || frames[0] == null) {
            System.err.println("Error: frames are not initialized or invalid.");
            return; // frames가 유효하지 않을 경우 렌더링을 건너뜀
        }

        if (state != BulletState.EXIT) { // EXIT 상태가 아닌 경우에만 렌더링
            if (type == BulletType.wea1) {
                // wea1 애니메이션 처리
                if (state == BulletState.NORMAL) {
                    currentFrame = Math.min(2+(frameCount / animationSpeed) % 4, frames.length - 1);
                } else if (state == BulletState.IMPACT) {
                    currentFrame = Math.min(4 + (frameCount / 1), frames.length - 1);
                    if (currentFrame >= frames.length - 1) {
                        state = BulletState.EXIT;
                    }
                }
            } else if (type == BulletType.wea2) {
                // wea2 애니메이션 처리
                if (state == BulletState.NORMAL) {
                    currentFrame = Math.min(4+(frameCount / animationSpeed) % 8, frames.length - 1);
                } else if (state == BulletState.IMPACT) {
                    currentFrame = Math.min(12 + (frameCount / 1), frames.length - 1);
                    if (currentFrame >= frames.length - 1) {
                        state = BulletState.EXIT;
                    }
                }
            } else if (type == BulletType.wea3) {
                // wea3 애니메이션 처리
                if (state == BulletState.NORMAL) {
                    currentFrame = Math.min(4+(frameCount / animationSpeed) % 12, frames.length - 1);
                } else if (state == BulletState.IMPACT) {
                    currentFrame = Math.min(15 + (frameCount / 1), frames.length - 1);
                    if (currentFrame >= frames.length - 1) {
                        state = BulletState.EXIT;
                    }
                }
            } else if (type == BulletType.ball1 || type == BulletType.ball2 || 
                       type == BulletType.ball3 || type == BulletType.ball4) {
                // ball 타입: 단일 프레임 렌더링
                currentFrame = 0;
                if (state == BulletState.IMPACT) {
                    state = BulletState.EXIT;
                }
            }

            // 렌더링
            g.drawImage(frames[currentFrame], x, y, null);
        }

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
        return state == BulletState.EXIT;
    }


    public int getDamage() {
        return this.damage;
    }

    public int getY() {
        return this.y;
    }

    public Rectangle getBounds() {
        if (state == BulletState.EXIT) {
            return new Rectangle(0, 0, 0, 0); // 충돌 영역 무효화
        }
        
        if (type == BulletType.wea1 ) {
        	return new Rectangle(x, y, 6, 4); // 기존 충돌 영역
        }
        else if (type == BulletType.wea2 ) {
        	return new Rectangle(x, y, 12, 12); // 기존 충돌 영역
        }
        else if (type == BulletType.wea3 ) {
        	return new Rectangle(x, y, 14, 8); // 기존 충돌 영역
        }
        else if (type == BulletType.ball1 ) {
        	return new Rectangle(x, y, 72, 108); // 기존 충돌 영역
        }
        else if (type == BulletType.ball2 ) {
        	return new Rectangle(x, y, 72, 108); // 기존 충돌 영역
        }
        else if (type == BulletType.ball3 ) {
        	return new Rectangle(x, y, 72, 108); // 기존 충돌 영역
        }
        else if (type == BulletType.ball4 ) {
        	return new Rectangle(x, y, 72, 108); // 기존 충돌 영역
        }else
        	return new Rectangle(0,0,0,0);
    }

	public int getX() {
		// TODO Auto-generated method stub
		return this.x;
	}
}
