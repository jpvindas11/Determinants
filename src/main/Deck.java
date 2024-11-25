package main;

import draw.Button;
import draw.FontLoader;
import draw.SpriteLoader;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Deck{

    BufferedImage cards;
    BufferedImage roll_spr;
    Font game_font;

    FontLoader font;
    SpriteLoader spr_loader;

    int current_deck[];
    boolean can_select;
    public boolean update_data;
    public int changed_data;
    public int changed_index;
    public boolean reroll;

    int rolls_left;
    int shake = 0;

    List<Button> card_buttons = new ArrayList<>();
    Button roll;

    public int x;
    public int y;

    public Boolean do_hide = true;

    int x_goal;
    int y_goal;

    int x_hide;
    int y_hide;

    int sprite_width = 96;
    int sprite_height = 144;

    public Deck (int screen_width, int screen_height) {

        cards = SpriteLoader.load_sprite("/res/cards.png");
        game_font = FontLoader.loadCustomFont("/res/font.ttf", 48f);
        roll_spr = SpriteLoader.load_sprite("/res/roll.png");

        x = x_hide = x_goal = screen_width - screen_width/2 + -56;
        y_goal = screen_height - screen_height/4;

        y_hide = y = 816;

        card_buttons.add(new Button(x,y,sprite_width, sprite_height));
        card_buttons.add(new Button(x+sprite_width*1+2,y,sprite_width, sprite_height));
        card_buttons.add(new Button(x+sprite_width*2+2,y,sprite_width, sprite_height));
        card_buttons.add(new Button(x+sprite_width*3+2,y,sprite_width, sprite_height));
        card_buttons.add(new Button(x+sprite_width*4+2,y,sprite_width, sprite_height));

        roll = new Button(x+sprite_width * 6 - sprite_width/2, y + sprite_height - 192, 192, 192);

    }

    private BufferedImage getSprite(int sign) {
        int _x = sign * sprite_width;
        return cards.getSubimage(_x, 0, sprite_width, sprite_height); 
    }
    private int sign(int x)
    {
        if (x > 0) {return 0;}
        else if (x < 0) {return 1;}
        return 2;

    }

    public void update(int[] d, boolean select, int rolls, int mouse_x, int mouse_y, boolean mouse_click)
    {
        can_select = select;
        update_data = false;
        changed_data = 0;
        changed_index = 0;
        reroll = false;

        rolls_left = rolls;
        current_deck = d;
        for (int i = 0; i < card_buttons.size(); i++)
        {
            Button card = card_buttons.get(i);
            if(card.check_click(mouse_click, mouse_x, mouse_y) && can_select)
            {
                update_data = true;
                changed_data = d[i];
                changed_index = i;

            }
            card.y = y; //Carry card buttons
        }

        if (can_select) {
            reroll = roll.check_click(mouse_click, mouse_x, mouse_y);
            roll.can_flash = (rolls > 0);
            
            if (reroll && shake == 0) {shake = 30;}

            roll.y = y+ sprite_height - 192; //Carry reroll button
        }

        if (!do_hide)
        {
            y = SpriteLoader.lerp(y, y_goal, 0.2f);

        }
        else
        {
            y = SpriteLoader.lerp(y, y_hide, 0.2f);
        }


    }
    public void draw (Graphics2D d2)
    {
        d2.setColor(new Color(152,49,5,255));
        
        for(int i = 0; i < current_deck.length; i++)
        {
            //Shake deck
            int shake_x = 0;
            int shake_y = 0;
            if (shake > 0)
            {
                shake_x = (int) (Math.random() * ((8 * 2) + 1)) + -8;
                shake_y = (int) (Math.random() * ((8 * 2) + 1)) + -8;
                if (i == 0) shake--;
            }

            d2.drawImage(getSprite(sign(current_deck[i])),x+sprite_width*i + shake_x,y+shake_y,null);

            String text = String.valueOf(current_deck[i]);
            int negative_offset = 0;
            if (current_deck[i] < 0) {negative_offset = -28;}

            d2.setFont(game_font);

            d2.drawString(text,x+sprite_width*i + sprite_width/2 + negative_offset - 14 +shake_x,y + sprite_height/2 +shake_y);
        }

        if (!can_select)
        {
            d2.setColor(new Color(0,0,0, 200));
            d2.fillRect(x,y,sprite_width*5,sprite_height);
        }
        else{
            for (Button card: card_buttons)
            {
                card.draw(d2);
            }
        }


        //Roll button
        if (can_select) 
        {   
            int image = 0;
            if (rolls_left <= 0) image = 1;
            d2.drawImage(roll_spr.getSubimage(image*192, 0, 192, 192), roll.x, roll.y, null);

            String text = String.valueOf(rolls_left);

            d2.setFont(game_font);
            d2.setColor(Color.black);

            d2.drawString(text,roll.x+192/2 - 16, roll.y+192/2+10);

            roll.draw(d2);
        }
        
    }

}
