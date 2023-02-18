import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 *
 * @author Terrick
 */
public class MyMouseListener implements MouseListener {
    private static int    key_;
    private static Point  location;
    private static double mouseX = 0;
    private static double mouseY = 0;

    public int getKey()    { return  key_;        }
    public int getMouseX() { return (int) mouseX; }
    public int getMouseY() { return (int) mouseY; }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        key_ = e.getButton();
        location = MouseInfo.getPointerInfo().getLocation();
        mouseX = location.getX();
        mouseY = location.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        key_ = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
