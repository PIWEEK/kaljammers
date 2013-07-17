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

        if (SelectedGame == 1){
            StadiumActivity.this.startActivity(new Intent(StadiumActivity.this, GameOneActivity.class));
        }else{
            StadiumActivity.this.startActivity(new Intent(StadiumActivity.this, GameActivity.class));
        }
        StadiumActivity.this.finish();
        return true;
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
                        new TextMenuItem(MENU_ONE, mFont, "STADIUM 1", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(onePMenuItem);

        final IMenuItem twoPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_TWO, mFont, "STADIUM 2", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(twoPMenuItem);

        final IMenuItem threePMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_THREE, mFont, "STADIUM 3", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(threePMenuItem);


        final IMenuItem fourPMenuItem =
                new ColorMenuItemDecorator(
                        new TextMenuItem(MENU_FOUR, mFont, "STADIUM 4", this.getVertexBufferObjectManager()),pSelectedColor, pUnselectedColor);

        this.mMenuScene.addMenuItem(fourPMenuItem);



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

        this.mBackgroundScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

        this.mBackgroundScene.setBackgroundEnabled(true);

    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
