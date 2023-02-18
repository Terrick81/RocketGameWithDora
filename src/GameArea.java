import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Terrick
 */
public class GameArea extends JPanel {

    private static final byte[] COUNT_MASS_IMG = {10, 2};
    private static final int DELAY_FRAME    = 25;
    private static final int DFT_COUNT_DFCT  = 8;
    private static final int DFT_COUNT_BTN   = 4 + DFT_COUNT_DFCT;
    public  enum STATES{MENU, PLAY1P, PLAY2P, PLAY3P, PlAYAREA, ERRORLOAD, GAMEOVER}

    private static STATES            state      = STATES.MENU;
    private static int               difficult  = 4;
    private static int               score      = 0;
    private static int      localBetterScore    = 0;
    private static ArrayList<Player> players    = new ArrayList<>();
    private static Object[]          gameObject = new Object[COUNT_MASS_IMG[0]];
    private static Image[]           images     = new Image[COUNT_MASS_IMG[1]];
    private static Button[]          buttons    = new Button[DFT_COUNT_BTN];
    private static Font              font       = new Font( Font.SANS_SERIF , Font.PLAIN| Font.BOLD , 40);
    private static MyMouseListener   mouse      = new MyMouseListener();
    private        Timer             timerRepaint;

    static ArrayList<Player> getPlayers()                { return players;    }
    static void              setState(STATES localState) { state = localState;}
    static void              setDifficult(int dfct)      { difficult = dfct;  }
    static STATES            getState()                  { return state;      }

    public GameArea() {
        setFocusable(true);
        requestFocus();
        addKeyListener(new MyKeyListener());
        addMouseListener(mouse);

        timerRepaint = new Timer(DELAY_FRAME, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timerRepaint.start();
        for (int count = 0; count < COUNT_MASS_IMG.length; count++) {
            for (int i = 0; i < COUNT_MASS_IMG[count]; i++){
                try{
                    if (count == 0) {
                        gameObject[i] = new Object(ImageIO.read(new File("images\\gameArea\\gameObject\\" + i + ".png")), i);
                    }
                    if (count == 1) {
                        images[i] = ImageIO.read(new File("images\\gameArea\\1" + i + ".png"));
                    }
                }
                catch (IOException exp){
                    state = STATES.ERRORLOAD;
                    break;
                }
            }
        }

        buttons[0] = new Button(650, 520, 220, 100, 60, "exit", mouse);
        buttons[1] = new Button(250, 260, 310, 100, 55, "1 Player", mouse);
        buttons[2] = new Button(600, 260, 310, 100, 55, "2 Players", mouse);
        buttons[3] = new Button(950, 260, 310, 100, 55, "3 Players", mouse);

        for (int i = DFT_COUNT_BTN - DFT_COUNT_DFCT; i < DFT_COUNT_BTN; i++) {
            Integer label = i - (DFT_COUNT_BTN - DFT_COUNT_DFCT) + 1;
            buttons[i] = new Button(210 + label * 110, 400, 80, 80, 28, label.toString(), mouse);
        }
    }
    //метод регулирующий вызов других методов.
    public void paintComponent(Graphics gr)
    {
        switch (state) {
            case PLAY1P    -> spawn(1);
            case PLAY2P    -> spawn(2);
            case PLAY3P    -> spawn(3);
            case PlAYAREA  -> playMethod(gr);
            case MENU      -> menuMethod(gr);
            case GAMEOVER  -> gameOverMethod(gr);
            default        -> errorLoadMethod(gr);
        }
    }

    //метод регулирующий вызов методов отрисовки окон по нажатию кнопки ESC.
    static void switchMenu(){
        switch (state) {
            case PlAYAREA  -> state = STATES.MENU;
            case GAMEOVER  -> state = STATES.MENU;
            case MENU      -> {
                if (players.size() > 0) state = STATES.PlAYAREA;
            }
        }
    }

    //метод регулирующий количество игроков на игровом поле.
    private void spawn(int count) {
        players.clear();
        for (byte i = 0; i < count; i ++) {
            players.add(new Player(300 + 300 * i));
        }
        state =  STATES.PlAYAREA;if (FinalScore() > localBetterScore) {
            localBetterScore = FinalScore();
        }
        Player.setCountActPlayer((byte)count);
        score = 0;
        for (Object object: gameObject) {
            object.clear();
        }
    }
    //метод отрисовки игрового цикла
    private void playMethod(Graphics gr) {
        score += difficult/3 + 2;
        gr.setColor(Color.WHITE);
        gr.drawImage(images[0], 0, 0, null);

        for (Player player : players) {
            player.draw(gr);
            for (Object object: gameObject) {
                if (object.getActive()) {
                    if (player.touchObject(object.getType(), object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
                        object.destruction();
                    }
                }
            }
        }

        if (Object.getCountObjectActive() < difficult/2 + 3) {
            gameObject[random(0, COUNT_MASS_IMG[0] - 1)].start();
        }
        for ( Object object: gameObject) {
            object.draw(gr);
            object.down();
        }

        gr.drawString("Выход в меню [ESC]", 10, 20);
        gr.drawString("Your score: " + FinalScore(), 10, 40);
    }

    //метод отрисовки меню
    private void menuMethod(Graphics gr) {
        gr.drawImage(images[0], 0, 0, null);
        gr.setFont(font);
        gr.setColor(Color.WHITE);
        gr.drawString("Play", 700, 230);
        for (Button button: buttons){
            button.draw(gr);
            button.touchButton();
        }
    }

    //метод отрисовки окна поражения
    private void gameOverMethod(Graphics gr){
        gr.drawImage(images[0], 0, 0, null);
        gr.drawImage(images[1], 550, 400, null);
        gr.setFont(font);
        gr.setColor(Color.WHITE);
        if (FinalScore() > localBetterScore) {
            localBetterScore = FinalScore();
        }
        gr.drawString("текущий счет: " + FinalScore(), 580, 560);
        gr.drawString(" лучший счет: " + localBetterScore, 580, 610);
        gr.drawString("Выход в меню [ESC]", 580, 840);
    }

    //метод отрисовки окна, предупреждающего об ошибке
    private void errorLoadMethod(Graphics gr){
        gr.setFont(font);
        gr.drawString("Возникли какие либо ошибки", 440, 450);
        buttons[0].draw(gr);
        buttons[0].touchButton();
    }

    //метод генерирующий случайное лисло в заданном диапозоне
    private int random(int min, int max){
        return (int)(Math.random() * (max - min + 1) + min);
    }
    //метод обрабатывающий игровой счет
    private int FinalScore() { return score/20;}
}
