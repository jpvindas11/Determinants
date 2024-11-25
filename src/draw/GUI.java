package draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUI {

    int screen_width;
    int screen_height;
    int state;
    int p_regular_y = 36;
    public int turn[] = new int[2];
    List<Element> player = new ArrayList<>();

    Element turns_left;
    Element player_1;
    Element player_2;
    Element player_3;

    Element game_options;
    Element title;

    Element matrix_options;

    Element start_spr;
    Element restart_spr;
    Element roll_spr;
    Button restart;
    Button start;
    Button roll_matrix;

    Element finish;
    Fire21 fire;

    public int score_add = 0;
    int current_player;

    public Boolean send_restart;
    public Boolean send_start;
    public Boolean send_roll;

    public GUI(int s_w, int s_h) {

        screen_width = s_w;
        screen_height = s_h;


        turns_left = new Element(86, 36, 86, -288, 432, 48, 2, "/res/turns_left.png");
        player_1 = new Element(1152, 36, 1152, -104, 288, 96, 2, "/res/player.png");
        player_2 = new Element(1152, 36, 1152, -104, 288, 96, 2, "/res/player.png");
        player_3 = new Element(1152, 36, 1152, -104, 288, 96, 2, "/res/player.png");

        finish = new Element(screen_width / 2 - 468 / 2, screen_height / 2 - 72 * 4, screen_width / 2 - 468 / 2, -86, 468, 72, 3, "/res/finish.png");
        game_options = new Element(screen_width / 2 - 468 / 2, screen_height / 2 - 120 * 3, screen_width / 2 - 468 / 2, -144, 468, 144, 1, "/res/game_options.png");
        title = new Element(screen_width / 2 - 1056 / 2, screen_height / 2 - 384, screen_width / 2 - 1056 / 2, -344,1056, 384, 0, "/res/title.png");

        restart = new Button(24, 24, 48, 48);
        restart_spr = new Element(restart.x, restart.y, restart.x, -56, 48, 48, 2, "/res/exit.png");
        start = new Button(screen_width / 2 - 468 / 2, screen_height - 144, 468, 96);
        start_spr = new Element(start.x, start.y, start.x, screen_height + 144, 468, 96, 0, "/res/start.png");

        matrix_options = new Element(64, (810 - 672) / 2 + 186, -656, (810 - 672) / 2 + 186, 636, 504, 1,"/res/matrix_select.png");

        roll_matrix = new Button(screen_width / 2 + 284, screen_height / 2 - 64, 192, 192);
        roll_spr = new Element(roll_matrix.x, roll_matrix.y, screen_width + 224, roll_matrix.y, 192, 192, 1,"/res/roll_matrix.png");

        fire = new Fire21("/res/fire_21.png");

        player.add(player_1);
        player.add(player_2);
        player.add(player_3);

    }

    public void update(int c_state, int c_player, int _turn[], Boolean mouse_click, int m_x, int m_y) {

        send_restart = false;
        send_start = false;

        if (c_state == 2 || c_state == 3) {
            send_restart = restart.check_click(mouse_click, m_x, m_y);
            restart_spr.state = c_state;
        }

        if (c_state == 0 || c_state == 1)
            send_start = start.check_click(mouse_click, m_x, m_y);

        if (c_state == 1) {

            start_spr.state = 1;
            start_spr.x_goal = screen_width / 2 - 468 / 2 + 384;

            send_roll = roll_matrix.check_click(mouse_click, m_x, m_y);
            if (send_roll)
                matrix_options.shake = 30;

        }

        turns_left.update(c_state);
        player_1.update(c_state);
        player_2.update(c_state);
        player_3.update(c_state);

        finish.update(c_state);
        game_options.update(c_state);
        title.update(c_state);

        matrix_options.update(c_state);

        start_spr.update(c_state);
        restart_spr.update(c_state);
        start.x = start_spr.x;
        start.y = start_spr.y;

        roll_spr.update(c_state);

        restart.y = restart_spr.y;
        roll_matrix.x = roll_spr.x;

        current_player = c_player;

        state = c_state;

        turn = _turn;

        if (c_state == 3) {
            p_regular_y = screen_height / 2 - player_1.height;
            player_1.x_goal = player_2.x_goal = player_3.x_goal = screen_width / 2 - player_1.width / 2;
            player_1.state = player_2.state = player_3.state = 3;
        } else if (c_state == 2) {
            p_regular_y = 36;
            player_1.x_goal = player_2.x_goal = player_3.x_goal = 1152;
            player_1.state = player_2.state = player_3.state = 2;
        }

    }

    private int tiebreaker(int[][] marks, int[] a, int[] b, boolean negative) {
        if (negative && turn[1] < 1) {

            if (marks[a[1]][3] != marks[b[1]][3]) {
                return Integer.compare(marks[a[1]][3], marks[b[1]][3]);
            }
        } else if (!negative && turn[1] < turn[0]) {

            if (marks[a[1]][2] != marks[b[1]][2]) {
                return Integer.compare(marks[b[1]][2], marks[a[1]][2]);
            }
        }

        return Integer.compare(a[1], b[1]);
    }

    public int get_ranking(int c, int[][] marks) {

        int[][] scores = {
                { marks[0][0], 0 },
                { marks[1][0], 1 },
                { marks[2][0], 2 }
        };

        boolean negative = marks[0][0] < 0 && marks[1][0] < 0 && marks[2][0] < 0;

        if (negative) {

            Arrays.sort(scores, (a, b) -> {
                if (a[0] != b[0]) {
                    return Integer.compare(a[0], b[0]);
                }
                return tiebreaker(marks, a, b, true);
            });
        } else {

            Arrays.sort(scores, (a, b) -> {
                if (b[0] != a[0]) {
                    return Integer.compare(b[0], a[0]);
                }
                return tiebreaker(marks, a, b, false);
            });
        }

        // Calcular posiciones de ranking
        int[] rankings = new int[3];
        for (int i = 0; i < scores.length; i++) {
            rankings[scores[i][1]] = i;
        }

        return rankings[c];
    }

    public void draw(Graphics2D d2, int turns, int[][] marks, int frame, int is_21, int main_det) {

        // Title
        title.draw(d2, 0, 0);

        start_spr.draw(d2, 0, 0);
        start.draw(d2);

        // Options menu

        game_options.draw(d2, 0, 0);

        roll_spr.draw(d2, 0, 0);
        roll_matrix.draw(d2);

        // Restart
        restart_spr.draw(d2, 0, 0);
        restart.draw(d2);

        int last_round = 1;

        d2.setColor(Color.white);
        if (turns > 1) {
            String text = String.valueOf(turns);
            d2.setFont(FontLoader.loadCustomFont("/res/font.ttf", 86f));
            d2.drawString(text, turns_left.x + turns_left.width + 12, turns_left.y + 44);
            last_round = 0;
        }

        turns_left.draw(d2, 0, last_round);

        // Player scores
        d2.setFont(FontLoader.loadCustomFont("/res/font.ttf", 48f));

        for (int i = 0; i < player.size(); i++) {

            d2.setColor(Color.white);
            String scr = String.valueOf(marks[i][0]);
            String det = String.valueOf(marks[i][1]);

            Element e = player.get(i);

            e.y_goal = p_regular_y + 96 * get_ranking(i, marks);

            // Win screen modifiers
            if (state == 3) {
                if (is_21 == -1) {
                    if (get_ranking(i, marks) == 0) {
                        e.y_goal -= 112;
                    } else {
                        e.y_goal += 48;
                    }
                } else {
                    e.y_goal -= 112;
                }
            }

            if (state != 3 || state == 3 && is_21 == -1 || state == 3 && is_21 == i) {
                e.draw(d2, i, 0);
                if (is_21 == i) { //Fire sprite
                    fire.update();
                    fire.draw(d2, e.x + e.width / 2 - 52, e.y - 8);
                    d2.setColor(Color.black);
                }

                d2.drawString(det, e.x + e.width / 2 - 48, e.y + 38);

                d2.setColor(Color.white);
                d2.drawString(scr, e.x + e.width / 2 - 24, e.y + e.height - 8);

                //BLOCK SIGN

                if (marks[i][4] == 1)
                {
                    d2.setColor(Color.RED);
                    d2.drawString("BLOCKED!", e.x, e.y+ 48);
                }

            }

            if (i == current_player - 1 && score_add != -9999) { //Score adder
                if (score_add >= 0) {
                    d2.setColor(Color.white);
                } else
                    d2.setColor(Color.red);
                String plus = " + ";

                d2.drawString(String.valueOf(score_add) + plus, e.x - e.width / 2 - 24, e.y + 64);

                d2.setColor(Color.white);
            }
        }

        //Main determinant
        if (turn[0] != -1) {d2.drawString("|Main| = " + main_det, 160, 750);}

        // Finish game
        int col = 0;

        if (marks[0][0] < 0 && marks[1][0] < 0 && marks[2][0] < 0)
            col = 1;

        finish.draw(d2, 0, col);


        //Skipped turn
        if (turn[2] == 1)
        {
            d2.setColor(Color.white);
            d2.setFont(FontLoader.loadCustomFont("/res/font.ttf", 64f));

            d2.drawString("Turn blocked for Player " + current_player, screen_width/2 - screen_width/3 + 16, screen_height/2);
        }
        //Repeat turn
        if (turn[3] == 2)
        {
            d2.setColor(Color.white);
            d2.setFont(FontLoader.loadCustomFont("/res/font.ttf", 64f));

            d2.drawString("Player " + current_player + " goes again!", screen_width/2 - screen_width/4 + 16, screen_height/2);
        }


    }

    public void draw_matrix(Graphics2D d2, int[][] matrix) {
        matrix_options.draw(d2, 0, 0);

        if (state == 1) {
            d2.setColor(Color.BLACK);
            d2.setFont(FontLoader.loadCustomFont("/res/font.ttf", 48f));

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    String text = String.valueOf(matrix[i][j]);
                    int negative_offset = 0;
                    if (matrix[i][j] < 0) {
                        negative_offset = -28;
                    }

                    d2.drawString(text, (matrix_options.x + 36 + 96 * j + 72 * j) + negative_offset + 32,
                            (matrix_options.x + 36 + 96 * i + 72 * i) + 256);

                }
            }

            for (int k = 0; k < 3; k++) {
                String text = String.valueOf(matrix[k][3]);
                int negative_offset = 0;
                if (matrix[k][3] < 0) {
                    negative_offset = -28;
                }
                d2.drawString(text, (matrix_options.x + 36 + 96 * 3 + 72 * 3) + negative_offset + 16,
                        (matrix_options.x + 36 + 96 * k + 72 * k) + 256);
            }
        }
    }
}
