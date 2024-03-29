public class GBar extends ToolBar {

    public GBar(String title, int x, int y, int width, int height, int rows, int columns) {
        super(title, x, y, width, height, rows, columns);
        initToolBar();
    }

    private void initToolBar() {

        Button gridButton = new GridB("OFF");
        gridButton.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                gridButton.isClicked(x, y);
                if (gridButton.isPressed()) {
                    int size;
                    if (gridButton.getText().equals("OFF"))
                        size = 0;
                    else size = Integer.parseInt(gridButton.getText());
                    Board.setGrid(Grid.getInstance(size, Board.getDrawingX(), Board.getDrawingY(), Board.getDrawingWidth(), Board.getDrawingHeight()));
                }
            }

            @Override
            public void onRelease() {

            }

            @Override
            public void onHover(int x, int y) {
                gridButton.hover(x, y);
            }
        });
        addButton(gridButton);

    }

    @Override
    public void onPress(int x, int y) {
        for (Button b : buttons) {
            b.getListener().onPress(x, y);
        }
    }
}