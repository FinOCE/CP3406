package au.edu.jcu.cp3406.guesstheceleb.game;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class CelebrityManager {
    private final String assetPath;
    private String[] imageNames;
    private final AssetManager assetManager;

    public CelebrityManager(AssetManager assetManager, String assetPath) {
        this.assetPath = assetPath;
        this.assetManager = assetManager;

        try {
            imageNames = assetManager.list(assetPath);
        } catch (IOException e) {
            imageNames = null;
        }
    }

    public int count() {
        return imageNames.length;
    }

    public String getName(int i) {
        return imageNames[i]
                .replaceAll("\\.jpg", "")
                .replaceAll("-", " ")
                .trim();
    }

    Bitmap getBitmap(int i) {
        try {
            String path = assetPath + "/" + imageNames[i];
            InputStream stream = assetManager.open(path);

            Bitmap result = BitmapFactory.decodeStream(stream);
            stream.close();

            return result;
        } catch (IOException e) {
            System.out.println("error: " + e);

            return null;
        }
    }
}
