import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StageSelection::new);
    }
}

class StageSelection {
    private JFrame frame;

    public StageSelection() {
        frame = new JFrame("Stage Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 587);
        frame.setLayout(null);

        
        JLabel background = new JLabel(new ImageIcon("res/hehe.jpg"));
        background.setBounds(0, 0, 600, 587);
        frame.add(background);

        // 타이틀
        JLabel title = new JLabel("Select a Stage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBounds(150, 50, 300, 50);
        background.add(title);

        
        JButton stage1Button = createButton("Stage 1", 100, 150);
        JButton stage2Button = createButton("Stage 2", 100, 250);
        JButton stage3Button = createButton("Stage 3", 100, 350);
        JButton stage4Button = createButton("Stage 4",100,450);
  
        stage1Button.addActionListener(e -> openGameWindow(1));
        stage2Button.addActionListener(e -> openGameWindow(2));
        stage3Button.addActionListener(e -> openGameWindow(3));
        stage4Button.addActionListener(e->openGameWindow(4));
        
        background.add(stage1Button);
        background.add(stage2Button);
        background.add(stage3Button);
        background.add(stage4Button);
        
        frame.setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 50);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        return button;
    }

    private void openGameWindow(int stage) {
        frame.dispose();
         JFrame gameFrame = new JFrame("Shooting Game - Stage" + stage);
         Character characterPanel = new Character();
         
         gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
         gameFrame.setSize(360,576);
         gameFrame.add(characterPanel);
         gameFrame.setVisible(true);
    }
}

// 게임 화면
class GameWindow {
    private JFrame frame;
    private Timer gameLoopTimer;

    public GameWindow(int stage) {
        frame = new JFrame("Shooting Game - Stage " + stage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 게임 캔버스
        GameCanvas canvas = new GameCanvas(stage);
        frame.add(canvas);
        frame.setVisible(true);

        // 게임 루프
        gameLoopTimer = new Timer(16, e -> {
            canvas.update();
            canvas.repaint();
        });
        gameLoopTimer.start();
    }
}

// 게임 캔버스
class GameCanvas extends JPanel {
    private int stage;
    private int playerX, playerY;
    private int enemyX, enemyY;
    private Random random;

    public GameCanvas(int stage) {
        this.stage = stage;
        this.playerX = 400; // 플레이어 초기 위치
        this.playerY = 500;
        this.enemyX = 400; // 적 초기 위치
        this.enemyY = 100;
        this.random = new Random();
        setBackground(Color.BLACK);
    }

    // 업데이트 로직
    public void update() {
        // 적 움직임 업데이트 (임의의 패턴 추가)
        enemyX += random.nextInt(11) - 5; // 좌우로 랜덤 이동
        enemyY += random.nextInt(3); // 아래로 천천히 이동

        // 화면 경계 체크
        if (enemyX < 0) enemyX = 0;
        if (enemyX > getWidth() - 50) enemyX = getWidth() - 50;
        if (enemyY > getHeight()) enemyY = 0; // 화면 끝으로 가면 초기화
    }

    // 화면 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 스테이지 표시
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Stage: " + stage, 10, 20);

        // 플레이어 그리기
        g.setColor(Color.BLUE);
        g.fillRect(playerX - 15, playerY - 15, 30, 30);

        // 적 그리기
        g.setColor(Color.RED);
        g.fillOval(enemyX - 20, enemyY - 20, 40, 40);

        // TODO: 추가 그래픽 요소 및 총알 그리기
    }
}
