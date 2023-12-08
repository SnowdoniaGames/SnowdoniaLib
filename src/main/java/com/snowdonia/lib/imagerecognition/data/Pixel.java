package com.snowdonia.lib.imagerecognition.data;

public class Pixel
{
    public int r;
    public int g;
    public int b;

    public Pixel(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Pixel)) return false;

        return this.r == ((Pixel) obj).r && this.b == ((Pixel) obj).b && this.g == ((Pixel) obj).g;
    }
}
