package main;

import draw.Background;
import draw.GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {

    // Pantalla
    final int og_tile_size = 16;
    final int scale = 1;

    final int tile_size = og_tile_size * scale;

    final int screen_width = 1440;
    final int screen_height = 810;

    state screen;

    enum state {
        TITLE, SELECTION, GAME, END
    }

    Thread game_thread;
    MouseHandler mouse;
    Matrix matrix;
    Deck deck;
    GUI gui;

    Background background;

    int fps = 60;
    public int frame = 0;

    boolean running;
    int matrix_rows;
    int matrix_cols;
    int[][] main_matrix = new int[3][4];
    int main_determinant;

    int[][] p1_matrix;
    int[][] p2_matrix;
    int[][] p3_matrix;

    int p1_last_determinant;
    int p2_last_determinant;
    int p3_last_determinant;

    int p1_first_determinant;
    int p2_first_determinant;
    int p3_first_determinant;

    int p1_final_determinant;
    int p2_final_determinant;
    int p3_final_determinant;

    int[] p1_deck = new int[5];
    int[] p2_deck = new int[5];
    int[] p3_deck = new int[5];

    int p1_block;
    int p2_block;
    int p3_block;
    int c_block;

    int p1_score;
    int p2_score;
    int p3_score;

    int[] c_deck;
    int[][] c_matrix;
    int c_rolls;
    int c_score;

    int c_last_determinant;
    int score_adder = -9999;

    int[][] marks = new int[3][5];

    int p1_rolls = 3;
    int p2_rolls = 3;
    int p3_rolls = 3;

    int def_max_turns = 5;

    int[] p1_dets = new int[def_max_turns];
    int[] p2_dets = new int[def_max_turns];
    int[] p3_dets = new int[def_max_turns];
    int[] c_dets = new int[def_max_turns];

    Boolean finish_turn = false;
    int hide_skip = 0;

    int finish_turn_timer = 60;

    int turns;
    int current_turn;
    int current_player;

    int upper_limit = 10;
    int bottom_limit = -10;

    public Game() {

        screen = state.TITLE;

        matrix_rows = 3;
        matrix_cols = 4;

        mouse = new MouseHandler(this);
        matrix = new Matrix();
        background = new Background(screen_width + 64, screen_height + 64);
        deck = new Deck(screen_width, screen_height);
        gui = new GUI(screen_width, screen_height);

        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.setFocusable(true);

        main_matrix = new int[matrix_rows][matrix_cols];
        init_matrix();

        running = true;

    }

    private void init_game() {
        turns = def_max_turns;
        current_turn = turns;
        current_player = 1;

        p1_score = p2_score = p3_score = 0;

        p1_deck = fill_deck(p1_deck);
        p2_deck = fill_deck(p2_deck);
        p3_deck = fill_deck(p3_deck);

        p1_last_determinant = p2_last_determinant = p3_last_determinant = c_last_determinant = main_determinant;
        p1_matrix = p2_matrix = p3_matrix = c_matrix = main_matrix;
        p1_rolls = p2_rolls = p3_rolls = c_rolls = 3;
        p1_block = p2_block = p3_block = c_block = 0;

        p1_dets = empty_array(p1_dets);
        p2_dets = empty_array(p2_dets);
        p3_dets = empty_array(p3_dets);

        finish_turn = false;
        finish_turn_timer = 60;

        score_adder = -9999;

        update_marks();

        screen = state.GAME;
    }

    private void init_matrix() {

        int determinant = 0;

        while (determinant == 0) // Mientras que el determinante sea 0, no tendrá solución única
        {
            for (int i = 0; i < matrix_rows; ++i) {
                for (int j = 0; j < matrix_cols; ++j) {
                    main_matrix[i][j] = (int) (Math.random() * ((upper_limit * 2) + 1)) + bottom_limit;
                }
            }
            main_determinant = determinant = get_determinant(main_matrix);
            if (determinant == 0) {
                System.out.println("Matrix with no solution generated! " + matrix_rows + " / " + matrix_cols);

            }
        }

        // Matrices para los jugadores
        p1_matrix = p2_matrix = p3_matrix = main_matrix;

        System.out.println("Matrix Initialized");
    }

    private int get_random() {
        return (int) (Math.random() * ((upper_limit * 2) + 1)) + bottom_limit;
    }

    private int[] fill_deck(int[] deck) {
        for (int i = 0; i < 5; i++) {
            deck[i] = get_random();
        }
        return deck;
    }
    private int[] empty_array(int[] A) {
        for (int i = 0; i < A.length; i++) {
            A[i] = 0;
        }
        return A;
    }
    private Boolean look_value(int[] A, int val)
    {
        for (int i = 0; i < A.length; i++) {
            if (A[i] == val) return true;
        }
        return false;
    }
    

    private int get_score(int det) {
        if (det >= 0) {
            return Math.abs(main_determinant - det);
        }
        return det;
    }

    private int get_21() {
        int num = 0;
        int p = -1;
        for (int i = 0; i < 3; i++) {
            if (marks[i][1] == 21) {
                num++;
                p = i;
            }
        }

        if (num != 1) {
            p = -1;
        }
        return p;
    }

    private void save_values() {
        // Get player
        switch (current_player) {
            case 1:
                c_deck = p1_deck;
                c_matrix = clone_matrix(p1_matrix);
                c_rolls = p1_rolls;
                c_score = p1_score;
                c_last_determinant = p1_last_determinant;
                c_block = p1_block;
                c_dets = clone_arr(p1_dets);

                if (current_turn == turns) {
                    p1_first_determinant = c_last_determinant;
                } else if (current_turn == 1) {
                    p1_final_determinant = c_last_determinant;
                }
                break;
            case 2:
                c_deck = p2_deck;
                c_matrix = clone_matrix(p2_matrix);
                c_rolls = p2_rolls;
                c_score = p2_score;
                c_last_determinant = p2_last_determinant;
                c_block = p2_block;
                c_dets = clone_arr(p2_dets);

                if (current_turn == turns) {
                    p2_first_determinant = c_last_determinant;
                } else if (current_turn == 1) {
                    p2_final_determinant = c_last_determinant;
                }
                break;
            case 3:
                c_deck = p3_deck;
                c_matrix = clone_matrix(p3_matrix);
                c_rolls = p3_rolls;
                c_score = p3_score;
                c_last_determinant = p3_last_determinant;
                c_block = p3_block;
                c_dets = clone_arr(p3_dets);

                if (current_turn == turns) {
                    p3_first_determinant = c_last_determinant;
                } else if (current_turn == 1) {
                    p3_final_determinant = c_last_determinant;
                }
                break;
        }
    }

    private void set_values() {
        switch (current_player) {
            case 1:
                p1_deck = c_deck;
                p1_matrix = clone_matrix(c_matrix);
                p1_rolls = c_rolls;
                p1_score = c_score;
                p1_block = c_block;

                if (finish_turn)
                    p1_dets[current_turn-1] = p1_last_determinant = c_last_determinant; 
                break;
            case 2:
                p2_deck = c_deck;
                p2_matrix = clone_matrix(c_matrix);
                p2_rolls = c_rolls;
                p2_score = c_score;
                p2_block = c_block;
                if (finish_turn)
                    p2_dets[current_turn-1] = p2_last_determinant = c_last_determinant;
                break;
            case 3:
                p3_deck = c_deck;
                p3_matrix = clone_matrix(c_matrix);
                p3_rolls = c_rolls;
                p3_score = c_score;
                p3_block = c_block;
                if (finish_turn)
                    p3_dets[current_turn-1] = p3_last_determinant = c_last_determinant;
                break;
        }
    }

    private void update_marks() {
        marks[0][0] = p1_score;
        marks[0][1] = p1_last_determinant;
        marks[1][0] = p2_score;
        marks[1][1] = p2_last_determinant;
        marks[2][0] = p3_score;
        marks[2][1] = p3_last_determinant;

        marks[0][2] = p1_first_determinant;
        marks[0][3] = p1_final_determinant;
        marks[1][2] = p2_first_determinant;
        marks[1][3] = p2_final_determinant;
        marks[2][2] = p3_first_determinant;
        marks[2][3] = p3_final_determinant;

        marks[0][4] = p1_block;
        marks[1][4] = p2_block;
        marks[2][4] = p3_block;
    }

    private void next_player() {
        if (finish_turn_timer > 0) {
            if (finish_turn_timer == 60) {

                if (c_block != 1 && look_value(c_dets, get_determinant(c_matrix)) && c_last_determinant != main_determinant) // Block if same determinant as
                                                                                                                               // last turn or before
                {
                    c_block = 1;
                    score_adder = get_score(get_determinant(c_matrix));

                    c_last_determinant = get_determinant(c_matrix);
                    c_score += score_adder;
                } 
                else if (c_block != 2 && get_determinant(c_matrix) == main_determinant && current_turn != turns) //Go again if determinant gotten same as main determinant
                {
                    c_block = 2;
                    score_adder = get_score(get_determinant(c_matrix));

                    c_last_determinant = get_determinant(c_matrix);
                    c_score += score_adder;
                }
                else {
                    if (c_block != 1) {
                        score_adder = get_score(get_determinant(c_matrix));

                        c_last_determinant = get_determinant(c_matrix);
                        c_score += score_adder;
                    } else
                        c_block = 0;
                }

                set_values();
            }
            finish_turn_timer--;

        } else {

            if (c_block != 2) {current_player++;}
            else {c_block = 0; set_values();}

            if (current_player > 3) {
                current_player = 1;
                current_turn--;

                if (get_21() != -1) {
                    current_turn = 0;
                }
            }

            finish_turn = false;
            hide_skip = 0;
            finish_turn_timer = 60;
            score_adder = -9999;

        }

        // Move elements
        if (finish_turn_timer % 30 == 0) {
            if (current_player == 3 && current_turn <= 1 || current_player == 3 && get_21() != -1) {
                deck.do_hide = matrix.do_hide = true;

            } else {
                deck.do_hide = !deck.do_hide;
                matrix.do_hide = !matrix.do_hide;
            }

        }
    }

    private int get_determinant(int[][] matrix) {
        return ((matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][2] * matrix[2][0]
                + matrix[0][2] * matrix[1][0] * matrix[2][1])
                - (matrix[2][0] * matrix[1][1] * matrix[0][2] + matrix[2][1] * matrix[1][2] * matrix[0][0]
                        + matrix[2][2] * matrix[1][0] * matrix[0][1]));
    }

    private int[][] clone_matrix(int[][] matrix) {
        int[][] clone = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, clone[i], 0, matrix[i].length);
        }
        return clone;
    }

    
    private int[] clone_arr(int[] arr) {
        int[] clone = new int[arr.length];
        System.arraycopy(arr, 0, clone, 0, arr.length);
        return clone;
    }

    // Iniciar el ciclo

    public void start_thread() {
        game_thread = new Thread(this);
        game_thread.start();
    }

    // Ejecuta el ciclo infinito del juego, update tiene la lógica, render tiene
    // dibujado

    @Override
    public void run() {

        // Sistema para ejecutar a 60 fps
        double draw_interval = 1000000000 / fps;
        double delta = 0;
        long last_time = System.nanoTime();
        long current_time;

        long timer = 0;

        while (game_thread != null) {

            current_time = System.nanoTime();

            delta += (current_time - last_time) / draw_interval;
            timer += (current_time - last_time);
            last_time = current_time;

            if (delta >= 1) // Cuando es 1, ejecuta actualizaciones
            {
                update();
                repaint();
                delta--;

                frame++;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }

    }

    //////////////////////////////////////
    // Evento update del juego, aquí toda la lógica
    //////////////////////////////////////

    private void update() {

        int[] turn_info = new int[4];

        background.update(frame);
        if (screen != state.GAME) {
            background.current_bg = 0;
            turn_info[0] = -1;
        }
        else{
            turn_info[0] = turns;
            turn_info[1] = current_turn;
            turn_info[2] = Integer.valueOf(hide_skip);
            turn_info[3] = c_block;
        }


        gui.update(screen.ordinal(), current_player, turn_info, mouse.click, mouse.mouse_x, mouse.mouse_y);

        if (null != screen)
            switch (screen) {
                case TITLE:
                    if (gui.send_start) {
                        gui.send_restart = false;
                        gui.send_start = false;
                        mouse.click = false;
                        screen = state.SELECTION;
                        return;
                    }
                    break;
                case SELECTION:
                    if (gui.send_start) {
                        gui.send_restart = false;
                        gui.send_start = false;
                        mouse.click = false;
                        init_game();
                        return;
                    }
                    if (gui.send_roll) {
                        gui.send_roll = false;
                        mouse.click = false;
                        init_matrix();
                    }
                    break;
                case GAME:
                    save_values();

                    if (c_block == 1 && !finish_turn) {
                        finish_turn = true;
                        hide_skip = 1;
                    }

                    background.current_bg = current_player;
                    deck.update(c_deck, matrix.get_selected(), c_rolls, mouse.mouse_x, mouse.mouse_y, mouse.click);
                    matrix.update(c_matrix, get_determinant(c_matrix), mouse.click, mouse.mouse_x, mouse.mouse_y);
                    if (deck.update_data && !finish_turn) {
                        if (matrix.selected[0] != -1 && matrix.selected[1] != -1) {
                            c_matrix[matrix.selected[0]][matrix.selected[1]] = deck.changed_data;
                        }
                        c_deck[deck.changed_index] = get_random();
                        matrix.selected[0] = matrix.selected[1] = -1;

                        deck.update_data = false;
                        finish_turn = true;

                    }
                    mouse.click = false;
                    if (deck.reroll) {
                        c_deck = fill_deck(p1_deck);
                        c_rolls--;
                    }
                    set_values();
                    update_marks();
                    if (finish_turn)
                        next_player(); // Next
                    else {
                        deck.do_hide = false;
                        matrix.do_hide = false;
                        finish_turn = false;
                    }
                    gui.score_add = score_adder;
                    // Turn management
                    if (current_turn <= 0) {
                        update_marks();
                        p1_block = p2_block = p3_block = 0;
                        screen = state.END;
                    }
                    break;
                default:
                    break;
            }

        // Restarted
        if (gui.send_restart)
            screen = state.SELECTION;

    }

    @Override
    public void paintComponent(Graphics draw) {
        super.paintComponent(draw);

        Graphics2D draw2 = (Graphics2D) draw;

        background.draw(draw2);

        if (screen == state.GAME) {
            if (hide_skip == 0) {
                deck.draw(draw2);

                matrix.draw(draw2, frame, current_player);
            }
        }

        gui.draw(draw2, current_turn, marks, frame, get_21(), main_determinant);

        if (screen.ordinal() > 0)
            gui.draw_matrix(draw2, main_matrix);


        draw2.dispose();
    }

}
