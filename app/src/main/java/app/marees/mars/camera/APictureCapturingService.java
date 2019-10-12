package app.marees.mars.camera;


import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;


import app.marees.mars.Singletons.Myapp;
import app.marees.mars.listeners.PictureCapturingListener;


/**
 * Abstract Picture Taking Service.
 *
 * @author hzitoun (zitoun.hamed@gmail.com)
 */
public abstract class APictureCapturingService {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int ORIENTATION_0 = 0;
    private static final int ORIENTATION_90 = 3;
    private static final int ORIENTATION_270 = 1;


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    final Context context;
    final CameraManager manager;



    public APictureCapturingService(Context context) {

        this.context = Myapp.getAppContext();
        this.manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    /***
     * @return  orientation
     */
    int getOrientation() {
        Display display = ((WindowManager)
                Myapp.getAppContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenOrientation = display.getRotation();
        int value = 0;
        switch (screenOrientation)
        {
            default:
            case ORIENTATION_0: // Portrait
                // do smth.
                value =0;
                break;
            case ORIENTATION_90: // Landscape right
                // do smth.
                value = 3;
                break;
            case ORIENTATION_270: // Landscape left
                // do smth.
               value = 1;
                break;
        }

        return value;
    }


    /**
     * starts pictures capturing process.
     *
     * @param listener picture capturing listener
     */
    public abstract void startCapturing(final PictureCapturingListener listener);
}
