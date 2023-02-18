import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
/**
 *
 * @author Terrick
 */
public class MyKeyListener implements KeyListener {
    private ArrayList<Player> players = GameArea.getPlayers();
    private int[] KEYCODE = {KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
    private int key_;

    public void keyPressed(KeyEvent e) {
        int c = 0;
        for (Player player : players) {
            if (e.getKeyCode() == KEYCODE[c]) {
                player.setBtnLeft(true);
            }
            if (e.getKeyCode() == KEYCODE[c+1]) {
                player.setBtnRight(true);
            }
            c += 2;
        }
    }
    public void keyReleased(KeyEvent e) {
        key_ = e.getKeyCode();

        if (key_ == KeyEvent.VK_ESCAPE) {
            GameArea.switchMenu();
        }

        int c = 0;
        for (Player player : players) {
            if (e.getKeyCode() == KEYCODE[c]) {
                player.setBtnLeft(false);
            }
            if (e.getKeyCode() == KEYCODE[c+1]) {
                player.setBtnRight(false);
            }
            c += 2;
        }
    }
    public void keyTyped(KeyEvent e) {}
}
