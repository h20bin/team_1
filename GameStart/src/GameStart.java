import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameStart {

    // 생성자
    public GameStart() {

        JFrame frame = new JFrame("Game Start");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);

        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Press Start to Begin!");

        JButton startButton = new JButton("Start");

        frame.add(panel, BorderLayout.CENTER);

        panel.add(label);

        panel.add(startButton);

        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Random random = new Random();

                int randomValue = random.nextInt(100);

                if (randomValue % 2 == 0) {

                    System.out.println("Even number: " + randomValue);

                } else {

                    System.out.println("Odd number: " + randomValue);

                }

            }

        });

        frame.setVisible(true);

    }

    // main 메서드
    public static void main(String[] args) {

        new GameStart();

    }

}
