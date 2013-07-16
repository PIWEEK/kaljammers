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
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
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
    private ITextureRegion mBackgroundTextureRegion;


    private BitmapTextureAtlas mOnScreenControlTexture;
    private ITextureRegion mOnScreenControlBaseTextureRegion;
    private ITextureRegion mOnScreenControlKnobTextureRegion;

    private BitmapTextureAtlas mOnScreenButton1Texture;
    private ITextureRegion mOnScreenButton1TextureRegion;
    private BitmapTextureAtlas mOnScreenButton2Texture;
    private ITextureRegion mOnScreenButton2TextureRegion;



    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private static final int LAYER_BACKGROUND = 0;
    private static final int LAYER_PLAYER = 1;
    private static final int LAYER_FRISBEE = 2;
    private static final int LAYER_TEXT = 3;
    private static final int LAYER_COUNT = 4;


    private static final int FIELD_LIMIT_UP = 50;


    private static final int MOVE_NONE = 0;
    private static final int MOVE_UP = 10;
    private static final int MOVE_UP_RIGHT = 11;
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_DOWN_RIGHT = 21;
    private static final int MOVE_DOWN = 20;
    private static final int MOVE_DOWN_LEFT = 22;
    private static final int MOVE_LEFT = 2;
    private static final int MOVE_UP_LEFT = 12;



    int lastMove = MOVE_NONE;



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


        this.mOnScreenButton1Texture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
        this.mOnScreenButton2Texture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
        this.mOnScreenButton1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromResource(this.mOnScreenButton1Texture, this, R.drawable.button1, 0, 0);
        this.mOnScreenButton2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromResource(this.mOnScreenButton2Texture, this, R.drawable.button2, 0, 0);

        this.mOnScreenButton1Texture.load();
        this.mOnScreenButton2Texture.load();



        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);

        this.mFont.load();






        BitmapTexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.stadium);
            }
        });



        this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();



        onCreateResourcesCallback.onCreateResourcesFinished();
    }


    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        scene = new Scene();

        for(int i = 0; i < LAYER_COUNT; i++) {
            this.scene.attachChild(new Entity());
        }


        /* No background color needed as we have a fullscreen background sprite. */
        scene.setBackgroundEnabled(false);
        scene.getChildByIndex(LAYER_BACKGROUND).attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));





        final float centerX = (GameActivity.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
        final float centerY = (GameActivity.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

        frisbee = new Frisbee(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_FRISBEE).attachChild(frisbee);

        player = new Player(0, centerY, this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_PLAYER).attachChild(player);

        debugText = new Text(0, 100, this.mFont, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_TEXT).attachChild(debugText);

        initOnScreenControls();

        onCreateSceneCallback.onCreateSceneFinished(scene);
    }



    private void initOnScreenControls() {
        final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.camera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new DigitalOnScreenControl.IOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {


                GameActivity.this.lastMove = MOVE_NONE;

                float x = GameActivity.this.player.getX();
                float y = GameActivity.this.player.getY();
                if(pValueX > 0) { //RIGHT
                    x = x + GameActivity.this.player.getVelX();
                    GameActivity.this.lastMove += MOVE_RIGHT;
                } else if(pValueX < 0) { //LEFT
                    x = x - GameActivity.this.player.getVelX();
                    GameActivity.this.lastMove += MOVE_LEFT;
                }

                if(pValueY > 0) { //DOWN
                    y = y + GameActivity.this.player.getVelY();
                    GameActivity.this.lastMove += MOVE_DOWN;
                } else if(pValueY < 0) { //UP
                    y = y - GameActivity.this.player.getVelY();
                    GameActivity.this.lastMove += MOVE_UP;
                }

                GameActivity.this.debugText.setText("MOVE = "+GameActivity.this.lastMove);

                GameActivity.this.player.setPosition(x, y);
            }

        });
        digitalOnScreenControl.setAllowDiagonal(true);
        digitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        digitalOnScreenControl.getControlBase().setAlpha(0.5f);
        digitalOnScreenControl.refreshControlKnobPosition();

        this.scene.setChildScene(digitalOnScreenControl);




        /* Create the button and add it to the scene. */
        final Sprite button1 = new ButtonSprite(CAMERA_WIDTH - 150, CAMERA_HEIGHT - this.mOnScreenButton1TextureRegion.getHeight(),
                this.mOnScreenButton1TextureRegion, this.mOnScreenButton1TextureRegion, this.mOnScreenButton1TextureRegion, this.getVertexBufferObjectManager(),
                new org.andengine.entity.sprite.ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        GameActivity.this.debugText.setText("Button 1");
                    }
                }
        );
        scene.registerTouchArea(button1);
        scene.getChildByIndex(LAYER_TEXT).attachChild(button1);



        /* Create the button and add it to the scene. */
        final Sprite button2 = new ButtonSprite(CAMERA_WIDTH - 75, CAMERA_HEIGHT - this.mOnScreenButton2TextureRegion.getHeight(),
                this.mOnScreenButton2TextureRegion, this.mOnScreenButton2TextureRegion, this.mOnScreenButton2TextureRegion, this.getVertexBufferObjectManager(),
                new org.andengine.entity.sprite.ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        GameActivity.this.debugText.setText("Button 2");
                    }
                }
        );
        scene.registerTouchArea(button2);
        scene.getChildByIndex(LAYER_TEXT).attachChild(button2);


        scene.setTouchAreaBindingOnActionDownEnabled(true);



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
                this.mX = 0;
                this.mPhysicsHandler.setVelocityX(velX);
            } else if(this.mX + this.getWidth() > GameActivity.CAMERA_WIDTH) {
                this.mX = GameActivity.CAMERA_WIDTH - this.getWidth();
                this.mPhysicsHandler.setVelocityX(-velX);
            }

            if(this.mY < FIELD_LIMIT_UP) {
                this.mY = FIELD_LIMIT_UP;
                this.mPhysicsHandler.setVelocityY(velY);
            } else if(this.mY + this.getHeight() > GameActivity.CAMERA_HEIGHT) {
                this.mY = GameActivity.CAMERA_HEIGHT - this.getHeight();
                this.mPhysicsHandler.setVelocityY(-velY);
            }

            super.onManagedUpdate(pSecondsElapsed);
        }
    }




            Log.v("[PLAYER position]","------------------>  (x,y):("+this.mX+","+this.mY+")");


}