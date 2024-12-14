import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ForgePanel extends JPanel {
    private GameManager manager;
    private int hpUpgradeCost = 100;
    private int speedUpgradeCost = 150;
    private int attackCycleUpgradeCost = 200;
    private ImageIcon forgebackgroundImageIcon;
    private JLabel goldLabel;  // 현재 골드를 표시할 레이블

    public ForgePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        // 배경 이미지를 로드
        forgebackgroundImageIcon = new ImageIcon(getClass().getResource("/background/forgeback.jpeg"));

        // 업그레이드 비용을 파일에서 로드
        loadUpgradeCosts();

        // 제목 레이블 설정
        JLabel title = new JLabel("Forge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setBounds(95, 30, 300, 50);
        add(title);

        // 현재 골드 표시 레이블
        goldLabel = new JLabel("Gold: " + manager.getPlayer().getGold());
        goldLabel.setFont(new Font("Arial", Font.BOLD, 20));
        goldLabel.setBounds(350, 60, 150, 30);  // 화면 상단에 위치
        add(goldLabel);

        int yPosition = 100;

        // MaxHP 업그레이드 버튼
        JButton hpButton = new JButton("Upgrade MaxHP - " + hpUpgradeCost + " Gold");
        hpButton.setBounds(90, yPosition, 300, 50);
        hpButton.setToolTipText("Increase your maximum HP by 10.");
        hpButton.addActionListener(e -> attemptUpgrade("MaxHP", hpUpgradeCost, () -> {
            Player player = manager.getPlayer();
            player.increaseMaxHP(10);
            JOptionPane.showMessageDialog(this, "MaxHP upgraded!");
            hpUpgradeCost += 50;  // 업그레이드 비용 증가
            player.addGold(50);  // 골드 50 증가
            hpButton.setText("Upgrade MaxHP - " + hpUpgradeCost + " Gold");
            saveUpgradeCosts();  // 업그레이드 비용 저장
            updateGoldLabel();   // 골드 레이블 업데이트
        }));
        add(hpButton);
        yPosition += 60;

        // MaxSpeed 업그레이드 버튼
        JButton speedButton = new JButton("Upgrade MaxSpeed - " + speedUpgradeCost + " Gold");
        speedButton.setBounds(90, yPosition, 300, 50);
        speedButton.setToolTipText("Increase your maximum speed by 0.1.");
        speedButton.addActionListener(e -> attemptUpgrade("MaxSpeed", speedUpgradeCost, () -> {
            Player player = manager.getPlayer();
            player.increaseMaxSpeed(0.1);
            JOptionPane.showMessageDialog(this, "MaxSpeed upgraded!");
            speedUpgradeCost += 50; // 업그레이드 비용 증가
            player.addGold(50);     // 골드 50 증가
            speedButton.setText("Upgrade MaxSpeed - " + speedUpgradeCost + " Gold");
            saveUpgradeCosts();     // 업그레이드 비용 저장
            updateGoldLabel();      // 골드 레이블 업데이트
        }));
        add(speedButton);
        yPosition += 60;

        // Attack Cycle 업그레이드 버튼
        JButton attackCycleButton = new JButton("Upgrade Attack Cycle - " + attackCycleUpgradeCost + " Gold");
        attackCycleButton.setBounds(90, yPosition, 300, 50);
        attackCycleButton.setToolTipText("Reduce your attack cycle by 0.1 seconds.");
        attackCycleButton.addActionListener(e -> attemptUpgrade("Attack Cycle", attackCycleUpgradeCost, () -> {
            Player player = manager.getPlayer();
            player.reduceAttackCycle(0.1);
            JOptionPane.showMessageDialog(this, "Attack Cycle upgraded!");
            attackCycleUpgradeCost += 50; // 업그레이드 비용 증가
            player.addGold(50);           // 골드 50 증가
            attackCycleButton.setText("Upgrade Attack Cycle - " + attackCycleUpgradeCost + " Gold");
            saveUpgradeCosts();           // 업그레이드 비용 저장
            updateGoldLabel();            // 골드 레이블 업데이트
        }));
        add(attackCycleButton);

        // Lobby로 돌아가기 버튼
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(140, 400, 200, 60);
        lobbyButton.setToolTipText("Return to the main lobby.");
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(lobbyButton);
    }

    private void attemptUpgrade(String stat, int cost, Runnable upgradeAction) {
        Player player = manager.getPlayer();
        if (player.getGold() >= cost) {
            player.addGold(-cost);
            if (Math.random() < 0.8) { // 80% 성공률
                upgradeAction.run();
            } else {
                JOptionPane.showMessageDialog(this, "Upgrade failed!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not enough gold!");
        }
    }

    // 골드 레이블을 업데이트하는 메서드
    private void updateGoldLabel() {
        goldLabel.setText("Gold: " + manager.getPlayer().getGold());
    }

    // 업그레이드 비용을 파일에 저장하는 메서드
    private void saveUpgradeCosts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("upgradeCosts.dat"))) {
            oos.writeInt(hpUpgradeCost);
            oos.writeInt(speedUpgradeCost);
            oos.writeInt(attackCycleUpgradeCost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 업그레이드 비용을 파일에서 불러오는 메서드
    private void loadUpgradeCosts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("upgradeCosts.dat"))) {
            this.hpUpgradeCost = ois.readInt();
            this.speedUpgradeCost = ois.readInt();
            this.attackCycleUpgradeCost = ois.readInt();
        } catch (IOException e) {
            // 파일이 없거나 오류가 생기면 기본값을 사용
            System.out.println("No saved upgrade costs found, using default values.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (forgebackgroundImageIcon != null) {
            Image backgroundImage = forgebackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        } else {
            // 이미지가 로드되지 않았을 경우 검정색 배경
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
