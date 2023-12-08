package com.snowdonia.lib.imagerecognition.data;

public class Image
{
    public Pixel[][] pixelArray;
    public int width;
    public int height;

    public Image(Pixel[][] pixelArray, int width, int height)
    {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }
}