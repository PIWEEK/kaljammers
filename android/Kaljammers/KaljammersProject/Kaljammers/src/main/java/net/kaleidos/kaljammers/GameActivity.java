package net.kaleidos.kaljammers;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;


public class GameActivity extends BaseGameActivity {
    private ITexture mTexture;
    private ITextureRegion mFaceTextureRegion;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, 800, 480);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
       try {

            this.mTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {

                    return getResources().openRawResource(R.drawable.ic_launcher);
                }
            });

            this.mTexture.load();
            this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();
        scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
        final Sprite face = new Sprite(200, 200, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(face);

        onCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }
}