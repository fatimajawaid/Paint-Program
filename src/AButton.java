
public class AButton extends Button {

    public AButton(Shape shape) {
        super(shape);
    }

    public AButton(String text, String tip) {
        super(text, tip);
    }

    public AButton(String text) {
        super(text);
    }
    public AButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    public AButton(String text, int x, int y, int width, int height) {
        super(text, x, y, width, height);
    }
    public void isClicked(int x, int y) {
        pressed = x > this.x && x < this.x + width && y > this.y && y < this.y + height;
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