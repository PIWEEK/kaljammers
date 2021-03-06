package net.kaleidos.kaljammers;


import android.content.Intent;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

public class PlayerSelectionActivity extends SimpleBaseGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    public static int SelectedGame = 0;

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;




    // ===========================================================
    // Fields
    // ===========================================================

    protected Camera mCamera;

    protected Scene mMainScene;


    protected MenuScene mMenuScene;
    protected Scene mBackgroundScene;

    private BitmapTextureAtlas mNinjaTexture;
    private ITextureRegion mNinjaTextureRegion;

    private BitmapTextureAtlas mPrimiTexture;
    private ITextureRegion mPrimiTextureRegion;

    private BitmapTextureAtlas mTonyoTexture;
    private ITextureRegion mTonyoTextureRegion;


    private BitmapTextureAtlas mPabloTexture;
    private ITextureRegion mPabloTextureRegion;




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
    public void onCreateResources() {



        BitmapTextureAtlas textureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mNinjaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas1, this, "gpx/ninja_detail.png", 0, 0);
        textureAtlas1.load();

        BitmapTextureAtlas textureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mPrimiTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas2, this, "gpx/primi_detail.png", 0, 0);
        textureAtlas2.load();

        BitmapTextureAtlas textureAtlas3 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mTonyoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas3, this, "gpx/tonyo_detail.png", 0, 0);
        textureAtlas3.load();

        BitmapTextureAtlas textureAtlas4 = new BitmapTextureAtlas(this.getTextureManager(), 400, 240, TextureOptions.BILINEAR);
        this.mPabloTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas4, this, "gpx/pablo_detail.png", 0, 0);
        textureAtlas4.load();

    }

    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.mMainScene = new Scene();
        this.mMainScene.setBackground(new Background(Color.WHITE));


        /* Create the button and add it to the scene. */
        final Sprite buttonNinja = new ButtonSprite(0, 0, this.mNinjaTextureRegion, this.mNinjaTextureRegion, this.mNinjaTextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(0);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonNinja);
        this.mMainScene.attachChild(buttonNinja);
        
        /* Create the button and add it to the scene. */
        final Sprite buttonPrimi = new ButtonSprite(400, 0, this.mPrimiTextureRegion, this.mPrimiTextureRegion, this.mPrimiTextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(1);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonPrimi);
        this.mMainScene.attachChild(buttonPrimi);
        
        /* Create the button and add it to the scene. */
        final Sprite buttonTonyo = new ButtonSprite(0, 240, this.mTonyoTextureRegion, this.mTonyoTextureRegion, this.mTonyoTextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(2);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonTonyo);
        this.mMainScene.attachChild(buttonTonyo);
        
        /* Create the button and add it to the scene. */
        final Sprite buttonPablo = new ButtonSprite(400, 240, this.mPabloTextureRegion, this.mPabloTextureRegion, this.mPabloTextureRegion, this.getVertexBufferObjectManager(),
                new ButtonSprite.OnClickListener() {

                    @Override
                    public void onClick(ButtonSprite buttonSprite, float v, float v2) {
                        PlayerSelectionActivity.this.selectPlayer(3);
                    }
                }
        );
        this.mMainScene.registerTouchArea(buttonPablo);
        this.mMainScene.attachChild(buttonPablo);


        this.mMainScene.setTouchAreaBindingOnActionDownEnabled(true);





        return this.mMainScene;
    }


    public boolean selectPlayer(int numPlayer) {

        Players player = null;

        if (numPlayer == 0) {
            player = Players.PLAYER1;
        }

        if (numPlayer == 1) {
            player = Players.PLAYER2;
        }

        if (numPlayer == 2) {
            player = Players.PLAYER3;
        }

        if (numPlayer == 3) {
            player = Players.PLAYER4;
        }

        GameOneActivity.SelectedPlayer = player.ordinal() + 1;
        GameOneActivity.SelectedPlayerVel = player.getVel();
        GameOneActivity.SelectedPlayerStrenght = player.getStrenght();
        GameOneActivity.SelectedPlayerSprite = player.getSprite();

        PlayerSelectionActivity.this.startActivity(new Intent(PlayerSelectionActivity.this, StadiumActivity.class));

        PlayerSelectionActivity.this.finish();
        return true;
    }


}

