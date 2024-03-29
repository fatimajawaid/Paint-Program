import java.util.LinkedList;

public class StrokeBar extends ToolBar {

    public StrokeBar(String title, int x, int y, int width, int height, int rows, int columns, int leftButtons, int rightButtons) {
        super(title, x, y, width, height, rows, columns, leftButtons, rightButtons);
        initToolBar();
    }

    private void initToolBar() {

        // stroke dropdown button
        LinkedList<Button> buttons3 = new LinkedList<>();
        for (int i = 0; i < 7; i++)
            buttons3.add(new AButton("" + i));

        Button strokeButton = new MButton("" + 1, buttons3);
        strokeButton.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                strokeButton.isClicked(x, y);
                if (strokeButton.isPressed())
                    strokeButton.setVisible(true);
            }

            @Override
            public void onRelease() {

            }

            @Override
            public void onHover(int x, int y) {
                strokeButton.hover(x, y);
            }
        });
        addLeftButton(strokeButton);

        int j = 0;
        for (Button b : strokeButton.getButtons()) {
            int finalJ = j;
            b.setListener(new AListener() {
                @Override
                public void onPress(int x, int y) {
                    b.isClicked(x, y);
                    if (b.isPressed())
                        strokeButton.setText("" + finalJ);
                }

                @Override
                public void onRelease() {
                    if (b.isPressed())
                        strokeButton.setVisible(false);
                }

                @Override
                public void onHover(int x, int y) {
                    b.hover(x, y);
                }
            });
            j++;
        }

        // increase/decrease stroke buttons
        Button strokeInc = (new AButton("+"));
        strokeInc.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                strokeInc.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (strokeInc.isPressed()) {
                    if (Integer.parseInt(strokeButton.getText()) == 6)
                        Board.setMessageWindow(MsgWindow.getInstance("Error", "Stroke can't be greater than 6!"));
                    else {
                        // increments the value of stroke
                        strokeButton.setText("" + (Integer.parseInt(strokeButton.getText()) + 1));
                    }
                }
            }

            @Override
            public void onHover(int x, int y) {
                strokeInc.hover(x, y);
            }
        });
        addButton(strokeInc);

        Button strokeDec = (new AButton("-"));
        strokeDec.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                strokeDec.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (strokeDec.isPressed()) {
                    if (Integer.parseInt(strokeButton.getText()) == 0)
                        Board.setMessageWindow(MsgWindow.getInstance("Error", "Stroke can't be less than 0!"));
                    else {
                        // increments the value of stroke
                        strokeButton.setText("" + (Integer.parseInt(strokeButton.getText()) - 1));
                    }
                }
            }

            @Override
            public void onHover(int x, int y) {
                strokeDec.hover(x, y);
            }
        });
        addButton(strokeDec);

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