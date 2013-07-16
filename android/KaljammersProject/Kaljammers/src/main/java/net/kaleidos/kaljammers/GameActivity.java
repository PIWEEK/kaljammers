package net.kaleidos.kaljammers;

import android.graphics.Typeface;
import android.opengl.GLES20;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import java.io.IOException;
import java.io.InputStream;
import android.util.Log;

public class GameActivity extends BaseGameActivity {
    private ITexture mTexture;
    private ITextureRegion mFaceTextureRegion;

    private ITextureRegion mPlayerTextureRegion;

    private DigitalOnScreenControl mDigitalOnScreenControl;
    private BitmapTextureAtlas mOnScreenControlTexture;
    private ITextureRegion mOnScreenControlBaseTextureRegion;
    private ITextureRegion mOnScreenControlKnobTextureRegion;



    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;


    Font mFont;

    int direction;
    Scene scene;
    Camera camera;


    Player player;
    Frisbee frisbee;
    Text debugText;

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        Log.v("[NEW PLAY]","********************************************************************************************************** ->  start");
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
        this.mTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.frisbee);
            }
        });

        this.mTexture.load();
        this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);


        BitmapTexture playerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.player01);
            }
        });

        playerTexture.load();
        this.mPlayerTextureRegion = TextureRegionFactory.extractFromTexture(playerTexture);




        this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
        this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromResource(this.mOnScreenControlTexture, this, R.drawable.onscreen_control_base, 0, 0);
        this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromResource(this.mOnScreenControlTexture, this, R.drawable.onscreen_control_knob, 128, 0);
        this.mOnScreenControlTexture.load();

        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);

        this.mFont.load();



        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        scene = new Scene();
        scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
        final float centerX = (GameActivity.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
        final float centerY = (GameActivity.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

        frisbee = new Frisbee(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(frisbee);

        player = new Player(0, centerY, this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(player);

        debugText = new Text(0, 0, this.mFont, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
        scene.attachChild(debugText);


        /* The On-Screen Controls to control the direction of the snake. */
        this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.camera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new BaseOnScreenControl.IOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {

                GameActivity.this.debugText.setText("X="+pValueX+"   Y="+pValueY);

                float x = GameActivity.this.player.getX();
                float y = GameActivity.this.player.getY();
                if(pValueX > 0) { //RIGHT
                    x = x + GameActivity.this.player.getVelX();
                } else if(pValueX < 0) { //LEFT
                    x = x - GameActivity.this.player.getVelX();
                }

                if(pValueY > 0) { //DOWN
                    y = y + GameActivity.this.player.getVelY();
                } else if(pValueY < 0) {
                    y = y - GameActivity.this.player.getVelY();
                }
                GameActivity.this.player.setPosition(x,y);
            }
        });

        //Allow diagonals
        this.mDigitalOnScreenControl.setAllowDiagonal(true);

		/* Make the controls semi-transparent. */
        this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);

        scene.setChildScene(this.mDigitalOnScreenControl);

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
            Log.v("[FRISBEE position]","------------------>  (x,y):("+this.mX+","+this.mY+")");
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



    private static class Player extends Sprite {
        private final PhysicsHandler mPhysicsHandler;

        private float getVelX() {
            return velX;
        }

        private float getVelY() {
            return velY;
        }

        private float velX = 20f;
        private float velY = 20f;

        public Player(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
            super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
            this.mPhysicsHandler = new PhysicsHandler(this);
            this.registerUpdateHandler(this.mPhysicsHandler);
            this.mPhysicsHandler.setVelocity(0, 0);

        }

        @Override
        protected void onManagedUpdate(final float pSecondsElapsed) {
            Log.v("[PLAYER position]","------------------>  (x,y):("+this.mX+","+this.mY+")");
            if(this.mX < 0) {
                this.mX = 0;
            } else if(this.mX + this.getWidth() > GameActivity.CAMERA_WIDTH) {
                this.mX = GameActivity.CAMERA_WIDTH - this.getWidth();
            }

            if(this.mY < 0) {
                this.mY = 0;
            } else if(this.mY + this.getHeight() > GameActivity.CAMERA_HEIGHT) {
                this.mY = GameActivity.CAMERA_HEIGHT - this.getHeight();
            }

            super.onManagedUpdate(pSecondsElapsed);
        }
    }


}