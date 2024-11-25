package draw;

import java.awt.Color;
import java.awt.Graphics2D;

public class Button {

    public int x;
    public int y;
    public int width;
    public int height;
    boolean clicked = false;
    boolean hover = false;
    public boolean is_selected;
    public boolean can_flash = true;

    public Button (int _x, int _y, int _width, int _height)
    {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
    }

    public boolean check_click (boolean mouse_click, int mouse_x, int mouse_y)
    {
      
        //Mouse hover
        hover = mouse_x >= x && mouse_x <= x+width && mouse_y >= y && mouse_y <= y+height;

        clicked = false;
        if (mouse_click && hover)
        {
            is_selected = true;
            if (can_flash) clicked = true;
        }
        else if (mouse_click && !hover)
        {
            is_selected = false;
        }
        return clicked;
    }

    public void draw (Graphics2D d2)
    {
        d2.setColor(new Color(255, 255, 255, 128)); 
        
        if (hover && can_flash) {
            d2.fillRect(x, y, width, height);
        }
    }

}
