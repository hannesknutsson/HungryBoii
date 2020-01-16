package com.github.hannesknutsson.hungryboii.utilities.statichelpers;

import com.github.hannesknutsson.hungryboii.exceptions.OCRException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class OpticalCharacterRecognitionHelper {

    public static List<String> parseImageArea(BufferedImage image, Rectangle area) throws OCRException {
        String tessResult = "";
        try {
            MyTess temporaryTessObject = getFreshTess();
            tessResult = temporaryTessObject.doOCR(image, area);
            temporaryTessObject.releaseResources();
        } catch (TesseractException e) {
            throw new OCRException("Failed to parse image!");
        }
        return Arrays.asList(tessResult.split("\n"));
    }

    private static MyTess getFreshTess() {
        MyTess newTessObject = new MyTess();
        newTessObject.setDatapath("./tess");
        newTessObject.setLanguage("swe");
        newTessObject.setHocr(false);
        return newTessObject;
    }

    public static class MyTess extends Tesseract {

        public void releaseResources() {
            dispose();
        }
    }
}
