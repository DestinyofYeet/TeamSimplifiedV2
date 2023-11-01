package de.uwuwhatsthis.TeamsSimplifiedV2.extensions.Bluemap;


import de.bluecolored.bluemap.api.math.Color;

import java.util.HashMap;

public class ColorMapping {

    private static final HashMap<String, Color> colorMap = new HashMap<>() {{
        put("&0", new Color("#000000")); // black
        put("&1", new Color("#0000AA")); // dark blue
        put("&2", new Color("#00AA00")); // dark green
        put("&3", new Color("#00AAAA")); // dark aqua
        put("&4", new Color("#AA0000")); // dark red
        put("&5", new Color("#AA00AA")); // dark purple
        put("&6", new Color("#FFAA00")); // gold
        put("&7", new Color("#AAAAAA")); // gray
        put("&8", new Color("#555555")); // dark gray
        put("&9", new Color("#5555FF")); // blue

        put("&a", new Color("#55FF55")); // green
        put("&b", new Color("#55FFFF")); // aqua
        put("&c", new Color("#FF5555")); // red
        put("&d", new Color("#FF55FF")); // light purple
        put("&e", new Color("#FFFF55")); // yellow

        put("&f", new Color("#FFFFFF")); // white
    }};

    private static Color putAlphaOnColor(Color color){
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.25f);
    }

    public static Color getColor(String teamColor){
        final Color defaultColor = colorMap.get("&7");


        if (teamColor == null || teamColor.length() == 0){
            return putAlphaOnColor(defaultColor);
        }

        for(String color: teamColor.split("&")){
            Color result = colorMap.get("&" + color);

            if (result == null)
                continue;

            return putAlphaOnColor(result);
        }

        return putAlphaOnColor(defaultColor);
    }
}
