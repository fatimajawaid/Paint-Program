import java.awt.*;

public class CButton extends AButton {

    public CButton(int x, int y, int width, int height) {
        super(x + 1, y + 1, width - 2, height - 2);
        hoverColor = new Color(143, 93, 93, 71);
    }

    @Override
    public void paint(Graphics g) {
        super.setStroke(0);
        setText("X");
        super.paint(g);
    }
}

