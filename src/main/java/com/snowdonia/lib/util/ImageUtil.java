package com.snowdonia.lib.util;

import com.snowdonia.lib.lib.Area;
import com.snowdonia.lib.lib.Pixel;
import com.snowdonia.lib.lib.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil
{
    private static Robot robot;
    private static final int RANGE = 3;

    public static Image loadImage(Class mainClass, String imgName)
    {
        BufferedImage target = null;
        try
        {
            target = ImageIO.read(mainClass.getResource(imgName));
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }

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

                    pixelArray[x][y] = new Pixel((rgb)&0xFF, (rgb>>8)&0xFF, (rgb>>16)&0xFF);
                }
            }

            return new Image(pixelArray, targetWidth, targetHeight);
        }
        return null;
    }

    public static BufferedImage screenBuffer = null;
    public static boolean bufferScreen(Area screenArea)
    {
        if (robot == null)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                return false;
            }
        }
        screenBuffer = robot.createScreenCapture(new Rectangle(screenArea.x, screenArea.y, screenArea.w, screenArea.h));
        return screenBuffer != null;
    }

    public static void clearBuffer()
    {
        screenBuffer = null;
    }

    public static List<Area> locateImages(Area screenArea, Image image)
    {
        //get robot
        List<Area> areas = new ArrayList<>();
        if (robot == null)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                return null;
            }
        }

        //get screen from buffer if it is stored
        BufferedImage imageScreen;
        if (screenBuffer != null) imageScreen = screenBuffer;
        else imageScreen = robot.createScreenCapture(new Rectangle(screenArea.x, screenArea.y, screenArea.w, screenArea.h));

        //scan image
        for (int x = 0; x < screenArea.w; x++)
        {
            for (int y = 0; y < screenArea.h; y++)
            {
                if (colorMatch(image, imageScreen.getRGB(x, y)))
                {
                    Area match = imageMatch(image, imageScreen, x, y);
                    if (match != null)
                    {
                        areas.add(match);
                    }
                }
            }
        }
        return areas;
    }

    public static List<Area> locateImages(Area screenArea, Area scanArea, Image image)
    {
        //get robot
        List<Area> areas = new ArrayList<>();
        if (robot == null)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                return null;
            }
        }

        //get screen from buffer if it is stored
        BufferedImage imageScreen;
        if (screenBuffer != null) imageScreen = screenBuffer;
        else imageScreen = robot.createScreenCapture(new Rectangle(screenArea.x, screenArea.y, screenArea.w, screenArea.h));

        //scan image
        for (int x = scanArea.x; x < (scanArea.w + scanArea.x); x++)
        {
            for (int y = scanArea.y; y < (scanArea.h + scanArea.y); y++)
            {
                if (x < imageScreen.getWidth() && y < imageScreen.getHeight())
                {
                    if (colorMatch(image, imageScreen.getRGB(x, y)))
                    {
                        Area area = imageMatch(image, imageScreen, x, y);
                        if (area != null)
                        {
                            areas.add(area);
                        }
                    }
                }
            }
        }
        return areas;
    }

    /*public static List<ScreenArea> locateImages(List<ScreenStage> screens, Image image)
    {
        List<ScreenArea> areas = new ArrayList<>();
        if (robot == null)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                Main.logger.error(ImageUtil.class, "Unable to create Robot");
            }
        }

        if (robot != null)
        {
            for (ScreenStage screen : screens)
            {
                BufferedImage imageScreen = robot.createScreenCapture(new Rectangle(screen.screenX, screen.screenY, screen.screenW, screen.screenH));
                for (int x = 0; x < screen.screenW; x++)
                {
                    for (int y = 0; y < screen.screenH; y++)
                    {
                        if (colorMatch(image, imageScreen.getRGB(x, y)))
                        {
                            Area area = imageMatch(image, imageScreen, x, y);
                            if (area != null)
                            {
                                areas.add(new ScreenArea(screen, area));
                            }
                        }
                    }
                }
            }
        }
        return areas;
    }*/

    /*public static List<ScreenArea> locateImages(ScreenStage screen, Area scanArea, Image image)
    {
        List<ScreenArea> areas = new ArrayList<>();
        if (robot == null)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                Main.logger.error(ImageUtil.class, "Unable to create Robot");
            }
        }

        if (robot != null)
        {
            BufferedImage imageScreen = robot.createScreenCapture(new Rectangle(screen.screenX, screen.screenY, screen.screenW, screen.screenH));
            for (int x = scanArea.x; x < (scanArea.w + scanArea.x); x++)
            {
                for (int y = scanArea.y; y < (scanArea.h + scanArea.y); y++)
                {
                    if (x < imageScreen.getWidth() && y < imageScreen.getHeight())
                    {
                        if (colorMatch(image, imageScreen.getRGB(x, y)))
                        {
                            Area area = imageMatch(image, imageScreen, x, y);
                            if (area != null)
                            {
                                areas.add(new ScreenArea(screen, area));
                            }
                        }
                    }
                }
            }
        }
        return areas;
    }*/

    public static boolean isInRange(int color, int target)
    {
        return Math.abs(color - target) < RANGE;
    }

    private static boolean colorMatch(Image image, int argb)
    {
        Pixel pixel = image.pixelArray[0][0];
        if (pixel.r == 255 && pixel.b == 255 && pixel.g == 0) return true;
        return isInRange((argb) & 0xFF, pixel.r) && isInRange((argb >> 8) & 0xFF, pixel.g) && isInRange((argb >> 16) & 0xFF, pixel.b);
    }

    public static Area imageMatch(Image image, BufferedImage screen, int xStart, int yStart)
    {
        try
        {
            boolean match = true;
            for (int x = 0; x < image.pixelArray.length; x++)
            {
                for (int y = 0; y < image.pixelArray[x].length; y++)
                {
                    int argb = screen.getRGB(x + xStart, y + yStart);

                    Pixel pixel = image.pixelArray[x][y];
                    if (!(pixel.r == 255 && pixel.b == 255 && pixel.g == 0))
                    {
                        if (!(isInRange((argb) & 0xFF, pixel.r) && isInRange((argb >> 8) & 0xFF, pixel.g) && isInRange((argb >> 16) & 0xFF, pixel.b)))
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
                return new Area(xStart, yStart, image.width, image.height);
            }
        }
        catch (Exception e)
        {
            return null;
        }

        return null;
    }
}
