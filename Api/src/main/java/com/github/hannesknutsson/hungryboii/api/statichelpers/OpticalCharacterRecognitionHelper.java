package com.github.hannesknutsson.hungryboii.api.statichelpers;

import com.github.hannesknutsson.hungryboii.api.exceptions.OCRException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class OpticalCharacterRecognitionHelper {

    private static Tesseract tessInstance;

    public static List<String> parseImageArea(BufferedImage image, Rectangle area) throws OCRException {
        String tessResult;
        try {
            tessResult = getTesseract().doOCR(image, area);
        } catch (TesseractException e) {
            throw new OCRException("Failed to parse image!");
        }
        return Arrays.asList(tessResult.split("\n"));
    }

    private static Tesseract getTesseract() {
        if (tessInstance == null) {
            tessInstance = new Tesseract();
            tessInstance.setLanguage("swe");
            tessInstance.setHocr(false);
        }
        return tessInstance;
    }
}
