package au.edu.jcu.cp3406.guesstheceleb;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

class ImageManager {
    private final String assetPath;
    private String[] imageNames;
    private final AssetManager assetManager;

    public ImageManager(AssetManager assetManager, String assetPath) {
        this.assetPath = assetPath;
        this.assetManager = assetManager;

        try {
            imageNames = assetManager.list(assetPath);
        } catch (IOException e) {
            imageNames = null;
        }
    }

    public Bitmap get(int index) {
        try {
            String path = assetPath + "/" + imageNames[index];
            InputStream stream = assetManager.open(path);
            Bitmap result = BitmapFactory.decodeStream(stream);

            stream.close();
            return result;
        } catch (IOException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}
