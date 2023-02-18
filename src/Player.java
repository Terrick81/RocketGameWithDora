import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
/**
 *
 * @author Terrick
 */
public class Player {
    private static final byte DFT_LIFE         = 4;
    private static final byte DFT_FIRE_FRAME   = 6;
    private static final byte DFT_DSTR_FRAME   = 8;
    private static final byte DFT_SPEED        = 12;
    private static final byte DFT_BOOST        = 2;
    private static final byte DFT_COUNT_EFT    = 9;
    private static final int  DFT_TIME_SPEED   = 10;
    private static final int  DFT_TIME_SHIELD  = 5;

    private static  Image[]   lifeImages       = new Image[DFT_LIFE];
    private static  Image[]   fireImages       = new Image[DFT_FIRE_FRAME];
    private static  Image[]   effectImages     = new Image[DFT_COUNT_EFT];
    private static  byte      countActPlayer;

    private byte    life                       = DFT_LIFE-1;
    private int     x                          = Window.getScreenWidth()/2;
    private int     y                          = Window.getScreenHeight() - Window.getScreenHeight()/3;
    private int     speed                      = DFT_SPEED;
    private byte    numberFireFrame            = 0;
    private byte    numberDstrFrame            = 0;
    private byte    frameHelper                = 0;
    private boolean active                     = true;
    private int     height, width, timeSpeed, timeShield = 0;
    private Timer   timerUpdate;
    private boolean btnLeft, btnRight = false;

    static void setCountActPlayer(byte count) { countActPlayer = count;   }
    public void setBtnLeft(boolean btnLeft)   { this.btnLeft = btnLeft;   }
    public void setBtnRight(boolean btnRight) { this.btnRight = btnRight; }

    Player(int x){
        this.x = x;
        spawn();
    }
    //метод создания игрока
    private void spawn(){
        timerUpdate = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timeShield > 0) timeShield--;
                if (timeSpeed > 0) timeSpeed--;
                else speed = DFT_SPEED;
            }
        });
        timerUpdate.start();
        for (int i = 0; i < DFT_LIFE; i++){
            try { lifeImages[i] = ImageIO.read(new File("images\\gameArea\\player\\life\\" + i + ".png")); }
            catch (IOException exp){
                GameArea.setState(GameArea.STATES.ERRORLOAD);
                break;
            }
        }
        for (int i = 0; i < DFT_FIRE_FRAME; i++){
            try { fireImages[i] = ImageIO.read(new File("images\\gameArea\\player\\fireFrame\\" + i + ".png")); }
            catch (IOException exp){
                GameArea.setState(GameArea.STATES.ERRORLOAD);
                break;
            }
        }
        for (int i = 0; i < DFT_COUNT_EFT; i++){
            try {effectImages[i] = ImageIO.read(new File("images\\gameArea\\player\\effects\\" + i + ".png")); }
            catch (IOException exp){
                GameArea.setState(GameArea.STATES.ERRORLOAD);
                break;
            }
        }
        height = lifeImages[0].getHeight(null);
        width = lifeImages[0].getWidth(null);
    }

    // метод, регулирующий передвижение игрока
    public void move(){
        if (active) {
            if (btnLeft && x > 0){
                x -= speed;
            }
            if (btnRight && x + width < Window.getScreenWidth()) {
                x += speed;
            }
        }
    }

    //метод, отрисовывающий игрока
    public void draw(Graphics gr){
        if (active) {
            move();
            gr.drawImage(lifeImages[life], x, y, null);
            if (numberFireFrame == DFT_FIRE_FRAME) numberFireFrame = 0;
            gr.drawImage(fireImages[numberFireFrame], x + 25, y + height, null);
            numberFireFrame++;
            if (timeShield > 0) {
                gr.drawImage(effectImages[8], x - 30, y - 30, null);
                gr.drawString("shild: " + timeShield, x - 10, y - 40);
            }
            if (timeSpeed > 0) gr.drawString("boost: " + timeSpeed, x - 10, y - 50);
        }
        else drawDestruction(gr);
    }

    //метод, отрисовывающий анимацию разрущения корабля
    public void drawDestruction(Graphics gr){
        gr.drawImage(lifeImages[life], x, y, null);
        if (numberDstrFrame < DFT_DSTR_FRAME) {
            gr.drawImage(effectImages[numberDstrFrame], x - 70, y - 50, null);

            if (frameHelper == 5) {
                numberDstrFrame++;
                frameHelper = 0;
            }
            frameHelper++;
        }
        if (y < Window.getScreenWidth()) y += Object.getSpeed();
        else if (countActPlayer == 0) {
            GameArea.setState(GameArea.STATES.GAMEOVER);
        }
        timerUpdate.stop();
    }

    //метод, проверяющий активацию коллизии игрока с обьектами.
    // При активации коллизии запускает соответствующее действие
    public Boolean touchObject(Object.TYPE object, int ObjectX, int ObjectY, int ObjectW, int ObjectH ){
        if (active) {
            if ((x <= ObjectX + ObjectW && x + width >= ObjectX && y + 10 <= ObjectY + ObjectH*3/4 && y + 10 + height >= ObjectY + ObjectH/4)
                    || (x <=ObjectX + ObjectW*3/4 && x + width >= ObjectX + ObjectW/4 && y + 10 < ObjectY + ObjectH && y + 10 + height >= ObjectY)) {
                switch (object) {
                    case METEORITE -> {
                        if (life > 0) {
                            if (timeShield == 0) life--;
                        }
                    }
                    case HEALTH -> {
                        if (life < DFT_LIFE - 1) life++;
                    }
                    case BOOST -> {
                        speed *= DFT_BOOST;
                        timeSpeed = DFT_TIME_SPEED;
                    }
                    case SHIELD -> {
                        timeShield = DFT_TIME_SHIELD;
                    }
                }

                if (life == 0) {
                    active = false;
                    countActPlayer --;
                }
                return true;
            }
        }
        return false;
    }
}
