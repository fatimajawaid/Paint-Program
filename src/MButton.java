import java.awt.*;
import java.util.LinkedList;

public class MButton extends TButton{

    public MButton(String text, LinkedList<Button> buttons) {
        super(text, buttons);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(pressed)
            for (Button button : buttons)
                button.paint(g);
    }
}