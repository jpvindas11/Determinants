package main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class MouseHandler implements MouseListener, MouseMotionListener {

    public boolean click;
    public int mouse_x;
    public int mouse_y;
    private final JPanel panel;
    
    public MouseHandler(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = true;
        mouse_x = e.getX();
        mouse_y = e.getY();
        panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        click = false;
        mouse_x = e.getX();
        mouse_y = e.getY();
        panel.repaint(); 
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse_x = e.getX();
        mouse_y = e.getY();
        panel.repaint(); 
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouse_x = e.getX();
        mouse_y = e.getY();
        panel.repaint(); 
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
