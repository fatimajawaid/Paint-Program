import java.awt.*;

public abstract class triangle extends Shape {
    public triangle() {
    }

    public triangle(Color fillColor, Color strokeColor) {
        super(fillColor, strokeColor);
    }

    public triangle(Color fillColor) {
        super(fillColor);
    }

    @Override
    public abstract void draw(Graphics g);

}