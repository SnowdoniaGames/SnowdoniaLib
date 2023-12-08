package com.snowdonia.lib.imagerecognition;

import com.snowdonia.lib.imagerecognition.data.Area;
import com.snowdonia.lib.imagerecognition.data.Image;
import com.snowdonia.lib.imagerecognition.data.Pixel;
import com.snowdonia.lib.logging.SnowdoniaLogger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ImageRobot
{
    private static Robot robot;

    public static void loadRobot()
    {
        if (robot == null)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                SnowdoniaLogger.fatal(ImageRobot.class, "Unable to create robot");
            }
        }
    }

    public static BufferedImage captureScreen(Area screenArea)
    {
        loadRobot();
        return robot.createScreenCapture(new Rectangle(screenArea.x, screenArea.y, screenArea.w, screenArea.h));
    }

    @SuppressWarnings("ManualArrayCopy")
    public static Pixel[][] snapshotImage(Image screenImage, Area snapshotArea)
    {
        //get robot
        Pixel[][] retArray = new Pixel[snapshotArea.w][snapshotArea.h];
        loadRobot();

        for (int x = 0; x < snapshotArea.w; x++)
        {
            for (int y = 0; y < snapshotArea.h; y++)
            {
                retArray[x][y] = screenImage.pixelArray[x + snapshotArea.x][y + snapshotArea.y];
            }
        }

        return retArray;
    }

    public static List<Area> locateImages(BufferedImage screenCapture, Pixel[][] pixelArray)
    {
        //get robot
        List<Area> areas = new ArrayList<>();
        loadRobot();

        //scan image
        for (int x = 0; x < screenCapture.getWidth(); x++)
        {
            for (int y = 0; y < screenCapture.getHeight(); y++)
            {
                if (ImageUtil.colorMatch(pixelArray, screenCapture.getRGB(x, y)))
                {
                    Area match = ImageUtil.imageMatch(pixelArray, screenCapture, x, y);
                    if (match != null)
                    {
                        areas.add(match);
                    }
                }
            }
        }
        return areas;
    }
}
