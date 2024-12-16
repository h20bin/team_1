import javax.swing.*;
import java.awt.*;

public class DoubleBufferPanel extends JPanel {

    private static final long serialVersionUID = 1L; // 직렬화 ID 추가
    private Image offscreen;     // 오프스크린 버퍼 이미지
    private Graphics bg;         // 오프스크린 버퍼 그래픽 객체
    private Dimension dim;       // 패널의 현재 크기

    public DoubleBufferPanel() {
        setBackground(Color.WHITE); // 기본 배경 색상
    }

    /**
     * 버퍼를 초기화하는 메서드
     */
    protected void initBuffer() {
        if (offscreen == null || dim == null || !dim.equals(getSize())) {
            dim = getSize();
            offscreen = createImage(dim.width, dim.height);
            bg = offscreen.getGraphics();
        }
    }

    /**
     * 버퍼를 가져오는 메서드
     */
    protected Graphics getBufferedGraphics() {
        initBuffer();
        return bg;
    }

    /**
     * 화면에 버퍼를 그리는 메서드
     */
    protected void renderBuffer(Graphics g) {
        if (offscreen != null) {
            g.drawImage(offscreen, 0, 0, this);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        initBuffer(); // 버퍼 초기화
        renderBuffer(g); // 화면에 버퍼 출력
    }
}
