package net.kaleidos.kaljammers;

import android.content.Intent;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends SimpleBaseGameActivity{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    // ===========================================================
    // Fields
    // ===========================================================

    protected Camera mCamera;

    protected Scene mMainScene;

    private ITextureRegion mBackgroundTextureRegion;

    private float totalTime = 0;




    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
    }

    @Override
    public void onCreateResources(){

        BitmapTexture backgroundTexture = null;
        try {
            backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getResources().openRawResource(R.drawable.kaljammers);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();

    }

    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());



        this.mMainScene = new Scene();

        this.mMainScene.registerUpdateHandler(new IUpdateHandler() {
            public void reset() {
            }
            public void onUpdate(float pSecondsElapsed) {
                totalTime += pSecondsElapsed;
                if (totalTime > 4){
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainMenuActivity.class));
                    SplashActivity.this.finish();
                }

            }
        });


        /* No background color needed as we have a fullscreen background sprite. */
        this.mMainScene.setBackgroundEnabled(false);
        this.mMainScene.attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));



        return this.mMainScene;
    }


}