import java.awt.*;

public class CToolB extends ToolBar{

    public CToolB(String title, int x, int y, int width, int height, int rows, int columns, int leftButtons, int rightButtons) {
        super(title, x, y, width, height, rows, columns, leftButtons, rightButtons);
        initToolBar();
    }

    private void initToolBar(){

        // stroke color button
        Button strokeColorButton = new TButton(new Rectangle(Color.black));
        strokeColorButton.setText("Stroke");

        // fill color button
        Button fillColorButton = new TButton(new Rectangle(Color.white));
        fillColorButton.setText("Fill");

        strokeColorButton.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                strokeColorButton.isClicked(x, y);
                if (strokeColorButton.isPressed())
                    fillColorButton.setPressed(false);
            }

            @Override
            public void onRelease() {

            }

            @Override
            public void onHover(int x, int y) {
                strokeColorButton.hover(x, y);
            }
        });
        addLeftButton(strokeColorButton);

        fillColorButton.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                fillColorButton.isClicked(x, y);
                if (fillColorButton.isPressed())
                    strokeColorButton.setPressed(false);
            }

            @Override
            public void onRelease() {

            }

            @Override
            public void onHover(int x, int y) {
                fillColorButton.hover(x, y);
            }
        });
        addLeftButton(fillColorButton);

        // basic colored buttons
        addButton(new AButton(new Rectangle(new Color(0, 0, 0))));
        addButton(new AButton(new Rectangle(new Color(128, 128, 128))));
        addButton(new AButton(new Rectangle(new Color(128, 0, 0))));
        addButton(new AButton(new Rectangle(new Color(255, 0, 0))));
        addButton(new AButton(new Rectangle(new Color(255, 165, 0))));
        addButton(new AButton(new Rectangle(new Color(255, 255, 0))));
        addButton(new AButton(new Rectangle(new Color(0, 128, 0))));
        addButton(new AButton(new Rectangle(new Color(0, 0, 255))));
        addButton(new AButton(new Rectangle(new Color(0, 0, 190))));
        addButton(new AButton(new Rectangle(new Color(150, 0, 150))));
        addButton(new AButton(new Rectangle(new Color(255, 255, 255))));
        addButton(new AButton(new Rectangle(new Color(190, 190, 190))));
        addButton(new AButton(new Rectangle(new Color(193, 128, 0))));
        addButton(new AButton(new Rectangle(new Color(244, 194, 194))));
        addButton(new AButton(new Rectangle(new Color(200, 100, 0))));
        addButton(new AButton(new Rectangle(new Color(253, 253, 150))));
        addButton(new AButton(new Rectangle(new Color(128, 255, 0))));
        addButton(new AButton(new Rectangle(new Color(173, 216, 230))));
        addButton(new AButton(new Rectangle(new Color(0, 128, 128))));
        addButton(new AButton(new Rectangle(new Color(255, 0, 255))));

        for (int i = 0; i < 22; i++) {
            int red = buttons.get(i).getShape().getFillColor().getRed();
            int green = buttons.get(i).getShape().getFillColor().getGreen();
            int blue = buttons.get(i).getShape().getFillColor().getBlue();
            buttons.get(i).setTip("R:" + red + " G:" + green + " B:" + blue);
        }

        // empty buttons
        for (int i = 0; i < 10; i++) {
            addButton(new AButton(new Rectangle()));
            buttons.get(i + 22).setTip("R:" + 239 + " G:" + 239 + " B:" + 239);
        }

        for (int i = 2; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setListener(new AListener() {
                @Override
                public void onPress(int x, int y) {
                    buttons.get(finalI).isClicked(x, y);
                }

                @Override
                public void onRelease() {
                    if (buttons.get(finalI).isPressed()){
                        if (strokeColorButton.isPressed())
                            Board.setColorButton(strokeColorButton, buttons.get(finalI));
                        else if (fillColorButton.isPressed())
                            Board.setColorButton(fillColorButton, buttons.get(finalI));
                    }
                }

                @Override
                public void onHover(int x, int y) {
                    buttons.get(finalI).hover(x, y);
                }
            });
        }

        Button editColorButton = new AButton(new Rectangle());
        editColorButton.setText("Edit");
        editColorButton.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                editColorButton.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (editColorButton.isPressed())
                    Board.getColorWindow().open();
            }

            @Override
            public void onHover(int x, int y) {
                editColorButton.hover(x, y);
            }
        });
        addRightButton(editColorButton);


    }

    @Override
    public void onPress(int x, int y) {
        // gets the index of the toggle button pressed if any so that it is later depressed after any click on the toolbar
        int index = -1;
        for (int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).isPressed() && buttons.get(i) instanceof TButton) {
                index = i;
            }
        }

        // buttons pressed
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getListener() != null){
                buttons.get(i).getListener().onPress(x, y);
                if (i != index && index != -1 && buttons.get(i) instanceof TButton && buttons.get(i).isPressed()){
                    // button previously toggled is depressed
                    buttons.get(index).setPressed(false);
                }
            }

        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
    }

}
