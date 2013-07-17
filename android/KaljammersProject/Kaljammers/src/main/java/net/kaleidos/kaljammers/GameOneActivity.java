package net.kaleidos.kaljammers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.GLES20;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
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
import java.util.Random;


public class GameOneActivity extends BaseGameActivity {
    private ITexture mTexture;
    private ITextureRegion mFaceTextureRegion;

    private ITextureRegion mPlayer1TextureRegion;
    private ITextureRegion mPlayer2TextureRegion;
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





    private static final int MOVE_NONE = 0;
    private static final int MOVE_UP = 10;
    private static final int MOVE_UP_RIGHT = 11;
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_DOWN_RIGHT = 21;
    private static final int MOVE_DOWN = 20;
    private static final int MOVE_DOWN_LEFT = 22;
    private static final int MOVE_LEFT = 2;
    private static final int MOVE_UP_LEFT = 12;


    private static final int STATUS_PLAYER1_FRISBEE = 1;
    private static final int STATUS_PLAYER1_LAUNCH = 2;
    private static final int STATUS_PLAYER2_FRISBEE = 3;
    private static final int STATUS_PLAYER2_LAUNCH = 4;
    private static final int STATUS_PLAYER1_PRE_LAUNCH = 5;


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

    boolean atacking = true;

    GameField gameField = new GameField();

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

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
        this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);


        BitmapTexture playerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getResources().openRawResource(R.drawable.player01);
            }
        });

        playerTexture.load();
        this.mPlayer1TextureRegion = TextureRegionFactory.extractFromTexture(playerTexture);
        this.mPlayer2TextureRegion = TextureRegionFactory.extractFromTexture(playerTexture);




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


        //GOALS
        if (status!=STATUS_PLAYER1_FRISBEE && status!=STATUS_PLAYER2_FRISBEE && status!=STATUS_PLAYER1_PRE_LAUNCH){
            final float centerY = (GameOneActivity.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

            if(frisbee.getX() <= 0) {
                //Player 2 goal
                score2 += 3;
                if (score2<10){
                    score2Text.setText("0"+score2);
                } else {
                    score2Text.setText(""+score2);
                }
                this.status = STATUS_PLAYER1_FRISBEE;
                this.frisbee.setVisible(false);

                player1.setPosition(0, centerY);
                player2.setPosition(700, centerY);

            } else if(frisbee.getX() + frisbee.getWidth() >= CAMERA_WIDTH) {
                //Player 1 goal
                score1 += 3;
                if (score1<10){
                    score1Text.setText("0"+score1);
                } else {
                    score1Text.setText(""+score1);
                }
                this.status = STATUS_PLAYER2_FRISBEE;
                this.frisbee.setVisible(false);


                player1.setPosition(0, centerY);
                player2.setPosition(700, centerY);
            }
        }



        if (this.status == STATUS_PLAYER2_FRISBEE) {
            timePlayer2Frisbee += pSecondsElapsed;
            if (timePlayer2Frisbee>0.6){
                //Launch player 2
                this.status = STATUS_PLAYER2_LAUNCH;

                float[] velY = {player2.getStrength()*1.2f,player2.getStrength()/2,0,-player2.getStrength()/2, player2.getStrength()*1.2f};


                frisbee.setPosition(player2.getX() - 20, player2.getY()+ player2.getHeight()/2 - 16);

                int y = random.nextInt(4);
                frisbee.mPhysicsHandler.setVelocity(-this.player2.getStrength(), velY[y]);


                this.frisbee.setVisible(true);
                timePlayer2Frisbee = 0;
            }

        }


        if (this.status == STATUS_PLAYER1_LAUNCH || this.status == STATUS_PLAYER2_LAUNCH || this.status == STATUS_PLAYER2_FRISBEE) {

            //Move player1
            float x = this.player1.getX();
            float y = this.player1.getY();

            if ((this.lastMove == MOVE_RIGHT)||(this.lastMove == MOVE_UP_RIGHT)||(this.lastMove == MOVE_DOWN_RIGHT)) {
                x += this.player1.getVel() * pSecondsElapsed;


            } else if ((this.lastMove == MOVE_LEFT)||(this.lastMove == MOVE_UP_LEFT)||(this.lastMove == MOVE_DOWN_LEFT)) {
                x -= this.player1.getVel() * pSecondsElapsed;

            }


            if ((this.lastMove == MOVE_UP)||(this.lastMove == MOVE_UP_RIGHT)||(this.lastMove == MOVE_UP_LEFT)) {
                y -= this.player1.getVel() * pSecondsElapsed;

            } else if ((this.lastMove == MOVE_DOWN)||(this.lastMove == MOVE_DOWN_LEFT)||(this.lastMove == MOVE_DOWN_RIGHT)) {
                y += this.player1.getVel() * pSecondsElapsed;

            }

            //Check limits player 1
            if (x+player1.getWidth()>gameField.getLimitMiddle()){
                x = gameField.getLimitMiddle()-player1.getWidth();
            } else if (x<gameField.getLimitLeft()){
                x = gameField.getLimitLeft();
            }

            if (y<gameField.getLimitUp()){
                y = gameField.getLimitUp();
            } else if (y+player1.getHeight()>gameField.getLimitDown()){
                y = gameField.getLimitDown()-player1.getHeight();
            }


            this.player1.setPosition(x, y);
        }

        if (this.status == STATUS_PLAYER1_LAUNCH || this.status == STATUS_PLAYER2_LAUNCH || this.status == STATUS_PLAYER1_FRISBEE) {
            //Move player2
            float x = this.player2.getX();
            if (atacking) {
                x -= this.player2.getVel() * pSecondsElapsed;
            } else {
                x += this.player2.getVel() * pSecondsElapsed;
            }

            //5% chance of change direction
            if (random.nextInt(100)<5){
                atacking = ! atacking;
            }



            float fy = this.frisbee.getY()+this.frisbee.getHeight()/2;
            float p2y = this.player2.getY()+this.player2.getHeight()/2;
            float y = this.player2.getY();


            if (fy<p2y){
                if (p2y-fy>this.player2.getHeight()/2){
                    y -= this.player2.getVel() * pSecondsElapsed;
                }
            }

            if (fy>p2y){
                if (fy-p2y>this.player2.getHeight()/2){
                    y += this.player2.getVel() * pSecondsElapsed;
                }
            }


            //Check limits
            if (x<gameField.getLimitMiddle()){
                x = gameField.getLimitMiddle();
            } else if (x+player2.getWidth()>gameField.getLimitRight()){
                x = gameField.getLimitRight() - player2.getWidth();
            }

            if (y<gameField.getLimitUp()){
                y = gameField.getLimitUp();
            } else if (y+player2.getHeight()>gameField.getLimitDown()){
                y = gameField.getLimitDown()-player2.getHeight();
            }

            this.player2.setPosition(x, y);
        }

        if (this.status == STATUS_PLAYER2_LAUNCH) {
            //Frisbee catch player 1
            if (this.frisbee.collidesWith(this.player1)){
                this.status = STATUS_PLAYER1_FRISBEE;
                this.frisbee.setVisible(false);
            }
        }

        if (this.status == STATUS_PLAYER1_LAUNCH) {
            //Frisbee catch player 2
            if (this.frisbee.collidesWith(this.player2)){
                this.status = STATUS_PLAYER2_FRISBEE;
                this.frisbee.setVisible(false);
            }
        }

        //Launch frisbee
        if (this.status == STATUS_PLAYER1_PRE_LAUNCH) {
            this.status = STATUS_PLAYER1_LAUNCH;
            float velY = 0;

            if (this.lastMove == MOVE_UP_LEFT || this.lastMove == MOVE_UP) {
                velY = -this.player1.getStrength() * 1.2f;
            }

            if (this.lastMove == MOVE_UP_RIGHT) {
                velY = -this.player1.getStrength() / 2;
            }

            if (this.lastMove == MOVE_RIGHT || this.lastMove == MOVE_LEFT) {
                velY = 0;
            }

            if (this.lastMove == MOVE_DOWN_RIGHT) {
                velY = this.player1.getStrength() / 2;
            }

            if (this.lastMove == MOVE_DOWN || this.lastMove == MOVE_DOWN_LEFT) {
                velY = this.player1.getStrength() * 1.2f;
            }


            frisbee.setPosition(player1.getX()+ player1.getWidth()+5, player1.getY()+ player1.getHeight()/2 - 16);
            frisbee.mPhysicsHandler.setVelocity(this.player1.getStrength(), velY);


            this.frisbee.setVisible(true);
        }



    }

    public void button1() {
        if (this.status == STATUS_PLAYER1_FRISBEE) {
            this.status = STATUS_PLAYER1_PRE_LAUNCH;
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





        final float centerX = (GameOneActivity.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
        final float centerY = (GameOneActivity.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

        frisbee = new Frisbee(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager(), gameField);
        scene.getChildByIndex(LAYER_FRISBEE).attachChild(frisbee);
        frisbee.setVisible(false);

        player1 = new Player(0, centerY, this.mPlayer1TextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_PLAYER).attachChild(player1);
        player1.setPlayer1(true);
        player1.setVel(300);

        player2 = new Player(700, centerY, this.mPlayer1TextureRegion, this.getVertexBufferObjectManager());
        scene.getChildByIndex(LAYER_PLAYER).attachChild(player2);
        player2.setVel(150);

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




    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private static class Frisbee extends Sprite {
        private final PhysicsHandler mPhysicsHandler;
        GameField gameField;

        public Frisbee(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, GameField gameField) {
            super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
            this.mPhysicsHandler = new PhysicsHandler(this);
            this.registerUpdateHandler(this.mPhysicsHandler);
            this.gameField = gameField;
        }

        @Override
        protected void onManagedUpdate(final float pSecondsElapsed) {
            if(this.mX < gameField.getLimitLeft()) {
                this.mX = gameField.getLimitLeft();
                this.mPhysicsHandler.setVelocity(0,0);

            } else if(this.mX + this.getWidth() > gameField.getLimitRight()) {
                this.mX = gameField.getLimitRight() - this.getWidth();
                this.mPhysicsHandler.setVelocity(0, 0);
            }

            if(this.mY < gameField.getLimitUp()) {
                this.mY = gameField.getLimitUp();
                this.mPhysicsHandler.setVelocityY(-this.mPhysicsHandler.getVelocityY());
            } else if(this.mY + this.getHeight() > gameField.getLimitDown()) {
                this.mY = gameField.getLimitDown() - this.getHeight();
                this.mPhysicsHandler.setVelocityY(-this.mPhysicsHandler.getVelocityY());
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }






}