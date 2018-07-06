package com.ulling.oneday.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ulling.lib.core.util.QcLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by KILHO on 2018-06-11.
 * <p>
 * 갤러리에서 선택한 이미지를 onActivityResult에서 비트맵으로 가져오면
 * 지정된 경로에 파일로 저장한다
 */
public class SaveBitmapToImageAsyncTask extends AsyncTask<Void, Integer, Boolean> {
    private SaveBitmapListener listener;
    private Bitmap bitmap;
    private File copyFile = null;

    public interface SaveBitmapListener {
        void onComplete(Boolean result);
    }

    public SaveBitmapToImageAsyncTask(Bitmap bitmap, File copyFile, SaveBitmapListener listener) {
        this.bitmap = bitmap;
        this.copyFile = copyFile;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (copyFile != null) {
                FileOutputStream out = new FileOutputStream(copyFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                return true;
            }

        } catch (FileNotFoundException exception) {
            QcLog.e("FileNotFoundException  === " + exception.getMessage());
        } catch (IOException exception) {
            QcLog.e("IOException  === " + exception.getMessage());
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (listener != null) {
            listener.onComplete(aBoolean);
        }
    }
}
