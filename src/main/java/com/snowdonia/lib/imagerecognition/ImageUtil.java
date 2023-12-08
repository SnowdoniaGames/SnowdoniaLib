package com.snowdonia.lib.imagerecognition;

import com.snowdonia.lib.imagerecognition.data.Area;
import com.snowdonia.lib.imagerecognition.data.Image;
import com.snowdonia.lib.imagerecognition.data.Pixel;
import com.snowdonia.lib.logging.SnowdoniaLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

@SuppressWarnings("unused")
public class ImageUtil
{
    public static int RANGE = 50;

    public static boolean isInRange(int color, int target, int range)
    {
        return Math.abs(color - target) < range;
    }

    public static boolean colorMatch(Pixel[][] pixelArray, int argb)
    {
        return colorMatch(pixelArray, argb, RANGE);
    }

    public static boolean colorMatch(Pixel[][] pixelArray, int argb, int range)
    {
        Pixel pixel = pixelArray[0][0];
        if (pixel.r == 255 && pixel.b == 255 && pixel.g == 0) return true;
        return isInRange((argb >> 16) & 0xFF, pixel.r, range) && isInRange((argb >> 8) & 0xFF, pixel.g, range) && isInRange((argb) & 0xFF, pixel.b, range);
    }

    public static Area imageMatch(Pixel[][] pixelArray, BufferedImage screen, int xStart, int yStart)
    {
        return imageMatch(pixelArray, screen, xStart, yStart, RANGE);
    }

    public static Area imageMatch(Pixel[][] pixelArray, BufferedImage screen, int xStart, int yStart, int range)
    {
        try
        {
            boolean match = true;
            for (int x = 0; x < pixelArray.length; x++)
            {
                for (int y = 0; y < pixelArray[x].length; y++)
                {
                    int argb = screen.getRGB(x + xStart, y + yStart);

                    Pixel pixel = pixelArray[x][y];
                    if (!(pixel.r == 255 && pixel.b == 255 && pixel.g == 0))
                    {
                        if (!(isInRange((argb >> 16) & 0xFF, pixel.r, range) && isInRange((argb >> 8) & 0xFF, pixel.g, range) && isInRange((argb) & 0xFF, pixel.b, range)))
                        {
                            match = false;
                            break;
                        }
                    }
                }
                if (!match) break;
            }

            if (match)
            {
                return new Area(xStart, yStart, pixelArray.length, pixelArray[0].length);
            }
        }
        catch (Exception e)
        {
            return null;
        }

        return null;
    }

    public static Image loadImage(Class mainClass, String imgName)
    {
        BufferedImage target = null;
        try
        {
            URL url = mainClass.getResource(imgName);
            if (url == null) return null;

            target = ImageIO.read(url);
        }
        catch (Exception e)
        {
            SnowdoniaLogger.error(ImageUtil.class, "Unable to load image : " + e);
        }

        return createImage(target);
    }

    public static Image createImage(BufferedImage target)
    {
        if (target != null)
        {
            int targetWidth = target.getWidth();
            int targetHeight = target.getHeight();

            Pixel[][] pixelArray = new Pixel[targetWidth][targetHeight];

            for (int x = 0; x < targetWidth; x++)
            {
                for (int y = 0; y < targetHeight; y++)
                {
                    int rgb = target.getRGB(x, y);

                    pixelArray[x][y] = new Pixel((rgb>>16)&0xFF, (rgb>>8)&0xFF, (rgb)&0xFF);
                }
            }

            return new Image(pixelArray, targetWidth, targetHeight);
        }
        return null;
    }
}
