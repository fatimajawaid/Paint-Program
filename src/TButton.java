import java.util.LinkedList;

public class TButton extends Button{

    public TButton(Shape shape) {
        super(shape);
    }

    public TButton(Shape shape, String tip) {
        super(shape, tip);
    }

    public TButton(String text) {
        super(text);
    }

    public TButton(String text, LinkedList<Button> buttons) {
        super(text, buttons);
    }

    @Override
    public void isClicked(int x, int y) {
        if(x > this.x && x < this.x + width && y > this.y && y < this.y + height)
            pressed = !pressed;
    }

    @Override
    public void onPress(int x, int y) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onHover(int x, int y) {

    }

}