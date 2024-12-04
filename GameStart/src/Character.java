import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Character extends JPanel implements ActionListener {
    private int x, y, width, height; // 캐릭터 위치와 크기
    private int dx, dy; // 이동 방향
    private double currentSpeed; // 현재 속도
    private final double maxSpeed = 200.0; // 최대 속도
    private final double minSpeed = 5.0; // 최소 속도
    private final double acceleration = 2.0; // 속도 증가량
    private final double deceleration = 10.0; // 속도 감소량

    private Timer timer;
    private long lastTime; // 이전 프레임의 시간 저장
    private long keyReleaseTime; // 키가 떼어진 시간 저장
    private boolean isKeyPressed; // 키가 눌렸는지 여부를 저장
    private long lastDecrementTime; // 마지막 속도 감소 시간 저장

    private long speedTimeStart; // 최고 속도 시작 시간
    private boolean isAtMaxSpeed; // 최고 속도 상태 확인
    private long stopTime; // 멈출 시간 기록
    private boolean isStopped; // 4초 동안 멈춤 상태 확인
    private Color characterColor = Color.BLUE; // 캐릭터 색상 기본값

    public Character() {
        this.x = 200; // 초기 x 좌표
        this.y = 400; // 초기 y 좌표
        this.width = 36; // 캐릭터 너비
        this.height = 36; // 캐릭터 높이
        this.currentSpeed = 50.0; // 초기 속도

        this.timer = new Timer(16, this); // 60fps 목표
        this.timer.start();
        this.lastTime = System.nanoTime(); // 초기 시간 기록

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                // 이동 방향 설정
                if (key == KeyEvent.VK_UP) dy = -1;  // 위쪽
                if (key == KeyEvent.VK_DOWN) dy = 1;   // 아래쪽
                if (key == KeyEvent.VK_LEFT) dx = -1;  // 왼쪽
                if (key == KeyEvent.VK_RIGHT) dx = 1;   // 오른쪽

                // 키가 눌릴 때마다 속도 증가
                isKeyPressed = true;
                updateSpeed(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();

                // 키가 떼어지면 해당 방향의 이동 멈추기
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) dy = 0; // 상하 이동 멈춤
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) dx = 0; // 좌우 이동 멈춤

                // 키가 떼어졌음을 표시하고 시간을 기록
                isKeyPressed = false;
                keyReleaseTime = System.currentTimeMillis(); // 키가 떼어진 시간 기록
            }
        });
    }

    // 속도 업데이트 메서드
    private void updateSpeed(boolean isKeyPressed) {
        if (isKeyPressed) {
            // 키가 눌려있는 동안 currentSpeed 증가
            if (currentSpeed < maxSpeed) {
                currentSpeed += acceleration;
                if (currentSpeed > maxSpeed) {
                    currentSpeed = maxSpeed;
                    if (!isAtMaxSpeed) {
                        speedTimeStart = System.currentTimeMillis(); // 최고 속도 시작 시간 기록
                        isAtMaxSpeed = true; // 최고 속도 상태로 변경
                    }
                }
            }
        }
    }

    // 속도 감소 메서드
    private void decreaseSpeedIfNeeded() {
        // 0.5초마다 속도 감소
        if (!isKeyPressed && System.currentTimeMillis() - keyReleaseTime >= 500) {
            // 마지막 감소 이후 0.5초가 지난 경우에만 속도 감소
            if (System.currentTimeMillis() - lastDecrementTime >= 500) {
                if (currentSpeed > minSpeed) {
                    currentSpeed -= deceleration;
                    if (currentSpeed < minSpeed) {
                        currentSpeed = minSpeed;
                    }
                }
                lastDecrementTime = System.currentTimeMillis(); // 마지막 감소 시간 기록
            }
        }
    }

    // 최고 속도로 5초 이상 이동하면 4초 동안 멈추고 캐릭터 색상을 빨간색으로 변경
    private long cooldownTime; // 최고 속도로 이동할 수 없는 시간 (쿨타임)
    private boolean canReachMaxSpeed = true; // 최고 속도에 도달할 수 있는지 여부

    private void handleOverload() {
        // 최고 속도에 도달했을 때 (currentSpeed가 maxSpeed에 도달)
        if (currentSpeed >= maxSpeed && !isAtMaxSpeed && canReachMaxSpeed) {
            speedTimeStart = System.currentTimeMillis(); // 최고 속도 시작 시간 기록
            isAtMaxSpeed = true; // 최고 속도 상태 활성화
        }

        // 최고 속도로 5초 이상 이동하면 멈추기
        if (isAtMaxSpeed && (System.currentTimeMillis() - speedTimeStart) >= 5000) { 
            stopTime = System.currentTimeMillis(); // 멈출 시간을 기록
            isAtMaxSpeed = false; // 최고 속도 상태 비활성화
            isStopped = true; // 멈추는 상태 활성화
            characterColor = Color.RED; // 캐릭터 색상을 빨간색으로 변경
            cooldownTime = System.currentTimeMillis(); // 쿨타임 시작
            canReachMaxSpeed = false; // 최고 속도에 도달할 수 없도록 설정
        }

        // 4초 동안 멈춤
        if (isStopped && System.currentTimeMillis() - stopTime >= 4000) { 
            isStopped = false; // 멈추는 상태 비활성화
            characterColor = Color.BLUE; // 캐릭터 색상을 원래 색인 파란색으로 변경
            long stopDuration = System.currentTimeMillis() - stopTime; // 멈춘 시간 기록
        }

        // 5초 쿨타임 후 최고 속도 허용
        if (!canReachMaxSpeed && System.currentTimeMillis() - cooldownTime >= 5000) {
            canReachMaxSpeed = true; // 5초 후 최고 속도에 도달할 수 있도록 설정
        }
    }

    // 캐릭터 위치 업데이트
    private void update(double deltaTime) {
        // 최고 속도로 5초 이상 이동하면 멈추는 처리
        handleOverload();

        if (!isStopped) { // 멈추지 않은 상태에서만 이동
            // deltaTime을 이용하여 프레임 독립적으로 이동 처리
            x += dx * currentSpeed * deltaTime;
            y += dy * currentSpeed * deltaTime;

            // 화면 경계 처리
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x + width > 360) x = 360 - width; // 화면 너비에 맞춤
            if (y + height > 576) y = 576 - height; // 화면 높이에 맞춤
        }
    }

    // 캐릭터 그리기
    private void draw(Graphics g) {
        g.setColor(characterColor); // 캐릭터 색상 설정
        g.fillRect(x, y, width, height);
    }

    // 속도 텍스트 그리기
    private void drawSpeed(Graphics g) {
        g.setColor(Color.WHITE); // 텍스트 색상 설정
        g.setFont(new Font("Arial", Font.PLAIN, 20)); // 폰트 설정
        g.drawString("Speed: " + String.format("%.2f", currentSpeed), 10, 30); // 속도 표시
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK); // 배경색을 기본값으로 설정 (검정색)
        draw(g);
        drawSpeed(g); // 속도 표시 추가
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / 1_000_000_000.0; // 초 단위 경과 시간
        lastTime = currentTime;

        decreaseSpeedIfNeeded(); // 0.5초마다 속도 감소 처리
        update(deltaTime); // deltaTime을 이용해 캐릭터 위치 갱신
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        Character game = new Character();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 576); // 게임 화면 크기
        frame.add(game);
        frame.setVisible(true);
    }
}