import java.awt.*;
/**
 *
 * @author Terrick
 */
public class Button {
    private int    DFT_PADDING = 5;
    private int    DFT_ROUND   = 50;
    private int    x, y, width, height, Left_padding;
    private static MyMouseListener mouse;
    private String label;

    Button(int x, int y, int width, int height, int Left_padding, String label, MyMouseListener mouse) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.Left_padding = Left_padding;
        this.mouse = mouse;
    }

    // метод отрисовки
    public void draw(Graphics gr) {
        gr.setColor(Color.gray);
        gr.fillRoundRect(x, y, width, height, DFT_ROUND, DFT_ROUND);
        gr.setColor(Color.WHITE);
        gr.fillRoundRect(x + DFT_PADDING, y + DFT_PADDING, width - DFT_PADDING * 2, height - DFT_PADDING * 2, DFT_ROUND - DFT_PADDING, DFT_ROUND - DFT_PADDING);
        gr.setColor(Color.BLACK);
        gr.drawString(label, x + Left_padding, y + height / 2 + 10);
    }

    //метод, определяющий нажатие на кнопку.
    //регулирует действия по нажатию на кнопку
    public void touchButton() {
        if (mouse.getKey() == 1) {
            if (mouse.getMouseX() > x && mouse.getMouseX() < x + width && mouse.getMouseY() > y && mouse.getMouseY() < y + height) {

                switch (label) {
                    case "1 Player"  -> GameArea.setState(GameArea.STATES.PLAY1P);
                    case "2 Players" -> GameArea.setState(GameArea.STATES.PLAY2P);
                    case "3 Players" -> GameArea.setState(GameArea.STATES.PLAY3P);
                    case "exit" -> System.exit(0);
                    default -> {
                        GameArea.setDifficult(Integer.parseInt(label));
                        Object.setSpeed(3 + Integer.parseInt(label)/3);
                    }

                }
            }
        }
    }
}


