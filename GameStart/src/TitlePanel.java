import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;

public class TitlePanel extends JPanel {
    private GameManager manager;
    private ImageIcon backgroundImageIcon;
    private Thread musicThread; // 배경 음악을 재생할 스레드

    public TitlePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        backgroundImageIcon = new ImageIcon("Resource/hehe.jpg");
        
        JButton startButton = new JButton("Start");
        startButton.setBounds(180, 150, 100, 50);
        startButton.addActionListener(e -> {
        	stopBackgroundMusic();
        	manager.switchPanel(new LobbyPanel(manager));
        });
        add(startButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(180, 220, 100, 50);
        resetButton.addActionListener(e -> {
            manager.getPlayer().addGold(-manager.getPlayer().getGold());
            System.out.println("Game Reset!");
        });
        
        startBackgroundMusic();
        
        add(resetButton);
        
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(180, 290, 100, 50);
        
        exitButton.addActionListener(e -> {
            System.out.println("게임 종료");
            System.exit(0);
        });
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImageIcon != null) {
            Image backgroundImage = backgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        } else {
            // 이미지가 로드되지 않았을 경우 검정색 배경
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        String titleText = "빵빵 비행단";
        Font titleFont = new Font("맑은 고딕", Font.BOLD, 36);
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(titleText);
        int textHeight = fm.getHeight();
        
        int x = (getWidth() - textWidth) / 2;
        int y = 80;

        g.drawString(titleText, x, y); // 텍스트 그리기
        // Render background or animations
    }
    
    private void startBackgroundMusic() {
        musicThread = new Thread(() -> {
            try {
                String musicFilePath = "GameStart/src/backgroundmusic.mp3"; // 경로 설정
                FileInputStream fileInputStream = new FileInputStream(musicFilePath);
                Player player = new Player(fileInputStream);
                player.play(); // 음악 재생
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    // 배경 음악을 중지하는 메서드
    private void stopBackgroundMusic() {
        if (musicThread != null && musicThread.isAlive()) {
            musicThread.interrupt();
        }
    }

}