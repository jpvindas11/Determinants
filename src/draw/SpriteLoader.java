package draw;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteLoader {
    public SpriteLoader() {}

    public static BufferedImage load_sprite(String path) {
        BufferedImage sprite = null;
        try {
            System.out.println("Ruta del recurso: " + SpriteLoader.class.getResource(path));

            sprite = ImageIO.read(SpriteLoader.class.getResourceAsStream(path));
            if (sprite == null) {
                throw new IOException("La imagen no pudo ser cargada. Verifica la ruta: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + path);
            e.printStackTrace();
        }
        return sprite;
    }

    public static BufferedImage sub_image (int col, int row, int w, int h, BufferedImage spr)
    {
        int x = col * w;
        int y = row * h;
        return spr.getSubimage(x, y, w, h); 
    }

    public static int lerp(int start, int end, float t) {
        return Math.round(start + t * (end - start));
    }
}
