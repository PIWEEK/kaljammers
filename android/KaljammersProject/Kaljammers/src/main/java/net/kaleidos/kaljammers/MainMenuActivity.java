package net.kaleidos.kaljammers;

import android.content.Intent;
import android.graphics.Typeface;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;


public class MainMenuActivity extends SimpleBaseGameActivity implements IOnMenuItemClickListener {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    protected static final int MENU_QUIT = 0;
    protected static final int MENU_ONE = 1;
    protected static final int MENU_TWO = 2;

    // ===========================================================
    // Fields
    // ===========================================================

    protected Camera mCamera;

    protected Scene mMainScene;

    private BitmapTextureAtlas mBitmapTextureAtlas;

    protected MenuScene mMenuScene;
    protected Scene mBackgroundScene;

    private Font mFont;

    private ITextureRegion mFaceTextureRegion;
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
    }

    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.createMenuScene();
        this.createBackgroundScene();


        this.mMainScene = new Scene();
        this.mMainScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

        this.mMainScene.setChildScene(this.mMenuScene, false, true, true);

        return this.mMainScene;
    }


    @Override
    public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
        switch(pMenuItem.getID()) {
            case MENU_QUIT:
				/* End Activity. */
                this.finish();
                return true;
            case MENU_ONE:
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, GameOneActivity.class));
                MainMenuActivity.this.finish();
                return true;
            case MENU_TWO:
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, GameActivity.class));
                MainMenuActivity.this.finish();
                return true;
            default:
                return false;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void createMenuScene() {
        this.mMenuScene = new MenuScene(this.mCamera);


        Color pSelectedColor = new Color(0.5f, 0.5f, 0.5f);
        Color pUnselectedColor = new Color(1f, 0f, 0f);


        final IMenuItem onePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_ONE, mFont, "START ONE PLAYER", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(onePMenuItem);

        final IMenuItem twoPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_TWO, mFont, "START TWO PLAYERS", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(twoPMenuItem);


        final IMenuItem quitMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_QUIT, mFont, "QUIT", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(quitMenuItem);



        this.mMenuScene.buildAnimations();

        this.mMenuScene.setBackgroundEnabled(false);

        this.mMenuScene.setOnMenuItemClickListener(this);
    }


    protected void createBackgroundScene() {
        this.mBackgroundScene = new Scene();

/*
        final float centerX = (CAMERA_WIDTH - mBackgroundTextureRegion.getWidth()) / 2;
        final float centerY = (CAMERA_HEIGHT - mBackgroundTextureRegion.getHeight()) / 2;
        SpriteBackground bg = new SpriteBackground(new Sprite(centerX, centerY, mBackgroundTextureRegion, this.getVertexBufferObjectManager()));
        this.mBackgroundScene.setBackground(bg);

        */

        this.mBackgroundScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

        this.mBackgroundScene.setBackgroundEnabled(true);

    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}