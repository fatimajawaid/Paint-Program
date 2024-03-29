import java.awt.*;

public class ShapeBar extends ToolBar {

    public ShapeBar(String title, int x, int y, int width, int height, int rows, int columns, int leftButtons, int rightButtons) {
        super(title, x, y, width, height, rows, columns, leftButtons, rightButtons);
        initToolBar();
    }

    private void initToolBar() {

        Button freeDraw = new TButton(new Fdraw(Color.black));
        freeDraw.setText("Free Draw");
        addLeftButton(freeDraw);

        addButton(new TButton(new Circle(), "Circle"));
        addButton(new TButton(new Rectangle(), "Rectangle"));
        addButton(new TButton(new Rtriangle(), "Right Triangle"));
        addButton(new TButton(new Etriangle(), "Equilateral Triangle"));
        addButton(new TButton(new Hex(), "Hexagon"));
        addButton(new TButton(new Pent(), "Pentagram"));

        Button eraserButton = new TButton(new Bcurve(Color.black));
        eraserButton.setText("Bezier Curve");
        addRightButton(eraserButton);

        int j = 1;
        for (Button button : buttons) {
            int finalI = j;
            button.setListener(new AListener() {
                @Override
                public void onPress(int x, int y) {

                    button.isClicked(x, y);

                    if (button.isPressed())
                        Board.setShapeChoice(finalI);
                }

                @Override
                public void onRelease() {

                }

                @Override
                public void onHover(int x, int y) {
                    button.hover(x, y);
                }
            });
            j++;
        }

    }

    @Override
    public void onPress(int x, int y) {
        // gets the index of the toggle button pressed if any so that it is later depressed after any click on the toolbar
        int index = -1;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isPressed() && buttons.get(i) instanceof TButton) {
                index = i;
            }
        }

        // buttons pressed
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getListener() != null) {
                buttons.get(i).getListener().onPress(x, y);
                if (i != index && index != -1 && buttons.get(i) instanceof TButton && buttons.get(i).isPressed()) {
                    // button previously toggled is depressed
                    buttons.get(index).setPressed(false);
                }
            }

        }
    }
}