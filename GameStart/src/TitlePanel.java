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

    private GameManager manager; // 게임 관리 객체
    private ImageIcon mainbackgroundImageIcon; // 배경 이미지 아이콘
    private Thread musicThread; // 배경 음악을 재생할 스레드
    private Clip musicClip; // 배경 음악 클립
    private JButton startButton; // 시작 버튼
    private JButton resetButton; // 리셋 버튼
    private JButton gameInfoButton; // 게임 정보 버튼
    private JButton exitButton; // 종료 버튼

    // TitlePanel 생성자
    public TitlePanel(GameManager manager) {
        this.manager = manager;
        
        // 레이아웃 설정
        setLayout(null);

        // 배경 이미지 초기화
        mainbackgroundImageIcon = new ImageIcon(getClass().getResource("/background/mainback.jpg"));
        
        // 버튼 생성 및 설정
        createButtons();
        
        // 배경 음악 시작
        startBackgroundMusic();
    }

    // 버튼 생성 및 설정 메서드
    private void createButtons() {
        startButton = new JButton("Start");
        startButton.setBounds(180, 150, 100, 50);
        
        // 시작 버튼 클릭 시 이벤트 처리
        startButton.addActionListener(e -> {
            stopBackgroundMusic(); // 배경 음악 정지
            manager.switchPanel(new LobbyPanel(manager)); // 로비 패널로 전환
        });
        
        // 시작 버튼 패널에 추가
        add(startButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(180, 220, 100, 50);
        
        // 리셋 버튼 클릭 시 이벤트 처리
        resetButton.addActionListener(e -> {
            manager.getPlayer().addGold(-manager.getPlayer().getGold()); // 플레이어 금액 초기화
            System.out.println("Game Reset!"); // 게임 리셋 메시지 출력
        });
        
        // 리셋 버튼 패널에 추가
        add(resetButton);

        gameInfoButton = new JButton("Game Info");
        gameInfoButton.setBounds(180, 290, 100, 50);
        
        // 게임 정보 버튼 클릭 시 이벤트 처리
        gameInfoButton.addActionListener(e -> showGameInfo()); // 게임 설명 표시
        add(gameInfoButton);
        
        exitButton = new JButton("Exit");
        exitButton.setBounds(180, 360, 100, 50);
        
        // 종료 버튼 클릭 시 이벤트 처리
        exitButton.addActionListener(e -> {
            System.out.println("게임 종료"); // 게임 종료 메시지 출력
            System.exit(0); // 프로그램 종료
        });
        
        // 종료 버튼 패널에 추가
        add(exitButton);
    }

    // 패널을 그리는 메서드 (배경 이미지 및 제목 텍스트)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 배경 이미지가 있을 경우 그리기
        if (mainbackgroundImageIcon != null) {
            Image backgroundImage = mainbackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        } else {
            // 이미지가 로드되지 않았을 경우 검정색 배경으로 설정
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight()); // 검정색 배경 채우기
        }
        
        // 제목 텍스트 설정
        String titleText = "빵빵 비행단";
        Font titleFont = new Font("\uB9C8\uB984 \uACE0\uB515", Font.BOLD, 36); // 제목 폰트 설정
        g.setFont(titleFont);
        g.setColor(Color.WHITE); // 텍스트 색상 설정
        
        // 텍스트 크기 계산
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(titleText);
        int textHeight = fm.getHeight();
        
        // 텍스트 위치 계산 (중앙 정렬)
        int x = (getWidth() - textWidth) / 2;
        int y = 80;

        // 제목 텍스트 그리기
        g.drawString(titleText, x, y); // 텍스트 그리기
    }
    
    // 배경 음악 시작 메서드
    private void startBackgroundMusic() {
        try {
            // 클래스패스에서 WAV 파일을 로드
            InputStream musicStream = getClass().getResourceAsStream("/background/mainbackgroundmusic.wav");
            if (musicStream == null) {
                throw new IOException("Music file not found"); // 파일이 없을 경우 예외 처리
            }

            // AudioInputStream을 사용하여 오디오 데이터를 읽어 Clip 객체에 로드
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicStream);
            musicClip = AudioSystem.getClip(); // Clip 객체 생성
            musicClip.open(audioStream); // 오디오 데이터 로드
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생 설정

        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    // 배경 음악을 중지하는 메서드
    private void stopBackgroundMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop(); // 음악 정지
        }
    }
    
    // 게임 설명을 보여주는 메서드
    private void showGameInfo() {
        // 게임 설명 텍스트
    	String gameInfo = 
    		    "게임 설명: StageGame\n\n" +
    		    "-------------------------------\n" +
    		    "스토리 배경:\n" +
    		    "미래의 어느 날, 지구는 정체불명의 적들로 인해 위협받고 있습니다. \n" +
    		    "당신은 최후의 희망이자 용감한 전사로서 이 위기를 해결하기 위해 파견되었습니다. \n" +
    		    "적의 침공을 막고, 위험으로부터 지구를 구해내세요! \n" +
    		    "전쟁의 참혹함 속에서 당신의 용기와 결단력이 필요합니다. \n" +
    		    "모든 선택은 당신의 손에 달려 있습니다. 생과 사의 경계를 넘나드는 순간들이 기다리고 있습니다.\n\n" +
    		    "게임 목표:\n" +
    		    "- 모든 적을 처치하고 목표 지점에 도달해 스테이지를 클리어하세요.\n" +
    		    "- 각 스테이지는 점점 어려워지며, 더 강력한 적들이 등장합니다.\n" +
    		    "- 스테이지를 클리어할 때마다 보상으로 Gold를 획득할 수 있습니다.\n\n" +
    		    "-------------------------------\n" +
    		    "조작법 안내:\n" +
    		    "- A 키: 왼쪽으로 이동\n" +
    		    "- D 키: 오른쪽으로 이동\n" +
    		    "- W 키: 위로 이동\n" +
    		    "- S 키: 아래로 이동\n" +
    		    "- M 키: 총 발사 (누르고 있으면 자동 발사)\n" +
    		    "- 스페이스바: 게임 일시정지 / 재개\n\n" +
    		    "-------------------------------\n" +
    		    "게임 시스템:\n" +
    		    "1. 플레이어 HP: 체력이 0이 되면 게임 오버입니다. 적과의 충돌이나 공격을 피하세요!\n" +
    		    "2. 적들: 각 스테이지에는 다양한 패턴과 속도를 가진 적들이 등장합니다.\n" +
    		    "3. 무기 시스템: 기본 무기로 적을 처치할 수 있으며, 총알을 발사할 때마다 총 소리가 재생됩니다.\n" +
    		    "4. 목표 지점: 모든 적을 처치하면 별 모양의 목표 지점이 생성됩니다. 목표 지점에 도달하면 스테이지 클리어!\n" +
    		    "5. Gold 획득: 적을 처치하거나 스테이지를 클리어하면 Gold를 획득할 수 있습니다. \n" +
    		    "   Gold는 이후 게임에서 다양한 업그레이드에 사용될 수 있습니다.\n\n" +
    		    "-------------------------------\n" +
    		    "특징:\n" +
    		    "- 배경 음악: 몰입감을 높여주는 배경 음악이 게임 중에 재생됩니다.\n" +
    		    "- 효과음: 총 소리와 충돌 소리 등 현실감 있는 효과음을 경험할 수 있습니다.\n" +
    		    "- 반복 스크롤 배경: 스테이지에 맞는 배경이 끊임없이 스크롤됩니다.\n" +
    		    "- 일시정지 기능: 스페이스바나 Pause 버튼을 눌러 언제든지 게임을 일시정지할 수 있습니다.\n\n" +
    		    "-------------------------------\n" +
    		    "플레이 팁:\n" +
    		    "1. 지속적으로 이동: 적의 공격을 피하기 위해 항상 움직이세요.\n" +
    		    "2. 자동 발사 활용: M 키를 눌러 자동 발사를 활용하면 적을 빠르게 처치할 수 있습니다.\n" +
    		    "3. 적의 패턴 파악: 각 적은 고유한 이동 패턴이 있습니다. 패턴을 익히면 쉽게 피할 수 있습니다.\n" +
    		    "4. 체력 관리: 체력이 낮아지면 무리하지 말고 안전하게 플레이하세요.\n\n" +
    		    "-------------------------------\n" +
    		    "행운을 빕니다, 용사여!\n" +
    		    "지구의 운명은 당신의 손에 달려 있습니다!\n" +
    		    "-------------------------------";

        // 게임 정보 텍스트 영역 생성
        JTextArea textArea = new JTextArea(gameInfo);
        textArea.setEditable(false); // 편집 불가
        textArea.setWrapStyleWord(true); // 단어가 잘리지 않도록 설정
        textArea.setLineWrap(true); // 줄 바꿈을 자동으로 해줌

        // 스크롤 패널 생성
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // 원하는 크기 조절

        // 게임 정보 다이얼로그 표시
        JOptionPane.showMessageDialog(this, scrollPane, "Game Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
