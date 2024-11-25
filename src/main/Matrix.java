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

public class Matrix {

    int current_matrix[][];
    int current_determinant;

    int selected[] = new int[2];
    int selected_box_offset = 2;

    BufferedImage matrixSpr;
    Font game_font;
    List<Button> entries = new ArrayList<>();

    int x_pos;
    int y_pos = (810 - 672) / 2 + 48;

    int x_goal;
    int x_hide;

    public Boolean do_hide = true;

    public Matrix() {

        matrixSpr = SpriteLoader.load_sprite("/res/matrix.png");
        game_font = FontLoader.loadCustomFont("/res/font.ttf", 48f);

        entries.add(new Button(x_pos + 36, y_pos + 36, 96, 96));
        entries.add(new Button(x_pos + 36 + 96 + 72, y_pos + 36, 96, 96));
        entries.add(new Button(x_pos + 36 + 96 * 2 + 72 * 2, y_pos + 36, 96, 96));

        entries.add(new Button(x_pos + 36, y_pos + 36 + 96 + 72, 96, 96));
        entries.add(new Button(x_pos + 36 + 96 + 72, y_pos + 36 + 96 + 72, 96, 96));
        entries.add(new Button(x_pos + 36 + 96 * 2 + 72 * 2, y_pos + 36 + 96 + 72, 96, 96));

        entries.add(new Button(x_pos + 36, y_pos + 36 + 96 * 2 + 72 * 2, 96, 96));
        entries.add(new Button(x_pos + 36 + 96 + 72, y_pos + 36 + 96 * 2 + 72 * 2, 96, 96));
        entries.add(new Button(x_pos + 36 + 96 * 2 + 72 * 2, y_pos + 36 + 96 * 2 + 72 * 2, 96, 96));

        selected[0] = -1;
        selected[1] = -1;

        x_pos = x_hide = -512;
        x_goal = 64;

    }

    private int get_list_pos(int i, int j) {
        return i * 3 + j;
    }

    public void update(int[][] m, int det, boolean mouse_click, int mouse_x, int mouse_y) {
        current_matrix = m;
        current_determinant = det;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button current = entries.get(get_list_pos(i, j));
                if (current.check_click(mouse_click, mouse_x, mouse_y)) {
                    if (current.is_selected) {
                        selected[0] = i;
                        selected[1] = j;
                    }
                }
                //Carry buttons
                current.x = x_pos + 36 + 96 * j + 72 * j;
            }
        }

        if (!do_hide) {
            x_pos = SpriteLoader.lerp(x_pos, x_goal, 0.2f);

        } else {
            x_pos = SpriteLoader.lerp(x_pos, x_hide, 0.2f);
        }
    }

    public boolean get_selected() {
        return selected[0] != -1;
    }

    public void draw(Graphics2D d2, int frame, int current_player) {
        if (frame % 30 == 0) {
            if (selected_box_offset == 2) {
                selected_box_offset = 8;
            } else {
                selected_box_offset = 2;
            }
        }

        d2.drawImage(SpriteLoader.sub_image(current_player - 1, 0, 504, 672, matrixSpr), x_pos, y_pos - 48, null);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button current = entries.get(get_list_pos(i, j));

                d2.setColor(Color.black);

                d2.setFont(game_font);

                String text = String.valueOf(current_matrix[i][j]);
                int negative_offset = 0;
                if (current_matrix[i][j] < 0) {
                    negative_offset = -28;
                }

                d2.drawString(text, current.x + current.width / 2 + negative_offset - 14, current.y + current.height / 2 + 8);

                if (selected[0] == i && selected[1] == j)//(current.is_selected) SELECTION BOX
                {
                    d2.setColor(Color.white);
                    d2.drawRect(current.x - selected_box_offset, current.y - selected_box_offset, current.width + selected_box_offset * 2, current.height + selected_box_offset * 2);
                }

                current.draw(d2);

            }
        }
        String text = String.valueOf(current_determinant);
        int negative_offset = 0;
        if (current_determinant < 0) {
            negative_offset = -28;
        }
        d2.setColor(Color.white);
        d2.drawString(text, x_pos + 504 / 2 + negative_offset + 76, y_pos + 570);
    }
}
