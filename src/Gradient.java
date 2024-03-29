import java.awt.*;

public class Gradient extends ToolBar {

    public Gradient(String title, int x, int y, int width, int height, int rows, int columns) {
        super(title, x, y, width, height, rows, columns);
        initToolBar();
    }

    private void initToolBar() {

        TITLE_ON_TOP();

        for (int i = 0; i < 16; i++) {
            addButton(new AButton(new Rectangle(Color.white)));
            buttons.get(i).setTip("R:" + 255 + " G:" + 255 + " B:" + 255);
        }

        for (Button b : buttons) {
            b.setListener(new AListener() {
                @Override
                public void onPress(int x, int y) {
                    b.isClicked(x, y);
                }

                @Override
                public void onRelease() {
                    if (b.isPressed())
                        Board.setColorButton(Board.getColorWindow().getButtons().get(4), b);
                }

                @Override
                public void onHover(int x, int y) {
                    b.hover(x, y);
                }
            });
        }

    }

    @Override
    public void onPress(int x, int y) {
        for (Button b : buttons) {
            b.getListener().onPress(x, y);
        }
    }

}