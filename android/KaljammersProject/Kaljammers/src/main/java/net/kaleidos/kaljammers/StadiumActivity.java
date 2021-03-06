package net.kaleidos.kaljammers;


import android.content.Intent;
import android.graphics.Typeface;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;

import java.io.IOException;
import java.io.InputStream;

public class StadiumActivity extends SimpleBaseGameActivity implements MenuScene.IOnMenuItemClickListener {
    // ===========================================================
    // Constants
    // ===========================================================

    public static int SelectedGame = 0;

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    protected static final int MENU_QUIT = 0;
    protected static final int MENU_ONE = 1;
    protected static final int MENU_TWO = 2;
    protected static final int MENU_THREE = 3;
    protected static final int MENU_FOUR = 4;
    protected static final int MENU_FIVE = 5;
    protected static final int MENU_SIX = 6;

    // ===========================================================
    // Fields
    // ===========================================================

    protected Camera mCamera;

    protected Scene mMainScene;

    private BitmapTextureAtlas mBitmapTextureAtlas;

    protected MenuScene mMenuScene;
    protected Scene mBackgroundScene;

    private Font mFont, mUnrealTournamenFont, mDroidFont;
    private ITextureRegion mBackgroundTextureRegion;


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
        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 64);
        this.mFont.load();


        BitmapTexture backgroundTexture = null;
        try {
            backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getResources().openRawResource(R.drawable.kaljammers_bg);
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

        this.createMenuScene();



        this.mMainScene = new Scene();
        
         /* No background color needed as we have a fullscreen background sprite. */
        this.mMainScene.setBackgroundEnabled(false);
        this.mMainScene.attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));

        this.mMainScene.setChildScene(this.mMenuScene, false, true, true);

        return this.mMainScene;
    }


    @Override
    public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
        if (pMenuItem.getID() == MENU_QUIT) {
				/* End Activity. */
                this.finish();
                return true;
        }
        if (pMenuItem.getID() == MENU_ONE) {
           GameOneActivity.SelectedStadium = 1;
        }

        if (pMenuItem.getID() == MENU_TWO) {
            GameOneActivity.SelectedStadium = 2;
        }

        if (pMenuItem.getID() == MENU_THREE) {
            GameOneActivity.SelectedStadium = 3;
        }

        if (pMenuItem.getID() == MENU_FOUR) {
            GameOneActivity.SelectedStadium = 4;
        }

        if (pMenuItem.getID() == MENU_FIVE) {
            GameOneActivity.SelectedStadium = 5;
        }

        if (pMenuItem.getID() == MENU_SIX) {
            GameOneActivity.SelectedStadium = 6;
        }


        if (SelectedGame == 1){
            GameOneActivity.gameEngine = new GameEngineOnePlayer();
            StadiumActivity.this.startActivity(new Intent(StadiumActivity.this, GameOneActivity.class));
        }else{
            GameOneActivity.gameEngine = new GameEngineSyncTwoPlayers();
            StadiumActivity.this.startActivity(new Intent(StadiumActivity.this, GameOneActivity.class));
        }

        return true;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void createMenuScene() {
        this.mMenuScene = new MenuScene(this.mCamera);


        Color pSelectedColor = new Color(0.5f, 0.5f, 0.5f);
        Color pUnselectedColor = Color.BLUE;
        Color pQuitColor = Color.RED;

        FontFactory.setAssetBasePath("font/");


        final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);

        this.mUnrealTournamenFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "UnrealTournament.ttf", 38, true, android.graphics.Color.WHITE);
        this.mUnrealTournamenFont.load();

        this.mDroidFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "Droid.ttf", 38, true, android.graphics.Color.WHITE);
        this.mDroidFont.load();

        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 44);
        this.mFont.load();

        final IMenuItem onePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_ONE, mDroidFont, "London", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(onePMenuItem);

        final IMenuItem twoPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_TWO, mDroidFont, "Madrid", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(twoPMenuItem);

        final IMenuItem threePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_THREE, mDroidFont, "París", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(threePMenuItem);


        final IMenuItem fourPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_FOUR, mDroidFont, "New York", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(fourPMenuItem);

        final IMenuItem fivePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_FIVE, mDroidFont, "Hawaii", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(fivePMenuItem);

        final IMenuItem sixPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_SIX, mDroidFont, "Space", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(sixPMenuItem);


        final IMenuItem quitMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_QUIT, mDroidFont, "QUIT", this.getVertexBufferObjectManager()),pSelectedColor, pQuitColor);

        this.mMenuScene.addMenuItem(quitMenuItem);


        this.mMenuScene.buildAnimations();

        this.mMenuScene.setBackgroundEnabled(false);

        this.mMenuScene.setOnMenuItemClickListener(this);
    }
    public void onExit() {
        StadiumActivity.this.finish();
    }



    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
