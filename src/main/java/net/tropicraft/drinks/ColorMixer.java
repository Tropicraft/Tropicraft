package net.tropicraft.drinks;

public final class ColorMixer {
    private static ColorMixer instance = new ColorMixer();

    public ColorMixer() {
    }
    
    public static ColorMixer getInstance() {
        return instance;
    }
    
    public void normalizeRGBA(int[] rgba, float[] result) {
        result[0] = rgba[0]/255f;
        result[1] = rgba[1]/255f;
        result[2] = rgba[2]/255f;
        result[3] = rgba[3]/255f;
    }
    
    public void denormalizeRGBA(float[] rgba, int[] result) {
        result[0] = (int)(255*rgba[0]);
        result[1] = (int)(255*rgba[1]);
        result[2] = (int)(255*rgba[2]);
        result[3] = (int)(255*rgba[3]);
    }
    
    public void splitRGBA(long color, int[] result) {
        result[0] = (int)((color>>24) & 0xff);
        result[1] = (int)((color>>16) & 0xff);
        result[2] = (int)((color>>8) & 0xff);
        result[3] = (int)(color & 0xff);
    }
    
    public long unsplitRGBA(int[] rgb) {
        return ((rgb[0]&0xff)<<24L)|((rgb[1]&0xff)<<16L)|((rgb[2]&0xff)<<8L)|(rgb[3]&0xff);
    }       
    
    public void normalizeRGB(int[] rgb, float[] result) {
        result[0] = rgb[0]/255f;
        result[1] = rgb[1]/255f;
        result[2] = rgb[2]/255f;
    }
    
    public void denormalizeRGB(float[] rgb, int[] result) {
        result[0] = (int)(255*rgb[0]);
        result[1] = (int)(255*rgb[1]);
        result[2] = (int)(255*rgb[2]);
    }
    
    public void splitRGB(int color, int[] result) {
        result[0] = (color>>16) & 0xff;
        result[1] = (color>>8) & 0xff;
        result[2] = color & 0xff;
    }
    
    public int unsplitRGB(int[] rgb) {
        return ((rgb[0]&0xff)<<16)|((rgb[1]&0xff)<<8)|(rgb[2]&0xff);
    }
    
    public void convertRGBToCMYK(float[] rgb, float[] cmyk) {
        float tempC = 1 - rgb[0];
        float tempM = 1 - rgb[1];
        float tempY = 1 - rgb[2];
        
        float black = Math.min(tempC,Math.min(tempM,tempY));
        float cyan = (tempC - black) / (1-black);
        float magenta = (tempM - black) / (1-black);
        float yellow = (tempY - black) / (1-black);
        
        cmyk[0] = cyan;
        cmyk[1] = magenta;
        cmyk[2] = yellow;
        cmyk[3] = black;
    }
    
    public void convertCMYKToRGB(float[] cmyk, float[] rgb) {
        float c = cmyk[0];
        float m = cmyk[1];
        float y = cmyk[2];
        float k = cmyk[3];
        
        float nc = c * (1-k) + k;
        float nm = m * (1-k) + k;
        float ny = y * (1-k) + k;
        
        float r = 1-nc;
        float g = 1-nm;
        float b = 1-ny;
        
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }
    
    public void convertRYBToRGB(float[] ryb, float[] rgb) {
        float r = ryb[0];
        float y = ryb[1];
        float b = ryb[2];
        
        // remove whiteness
        
        float w = Math.min(r, Math.min(y, b));
        r -= w;
        y -= w;
        b -= w;
        
        float my = Math.max(r, Math.max(y, b));
        
        // get green out of yellow and blue
        float g = Math.min(y, b);
        y -= g;
        b -= g;
        
        if (b != 0 && g != 0) {
            b *= 2;
            g *= 2;
        }
        
        // redistribute remaining yellow
        r += y;
        g += y;
        
        // normalize to values
        float mg = Math.max(r, Math.max(g, b));
        
        if (mg != 0) {
            float n = my / mg;
            r *= n;
            g *= n;
            b *= n;
        }
        
        // add the white back in
        r += w;
        g += w;
        b += w;
        
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }
    
    public void convertRGBToRYB(float[] rgb, float[] ryb) {
        float r = rgb[0];
        float g = rgb[1];
        float b = rgb[2];
        
        // remove white
        float w = Math.min(r,Math.min(g, b));
        r -= w;
        g -= w;
        b -= w;
        
        float mg = Math.max(r, Math.max(g, b));
        
        // remove yellow
        float y = Math.min(r, g);
        r -= y;
        g -= y;
        
        // if both blue and green then cut in half to preserve range
        if (b != 0 && g != 0) {
            b /= 2f;
            g /= 2f;
        }
        
        // redistribute the green
        y += g;
        b += g;
        
        float my = Math.max(r, Math.max(y, b));
        
        // normalize to values
        if (my != 0) {
            float n = mg/my;
            r *= n;
            y *= n;
            b *= n;
        }
        
        // add back in white
        r += w;
        y += w;
        b += w;
        
        ryb[0] = r;
        ryb[1] = y;
        ryb[2] = b;
    }
    
