public class File extends ToolBar {

    public File(String title, int x, int y, int width, int height, int rows, int columns) {
        super(title, x, y, width, height, rows, columns);
        initToolBar();
    }

    private void initToolBar() {

        TITLE_ON_TOP();
        LEFT_ALIGN();

        for (String file : Board.getFiles())
            addButton(new AButton(file));

        for (Button b : buttons) {
            b.setListener(new AListener() {
                @Override
                public void onPress(int x, int y) {
                    b.isClicked(x, y);
                }

                @Override
                public void onRelease() {
                    if (b.isPressed())
                        Board.getFileWindow().getTextBoxes().get(0).setText("File name: " + b.getText());
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