package draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Element {
    public int x;
    public int y;
    int x_goal;
    int y_goal;
    int x_hide;
    int y_hide;
    public int width;
    public int shake = 0;
    int height;
    

    int state;

    BufferedImage sprite;

    public Element (int _x_g, int _y_g, int _x_h, int _y_h, int w, int h, int _state, String path)
    {
        x = x_hide = _x_h;
        y = y_hide = _y_h;
        x_goal = _x_g;
        y_goal = _y_g;

        width = w;
        height = h;

        state = _state;

        sprite = SpriteLoader.load_sprite(path);
    }

    public void update(int current_state){
        if (state == current_state)
        {
            x = SpriteLoader.lerp(x, x_goal, 0.2f);
            y = SpriteLoader.lerp(y, y_goal, 0.2f);
        }
        else
        {
            x = SpriteLoader.lerp(x, x_hide, 0.2f);
            y = SpriteLoader.lerp(y, y_hide, 0.2f);
        }
    }
    public void draw(Graphics2D d2, int col, int row)
    {
        //Shake deck
        int shake_x = 0;
        int shake_y = 0;
        if (shake > 0)
        {
            shake_x = (int) (Math.random() * ((8 * 2) + 1)) + -8;
            shake_y = (int) (Math.random() * ((8 * 2) + 1)) + -8;
            shake--;
        }

        d2.drawImage(SpriteLoader.sub_image(col, row, width, height, sprite), x+shake_x, y+shake_y, null);
    }
}
