import javax.swing.*;
/**
 *
 * @author Terrick
 */
public class Window extends JFrame {

    public Window()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("game_rocket_with_DORA");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        add(new GameArea());
        setVisible(true);
    }

    //автоматическое получение ширины экрана
    public static int getScreenWidth() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    }
    //автоматическое получение высоты экрана
    public static int getScreenHeight() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    }

}
