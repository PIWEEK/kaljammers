package net.kaleidos.kaljammers;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;


public class GameActivity extends BaseGameActivity {
    private ITexture mTexture;
    private ITextureRegion mFaceTextureRegion;


    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    AnimatedSprite frisbee;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
       try {

            this.mTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {

                    return getResources().openRawResource(R.drawable.frisbee);
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
        final float centerX = (GameActivity.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
        final float centerY = (GameActivity.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

        final Frisbee frisbee = new Frisbee(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());

        scene.attachChild(frisbee);

        onCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }




    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private static class Frisbee extends Sprite {
        private final PhysicsHandler mPhysicsHandler;
        private float velX = 500f;
        private float velY = 500f;

        public Frisbee(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
            super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
            this.mPhysicsHandler = new PhysicsHandler(this);
            this.registerUpdateHandler(this.mPhysicsHandler);
            this.mPhysicsHandler.setVelocity(velX, velY);

        }

        @Override
        protected void onManagedUpdate(final float pSecondsElapsed) {
            if(this.mX < 0) {
                this.mPhysicsHandler.setVelocityX(velX);
            } else if(this.mX + this.getWidth() > GameActivity.CAMERA_WIDTH) {
                this.mPhysicsHandler.setVelocityX(-velX);
            }

            if(this.mY < 0) {
                this.mPhysicsHandler.setVelocityY(velY);
            } else if(this.mY + this.getHeight() > GameActivity.CAMERA_HEIGHT) {
                this.mPhysicsHandler.setVelocityY(-velY);
            }

            super.onManagedUpdate(pSecondsElapsed);
        }
    }


}