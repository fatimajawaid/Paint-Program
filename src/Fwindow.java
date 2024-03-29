public class Fwindow extends Window {
    public Fwindow(int x, int y, int width, int height) {
        super(x, y, width, height);
        initWindow();
    }

    private void initWindow() {

        int columnWidth = width / 16;
        int rowHeight = height / 14;
        int spacing = 5;


        TBox fileSelected = new TBox("File name: ", x + columnWidth, y + rowHeight * 11, columnWidth * 14, rowHeight);
        fileSelected.LEFT_ALIGN();
        textBoxes.add(fileSelected);

        Button close = (new CButton(x + width - columnWidth, y + 1, columnWidth, rowHeight - 1));
        close.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                close.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (close.isPressed())
                    close();
            }

            @Override
            public void onHover(int x, int y) {
                close.hover(x, y);
            }
        });
        buttons.add(close);

        Button open = (new AButton("Open", x + width - 6 * (columnWidth) + spacing, y + height - rowHeight + spacing, 3 * (columnWidth - spacing), rowHeight - spacing * 2));
        open.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                open.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (open.isPressed()) {
                   Board.openFile();
                }
            }

            @Override
            public void onHover(int x, int y) {
                open.hover(x, y);
            }
        });
        buttons.add(open);

        Button cancel = (new AButton("Cancel", x + width - 3 * (columnWidth) + spacing, y + height - rowHeight + spacing, 3 * (columnWidth - spacing), rowHeight - spacing * 2));
        cancel.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                cancel.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (cancel.isPressed())
                    close();
            }

            @Override
            public void onHover(int x, int y) {
                cancel.hover(x, y);
            }
        });
        buttons.add(cancel);

        TBox title = new TBox("Open File", x, y, width, rowHeight);
        title.LEFT_ALIGN();
        textBoxes.add(title);


        ToolBar filesToolBar = new File("Files:", x + columnWidth, y + rowHeight * 2, columnWidth * 14, rowHeight * 8, 8, 1);
        toolBars.add(filesToolBar);


    }
}