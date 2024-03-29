public class MWindow extends Window{

        public MWindow(int x, int y, int width, int height) {
            super(x, y, width, height);
            initWindow();
        }

        private void initWindow(){

            int menuHeight = 30;
            int toolBarHeight = 140;
            int shapeToolBarWidth = 350;
            int gridToolBarWidth = 110;
            int strokeToolBarWidth = 150;
            int colorToolBarWidth = 590;
            int layerToolBarWidth = Board.getPanelWidth() - shapeToolBarWidth - gridToolBarWidth - strokeToolBarWidth - colorToolBarWidth;


            // menu bar
            ToolBar menuBar = new MBar("", x, y, Board.getPanelWidth(), menuHeight, 1, 19);
            toolBars.add(menuBar);

            //shapes toolbar
            ToolBar shapeToolBar = new ShapeBar("Shapes" ,x, y + menuHeight,shapeToolBarWidth, toolBarHeight, 2, 3, 1, 1);
            toolBars.add(shapeToolBar);

            // gridToolBar
            ToolBar gridToolBar = new GBar("Grid" ,x + shapeToolBarWidth, y + menuHeight, gridToolBarWidth, toolBarHeight, 1, 1);
            toolBars.add(gridToolBar);

            // strokeToolBar
            ToolBar strokeToolBar = new StrokeBar("Stroke", x + shapeToolBarWidth + gridToolBarWidth, y + menuHeight, strokeToolBarWidth, toolBarHeight, 2, 1, 1, 0);
            toolBars.add(strokeToolBar);

            // color toolbar
            ToolBar colorToolBar = new CToolB("Colors", x + shapeToolBarWidth + gridToolBarWidth + strokeToolBarWidth, y + menuHeight,colorToolBarWidth, toolBarHeight, 3, 10, 2, 1);
            toolBars.add(colorToolBar);

            // layers toolbar
            ToolBar layerToolBar = new LayerPanel("Layers", x + shapeToolBarWidth + gridToolBarWidth + strokeToolBarWidth + colorToolBarWidth, y + menuHeight, layerToolBarWidth, Board.getPanelHeight() - menuHeight);
            toolBars.add(layerToolBar);

        }

        @Override
        public void onPress(int x, int y) {

            // file menu dropdown
            if (toolBars.get(0).getButtons().get(0).isVisible()){
                for (int i = 0; i < 3; i++) {
                    toolBars.get(0).getButtons().get(0).getButtons().get(i).getListener().onPress(x, y);
                }
                toolBars.get(0).getButtons().get(0).setVisible(false);
                return;
            }
            if (toolBars.get(0).getButtons().get(1).isVisible()) {
                for (int i = 0; i < 2; i++) {
                    toolBars.get(0).getButtons().get(1).getButtons().get(i).getListener().onPress(x, y);
                }
                toolBars.get(0).getButtons().get(1).setVisible(false);
                return;
            }

            if (toolBars.get(3).getButtons().get(0).isVisible()) {
                for (int i = 0; i < 7; i++) {
                    toolBars.get(3).getButtons().get(0).getButtons().get(i).getListener().onPress(x, y);
                }
                return;
            }

            super.onPress(x, y);
        }

        @Override
        public void onHover(int x, int y) {
            if (toolBars.get(0).getButtons().get(0).isVisible()){
                for (int i = 0; i < 3; i++) {
                    toolBars.get(0).getButtons().get(0).getButtons().get(i).getListener().onHover(x, y);
                }
                return;
            }

            if (toolBars.get(0).getButtons().get(1).isVisible()) {
                for (int i = 0; i < 2; i++) {
                    toolBars.get(0).getButtons().get(1).getButtons().get(i).getListener().onHover(x, y);
                }
                return;
            }

            if (toolBars.get(3).getButtons().get(0).isVisible()) {
                for (int i = 0; i < 7; i++) {
                    toolBars.get(3).getButtons().get(0).getButtons().get(i).getListener().onHover(x, y);
                }
                return;
            }

            super.onHover(x, y);
        }
}
