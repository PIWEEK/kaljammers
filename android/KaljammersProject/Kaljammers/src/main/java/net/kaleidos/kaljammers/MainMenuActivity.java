package net.kaleidos.kaljammers;

import android.content.Intent;
import android.graphics.Typeface;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
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
    private Font mDroidFont;
    private Font mDroidFontMax;
    private Font mKingdomOfHeartsFont;
    private Font mNeverwinterNightsFont;
    private Font mPlokFont;
    private Font mUnrealTournamenFont;

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

        FontFactory.setAssetBasePath("font/");


        final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        this.mDroidFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "Droid.ttf", 48, true, android.graphics.Color.WHITE);
        this.mDroidFont.load();

        this.mDroidFontMax = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "Droid.ttf", 58, true, android.graphics.Color.WHITE);
        this.mDroidFontMax.load();


        this.mKingdomOfHeartsFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "KingdomOfHearts.ttf", 48, true, android.graphics.Color.WHITE);
        this.mKingdomOfHeartsFont.load();

        this.mNeverwinterNightsFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "NeverwinterNights.ttf", 48, true, android.graphics.Color.WHITE);
        this.mNeverwinterNightsFont.load();

        this.mPlokFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "Plok.ttf", 48, true, android.graphics.Color.WHITE);
        this.mPlokFont.load();

        this.mUnrealTournamenFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "UnrealTournament.ttf", 48, true, android.graphics.Color.WHITE);
        this.mUnrealTournamenFont.load();




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
        this.mMainScene.setBackgroundEnabled(false);
        this.mMainScene.attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));

        this.mMainScene.setChildScene(this.mMenuScene, false, true, true);

        return this.mMainScene;
    }


    @Override
    public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
        switch(pMenuItem.getID()) {
            case MENU_QUIT:
				/* End Activity. */
                this.finish();
                System.exit(0);
                return true;
            case MENU_ONE:
                StadiumActivity.SelectedGame = 1;
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, PlayerSelectionActivity.class));
                MainMenuActivity.this.finish();
                return true;
            case MENU_TWO:
                StadiumActivity.SelectedGame = 2;
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, PlayerSelectionActivity.class));
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
        Color pUnselectedColor = Color.BLUE;
        Color pQuitColor = Color.RED;

        final IMenuItem onePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_ONE, mDroidFont, "START ONE PLAYER", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(onePMenuItem);

        final IMenuItem twoPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_TWO, mDroidFont, "START TWO PLAYERS", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(twoPMenuItem);


        final IMenuItem quitMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_QUIT, mDroidFont, "QUIT", this.getVertexBufferObjectManager()),pSelectedColor, pQuitColor);

        this.mMenuScene.addMenuItem(quitMenuItem);



        this.mMenuScene.buildAnimations();

        this.mMenuScene.setBackgroundEnabled(false);

        this.mMenuScene.setOnMenuItemClickListener(this);
    }



    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}