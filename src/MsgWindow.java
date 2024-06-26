import java.awt.*;

public class MsgWindow extends Window{

    private static final int width = 400;
    private static final int height = 200;
    private static final int x = Toolkit.getDefaultToolkit().getScreenSize().width/2 - width/2;
    private static final int y = Toolkit.getDefaultToolkit().getScreenSize().height/2 - height/2;

    private static String type = "";
    private static String message;

    public static MsgWindow instance = new MsgWindow(x, y, width, height);

    private MsgWindow(int x, int y, int width, int height) {
        super(x, y, width, height);
        initWindow();
    }

    private void initWindow(){

        int columnWidth = width/4;
        int rowHeight = height/5;
        int spacing = 5;

        TBox title = new TBox(type, x, y, width, rowHeight);
        title.LEFT_ALIGN();
        textBoxes.add(title);

        Button close = new CButton(x + width - columnWidth/2, y, columnWidth/2, rowHeight);
        close.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                close.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (close.isPressed()) {
                    close();
                }
            }

            @Override
            public void onHover(int x, int y) {
                close.hover(x, y);
            }
        });
        buttons.add(close);

        Button ok = (new AButton("OK", x + width/2 - columnWidth/2, y + height - rowHeight*2 + spacing, (columnWidth), rowHeight - spacing*2));
        ok.setListener(new AListener() {
            @Override
            public void onPress(int x, int y) {
                ok.isClicked(x, y);
            }

            @Override
            public void onRelease() {
                if (ok.isPressed()){
                    close();
                }
            }

            @Override
            public void onHover(int x, int y) {
                ok.hover(x, y);
            }
        });
        buttons.add(ok);

    }

    public static MsgWindow getInstance(String s1, String s2) {
        type = s1;
        message = s2;
        return instance;
    }

    @Override
    public void paint(Graphics g){

        int rowHeight = height/6;

        textBoxes.get(0).setText(type);

        super.paint(g);

        g.setColor(Color.black);
        g.setFont(new Font(Font.SERIF, Font.PLAIN, rowHeight/2));
        FontMetrics m = g.getFontMetrics();
        int stringWidth = m.stringWidth(message);
        int stringHeight = m.getAscent() - m.getDescent();

        g.setColor(Color.black);
        g.drawString(message, x + width/2 - stringWidth/2, y + rowHeight*3 - stringHeight/2);


    }

    @Override
    public void close() {
        super.close();
        Board.setMessageWindow(null);
    }
}
