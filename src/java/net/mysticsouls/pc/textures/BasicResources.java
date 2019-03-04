package net.mysticsouls.pc.textures;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class BasicResources {

    public static Image getBasicImage(String path) {
        path = "images/" + path;
        URL pathToImg = Objects.requireNonNull(BasicResources.class.getClassLoader().getResource(path));
        ImageIcon icon = new ImageIcon(pathToImg);
        return icon.getImage();
    }

}
