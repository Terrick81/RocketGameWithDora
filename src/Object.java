import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author Terrick
 */
class Object {
    public enum TYPE{METEORITE, BOOST, HEALTH, SHIELD}
    private static int     speed               = 6;
    private        int     reloading           = 0;
    private        Boolean active              = false;
    private static int     countObjectActive   = 0;
    private        int     x,y, width, height;
    private        Timer   timerUpdate;
    private        Image   img;
    private        TYPE    type;

    static void    setSpeed(int speed)          { Object.speed = speed;     }
    static int     getCountObjectActive()       { return countObjectActive; }
    static int     getSpeed()                   { return Object.speed;      }
    public boolean getActive()                  { return active;            }
    public TYPE    getType()                    { return type;              }
    public int     getX()                       { return x;                 }
    public int     getY()                       { return y;                 }
    public int     getHeight()                  { return height;            }
    public int     getWidth()                   { return width;             }

    Object(Image img, int number) {
        timerUpdate = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (reloading > 0) reloading--;
            }
        });
        timerUpdate.start();
        this.img = img;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
        this.x =  random(0,  Window.getScreenWidth() - width );

        switch (number){
            case 7  -> this.type = TYPE.HEALTH;
            case 8  -> this.type = TYPE.SHIELD;
            case 9  -> this.type = TYPE.BOOST;
            default -> this.type = TYPE.METEORITE;
        }
    }

    //метод активации объекта
    void start() {
        if (!active) {
            if (reloading == 0)
            {
                y = -height;
                x = random(0,  Window.getScreenWidth() - width );
                countObjectActive ++;
                active = true;
            }
        }
    }

    //метод, передвигающий объект
    void down(){
        if (active) {
            y += speed;
            if (y > Window.getScreenHeight() + 50) {
                countObjectActive --;
                active = false;
            }
        }
    }
    //метод отрисовки
    void draw(Graphics gr){
        if (active) {
            gr.drawImage(img, x, y, null);
        }
    }
    //метод генерации случайного числа в заданном диапозоне
    private int random(int min, int max){
        return (int)(Math.random() * (max - min + 1) + min);
    }

    //метод уничтожения объекта
    public void destruction(){
        switch (type){
            case HEALTH  -> reloading = random(5, 15);
            case BOOST   -> reloading = random(5, 15);
            case SHIELD  -> reloading = random(5, 15);
        }
        countObjectActive --;
        active = false;
    }

    //метод, для деактивации обьектов.
    // Используется при повторном запуске игры.
    public void clear(){
        countObjectActive = 0;
        active = false;
    }
}
