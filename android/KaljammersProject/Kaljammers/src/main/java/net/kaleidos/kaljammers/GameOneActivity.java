package net.kaleidos.kaljammers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.GLES20;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GameOneActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;
    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_PLAYER = 1;
    public static final int LAYER_FRISBEE = 2;
    public static final int LAYER_TEXT = 3;
    public static final int LAYER_COUNT = 4;

    public static final int MOVE_NONE = 0;
    public static final int MOVE_UP = 10;
    public static final int MOVE_UP_RIGHT = 11;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_DOWN_RIGHT = 21;
    public static final int MOVE_DOWN = 20;
    public static final int MOVE_DOWN_LEFT = 22;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_UP_LEFT = 12;


    public static final int STATUS_PLAYER1_FRISBEE = 1;
    public static final int STATUS_PLAYER1_LAUNCH = 2;
    public static final int STATUS_PLAYER2_FRISBEE = 3;
    public static final int STATUS_PLAYER2_LAUNCH = 4;


    private boolean buttonPresed = false;



    private ITexture mTexture;
    private ITextureRegion mFrisbeeTextureRegion;

    private TiledTextureRegion mPlayer1TextureRegion;
    private TiledTextureRegion mPlayer2TextureRegion;
    private ITextureRegion mBackgroundTextureRegion;


    private BitmapTextureAtlas mOnScreenControlTexture;
    private ITextureRegion mOnScreenControlBaseTextureRegion;
    private ITextureRegion mOnScreenControlKnobTextureRegion;

    private BitmapTextureAtlas mOnScreenButton1Texture;
    private ITextureRegion mOnScreenButton1TextureRegion;
    private BitmapTextureAtlas mOnScreenButton2Texture;
    private ITextureRegion mOnScreenButton2TextureRegion;

    public static int SelectedStadium = 0;



    public static int SelectedPlayer = 0;
    public static float SelectedPlayerVel = 1;
    public static float SelectedPlayerStrenght = 1;



    float timePlayer2Frisbee = 0;

    Random random = new Random();

    int lastMove = MOVE_NONE;

    Font mFont;

    int direction;
    Scene scene;
    Camera camera;

    byte score1 = 0;
    byte score2 = 0;


    byte status = STATUS_PLAYER1_FRISBEE;

    Player player1;
    Player player2;
    Frisbee frisbee;
    Text debugText;

    Text score1Text;
    Text score2Text;

    GameField gameField = new GameField();
    public static GameEngine gameEngine;


    public GameField getGameField() {
        return this.gameField;
    }
    public Frisbee getFrisbee() {
        return this.frisbee;
    }
    public Player getPlayer1() {
        return this.player1;
    }
    public Player getPlayer2() {
        return this.player2;
    }

    public byte getScore1() {
        return score1;
    }

    public void setScore1(byte score1) {
        this.score1 = score1;
        if (score1<10){
            score1Text.setText("0"+score1);
        } else {
            score1Text.setText(""+score1);
        }
    }

    public byte getScore2() {
        return score2;
    }

    public void setScore2(byte score2) {
        this.score2 = score2;
        if (score2<10){
            score2Text.setText("0"+score2);
        } else {
            score2Text.setText(""+score2);
        }
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);


        gameField.setImageId(this.SelectedStadium);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
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
        this.mFrisbeeTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);


        /*
        BitmapTexture playerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.player01);
            }
        });

        playerTexture.load();
        this.mPlayer1TextureRegion = TextureRegionFactory.extractFromTexture(playerTexture);
        this.mPlayer2TextureRegion = TextureRegionFactory.extractFromTexture(playerTexture);

        */


        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 384, TextureOptions.BILINEAR);
        this.mPlayer1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, this, "gpx/pablo.png", 0, 0, 4, 4);
        textureAtlas.load();

        BitmapTextureAtlas textureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 384, TextureOptions.BILINEAR);
        this.mPlayer2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas2, this, "gpx/primi.png", 0, 0, 4, 4);
        textureAtlas2.load();


        if (this.SelectedStadium == 6){
            textureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 384, TextureOptions.BILINEAR);
            this.mPlayer2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas2, this, "gpx/space.png", 0, 0, 4, 4);
            textureAtlas2.load();
        }



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



        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.WHITE);
        this.mFont.load();

        BitmapTexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(gameField.getImageId());
            }
        });



        this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
        backgroundTexture.load();



        onCreateResourcesCallback.onCreateResourcesFinished();
    }


    public void mainLoop(float pSecondsElapsed){
        status = gameEngine.mainLoop(this, pSecondsElapsed, status, lastMove, buttonPresed);
        buttonPresed = false;
    }

    public void button1() {
        if (this.status == STATUS_PLAYER1_FRISBEE) {
            this.buttonPresed = true;
        }
    }


    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        scene = new Scene();

        scene.registerUpdateHandler(new IUpdateHandler() {
            public void reset() {
            }
            public void onUpdate(float pSecondsElapsed) {
                GameOneActivity.this.mainLoop(pSecondsElapsed);
            }
        });

        for(int i = 0; i < LAYER_COUNT; i++) {
            this.scene.attachChild(new Entity());
        }


        /* No background color needed as we have a fullscreen background sprite. */
        scene.setBackgroundEnabled(false);
        scene.getChildByIndex(LAYER_BACKGROUND).attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));





        final float centerX = (CAMERA_WIDTH - this.mFrisbeeTextureRegion.getWidth()) / 2;
        final float centerY = (CAMERA_HEIGHT - this.mFrisbeeTextureRegion.getHeight()) / 2;

        frisbee = new Frisbee(centerX, centerY, this.mFrisbeeTextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_FRISBEE).attachChild(frisbee);
        frisbee.setVisible(false);

        player1 = new Player(0, centerY, this.mPlayer1TextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_PLAYER).attachChild(player1);
        player1.setPlayer1(true);

         // from 350
        float fvel = SelectedPlayerVel;
        float fstrenght = SelectedPlayerStrenght;
        if ((this.SelectedStadium == 3) || (this.SelectedStadium == 5)){
            fvel = fvel * 0.66f;
        } else if (this.SelectedStadium == 6) {
            fvel = fvel * 1.5f;
        }
        player1.applyFactorVel(fvel);
        player1.applyFactorStrength(fstrenght);


        //Log.e(">>> "+ player1.getVel());
        //Log.e(">>> "+ player1.getStrength());

        player2 = new Player(700, centerY, this.mPlayer2TextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_PLAYER).attachChild(player2);
        player2.setVel(300);
        player2.applyFactorVel(fvel);

        debugText = new Text(0, 100, this.mFont, "                                                                                    ", new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_TEXT).attachChild(debugText);


        score1Text = new Text(325, 430, this.mFont, "00", new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_TEXT).attachChild(score1Text);

        score2Text = new Text(430, 430, this.mFont, "00", new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_TEXT).attachChild(score2Text);

        initOnScreenControls();

        onCreateSceneCallback.onCreateSceneFinished(scene);
    }



    private void initOnScreenControls() {
        final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(20, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight()-10, this.camera,
                this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new DigitalOnScreenControl.IOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {


                GameOneActivity.this.lastMove = MOVE_NONE;


                if(pValueX > 0) { //RIGHT

                    GameOneActivity.this.lastMove += MOVE_RIGHT;
                } else if(pValueX < 0) { //LEFT

                    GameOneActivity.this.lastMove += MOVE_LEFT;
                }

                if(pValueY > 0) { //DOWN

                    GameOneActivity.this.lastMove += MOVE_DOWN;
                } else if(pValueY < 0) { //UP

                    GameOneActivity.this.lastMove += MOVE_UP;
                }
            }

        });
        digitalOnScreenControl.setAllowDiagonal(true);
        digitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        digitalOnScreenControl.getControlBase().setAlpha(0.5f);
        digitalOnScreenControl.refreshControlKnobPosition();

        this.scene.setChildScene(digitalOnScreenControl);




        /* Create the button and add it to the scene. */
        final Sprite button1 = new ButtonSprite(CAMERA_WIDTH - 100, CAMERA_HEIGHT - this.mOnScreenButton1TextureRegion.getHeight()-30,
                this.mOnScreenButton1TextureRegion, this.mOnScreenButton1TextureRegion, this.mOnScreenButton1TextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        GameOneActivity.this.button1();
                    }
                }
        );
        scene.registerTouchArea(button1);
        scene.getChildByIndex(LAYER_TEXT).attachChild(button1);



        /* Create the button and add it to the scene. */
        /*
        final Sprite button2 = new ButtonSprite(CAMERA_WIDTH - 75, CAMERA_HEIGHT - this.mOnScreenButton2TextureRegion.getHeight(),
                this.mOnScreenButton2TextureRegion, this.mOnScreenButton2TextureRegion, this.mOnScreenButton2TextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        GameOneActivity.this.debugText.setText("Button 2");
                    }
                }
        );
        scene.registerTouchArea(button2);
        scene.getChildByIndex(LAYER_TEXT).attachChild(button2);
*/

        scene.setTouchAreaBindingOnActionDownEnabled(true);



    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    private void debug(Object text) {
        GameOneActivity.this.debugText.setText(""+text);
    }

}