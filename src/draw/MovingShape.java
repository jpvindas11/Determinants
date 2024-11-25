package draw;

import java.util.Random;

public class MovingShape {

    int x_speed;
    int y_speed;

    public int x_pos;
    public int y_pos;

    public int sprite;

    public MovingShape(){

        sprite = get_random(2, 7);
        set_speed();

        x_pos = get_random(0, 800);
        y_pos = get_random(0, 800);
    }

    private int get_random(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void set_speed()
    {
        x_speed = get_random(1, 3);
        y_speed = get_random(1, 3);
    }
    public void update(int x_border, int y_border)
    {
        x_pos += x_speed;
        y_pos += y_speed;

        if (x_pos > x_border || y_pos > y_border)
        {
            x_pos = get_random(-256, x_border/2);
            int y_lim = -96;

            if (x_pos < -96) {y_lim = y_border/2;}

            y_pos = get_random(-256, y_lim);
            set_speed();
        }
    }
}
