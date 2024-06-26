import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private static final int panelX = 0;
    private static final int panelY = 0;
    private static final int panelWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int panelHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static int time = 0;

    private static Window mainWindow;
    private static Window colorWindow;
    private static Window fileWindow;
    private static Window messageWindow;

    private static int drawingX;
    private static int drawingY;
    private static int drawingWidth;
    private static int drawingHeight;
    private Shape shape;
    public static LinkedList<String> files;
    private static LinkedList<LinkedList<Shape>> layer;
    private static LinkedList<LinkedList<Shape>> redoList;
    private static int layerSelected = -1;
    private static boolean drawing;
    private boolean control;
    private int clicks;
    private static int shapeChoice;
    private int x1;
    private int y1;
    private static int colorCount = 0;
    private static int customCount = 0;

    private static int[][][] gradientColor;
    private static int fade = 255;

    private static ToolTip toolTip;
    private static Grid grid;

    private static LinkedList<String> layerName;

    Board() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBounds(0, 0, panelWidth, panelHeight);
        setFocusable(true);
        initializeAssets();

        int DELAY = 1;
        Timer timer = new Timer(DELAY, this);
        timer.start();

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    void initializeAssets() {

        files = new LinkedList<>();
        mainWindow = new MWindow(panelX, panelY, panelWidth, 30 + 140);
        fileWindow = new Fwindow(panelX + panelWidth / 2 - 600 / 2, panelY + panelHeight / 2 - 550 / 2, 600, 550);
        getFilesFromFolder();
        colorWindow = Gwindow.getInstance();
        toolTip = ToolTip.getInstance();
        layer = new LinkedList<>();
        redoList = new LinkedList<>();

        drawingX = panelX;
        drawingY = panelY + mainWindow.getToolBars().get(0).getHeight() + mainWindow.getToolBars().get(1).getHeight();
        drawingWidth = mainWindow.getToolBars().get(1).getWidth() + mainWindow.getToolBars().get(2).getWidth() + mainWindow.getToolBars().get(3).getWidth() + mainWindow.getToolBars().get(4).getWidth();
        drawingHeight = panelHeight - mainWindow.getToolBars().get(0).getHeight() - mainWindow.getToolBars().get(1).getHeight();
        layerName = new LinkedList<>();
        List<String> list = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eight", "Nineth", "Tenth");
        layerName.addAll(list);
        addLayer();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        time += 3;
        g.setColor(Color.white);
        g.fill3DRect(drawingX, drawingY, drawingWidth, drawingHeight, true);
        g.setColor(Color.pink);
        g.drawRect(drawingX, drawingY, drawingWidth, drawingHeight);
        drawShapes(g);
        if (grid != null)
            grid.drawGrid(g);
        mainWindow.paint(g);
        paintColorGradient(mainWindow.getToolBars().get(4).getButtons().get(32), 127, g);
        if (colorWindow.isOpen())
            colorWindow.paint(g);
        if (fileWindow.isOpen())
            fileWindow.paint(g);
        if (toolTip != null)
            toolTip.draw(g);
        if (messageWindow != null)
            messageWindow.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (messageWindow != null) {
            messageWindow.onPress(x, y);
            return;
        }
        if (colorWindow.isOpen()) {
            colorWindow.onPress(x, y);
            return;
        }
        if (fileWindow.isOpen()) {
            fileWindow.onPress(x, y);
            return;
        }
        mainWindow.onPress(x, y);
        if (y > drawingY && x < drawingX + drawingWidth) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (shapeChoice > 0 && shapeChoice < 9) {
                    drawing = true;
                    if (shapeChoice == 8) {
                        if (clicks == 0) {
                            shape = new Bcurve(mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                            layer.get(0).push(shape);
                            redoList.get(0).clear();
                            shape.setStroke(Integer.parseInt(mainWindow.getToolBars().get(3).getButtons().get(0).getText()));
                            shape.setPoint(x, y, 0);
                            shape.setPoint(x, y, 3);
                        }
                        if (clicks == 1)
                            shape.setPoint(x, y, 1);
                        if (clicks == 2)
                            shape.setPoint(x, y, 2);
                        clicks++;
                        return;
                    }
                    switch (shapeChoice) {
                        case 1 -> shape = new Fdraw(mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                        case 2 -> shape = new Circle(mainWindow.getToolBars().get(4).getButtons().get(1).getShape().getFillColor(), mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                        case 3 -> shape = new Rectangle(mainWindow.getToolBars().get(4).getButtons().get(1).getShape().getFillColor(), mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                        case 4 -> shape = new Rtriangle(mainWindow.getToolBars().get(4).getButtons().get(1).getShape().getFillColor(), mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                        case 5 -> shape = new Etriangle(mainWindow.getToolBars().get(4).getButtons().get(1).getShape().getFillColor(), mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                        case 6 -> shape = new Hex(mainWindow.getToolBars().get(4).getButtons().get(1).getShape().getFillColor(), mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                        case 7 -> shape = new Pent(mainWindow.getToolBars().get(4).getButtons().get(1).getShape().getFillColor(), mainWindow.getToolBars().get(4).getButtons().get(0).getShape().getFillColor());
                    }
                    layer.get(0).push(shape);
                    redoList.get(0).clear();
                    shape.setStroke(Integer.parseInt(mainWindow.getToolBars().get(3).getButtons().get(0).getText()));
                    x1 = e.getX();
                    y1 = e.getY();
                    shape.setPoint(new Point(x1, y1));
                    if (shape instanceof Fdraw)
                        shape.addPixel(x1, y1);
                    clicks++;
                } else messageWindow = MsgWindow.getInstance("Error", "Select a shape first!");
            } else messageWindow = MsgWindow.getInstance("Error", "No layer exists to draw on!");
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (messageWindow != null)
            return;

        if (SwingUtilities.isLeftMouseButton(e)) {
            int x = e.getX();
            int y = e.getY();
            if (colorWindow.isOpen()) {
                if (colorWindow.getButtons().get(5).isPressed())
                    colorWindow.getButtons().get(5).getListener().onPress(x, y);
                if (colorWindow.getButtons().get(6).isPressed())
                    colorWindow.getButtons().get(6).getListener().onPress(x, y);
            }
            if (y > drawingY && x < drawingX + drawingWidth && !colorWindow.isOpen() && !fileWindow.isOpen() && !mainWindow.getToolBars().get(3).getButtons().get(0).isVisible()) {
                if (shape != null) {
                    if (shapeChoice > 0 && shapeChoice < 9) {
                        if (shapeChoice == 8) {
                            if (clicks == 1)
                                shape.setPoint(x, y, 3);
                            if (clicks == 2)
                                shape.setPoint(x, y, 1);
                            if (clicks == 3)
                                shape.setPoint(x, y, 2);
                        }
                        int width = Math.abs(x - x1);
                        int height = Math.abs(y - y1);
                        double base = x - shape.getX();
                        double perp = shape.getY() - y;
                        int initAngle = (int) (Math.toDegrees(Math.atan2(perp, base)));
                        shape.setInitAngle(initAngle);
                        int size = (int) (Math.sqrt((width * width) + (height * height)));
                        if (shapeChoice == 1) {
                            shape.addPixel(e.getX(), e.getY());
                            return;
                        }
                        if (shapeChoice == 2 || shapeChoice == 5 || shapeChoice == 7) {
                            shape.setSize(size);
                            return;
                        }
                        Point topLeft = new Point(x1, y1);
                        if (y < y1 && x < x1)
                            topLeft = new Point(x, y);
                        else if (y < y1)
                            topLeft = new Point(x1, y);
                        else if (x < x1)
                            topLeft = new Point(x, y1);

                        shape.setPoint(topLeft);
                        shape.setWidth(width);
                        shape.setHeight(height);
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (messageWindow != null)
            messageWindow.onRelease();

        if (colorWindow.isOpen())
            colorWindow.onRelease();

        if (fileWindow.isOpen())
            fileWindow.onRelease();

        mainWindow.onRelease();
        if (SwingUtilities.isLeftMouseButton(e)) {

            if (shapeChoice > 0 && shapeChoice < 9) {

                if (shapeChoice == 8 && clicks < 3)
                    return;

                shapeChoice = 0;
                clicks = 0;
                shape = null;
                drawing = false;
            }

        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (messageWindow != null) {
            messageWindow.onHover(x, y);
            return;
        }
        if (colorWindow.isOpen()) {
            colorWindow.onHover(x, y);
            return;
        }
        if (fileWindow.isOpen()) {
            fileWindow.onHover(x, y);
            return;
        }
        mainWindow.onHover(x, y);
        setToolTip();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (messageWindow != null) {
                messageWindow.close();
            } else if (colorWindow.isOpen()) {
                setColorButton(mainWindow.getToolBars().get(4).getButtons().get(22 + colorCount % 10), colorWindow.getButtons().get(4));
                colorCount++;
                colorWindow.close();
            } else if (fileWindow.isOpen()) {
                openFile();
            }
            return;
        }
        if (messageWindow != null)
            return;
        if (drawing) {
            messageWindow = MsgWindow.getInstance("Error", "Complete drawing the shape first!");
            return;
        }
        int keyCode = e.getKeyCode();
        if (keyCode == 17) {
            control = true;
            return;
        }
        switch (keyCode) {
            case KeyEvent.VK_ADD -> addLayer();
            case KeyEvent.VK_SUBTRACT -> removeLayer(layerSelected);
            case KeyEvent.VK_UP -> raiseLayer(layerSelected);
            case KeyEvent.VK_DOWN -> lowerLayer(layerSelected);
            case 'N' -> {
                if (control)
                    renew();
                else messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
            }
            case 'F' -> {
                if (!colorWindow.isOpen() && control)
                    fileWindow.open();
                else messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
            }
            case 'S' -> {
                if (control)
                    saveFile();
                else messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
            }
            case 'Z' -> {
                if (control)
                    undo();
                else messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
            }
            case 'Y' -> {
                if (control)
                    redo();
                else messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
            }
            case 'C' -> {
                if (!fileWindow.isOpen() && control)
                    colorWindow.open();
                else messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
            }
            case KeyEvent.VK_ESCAPE -> {
                if (messageWindow != null) {
                    messageWindow.close();
                } else if (colorWindow.isOpen()) {
                    colorWindow.close();
                } else if (fileWindow.isOpen()) {
                    fileWindow.close();
                }
            }
            default -> messageWindow = MsgWindow.getInstance("Error", "Invalid Key Pressed!");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 17)
            control = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        layerSelected = -1;
        for (int i = 4; i < mainWindow.getToolBars().get(5).getButtons().size(); i++) {
            if (mainWindow.getToolBars().get(5).getButtons().get(i).isPressed()) {
                layerSelected = i - 4;
            }
        }
        Toolkit.getDefaultToolkit().sync();
        repaint();
    }

    public static void undo() {
        if (drawing) {
            messageWindow = MsgWindow.getInstance("Error", "Complete drawing the shape first!");
            return;
        }
        if (layerSelected == -1) {
            messageWindow = MsgWindow.getInstance("Error", "Please select a layer!");
            return;
        }

        if (layer.get(layerSelected).isEmpty()) {
            messageWindow = MsgWindow.getInstance("Error", "Undo List Empty!");
            return;
        }

        redoList.get(layerSelected).push(layer.get(layerSelected).pop());

    }

    public static void redo() {
        if (drawing) {
            messageWindow = MsgWindow.getInstance("Error", "Complete drawing the shape first!");
            return;
        }

        if (layerSelected == -1) {
            messageWindow = MsgWindow.getInstance("Error", "Please select a layer!");
            return;
        }

        if (redoList.get(layerSelected).isEmpty()) {
            messageWindow = MsgWindow.getInstance("Error", "Redo List Empty!");
            return;
        }
        layer.get(layerSelected).push(redoList.get(layerSelected).pop());

    }

    private void drawShapes(Graphics g) {

        for (int i = layer.size() - 1; i >= 0; i--) {
            for (int j = layer.get(i).size() - 1; j >= 0; j--)
                layer.get(i).get(j).draw(g);
        }
    }

    private void getFilesFromFolder() {
        File folder = new File("./src/files");
        String[] fileName = folder.list();
        assert fileName != null;
        files.addAll(Arrays.asList(fileName));
    }

    public static void saveFile() {
        if (files.size() < 8) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy ~ HH mm ss");
                String timeStamp = df.format(new Date());
                String filePath = "./src/files/" + timeStamp + ".txt";
                File file = new File(filePath);
                if (file.createNewFile()) {
                    storeShapes(filePath);
                    files.add(timeStamp + ".txt");
                    Button newFile = new AButton(timeStamp + ".txt");
                    newFile.setListener(new AListener() {
                        @Override
                        public void onPress(int x, int y) {
                            newFile.isClicked(x, y);
                        }

                        @Override
                        public void onRelease() {
                            if (newFile.isPressed())
                                fileWindow.getTextBoxes().get(0).setText("File name: " + newFile.getText());
                        }

                        @Override
                        public void onHover(int x, int y) {
                            newFile.hover(x, y);
                        }
                    });
                    fileWindow.getToolBars().get(0).addButton(newFile);
                    messageWindow = MsgWindow.getInstance("Message", "File saved as \"" + timeStamp + ".txt\"!");

                } else messageWindow = MsgWindow.getInstance("Error", "File already exists!");

            } catch (IOException e) {

                messageWindow = MsgWindow.getInstance("Error", "File not found!");
                e.printStackTrace();
            }
        } else messageWindow = MsgWindow.getInstance("Error", "Save slots full!");
    }

    public static void openFile() {
        if (fileWindow.getTextBoxes().get(0).getText().substring(11).length() > 0) {
            getShapes("./src/files/" + fileWindow.getTextBoxes().get(0).getText().substring(11));
            fileWindow.close();
        } else messageWindow = MsgWindow.getInstance("Error", "No file selected!");
    }

    public static void getShapes(String filePath) {
        try {
            File file = new File(filePath);
            Scanner input = new Scanner(file);
            Shape shape = null;
            String data;
            String[] temp;
            renew();
            int i = -1;
            while (input.hasNextLine()) {
                String shapeName = input.nextLine();
                if (shapeName.startsWith("layer")) {
                    if (i != -1)
                        addLayer();
                    input.nextLine();
                    i++;
                    continue;
                }

                data = input.nextLine();
                temp = data.split(",");
                Color fillColor = new Color(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));

                switch (shapeName) {

                    case "Line" -> shape = new Fdraw(fillColor);
                    case "Circle" -> shape = new Circle(fillColor);
                    case "Rectangle" -> shape = new Rectangle(fillColor);
                    case "Equilateral Triangle" -> shape = new Etriangle(fillColor);
                    case "Right Triangle" -> shape = new Rtriangle(fillColor);
                    case "Hexagon" -> shape = new Hex(fillColor);
                    case "Pentagram" -> shape = new Pent(fillColor);
                    case "Bezier" -> shape = new Bcurve(fillColor);

                }

                if (shapeName.equals("Line")) {
                    shape.setStroke(Integer.parseInt(input.nextLine()));
                    data = input.nextLine();
                    while (data.length() > 0) {
                        temp = data.split(",");
                        shape.addPixel(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
                        data = input.nextLine();
                    }

                } else if (shapeName.equals("Bezier")) {
                    shape.setStroke(Integer.parseInt(input.nextLine()));
                    for (int j = 0; j < 4; j++) {
                        data = input.nextLine();
                        temp = data.split(",");
                        shape.setPoint(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), j);
                    }

                } else {
                    data = input.nextLine();
                    temp = data.split(",");
                    assert shape != null;
                    shape.setStrokeColor(new Color(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
                    shape.setStroke(Integer.parseInt(input.nextLine()));
                    data = input.nextLine();
                    temp = data.split(",");
                    shape.setPoint(new Point(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])));

                    if (shapeName.equals("Circle") || shapeName.equals("Pentagram") || shapeName.equals("Equilateral Triangle")) {
                        shape.setSize(Integer.parseInt(input.nextLine()));
                        if (!shapeName.equals("Circle"))
                            shape.setInitAngle(Integer.parseInt(input.nextLine()));
                    } else {
                        shape.setWidth(Integer.parseInt(input.nextLine()));
                        shape.setHeight(Integer.parseInt(input.nextLine()));
                    }
                }
                layer.get(i).add(shape);
                if (input.hasNextLine() && !shapeName.equals("Line"))
                    input.nextLine();
            }
            input.close();
        } catch (FileNotFoundException e) {
            messageWindow = MsgWindow.getInstance("Error", "File not found!");
            e.printStackTrace();
        }
    }

    private static void storeShapes(String filePath) {
        try {
            FileWriter fWriter = new FileWriter(filePath, false);
            StringBuilder finalData = new StringBuilder();
            for (int i = 0; i < layer.size(); i++) {
                finalData.append("layer ").append(i).append("\n\n");
                for (Shape shape : layer.get(i)) {
                    finalData.append(shape.toString()).append('\n');
                }
            }
            fWriter.write(finalData.toString());
            fWriter.close();
        } catch (IOException ex) {
            messageWindow = MsgWindow.getInstance("Error", "File not found!");
            ex.printStackTrace();
        }

    }

    public static void paintColorGradient(Button button, int fade, Graphics g) {

        int x;
        int y;
        int width;
        int height;

        if (button.getShape() != null) {
            x = button.getShape().getX();
            y = button.getShape().getY();
            width = button.getShape().getWidth();
            height = button.getShape().getHeight();
        } else {
            x = button.getX();
            y = button.getY();
            width = button.getWidth();
            height = button.getHeight();
        }

        gradientColor = new int[width][height][3];
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        for (int j = 0; j < height; j++) {

            for (int i = 0; i < width; i++) {

                double red = 255.0;
                double green = 255.0;
                double blue = 255.0;

                int interval = width / 6;
                double step = 255.0 / interval;

                if (i < interval) {
                    blue = 0;
                    green = (int) (i * step);
                } else if (i < interval * 2) {
                    blue = 0;
                    red = 255 - (int) ((i - interval) * step);
                } else if (i < interval * 3) {
                    red = 0;
                    blue = (int) ((i - interval * 2) * step);
                } else if (i < interval * 4) {
                    red = 0;
                    green = 255 - (int) ((i - interval * 3) * step);
                } else if (i < interval * 5) {
                    green = 0;
                    red = (int) ((i - interval * 4) * step);
                } else if (i < interval * 6) {
                    green = 0;
                    blue = 255 - (int) ((i - interval * 5) * step);
                }
                double changeRed = Math.abs((red - fade) / height);
                if (red >= fade)
                    red = (int) (red - (changeRed * j));
                else red = (int) (red + (changeRed * j));
                double changeGreen = Math.abs((green - fade) / height);
                if (green >= fade)
                    green = (int) (green - (changeGreen * j));
                else green = (int) (green + (changeGreen * j));
                double changeBlue = Math.abs((blue - fade) / height);
                if (blue >= fade)
                    blue = (int) (blue - (changeBlue * j));
                else blue = (int) (blue + (changeBlue * j));
                g2.setColor(new Color((int) red, (int) green, (int) blue));
                g2.draw(new Rectangle2D.Double(x + i, y + j, 1, 1));
                g2.fill(new Rectangle2D.Double(x + i, y + j, 1, 1));
                gradientColor[i][j][0] = (int) red;
                gradientColor[i][j][1] = (int) green;
                gradientColor[i][j][2] = (int) blue;
            }
        }
    }

    public static void setColorButton(Button b1, Button b2) {
        b1.setTip(b2.getTip());
        b1.getShape().setFillColor(b2.getShape().getFillColor());
    }

    public static void addLayer() {
        if (mainWindow.getToolBars().get(5).getButtons().size() < 14) {
            layer.add(new LinkedList<>());
            redoList.add(new LinkedList<>());
            Button newButton = (new TButton(layerName.remove((int) (Math.random() * layerName.size()))));
            newButton.setListener(new AListener() {
                @Override
                public void onPress(int x, int y) {
                    newButton.isClicked(x, y);
                }

                @Override
                public void onRelease() {
                }

                @Override
                public void onHover(int x, int y) {
                    newButton.hover(x, y);
                }
            });

            mainWindow.getToolBars().get(5).addButton(newButton, mainWindow.getToolBars().get(5).getButtons().size());
        } else messageWindow = MsgWindow.getInstance("Error", "Can not add more than 10 layers!");
    }

    public static void removeLayer(int i) {

        if (i < 0) {
            messageWindow = MsgWindow.getInstance("Error", "Please select a layer!");
            return;
        }

        if (layer.size() == 1) {
            messageWindow = MsgWindow.getInstance("Error", "You can not remove the last layer!");
            return;
        }

        layerName.add(mainWindow.getToolBars().get(5).getButtons().get(i + 4).getText());
        mainWindow.getToolBars().get(5).removeButton(i + 4);
        layer.remove(i);
        redoList.remove(i);
    }

    public static void raiseLayer(int i) {
        if (i > 0) {
            mainWindow.getToolBars().get(5).addButton(mainWindow.getToolBars().get(5).getButtons().remove(i + 4), i + 4 - 1);
            Collections.swap(layer, i, i - 1);
            Collections.swap(redoList, i, i - 1);
        } else if (i < 0) {
            messageWindow = MsgWindow.getInstance("Error", "Please select a layer!");
        } else {
            messageWindow = MsgWindow.getInstance("Error", "Layer can not be raised further!");
        }
    }

    public static void lowerLayer(int i) {
        if (i < 0) {
            messageWindow = MsgWindow.getInstance("Error", "Please select a layer!");
            return;
        }
        if (i + 4 < mainWindow.getToolBars().get(5).getButtons().size() - 1) {
            raiseLayer(i + 1);
        } else messageWindow = MsgWindow.getInstance("Error", "Layer can not be lowered further!");
    }

    public static void renew() {
        if (mainWindow.getToolBars().get(5).getButtons().size() > 4) {
            mainWindow.getToolBars().get(5).getButtons().subList(4, mainWindow.getToolBars().get(5).getButtons().size()).clear();
        }
        layer.clear();
        redoList.clear();
        addLayer();
    }

    private static void setToolTip() {

        if (fileWindow.isOpen()) {

            for (Button b : fileWindow.getButtons()) {
                if (b.isHovered()) {
                    toolTip.setButton(b);
                    return;
                }
            }

            for (ToolBar t : fileWindow.getToolBars()) {
                for (Button b : t.getButtons()) {
                    if (b.isHovered()) {
                        toolTip.setButton(b);
                        return;
                    }
                }
            }

        }

        if (colorWindow.isOpen()) {

            for (Button b : colorWindow.getButtons()) {
                if (b.isHovered()) {
                    toolTip.setButton(b);
                    return;
                }
            }

            for (ToolBar t : colorWindow.getToolBars()) {
                for (Button b : t.getButtons()) {
                    if (b.isHovered()) {
                        toolTip.setButton(b);
                        return;
                    }
                }
            }

        }

        for (Button b : mainWindow.getButtons()) {
            if (b.isHovered()) {
                toolTip.setButton(b);
                return;
            }
        }

        for (ToolBar t : mainWindow.getToolBars()) {
            for (Button b : t.getButtons()) {
                if (b instanceof MButton) {
                    for (Button mb : b.getButtons()) {
                        if (mb.isHovered()) {
                            toolTip.setButton(mb);
                            return;
                        }
                    }
                }
                if (b.isHovered()) {
                    toolTip.setButton(b);
                    return;
                }
            }
        }

        toolTip.setButton(null);

    }

    public static Window getMainWindow() {
        return mainWindow;
    }

    public static Window getColorWindow() {
        return colorWindow;
    }

    public static Window getFileWindow() {
        return fileWindow;
    }

    public static int getPanelX() {
        return panelX;
    }

    public static int getPanelY() {
        return panelY;
    }

    public static int getPanelWidth() {
        return panelWidth;
    }

    public static int getPanelHeight() {
        return panelHeight;
    }

    public static int getDrawingX() {
        return drawingX;
    }

    public static int getDrawingY() {
        return drawingY;
    }

    public static int getDrawingWidth() {
        return drawingWidth;
    }

    public static int getDrawingHeight() {
        return drawingHeight;
    }

    public static LinkedList<String> getFiles() {
        return files;
    }

    public static int getLayerSelected() {
        return layerSelected;
    }

    public static int getColorCount() {
        return colorCount;
    }

    public static int getCustomCount() {
        return customCount;
    }

    public static int[][][] getGradientColor() {
        return gradientColor;
    }

    public static int getFade() {
        return fade;
    }

    public static ToolTip getToolTip() {
        return toolTip;
    }

    public static void setColorCount(int colorCount) {
        Board.colorCount = colorCount;
    }

    public static void setCustomCount(int customCount) {
        Board.customCount = customCount;
    }

    public static void setFade(int fade) {
        Board.fade = fade;
    }

    public static void setGrid(Grid grid) {
        Board.grid = grid;
    }

    public static void setMessageWindow(Window messageWindow) {
        Board.messageWindow = messageWindow;
    }

    public static void setShapeChoice(int shapeChoice) {
        Board.shapeChoice = shapeChoice;
    }
}
