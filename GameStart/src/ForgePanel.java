import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ForgePanel extends JPanel {
    private GameManager manager;
    private int hpUpgradeCost;
    private int speedUpgradeCost;
    private int attackCycleUpgradeCost;
    private ImageIcon forgebackgroundImageIcon;
    private JLabel goldLabel;

    public ForgePanel(GameManager manager) {
        this.manager = manager;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(null);
        loadBackgroundImage();
        initializeUpgradeCosts();
        createAndAddTitleLabel();
        createAndAddGoldLabel();
        createUpgradeButtons();
        createLobbyButton();
    }

    private void loadBackgroundImage() {
        forgebackgroundImageIcon = new ImageIcon(getClass().getResource("/background/forgeback.jpeg"));
    }

    private void initializeUpgradeCosts() {
        hpUpgradeCost = 100;
        speedUpgradeCost = 150;
        attackCycleUpgradeCost = 200;
        loadUpgradeCostsFromFile();
    }

    private void createAndAddTitleLabel() {
        JLabel title = createTitleLabel();
        add(title);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Forge", SwingConstants.CENTER);
        configureTitleLabel(titleLabel);
        return titleLabel;
    }

    private void configureTitleLabel(JLabel titleLabel) {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(95, 30, 300, 50);
    }

    private void createAndAddGoldLabel() {
        goldLabel = createGoldLabel();
        add(goldLabel);
    }

    private JLabel createGoldLabel() {
        JLabel goldLabel = new JLabel("Gold: " + manager.getPlayer().getGold());
        configureGoldLabel(goldLabel);
        return goldLabel;
    }

    private void configureGoldLabel(JLabel goldLabel) {
        goldLabel.setFont(new Font("Arial", Font.BOLD, 20));
        goldLabel.setBounds(350, 60, 150, 30);
    }

    private void createUpgradeButtons() {
        int yPosition = 100;
        createAndAddHPUpgradeButton(yPosition);
        yPosition += 60;
        createAndAddSpeedUpgradeButton(yPosition);
        yPosition += 60;
        createAndAddAttackCycleUpgradeButton(yPosition);
    }

    private void createAndAddHPUpgradeButton(int yPosition) {
        JButton hpButton = createHPUpgradeButton();
        configureButton(hpButton, yPosition);
        add(hpButton);
    }

    private JButton createHPUpgradeButton() {
        JButton hpButton = new JButton("Upgrade MaxHP - " + hpUpgradeCost + " Gold");
        configureHPButtonAction(hpButton);
        return hpButton;
    }

    private void configureHPButtonAction(JButton hpButton) {
        hpButton.setToolTipText("Increase your maximum HP by 10.");
        hpButton.addActionListener(e -> handleHPUpgrade(hpButton));
    }

    private void handleHPUpgrade(JButton hpButton) {
        attemptUpgrade("MaxHP", hpUpgradeCost, () -> performHPUpgrade(hpButton));
    }

    private void performHPUpgrade(JButton hpButton) {
        Player player = manager.getPlayer();
        player.increaseMaxHP(10);
        showUpgradeSuccessMessage("MaxHP upgraded!");
        increaseHPUpgradeCost();
        awardGoldToPlayer(50);
        updateHPButtonText(hpButton);
        saveUpgradeCostsToFile();
        updateGoldLabel();
    }

    private void increaseHPUpgradeCost() {
        hpUpgradeCost += 50;
    }

    private void updateHPButtonText(JButton hpButton) {
        hpButton.setText("Upgrade MaxHP - " + hpUpgradeCost + " Gold");
    }

    private void createAndAddSpeedUpgradeButton(int yPosition) {
        JButton speedButton = createSpeedUpgradeButton();
        configureButton(speedButton, yPosition);
        add(speedButton);
    }

    private JButton createSpeedUpgradeButton() {
        JButton speedButton = new JButton("Upgrade MaxSpeed - " + speedUpgradeCost + " Gold");
        configureSpeedButtonAction(speedButton);
        return speedButton;
    }

    private void configureSpeedButtonAction(JButton speedButton) {
        speedButton.setToolTipText("Increase your maximum speed by 0.1.");
        speedButton.addActionListener(e -> handleSpeedUpgrade(speedButton));
    }

    private void handleSpeedUpgrade(JButton speedButton) {
        attemptUpgrade("MaxSpeed", speedUpgradeCost, () -> performSpeedUpgrade(speedButton));
    }

    private void performSpeedUpgrade(JButton speedButton) {
        Player player = manager.getPlayer();
        player.increaseMaxSpeed(0.1);
        showUpgradeSuccessMessage("MaxSpeed upgraded!");
        increaseSpeedUpgradeCost();
        awardGoldToPlayer(50);
        updateSpeedButtonText(speedButton);
        saveUpgradeCostsToFile();
        updateGoldLabel();
    }

    private void increaseSpeedUpgradeCost() {
        speedUpgradeCost += 50;
    }

    private void updateSpeedButtonText(JButton speedButton) {
        speedButton.setText("Upgrade MaxSpeed - " + speedUpgradeCost + " Gold");
    }

    private void createAndAddAttackCycleUpgradeButton(int yPosition) {
        JButton attackCycleButton = createAttackCycleUpgradeButton();
        configureButton(attackCycleButton, yPosition);
        add(attackCycleButton);
    }

    private JButton createAttackCycleUpgradeButton() {
        JButton attackCycleButton = new JButton("Upgrade Attack Cycle - " + attackCycleUpgradeCost + " Gold");
        configureAttackCycleButtonAction(attackCycleButton);
        return attackCycleButton;
    }

    private void configureAttackCycleButtonAction(JButton attackCycleButton) {
        attackCycleButton.setToolTipText("Reduce your attack cycle by 0.1 seconds.");
        attackCycleButton.addActionListener(e -> handleAttackCycleUpgrade(attackCycleButton));
    }

    private void handleAttackCycleUpgrade(JButton attackCycleButton) {
        attemptUpgrade("Attack Cycle", attackCycleUpgradeCost, () -> performAttackCycleUpgrade(attackCycleButton));
    }

    private void performAttackCycleUpgrade(JButton attackCycleButton) {
        Player player = manager.getPlayer();
        player.reduceAttackCycle(0.1);
        showUpgradeSuccessMessage("Attack Cycle upgraded!");
        increaseAttackCycleUpgradeCost();
        awardGoldToPlayer(50);
        updateAttackCycleButtonText(attackCycleButton);
        saveUpgradeCostsToFile();
        updateGoldLabel();
    }

    private void increaseAttackCycleUpgradeCost() {
        attackCycleUpgradeCost += 50;
    }

    private void updateAttackCycleButtonText(JButton attackCycleButton) {
        attackCycleButton.setText("Upgrade Attack Cycle - " + attackCycleUpgradeCost + " Gold");
    }

    private void createLobbyButton() {
        JButton lobbyButton = createLobbyButtonComponent();
        configureButton(lobbyButton, 400);
        add(lobbyButton);
    }

    private JButton createLobbyButtonComponent() {
        JButton lobbyButton = new JButton("Go to Lobby");
        configureLobbyButtonAction(lobbyButton);
        return lobbyButton;
    }

    private void configureLobbyButtonAction(JButton lobbyButton) {
        lobbyButton.setToolTipText("Return to the main lobby.");
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
    }

    private void configureButton(JButton button, int yPosition) {
        button.setBounds(90, yPosition, 300, 50);
    }

    private void attemptUpgrade(String stat, int cost, Runnable upgradeAction) {
        Player player = manager.getPlayer();
        if (playerHasEnoughGold(player, cost)) {
            processUpgrade(player, cost, upgradeAction);
        } else {
            showNotEnoughGoldMessage();
        }
    }

    private boolean playerHasEnoughGold(Player player, int cost) {
        return player.getGold() >= cost;
    }

    private void processUpgrade(Player player, int cost, Runnable upgradeAction) {
        deductGold(player, cost);
        if (upgradeSuccessful()) {
            upgradeAction.run();
        } else {
            showUpgradeFailureMessage();
        }
    }

    private void deductGold(Player player, int cost) {
        player.addGold(-cost);
    }

    private boolean upgradeSuccessful() {
        return Math.random() < 0.8;
    }

    private void showUpgradeSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showUpgradeFailureMessage() {
        JOptionPane.showMessageDialog(this, "Upgrade failed!");
    }

    private void showNotEnoughGoldMessage() {
        JOptionPane.showMessageDialog(this, "Not enough gold!");
    }

    private void awardGoldToPlayer(int amount) {
        manager.getPlayer().addGold(amount);
    }

    private void updateGoldLabel() {
        goldLabel.setText("Gold: " + manager.getPlayer().getGold());
    }

    private void saveUpgradeCostsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("upgradeCosts.dat"))) {
            oos.writeInt(hpUpgradeCost);
            oos.writeInt(speedUpgradeCost);
            oos.writeInt(attackCycleUpgradeCost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUpgradeCostsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("upgradeCosts.dat"))) {
            hpUpgradeCost = ois.readInt();
            speedUpgradeCost = ois.readInt();
            attackCycleUpgradeCost = ois.readInt();
        } catch (IOException e) {
            System.out.println("No saved upgrade costs found, using default values.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super
        .paintComponent(g);
        g.drawImage(forgebackgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
