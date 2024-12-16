import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TitlePanel extends DoubleBufferPanel {

    private GameManager manager; // 게임 관리 객체
    private ImageIcon mainbackgroundImageIcon; // 배경 이미지 아이콘
    private Thread musicThread; // 배경 음악을 재생할 스레드
    private Clip musicClip; // 배경 음악 클립
    private JButton startButton; // 시작 버튼
    private JButton resetButton; // 리셋 버튼
    private JButton gameInfoButton; // 게임 정보 버튼
    private JButton exitButton; // 종료 버튼
    private BufferedImage[] titlePanel;
    private BufferedImage[] title1;
    private BufferedImage[] title2;
    private BufferedImage[] title3;
    private BufferedImage[] start_button;
    private BufferedImage[] reset_button;
    private BufferedImage[] exit_button;
    
    
    private int animationStep = 0; // 현재 애니메이션의 순서
    private Timer animationTimer; // 애니메이션 타이머
    
    private ArrayList<ImageData> imagelist;
    private ArrayList<ImageData> button_imagelist;
    private int animationState = 0; // 현재 애니메이션 상태
    private int animationState2 = 0; // 현재 애니메이션 상태
    private int animationSubIndex = 0; // 현재 상태 내에서의 프레임 인덱스
    
    private int[][] animationFrame;
    private int[][] button_animationFrame;
    
    private int count;
    
    private int title1_x;
    private int title1_y;
    
    private int title2_x;
    private int title2_y;
    
    private int title3_x;
    private int title3_y;
    
    private int panel1_x;
    private int panel1_y;
    private int panel2_x;
    private int panel2_y;
    
    private int button1_x;
    private int button1_y;
    private int button2_x;
    private int button2_y;
    private int button3_x;
    private int button3_y;

    // TitlePanel 생성자
    public TitlePanel(GameManager manager) {
        this.manager = manager;
        try {
        	this.titlePanel = loadSpriteSheet("/UI/Panel-Sheet.png", 208, 82);
        	this.title1 = loadSpriteSheet("/UI/bang2-Sheet.png", 192, 64);
        	this.title2 = loadSpriteSheet("/UI/bang3-Sheet.png", 192, 64);
			this.title3 = loadSpriteSheet("/UI/title-Sheet1.png", 256, 1);
			this.start_button = loadSpriteSheet("/UI/start.png",2,2);
			initiallizeButtonImageList();
			
			this.title1_x = 27;
			this.title1_y = 27;
		    
		    this.title2_x = 27;
		    this.title2_y = 129;
		    
		    this.title3_x = 20;
		    this.title3_y = 220;
		    
		    this.panel1_x = 18;
		    this.panel1_y = 18;
		    this.panel2_x = 18;
		    this.panel2_y = 120;
		    
		    this.button1_x = 50;
		    this.button1_y = 400;
		    this.button2_x = 250;
		    this.button2_y = 500;
		    this.button3_x = 250;
		    this.button3_y = 600;
		    
		    
		    initializeAnimationFrames();
		    
	        initializeImageList();
	        startAnimation();
	        startButtonAnimation(); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 레이아웃 설정
        setLayout(null);

        // 배경 이미지 초기화
        mainbackgroundImageIcon = new ImageIcon(getClass().getResource("/background/titleback.png"));
        
        // 버튼 생성 및 설정
        createButtons();
        
        // 배경 음악 시작
        startBackgroundMusic();
    }

    private void initializeImageList() {
        imagelist = new ArrayList<>();

        imagelist.add(new ImageData(this.titlePanel[0], this.panel1_x, this.panel1_y));
        imagelist.add(new ImageData(this.titlePanel[0], this.panel2_x, this.panel2_y));

        imagelist.add(new ImageData(this.title1[0], this.title1_x, this.title1_y));
        imagelist.add(new ImageData(this.title2[0], this.title2_x, this.title2_y));

        imagelist.add(new ImageData(this.title1[1], this.title1_x, this.title1_y));
        imagelist.add(new ImageData(this.title1[2], this.title1_x, this.title1_y));
        imagelist.add(new ImageData(this.title1[3], this.title1_x, this.title1_y));
        imagelist.add(new ImageData(this.title1[4], this.title1_x, this.title1_y));

        imagelist.add(new ImageData(this.title2[1], this.title2_x, this.title2_y));
        imagelist.add(new ImageData(this.title2[2], this.title2_x, this.title2_y));
        imagelist.add(new ImageData(this.title2[3], this.title2_x, this.title2_y));
        imagelist.add(new ImageData(this.title2[4], this.title2_x, this.title2_y));

        imagelist.add(new ImageData(this.title3[0], this.title3_x, this.title3_y));
        imagelist.add(new ImageData(this.title3[1], this.title3_x, this.title3_y + 1));
        imagelist.add(new ImageData(this.title3[2], this.title3_x, this.title3_y + 2));
        imagelist.add(new ImageData(this.title3[3], this.title3_x, this.title3_y + 3));
        imagelist.add(new ImageData(this.title3[4], this.title3_x, this.title3_y + 4));
        imagelist.add(new ImageData(this.title3[5], this.title3_x, this.title3_y + 5));
        imagelist.add(new ImageData(this.title3[6], this.title3_x, this.title3_y + 6));
        imagelist.add(new ImageData(this.title3[7], this.title3_x, this.title3_y + 7));
        imagelist.add(new ImageData(this.title3[8], this.title3_x, this.title3_y + 8));
        imagelist.add(new ImageData(this.title3[9], this.title3_x, this.title3_y + 9));

        imagelist.add(new ImageData(this.title3[10], this.title3_x, this.title3_y + 10));
        imagelist.add(new ImageData(this.title3[11], this.title3_x, this.title3_y + 11));
        imagelist.add(new ImageData(this.title3[12], this.title3_x, this.title3_y + 12));
        imagelist.add(new ImageData(this.title3[13], this.title3_x, this.title3_y + 13));
        imagelist.add(new ImageData(this.title3[14], this.title3_x, this.title3_y + 14));
        imagelist.add(new ImageData(this.title3[15], this.title3_x, this.title3_y + 15));
        imagelist.add(new ImageData(this.title3[16], this.title3_x, this.title3_y + 16));
        imagelist.add(new ImageData(this.title3[17], this.title3_x, this.title3_y + 17));
        imagelist.add(new ImageData(this.title3[18], this.title3_x, this.title3_y + 18));
        imagelist.add(new ImageData(this.title3[19], this.title3_x, this.title3_y + 19));
        
        imagelist.add(new ImageData(this.title3[20], this.title3_x, this.title3_y + 20));
        imagelist.add(new ImageData(this.title3[21], this.title3_x, this.title3_y + 21));
        imagelist.add(new ImageData(this.title3[22], this.title3_x, this.title3_y + 22));
        imagelist.add(new ImageData(this.title3[23], this.title3_x, this.title3_y + 23));
        imagelist.add(new ImageData(this.title3[24], this.title3_x, this.title3_y + 24));
        imagelist.add(new ImageData(this.title3[25], this.title3_x, this.title3_y + 25));
        imagelist.add(new ImageData(this.title3[26], this.title3_x, this.title3_y + 26));
        imagelist.add(new ImageData(this.title3[27], this.title3_x, this.title3_y + 27));
        imagelist.add(new ImageData(this.title3[28], this.title3_x, this.title3_y + 28));
        imagelist.add(new ImageData(this.title3[29], this.title3_x, this.title3_y + 29));

        imagelist.add(new ImageData(this.title3[30], this.title3_x, this.title3_y + 30));
        imagelist.add(new ImageData(this.title3[31], this.title3_x, this.title3_y + 31));
        imagelist.add(new ImageData(this.title3[32], this.title3_x, this.title3_y + 32));
        imagelist.add(new ImageData(this.title3[33], this.title3_x, this.title3_y + 33));
        imagelist.add(new ImageData(this.title3[34], this.title3_x, this.title3_y + 34));
        imagelist.add(new ImageData(this.title3[35], this.title3_x, this.title3_y + 35));
        imagelist.add(new ImageData(this.title3[36], this.title3_x, this.title3_y + 36));
        imagelist.add(new ImageData(this.title3[37], this.title3_x, this.title3_y + 37));
        imagelist.add(new ImageData(this.title3[38], this.title3_x, this.title3_y + 38));
        imagelist.add(new ImageData(this.title3[39], this.title3_x, this.title3_y + 39));

        imagelist.add(new ImageData(this.title3[40], this.title3_x, this.title3_y + 40));
        imagelist.add(new ImageData(this.title3[41], this.title3_x, this.title3_y + 41));
        imagelist.add(new ImageData(this.title3[42], this.title3_x, this.title3_y + 42));
        imagelist.add(new ImageData(this.title3[43], this.title3_x, this.title3_y + 43));
        imagelist.add(new ImageData(this.title3[44], this.title3_x, this.title3_y + 44));
        imagelist.add(new ImageData(this.title3[45], this.title3_x, this.title3_y + 45));
        imagelist.add(new ImageData(this.title3[46], this.title3_x, this.title3_y + 46));
        imagelist.add(new ImageData(this.title3[47], this.title3_x, this.title3_y + 47));
        imagelist.add(new ImageData(this.title3[48], this.title3_x, this.title3_y + 48));
        imagelist.add(new ImageData(this.title3[49], this.title3_x, this.title3_y + 49));

        imagelist.add(new ImageData(this.title3[50], this.title3_x, this.title3_y + 50));
        imagelist.add(new ImageData(this.title3[51], this.title3_x, this.title3_y + 51));
        imagelist.add(new ImageData(this.title3[52], this.title3_x, this.title3_y + 52));
        imagelist.add(new ImageData(this.title3[53], this.title3_x, this.title3_y + 53));
        imagelist.add(new ImageData(this.title3[54], this.title3_x, this.title3_y + 54));
        imagelist.add(new ImageData(this.title3[55], this.title3_x, this.title3_y + 55));
        imagelist.add(new ImageData(this.title3[56], this.title3_x, this.title3_y + 56));
        imagelist.add(new ImageData(this.title3[57], this.title3_x, this.title3_y + 57));
        imagelist.add(new ImageData(this.title3[58], this.title3_x, this.title3_y + 58));
        imagelist.add(new ImageData(this.title3[59], this.title3_x, this.title3_y + 59));

        imagelist.add(new ImageData(this.title3[60], this.title3_x, this.title3_y + 60));
        imagelist.add(new ImageData(this.title3[61], this.title3_x, this.title3_y + 61));
        imagelist.add(new ImageData(this.title3[62], this.title3_x, this.title3_y + 62));
        imagelist.add(new ImageData(this.title3[63], this.title3_x, this.title3_y + 63));
        imagelist.add(new ImageData(this.title3[64], this.title3_x, this.title3_y + 64));
        imagelist.add(new ImageData(this.titlePanel[0], this.panel2_x-3,this.panel2_y + 100 ));
        imagelist.add(new ImageData(this.titlePanel[0], this.panel2_x+50,this.panel2_y + 100 ));
        
    }
    
    private void initiallizeButtonImageList() {
    	button_imagelist = new ArrayList<>();
        count = 0;
        
        for(int i = 0; i < 32; i ++) {
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 2, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 4, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 6, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 8, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 10, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 12, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 14, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 16, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 18, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 20, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 22, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 24, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 26, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 28, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 30, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 32, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 34, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 36, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 38, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 40, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 42, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 44, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 46, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 48, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 50, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 52, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 54, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 56, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 58, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 60, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 62, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 64, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 66, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 68, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 70, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 72, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 74, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 76, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 78, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 80, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 82, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 84, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 86, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 88, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 90, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 92, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 94, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 96, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 98, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 100, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 102, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 104, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 106, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 108, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 110, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 112, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 114, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 116, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 118, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 120, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 122, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 124, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 126, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 128, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 130, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 132, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 134, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 136, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 138, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 140, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 142, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 144, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 146, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 148, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 150, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 152, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 154, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 156, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 158, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 160, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 162, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 164, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 166, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 168, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 170, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 172, this.button1_y));
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 174, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 176, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 178, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 180, this.button1_y)); 
        	count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 182, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 184, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 186, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 188, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 190, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 192, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 194, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 196, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 198, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 200, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 202, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 204, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 206, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 208, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 210, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 212, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 214, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 216, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 218, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 220, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 222, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 224, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 226, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 228, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 230, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 232, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 234, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 236, this.button1_y)); 
			count++;
        	button_imagelist.add(new ImageData(this.start_button[count], this.button1_x + 238, this.button1_y)); 
			count++;

	        this.button1_y +=2;
	    }
    
    }

    private void initializeAnimationFrames() {
    	animationFrame = new int[][]{
				  {0,1,2,3,77,78,},
				  {0,1,77,78,4,8,12,22,32,42,52,62},
				  {0,1,77,78,5,9,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,6,10,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,18,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,21,31,41,51,61,71,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,72,21,31,41,51,61,71,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,73,72,21,31,41,51,61,71,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,74,73,72,21,31,41,51,61,71,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,75,74,73,72,21,31,41,51,61,71,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},
				  {0,1,77,78,7,11,76,75,74,73,72,21,31,41,51,61,71,20,30,40,50,60,70,19,29,39,49,59,69,28,38,48,58,68,17,27,37,47,57,67,16,26,36,46,56,66,15,25,35,45,55,65,14,24,34,44,54,64,13,23,33,43,53,63,12,22,32,42,52,62},		  
				  };
				  
		button_animationFrame = new int[8][480];
		List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < button_imagelist.size(); i++) {
            indexList.add(i);
        }

        // 2. 무작위 섞기
        Collections.shuffle(indexList);

        // 3. 480개씩 나누어 button_animationFrame에 저장
        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 480; j++) {
                button_animationFrame[i][j] = indexList.get(index++);
            }
        }
		
    }
    
    private void startAnimation() {
        animationState = 0; // 시작 상태 초기화
        animationTimer = new Timer(400, e -> {
            if (animationState < animationFrame.length - 1) {
                animationState++; // 다음 상태로 이동
            } else {
                animationTimer.stop(); // 마지막 상태에 도달하면 타이머 정지
            }
            repaint(); // 화면 갱신
        });
        animationTimer.start(); // 타이머 시작
    }
    
    private void startButtonAnimation() {
        animationState2 = 0; // 시작 상태 초기화
        animationTimer = new Timer(1000, e -> {
            if (  animationState2 < button_animationFrame.length - 1) {
                animationState++; // 다음 상태로 이동
            } else if (animationState == button_animationFrame.length - 1) {
                // 마지막 프레임: 1~8 상태를 동시에 표시
            	  animationState2++;
                repaint(); // 화면 갱신
            } else {
                animationTimer.stop(); // 애니메이션 완료
            }
            repaint(); // 화면 갱신
        });
        animationTimer.start(); // 타이머 시작
    }

    
    // 버튼 생성 및 설정 메서드
    private void createButtons() {
        startButton = new JButton("Start");
        startButton.setBounds(380, 400, 80, 40);
        
        // 시작 버튼 클릭 시 이벤트 처리
        startButton.addActionListener(e -> {
            stopBackgroundMusic(); // 배경 음악 정지
            manager.switchPanel(new LobbyPanel(manager)); // 로비 패널로 전환
        });
        
        // 시작 버튼 패널에 추가
        add(startButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(380, 500, 80, 40);
        
        // 리셋 버튼 클릭 시 이벤트 처리
        resetButton.addActionListener(e -> {
            manager.getPlayer().addGold(-manager.getPlayer().getGold()); // 플레이어 금액 초기화
            System.out.println("Game Reset!"); // 게임 리셋 메시지 출력
        });
        
        // 리셋 버튼 패널에 추가
        add(resetButton);

        gameInfoButton = new JButton("Game Info");
        gameInfoButton.setBounds(40, 600, 80, 40);
        
        // 게임 정보 버튼 클릭 시 이벤트 처리
        gameInfoButton.addActionListener(e -> showGameInfo()); // 게임 설명 표시
        add(gameInfoButton);
        
        exitButton = new JButton("Exit");
        exitButton.setBounds(380, 600, 80, 40);
        
        // 종료 버튼 클릭 시 이벤트 처리
        exitButton.addActionListener(e -> {
            System.out.println("게임 종료"); // 게임 종료 메시지 출력
            System.exit(0); // 프로그램 종료
        });
        
        // 종료 버튼 패널에 추가
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        initBuffer();

        Graphics bg = getBufferedGraphics();
        if (bg != null) {
            // 배경 이미지 그리기
            if (mainbackgroundImageIcon != null) {
                Image backgroundImage = mainbackgroundImageIcon.getImage();
                bg.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                bg.setColor(Color.BLACK); // 이미지가 없으면 배경색을 검정으로 설정
                bg.fillRect(0, 0, getWidth(), getHeight());
            }

            // 애니메이션 그리기
            if (animationState < animationFrame.length) {
                int[] frames = animationFrame[animationState];
                for (int frame : frames) {
                    if (frame < imagelist.size()) {
                        ImageData imageData = imagelist.get(frame);
                        bg.drawImage(imageData.image, imageData.x, imageData.y, this);
                    }
                }
            }
            
            // button_animationFrame 그리기
            if (  animationState2 < button_animationFrame.length) {
                // 현재 상태에 있는 프레임들을 하나씩 그리기
                for (int index : button_animationFrame[  animationState2]) {
                    ImageData imageData = button_imagelist.get(index);
                    g.drawImage(imageData.image, imageData.x, imageData.y, this);
                    System.out.println("is draw");
                }
            } else {
                // 마지막 상태: 1~8 프레임을 동시에 그리기
                for (int state = 0; state < 8; state++) {
                    for (int index : button_animationFrame[state]) {
                        ImageData imageData = button_imagelist.get(index);
                        g.drawImage(imageData.image, imageData.x, imageData.y, this);
                    }
                }
            }
        }
        super.paintComponent(g); // 더블 버퍼링된 화면을 그리기
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
    
	// 유틸리티 메서드: 스프라이트 시트를 로드합니다.
    private BufferedImage[] loadSpriteSheet(String resourcePath, int frameWidth, int frameHeight) throws IOException {
        return new SpriteSheet(resourcePath, frameWidth, frameHeight).getAllFrames();
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
