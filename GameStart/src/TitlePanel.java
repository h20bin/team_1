import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TitlePanel extends JPanel {
    private GameManager manager;
    private ImageIcon mainbackgroundImageIcon;
    private Thread musicThread; // 배경 음악을 재생할 스레드
	private Clip musicClip;

    public TitlePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        mainbackgroundImageIcon = new ImageIcon(getClass().getResource("/background/mainback.jpg"));
        
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
        if (mainbackgroundImageIcon != null) {
            Image backgroundImage = mainbackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
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
    	try {
             // 클래스패스에서 WAV 파일을 로드
             InputStream musicStream = getClass().getResourceAsStream("/background/mainbackgroundmusic.wav");
             if (musicStream == null) {
                 throw new IOException("Music file not found");
             }

             // AudioInputStream을 사용하여 오디오 데이터를 읽어 Clip 객체에 로드
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicStream);
             musicClip = AudioSystem.getClip();
             musicClip.open(audioStream);
             musicClip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생

         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     // 배경 음악을 중지하는 메서드
     private void stopBackgroundMusic() {
         if (musicClip != null && musicClip.isRunning()) {
             musicClip.stop();
         }
     }

}