    public void mixCMYK(float[][] cmyks, float[] result) {
        if (cmyks.length == 0) {
            result[0] = result[1] = result[2] = result[3] = 0;
            return;
        }
        
        if (cmyks.length == 1) {
            float[] cmyk = cmyks[0];
            result[0] = cmyk[0];
            result[1] = cmyk[1];
            result[2] = cmyk[2];
            result[3] = cmyk[3];
            return;
        }
        
        float cTotal = 0f;
        float mTotal = 0f;
        float yTotal = 0f;
        float kTotal = 0f;
        
        float cMax = 0f;
        float mMax = 0f;
        float yMax = 0f;
        float kMax = 0f;
        
        for (float[] cmyk: cmyks) {
            float c = cmyk[0];
            float m = cmyk[1];
            float y = cmyk[2];
            float k = cmyk[3];
            cTotal += c;
            mTotal += m;
            yTotal += y;
            kTotal += k;
            
            cMax = c > cMax ? c : cMax;
            mMax = m > mMax ? m : mMax;
            yMax = y > yMax ? y : yMax;
            kMax = k > kMax ? k : kMax;
        }
        
        int count = cmyks.length;
        float c = cTotal/(float)Math.sqrt(count+1);
        float m = mTotal/(float)Math.sqrt(count+1);
        float y = yTotal/(float)Math.sqrt(count+1);
        float k = kTotal/(float)Math.sqrt(Math.sqrt(count));
        
        result[0] = c;
        result[1] = m;
        result[2] = y;
        result[3] = k;
    }
    
    public void mixRYB(float[][] rybs, float[] result) {
        if (rybs.length == 0) {
            result[0] = result[1] = result[2] = 0;
            return;
        }
        
        if (rybs.length == 1) {
            float[] ryb = rybs[0];
            result[0] = ryb[0];
            result[1] = ryb[1];
            result[2] = ryb[2];
            return;
        }
        
        float rTotal = 0;
        float yTotal = 0;
        float bTotal = 0;
        
        for (float[] ryb: rybs) {
            rTotal += ryb[0];
            yTotal += ryb[1];
            bTotal += ryb[2];
        }
        
        int count = rybs.length;
        float br = rTotal/count;
        float r = rTotal/(float)Math.sqrt(Math.sqrt(count-br));
        br = yTotal/count;
        float y = yTotal/(float)Math.sqrt(Math.sqrt(count-br));
        br = bTotal/count;
        float b = bTotal/(float)Math.sqrt(Math.sqrt(count-br));
        
        result[0] = r;
        result[1] = y;
        result[2] = b;
    }
    
    public int mixRGBAsCMYK(int[] rgbs) { 
        float[][] cmyks = new float[rgbs.length][];
        int[] tempRGBi = new int[3];
        float[] tempRGBf = new float[3];
        float[] tempCMYKf = new float[4];
        
        for (int i = 0; i < rgbs.length; ++i) {
            int rgb = rgbs[i];
            splitRGB(rgb, tempRGBi);
            normalizeRGB(tempRGBi, tempRGBf);
            convertRGBToCMYK(tempRGBf, tempCMYKf);
            cmyks[i] = tempCMYKf;
        }
        
        mixCMYK(cmyks, tempCMYKf);
        convertCMYKToRGB(tempCMYKf, tempRGBf);
        denormalizeRGB(tempRGBf, tempRGBi);
        int rgb = unsplitRGB(tempRGBi);
        return rgb;
    }
    
    public int mixRGBAsRYB(int[] rgbs) { 
        float[][] rybs = new float[rgbs.length][];
        int[] tempRGBi = new int[3];
        float[] tempRGBf = new float[3];
        float[] tempRYBf = new float[3];
        
        for (int i = 0; i < rgbs.length; ++i) {
            int rgb = rgbs[i];
            splitRGB(rgb, tempRGBi);
            normalizeRGB(tempRGBi, tempRGBf);
            convertRGBToRYB(tempRGBf, tempRYBf);
            rybs[i] = tempRYBf;
        }
        
        mixRYB(rybs, tempRYBf);
        convertRYBToRGB(tempRYBf, tempRGBf);
        denormalizeRGB(tempRGBf, tempRGBi);
        int rgb = unsplitRGB(tempRGBi);
        return rgb;
    }
    
    public int alphaBlendRGBA(int bg, int fg, float fgAlpha) {
        float bgRed = ((bg>>16) & 0xff) / 255f;
        float bgGreen = ((bg>>8) & 0xff) / 255f;
        float bgBlue = (bg & 0xff) / 255f;
        
        float fgRed = ((fg>>16) & 0xff) / 255f;
        float fgGreen = ((fg>>8) & 0xff) / 255f;
        float fgBlue = (fg & 0xff) / 255f;
        
        float outRed, outGreen, outBlue;
        
        outRed = fgRed*fgAlpha + bgRed*(1f-fgAlpha);
        outGreen = fgGreen*fgAlpha + bgGreen*(1f-fgAlpha);
        outBlue = fgBlue*fgAlpha + bgBlue*(1f-fgAlpha);
        
        int outRedi = (int)(outRed*255f);
        int outGreeni = (int)(outGreen*255f);
        int outBluei = (int)(outBlue*255f);
        
        return ((outRedi&0xff)<<16)|((outGreeni&0xff)<<8)|(outBluei&0xff);
    }
    
//    public static void main(String[] args) {
//        int bgColor = Integer.parseInt(args[0], 16);
//        int fgColor = Integer.parseInt(args[1], 16);
//        float fgAlpha = Float.parseFloat(args[2]);
//
//        int result = ColorMixer.getInstance().alphaBlendRGBA(bgColor, fgColor, fgAlpha);
//        System.out.println(String.format("%06x + %06x@%.1f = %06x", bgColor, fgColor, fgAlpha, result));
//    }
}
