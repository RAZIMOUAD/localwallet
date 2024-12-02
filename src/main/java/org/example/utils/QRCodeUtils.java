package org.example.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class QRCodeUtils {

    /**
     * Génère un QR Code sous forme d'image JavaFX à partir d'un texte.
     *
     * @param text Le texte à encoder dans le QR Code.
     * @param width Largeur du QR Code.
     * @param height Hauteur du QR Code.
     * @return Une image JavaFX représentant le QR Code.
     */
    public static Image generateQRCode(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            return createImageFromBitMatrix(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException("Failed to generate QR code.", e);
        }
    }

    /**
     * Convertit un BitMatrix en une image JavaFX.
     *
     * @param bitMatrix La matrice binaire générée pour le QR Code.
     * @return Une image JavaFX.
     */
    private static Image createImageFromBitMatrix(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        WritableImage image = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                image.getPixelWriter().setColor(x, y, color);
            }
        }
        return image;
    }
}
