package draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Background {

    int x_border;
    int y_border;
    List<BufferedImage> bg = new ArrayList<>();
    SpriteLoader loader;

    BufferedImage bg_blue;
    BufferedImage bg_red;
    BufferedImage bg_green;
    BufferedImage bg_black;

    int spriteWidth = 96; // Ancho de cada sprite
    int spriteHeight = 96; // Alto de cada sprite

    int shapes_amount = 20;
    public int current_bg = 0;
    int moved_x = 0;

    List<MovingShape> shapes = new ArrayList<>();

    public Background(int x_b, int y_b) {

        bg_blue = SpriteLoader.load_sprite("/res/bg_shapes_blue.png");
        bg_green = SpriteLoader.load_sprite("/res/bg_shapes_green.png");
        bg_red = SpriteLoader.load_sprite("/res/bg_shapes_red.png");
        bg_black = SpriteLoader.load_sprite("/res/bg_shapes_black.png");

        bg.add(bg_black); bg.add(bg_blue); bg.add(bg_red); bg.add(bg_green); 

        x_border = x_b + spriteWidth;
        y_border = y_b + spriteHeight;

        //Shapes
        for (int i = 0; i < shapes_amount; i++)
        {   
            shapes.add(new MovingShape());
        }
    }

    // Método para obtener un solo sprite de la hoja de sprites
    private BufferedImage getSprite(int col, int spr) {
        int x = col * spriteWidth;
        BufferedImage item = bg.get(spr); 
        return item.getSubimage(x, 0, spriteWidth, spriteHeight); 
    }


    public void update (int frame)
    {
        for(MovingShape item: shapes)
        {
            item.update(x_border,y_border);
        }
        if (moved_x < spriteWidth*2)
        {
            moved_x++;
        } else moved_x = 0;
    }
    public int get_square_image (int i, int j)
    {
        if (i%2 == 0 && j%2 == 0) return 0;
        else if (i%2 == 0 && j%2 != 0) return 1;
        else if (i%2 != 0 && j%2 != 0) return 1;
        return 0;
    }

    public void draw(Graphics2D d2) {
        int x = 0; 
        int y = 0;
        for (int i = 0; i < y_border; i += spriteHeight) {
            for (int j = -(spriteWidth * 2); j < x_border; j += spriteWidth) {
                BufferedImage sprite = getSprite(get_square_image(x, y),current_bg);
                d2.drawImage(sprite, j + moved_x, i, null);
                y++;
            }
            x++;
        }
        for(MovingShape item: shapes)
        {
            BufferedImage shape = getSprite(item.sprite,current_bg);
            d2.drawImage(shape, item.x_pos,item.y_pos,null);
        }

    }
}
