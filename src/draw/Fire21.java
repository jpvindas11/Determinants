package draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Fire21 {

    int index = 0;
    int speed = 3;
    int current_frame = 0;
    int length = 4;

    BufferedImage sprite;

    public Fire21(String path)
    {
        sprite = SpriteLoader.load_sprite(path);
    }
    public void update()
    {
        if (index != speed)
        {
            index++;
        }
        else{
            index = 0;
            current_frame++;
            
            if (current_frame > length-1) {
                current_frame = 0;
            }
        }
    }

    public void draw(Graphics2D d2, int x, int y)
    {
        d2.drawImage(SpriteLoader.sub_image(current_frame,0,48,48,sprite), x, y, null);
    }

}
