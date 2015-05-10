import javax.microedition.lcdui.*;
import java.util.*;
import com.j_phone.system.DeviceControl;
import com.j_phone.util.*;
import javax.microedition.rms.*;
//ver1.2.0
import com.jblend.media.smaf.phrase.*;
import javax.microedition.io.*;
import java.io.*;

/** DrawCanvas.java */
public final class DrawCanvas extends Canvas implements Runnable, CommandListener{

/** 定数 */
final static int        KEY_NO           =0x00000000,
                        KEY_0            =0x00000001,
                        KEY_1            =0x00000002,
                        KEY_2            =0x00000004,
                        KEY_3            =0x00000008,
                        KEY_4            =0x00000010,
                        KEY_5            =0x00000020,
                        KEY_6            =0x00000040,
                        KEY_7            =0x00000080,
                        KEY_8            =0x00000100,
                        KEY_9            =0x00000200,
                        KEY_ASTERISK     =0x00000400,
                        KEY_POUND        =0x00000800,
                        KEY_UP           =0x00001000,
                        KEY_LEFT         =0x00002000,
                        KEY_RIGHT        =0x00004000,
                        KEY_DOWN         =0x00008000,
                        KEY_SELECT       =0x00010000,

                        SCREEN_WIDTH     =240, //スクリーンの幅
                        SCREEN_HEIGHT    =240, //スクリーンの高さ
                        FOODER_HEIGHT    =32,  //フッダの高さ
                        PLAYING_HEIGHT   =208,
                        NUM_WIDTH        =14,  //数字の幅
                        NUM_HEIGHT       =11,  //数字の高さ
                        CAL_WIDTH        =8,   //数字の幅
                        CAL_HEIGHT       =10,  //数字の高さ
                        F_WIDTH          =9,   //数字の幅
                        F_HEIGHT         =7,   //数字の高さ

                        NOW_LOADING      =0,
                        TITLE            =1,
                        MODE_SELECT      =2,
                        GAME_START       =4,
                        GAME_PLAYING     =5,
                        GAME_END         =6,
                        GAME_RESULT      =7,
                        RECORD           =8,
                        HELP             =9,
                        BONUS            =10,
                        ENDING           =11,

                        SLEEP_TIME       =70, //スリープタイム
                        MAX_SCORE        =9999999,
                        MAX_COMBO        =99,

                        COMBO_TIME       =4,
                        TIME_LIMIT       =112,//20,
                        CURSOR_TIME      =2,

                        HISCORE_SIZE     =5,
                        BONUS_SIZE       =12,
                        STAGE_SIZE       =3,
                        RS_SIZE          =256, //レコードストア
                        MODE_SIZE        =3,

                        STOP             =0,
                        WALK             =1,
                        JUMP             =2,
                        WALK_SPEED       =8,
                        JUMP_HEIGHT      =22,
                        MYCHARA_WIDTH    =24,
                        MYCHARA_HEIGHT   =24,

                        GROUND_TOP       =93,
                        GROUND_MIDDLE    =141,
                        GROUND_BOTTOM    =189,
                        GROUND_WIDTH     =96,
                        GROUND_HEIGHT    =48,

                        APPLE_SIZE       =6, //出てくるフルーツの大きさ
                        NONE             =-1,
                        APPLE            =0,
                        ORANGE           =1,
                        LEMON            =2,
                        SPECIAL          =3,

                        BONUS_SCORE      =2000,
                        BONUS_TIME       =8,
                        BONUS_APPEAR     =16,

                        SLOT_TIME        =20,
                        SLOT_BONUS       =0,
                        TIME_BONUS       =1,
                        GET_ITEM         =2,

                        IMAGE_MYCHARA    =0, //ImageIndex
                        IMAGE_BG         =1,
                        IMAGE_GROUND3    =2,
                        IMAGE_GROUND2    =3,
                        IMAGE_GROUND1    =4,
                        IMAGE_FOODER     =5,
                        IMAGE_APPLE      =6,
                        IMAGE_BONUS      =7,
                        IMAGE_LANG       =8,
                        IMAGE_NUM        =9,
                        IMAGE_LANG_FD    =10,
                        IMAGE_JUMP       =11,
                        IMAGE_SHINE      =12,
                        IMAGE_PRESENTS   =13,
                        IMAGE_STAGE      =14,
                        IMAGE_GAME       =15,
                        IMAGE_SLOT       =16,
                        IMAGE_BONUS2     =17,
                        IMAGE_PRESS      =18,
                        IMAGE_NUMB       =19,
                        IMAGE_NEKO       =20,
                        IMAGE_SIZE       =21, //Imageサイズ

                        IMAGE_STOP       =0,
                        IMAGE_WALK1      =2,
                        IMAGE_WALK2      =1,

                        L_TITLE1         =0, //文字
                        L_TITLE2         =1,
                        L_START          =2,
                        L_PAUSE          =3,
                        L_TIME_UP        =4,
                        L_RECORD         =5,
                        L_ITEM           =6,
                        L_HELP           =7,
                        L_STAGE          =8,
                        N_SLASH          =10,
                        N_PLUS           =11,
                        N_LEFT           =12,
                        N_RIGHT          =13,
                        F_SCORE          =0,
                        F_TIME           =1,
                        F_COMBO          =2,
                        F_SLOT           =3,

                        FINAL_END        =-1; //宣言終了

final static String     RS_GAMEDATA      ="A";

final static int[]      GROUND_TOP1X     ={ 241,  12, 208},
                        GROUND_TOP2X     ={ 241, 241, -64},
                        GROUND_MIDDLE1X  ={ 241,  48, -32},
                        GROUND_MIDDLE2X  ={ 241, 241, 176};

final static String[]   MODE_HELP={
                            "『ハイスコアを表示します』",
                            "『取得したアイテムを表示します』",
                            "『ヘルプを表示します』"
                        };

final static String[]   MENU_TEXT={
                            "ゲーム再開",
                            "タイトルに戻る"
                        };

final static String[][] HELP_TEXT={{
                            "◆はじめに",
                            "Jumping Fruitsはひよこを操作して",
                            "フルーツをキャッチするゲームです。",
                            "",
                            "フルーツをキャッチするとスコアが",
                            "加算されます。制限時間が０になると",
                            "ゲームオーバー。全３ステージです。"
                        },{
                            "◆操作方法",
                            "←→: 移動",
                            "決定: ジャンプ",
                            " ↓ : 降りる",
                            "",
                            "方向キーは２,４,６,８キー,",
                            "決定キーは５キーで代用できます。"
                        },{
                            "◆連続ジャンプ",
                            "フルーツをキャッチするとさらに",
                            "ひよこがジャンプします",
                            "連続ジャンプの回数によって加算され",
                            "る得点が増えるので、連続ジャンプで",
                            "高得点を狙ってください！",
                            ""
                        },{
                            "◆ボーナス１『SLOT BONUS』",
                            "取得したフルーツは左下のスロットに",
                            "格納されます。",
                            "左下のスロットが３つそろうと",
                            "『SLOT BONUS』となり、さらに高得点",
                            "が加算されます。",
                            ""
                        },{
                            "◆ボーナス２『TIME BONUS』",
                            "左下のスロットが３つ違う場合は、",
                            "『TIME BONUS』となります。",
                            "制限時間が少し延びるのでスロットを",
                            "上手に活用してください。",
                            "",
                            ""
                        },{
                            "◆ボーナス３『GET ITEM』",
                            "ある条件を満たすと変なものが落ちて",
                            "きます。見事にキャッチすると一万点",
                            "加算されます。",
                            "取得したアイテムはITEM LISTから",
                            "閲覧できます。",
                            "目指せ全アイテムコンプリート！"
                        },{
                            "◆さいごに",
                            "『Jumping Fruits』をDLして頂いて",
                            "ありがとうございました。",
                            "ご意見・要望はWebサイトへどうぞ。",
                            "",
                            "Copyright by bluesystem■",
                            "          All right reserved 2006."
                        }};
final static String[][]    ITEM_TEXT={{
                            "甘そうなバナナ。フィリピン産。",
                            "昔は貴重品だったそうだ。"
                        },{
                            "舌がしびれるパイナップル。",
                            "南国出身で風格があります。さすが。"
                        },{
                            "高級品の代表、メロン。",
                            "たまにハムがのっているがそれは嫌。"
                        },{
                            "さくらんぼ。中身がいっぱい詰まった",
                            "甘く甘いやつです。もう一回！"
                        },{
                            "ラ・フランス。たまにガムで食べる",
                            "程度なんでよく知りませんが…。"
                        },{
                            "すごく甘いみかん。ふつうのみかん",
                            "と微妙に色が違います。"
                        },{
                            "『星のかけら』というスナック菓子。",
                            "ひよこ界の今年一番のヒット商品。"
                        },{
                            "『ガンデム』のプラモのつのだ…。",
                            "この形はきっと武者ガンデムだ…。"
                        },{
                            "『○イパーダッシュモーター』だ！",
                            "コースアウトに気をつけよう。"
                        },{
                            "ちまたで人気の『ゲームボウィ』。",
                            "あなたは60秒で何点とれ…(以下略)"
                        },{
                            "伝説の『八星球』だ…。",
                            "何個集めても願いは叶いそうにない。"
                        },{
                            "『作者からの手紙』",
                            "決定キーを押して下さい"
                        }};
final static String[]    ITEM_NO={
                            "そのアイテムはまだ",
                            "              手に入れていません。"
                        };
final static String[]    END_TEXT={
                            "◆Thank You For Playing!!◆",
                            "全アイテムコンプリート",
                            "本当におめでとうございます!!",
                            "一応このゲームはここで終わりです。",
                            "お疲れ様です。あんたはすごい！",
                            "次回作にご期待ください(*'-')ノ"
                        };

/** メインオブジェクト */
private JumpingFruits    jumpingFruits;

/** イメージ */
private Image[]          img = new Image[IMAGE_SIZE];

private Image            logoImg;

/** ダブルバッファリング */
private Image             mainImg; //オフイメージ
private Graphics          g;       //オフグラフィクス

/** 背景画像 */
private Image             imgBg=Image.createImage(SCREEN_WIDTH,SCREEN_HEIGHT-FOODER_HEIGHT); //背景画像
private Image             imgFd=Image.createImage(SCREEN_WIDTH,FOODER_HEIGHT);    

/**グラフィックユーティリティ*/
private GraphicsUtil      gUtil;

/** デバイスコントロール */
private DeviceControl     devCtl = DeviceControl.getDefaultDeviceControl();

/** ストリングバッファ */
private StringBuffer      Sb                =new StringBuffer();
private String            drawSt            =null;

/** キーイベント */
private int               m_event           =KEY_NO;
/** キー状態 */
private int               key               =KEY_NO;

/** フォントを保持する変数 */
private Font              f                =Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
private int               stHeight         =f.getHeight();

/** 乱数 */
private Random            r                =new Random();

/** コマンド変数 */
private Command         exitCmd     = new Command("終了", Command.SCREEN,2);
private Command         pauseCmd    = new Command("中断", Command.SCREEN,3);
private Command         resumeCmd   = new Command("再開", Command.SCREEN,4);
private Command         backCmd     = new Command("戻る", Command.SCREEN,5);
private Command         onSoundCmd  = new Command("♪○",  Command.SCREEN,1);
private Command         offSoundCmd = new Command("♪×",  Command.SCREEN,1);

/** システム変数 */
private long            m_sleepTime        =0;    //スリープタイム
private boolean            m_isPause        =false;
private boolean            m_isDrawFooder    =false;
private boolean            m_isDrawBg        =false;
private int                m_hcenter        =getWidth()/2;
private int                m_vcenter        =getHeight()/2;
private int                m_load            =0;
private int                m_cursor        =0;
private int                m_cursorTime    =0;

/** ゲーム変数 */
private int                m_modeState        =NOW_LOADING;
private int                m_nextState        =NOW_LOADING;
private int                m_time            =0;
private int                m_lv            =0;
private int                m_score            =0;
private int                m_combo            =0;
private int                m_comboTime        =0;
private int                m_comboMax        =0;
private int                m_stage            =0;
private int                m_option        =0;
private int                m_teropX        =0;
private int                m_slot[]        ={NONE,NONE,NONE};
private int                m_slotTime        =0;
private int                m_slotState        =0;
private boolean            m_isAppearBonus    =false;
private int                m_bonusIndex    =0;
private boolean            m_isBonusGet    =false;
private int                m_hiScore[][]    ={    {    0,    0,    0,    0,    0},
                                            {    0,    0,    0,    0,    0},
                                            {    0,    0,    0,    0,    0}};
private int                m_hiCombo[][]    ={    {    0,    0,    0,    0,    0},
                                            {    0,    0,    0,    0,    0},
                                            {    0,    0,    0,    0,    0}};

private int                m_bonusGet[]    =    {    0,    0,    0,    0,    0,    0,
                                                0,    0,    0,    0,    0,    0};
private int                m_bonusNew[]    =    {    0,    0,    0,    0,    0,    0,
                                                0,    0,    0,    0,    0,    0};
private int                m_playTime        =0;


/** キャラクタ変数 */
private int             m_myX            =120;
private int             m_myY            =GROUND_BOTTOM;
private boolean            m_isLeft        =false;
private int                m_myState        =STOP;
private int             m_addY            =0;
private int                m_walkCount        =0;
private int                m_imgIndex        =IMAGE_STOP;
private int                m_speed            =0;
private int                m_ground        =GROUND_BOTTOM;
private int                m_hiScoreNum    =-1;

/** フルーツ変数 */
private int[]            m_appleState    ={NONE,NONE,NONE,NONE,NONE,NONE};
private int[]            m_appleX        ={0,0,0,0,0,0};
private int[]            m_appleY        ={0,0,0,0,0,0};
private int[]            m_appleSpeedX    ={4,4,4,4,4,4};
private int[]            m_appleSpeedY    ={4,4,4,4,4,4};
private boolean[]        m_appleIsLeft    ={false,false,false,false,false,false};

//ver1.1.0
private int                m_effX[]        ={0,0,0};
private int                m_effY[]        ={0,0,0};
private int                m_effTime[]     ={0,0,0};

//ver1.2.0 sound
/** フレーズプレイヤー */
static PhrasePlayer        phrasePlayer = null;
//トラック
static PhraseTrack        tr1 = null;
static PhraseTrack        tr2 = null;
static PhraseTrack        tr3 = null;
static PhraseTrack        tr4 = null;

//ver1.5.0
private int                m_isMute        =0;
private int                m_isShare        =0;

////////////////////////////////////////////////////////////////
/**
    名前:    DrawCanvas(JumpingFruits jumpingFruits)
    概要:    コンストラクタ
    機能:    -
    戻り値:    -
    引数:    -
*/
DrawCanvas(JumpingFruits jumpingFruits) {
    //メインオブジェクト作成
    this.jumpingFruits = jumpingFruits;

    //コマンドリスナー登録
    setCommandListener(this);

    //ダブルバッファリング
    mainImg=Image.createImage(SCREEN_WIDTH,SCREEN_HEIGHT);
    g=mainImg.getGraphics();

    g.setFont(f);

    //スレッド開始
    new Thread(this).start();
}


////////////////////////////////////////////////////////////////
/**
    名前:    resumeExe()
    概要:    一時停止時の処理
    機能:    -
    戻り値:    -
    引数:    -
*/
public void resumeExe() {
    m_isDrawBg=true;
    if( m_modeState==GAME_PLAYING && !m_isPause ){
        onMenu();
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    run()
    概要:    スレッド開始時に呼ばれるメソッド
    機能:    -
    戻り値:    -
    引数:    -
*/
public void run() {

    int i=0;

//--    スタートアップ    --------------------------------------//

    //ロゴ取得
    try {
        logoImg = Image.createImage("/logo.png");
    }catch(Exception e) {}

    //背景描画をセット
    m_isDrawBg=true;

//--    Now Loading        --------------------------------------//

    //スリープ初期化
    m_sleepTime = System.currentTimeMillis();

    drawNowloading();
    repaint();
    //serviceRepaints();

    //画像取得
    for(i=0;i<IMAGE_SIZE;i++){
        try{
            img[i] = Image.createImage("/"+i+".png");
        }catch(Exception e){
            System.out.println(e.toString());
        }
        m_load=i*100/IMAGE_SIZE;
        if( System.currentTimeMillis()-m_sleepTime>=SLEEP_TIME ){
            drawNowloading();
            repaint();
            m_sleepTime = System.currentTimeMillis();
        }
    }

    try{
        phrasePlayer=PhrasePlayer.getPlayer();
        //トラック
        tr1 = phrasePlayer.getTrack(0);
        tr2 = phrasePlayer.getTrack(1);
        tr3 = phrasePlayer.getTrack(2);
        tr4 = phrasePlayer.getTrack(3);
    }catch(Exception e){
        System.out.println(e.toString());
    }
    //サウンド
    initSound();

    m_load=100;
    //最後に描画
    drawNowloading();
    repaint();

    try{
        Thread.sleep(100);
    }catch(Exception e){
        System.out.println(e.toString());
    }
    loadRecordStore();

    //タイトル画面に遷移
    m_nextState=TITLE;

    //左側のソフトキーは使用しない
    //addCommand(noneCmd);
    //ver1.2.0 サウンドコマンド
    if( m_isMute==0 ){
        addCommand(onSoundCmd);
    }else{
        addCommand(offSoundCmd);
    }

//--    メインループ    --------------------------------------//

    try{
        while(true){
//--    初期処理        --------------------------------------//

            if( m_modeState!=m_nextState ){

                //状態を抜ける
                switch( m_modeState ){
                    case TITLE:
                        removeCommand(exitCmd);
                        m_cursor=0;
                        break;

                    case MODE_SELECT:
                        removeCommand(backCmd);
                        m_cursor=0;
                        break;
                    case RECORD:
                    case HELP:
                    case BONUS:
                        removeCommand(backCmd);
                        if( m_modeState==BONUS && m_nextState==ENDING ){
                        }else{
                            m_cursor=1;
                        }
                        break;
                    case ENDING:
                        removeCommand(backCmd);
                        break;
                    case GAME_PLAYING:
                        if( m_isPause ){
                            offMenu();
                        }
                        removeCommand(pauseCmd);
                        break;

                    //ver1.2.0
                    case GAME_RESULT:
                        //playCursor();
                        break;
                }

                //状態に入る
                switch( m_nextState ){
                    case TITLE:
                        //コマンド追加
                        addCommand(exitCmd);
                        initGame();
                        initTitle();
                        m_stage=0;//ステージ初期化
                        //ver1.2.0
                        stopBgm();
                        setTitleSound();
                        playCursor();
                        break;

                    case MODE_SELECT:
                    case RECORD:
                    case HELP:
                    case BONUS:
                    case ENDING:
                        m_teropX=0;
                        addCommand(backCmd);
                        if( m_nextState==RECORD ){
                            setStageImage(m_cursor);
                        }
                        m_cursorTime=0;
                        //ver1.5.0
                        if( m_nextState==MODE_SELECT && m_modeState==TITLE ){
                            playEnter();
                        }else{
                            playCursor();
                        }
                        break;

                    case GAME_START:
                        setStageImage(m_stage);
                        setFooderImage();
                        initGame();//ゲーム初期化
                        getBonusIndex();//ボーナス決定
                        m_time=0;
                        m_speed=41;
                        m_teropX=240;

                        //ver1.2.0
                        setStageSound();
                        playBgm();
                        m_slotState=1;
                        playBonus();
                        break;

                    case GAME_PLAYING:
                        addCommand(pauseCmd);
                        m_isDrawFooder=true;
                        m_time=0;
                        m_speed=0;
                        break;

                    case GAME_END:
                        m_time=0;
                        m_speed=41;
                        m_teropX=240;
                        if( m_isBonusGet ){
                            //初めて取ったら新しいアイテム
                            if( m_bonusGet[m_bonusIndex]==0 )
                                m_bonusNew[m_bonusIndex]=1;
                            m_bonusGet[m_bonusIndex]=1;
                        }
                        break;

                    case GAME_RESULT:
                        m_playTime++;
                        //現在コンボマックス
                        if( m_combo>m_comboMax ){
                            m_comboMax=m_combo;
                        }
                        sortHiscore();
                        saveRecordStore();
                        break;
                }
                //初期状態を解除
                m_modeState=m_nextState;

                m_event=KEY_NO;    //キーイベント初期化
                key=KEY_NO;        //キー状態初期化

            }

//--    キー            --------------------------------------//

            //キー状態の取得
            key=devCtl.getDeviceState( DeviceControl.KEY_STATE );

            switch( m_modeState ){
                case TITLE:
                    keyTitle();
                    break;
                case MODE_SELECT:
                    keyModeSelect();
                    break;
                case GAME_PLAYING:
                    if( !m_isPause ){
                        keyPlaying();
                    }else{
                        keyMenu();
                    }
                    break;
                case GAME_RESULT:
                    keyGameResult();
                    break;
                case RECORD:
                    keyRecord();
                    break;
                case BONUS:
                    keyBonus();
                    break;
                case HELP:
                    keyHelp();
                    break;
                case ENDING:
                    keyEnding();
                    break;
            }

            //キーイベント初期化
            m_event=KEY_NO;
            //キー状態初期化
            key=KEY_NO;


//--    描画            --------------------------------------//
            switch( m_modeState ){
                case TITLE:
                    drawTitle();
                    if( m_isDrawFooder ){
                        drawFooder();
                    }
                    break;

                case MODE_SELECT:
                    drawModeSelect();
                    break;

                case RECORD:
                    drawRecord();
                    break;

                case HELP:
                    drawHelp();
                    break;

                case BONUS:
                    drawBonus();
                    break;

                case GAME_START:
                    drawGameStart();
                    drawFooder();
                    break;

                case GAME_PLAYING:
                    drawPlaying();
                    if( m_isDrawFooder ){
                        drawFooder();
                    }
                    break;

                case GAME_END:
                    drawPlaying();
                    if( m_isDrawFooder ){
                        drawFooder();
                    }
                    drawGameEnd();
                    break;

                case GAME_RESULT:
                    drawGameResult();
                    break;

                case ENDING:
                    drawEnding();
                    break;
            }
            repaint();
            //serviceRepaints();

//--    処理            --------------------------------------//

            switch( m_modeState ){
                case TITLE:
                    myAction();
                    moveTitle();
                    moveApple();
                    if( m_time%10==0 ){
                        appearApple();
                    }
                    m_time++;
                    if( m_time%4==0 ){
                        m_time=0;
                    }
                    break;

                case MODE_SELECT:
                    moveApple();
                    if( m_time%10==0 ){
                        appearApple();
                    }
                    m_time++;
                    if( m_time%4==0 ){
                        m_time=0;
                    }
                    if( m_cursorTime<CURSOR_TIME )
                        m_cursorTime++;
                    break;

                case GAME_START:
                    if( m_time>20 ){
                        m_nextState=GAME_PLAYING;
                    }else{
                        m_time++;
                    }
                    break;

                case GAME_PLAYING:
                    if( !m_isPause ){
                        moveApple();
                        if( m_time%10==0 ){
                            appearApple();
                        }
// 波紋 ver1.1.0
                        for(i=0;i<3;i++){
                            if(m_effTime[i]>4){
                                m_effTime[i]=0;
                            }else if( m_effTime[i]>0 ){
                                m_effTime[i]++;
                            }
                        }

                        if( m_comboTime>0 ){
                            m_comboTime--;
                        }
                        if( m_slotTime>0 ){
                            m_slotTime--;
                        }
                        myAction();
                        m_time++;
                        if( m_time%4==0 ){
                            m_isDrawFooder=true;
                            m_time=0;
                            m_lv--;
                            if(m_lv<0){
                                m_isPause=false;
                                m_lv=0;
                                m_nextState=GAME_END;
                            }
                        }
                    }
                    break;

                case GAME_END:
                    m_time++;
                    if( m_time>20 ){
                        m_nextState=GAME_RESULT;
                    }
                    break;

                case GAME_RESULT:
                case RECORD:
                case HELP:
                case BONUS:
                    moveApple();
                    if( m_time%10==0 ){
                        appearApple();
                    }
                    m_time++;
                    if( m_time%4==0 ){
                        m_time=0;
                    }
                    break;

                case ENDING:
                    m_walkCount++;
                    if( m_walkCount>=240 ) m_walkCount=0;
                    m_teropX++;
                    if( m_teropX>=120 ) m_teropX=0;
                    moveApple();
                    if( m_time%10==0 ){
                        appearApple();
                    }
                    m_time++;
                    if( m_time%4==0 ){
                        m_time=0;
                    }
                    break;

            }//end of switch( m_modeState )

//--    スリープ        --------------------------------------//
/*
            m_sleepTime=m_sleepTime+70L-System.currentTimeMillis();
            if( m_sleepTime>0 ) Thread.sleep(SLEEP_TIME);
            m_sleepTime=System.currentTimeMillis();
*/
            Thread.sleep(SLEEP_TIME);
        }
    }catch(Exception e){
        System.out.println(e.toString());
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    paint()
    概要:    メイン描画処理
    機能:    オーバーライド
    戻り値:    -
    引数:    Graphics g    グラフィックオブジェクト
*/
public synchronized void paint(Graphics g2) {

    int i=0;

    try{
        //初期処理
        if( m_isDrawBg ){
            g2.setColor(0,0,0);
            g2.fillRect(0,0,getWidth(),getHeight());
            m_isDrawBg=false;
        }

        //オフイメージを描画
        g2.drawImage(mainImg, m_hcenter-120, m_vcenter-120, Graphics.LEFT|Graphics.TOP);

    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    commandAction(Command c, Displayable s)
    概要:    コマンドのイベント
    機能:    -
    戻り値:    -
    引数:    -
*/
public synchronized void commandAction(Command c, Displayable s) {
    if (c == exitCmd) {//Exitコマンド
        //終了処理
        jumpingFruits.doExit();
    }
    else if (c==pauseCmd) {//Pauseコマンド
        onMenu();
    }
    else if (c==resumeCmd) {//Resumeコマンド
        offMenu();
    }
    else if (c==backCmd) {
        switch (m_modeState) {
            case MODE_SELECT:
                m_nextState=TITLE;
                break;
            case RECORD:
            case HELP:
            case BONUS:
                m_nextState=MODE_SELECT;
                break;
            case ENDING:
                m_nextState=BONUS;
                break;
        }
    }
    //ver1.2.0
    else if (c==onSoundCmd) {//♪○コマンド
        switchMute();
    }
    else if (c==offSoundCmd) {//♪×コマンド
        switchMute();
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyPressed()
    概要:    キー押下イベント
    機能:    -
    戻り値:    -
    引数:    -
*/
public synchronized void keyPressed(int keyCode) {

    int    gameCode=getGameAction(keyCode);

    switch( gameCode ) {
        case UP: //上キー押下時
            m_event=KEY_UP;
            return;
        case DOWN: //下キー押下時
            m_event=KEY_DOWN;
            return;
        case LEFT: //左キー押下時
            m_event=KEY_LEFT;
            return;
        case RIGHT: //右キー押下時
            m_event=KEY_RIGHT;
            return;
        case FIRE: //追加 決定
            m_event=KEY_SELECT;
            return;
    }

    switch( keyCode ){
        case KEY_NUM2: //2キー押下時
            m_event=KEY_2;
            return;
        case KEY_NUM4: //4キー押下時
            m_event=KEY_4;
            return;
        case KEY_NUM5: //5キー押下時
            m_event=KEY_5;
            return;
        case KEY_NUM6: //6キー押下時
            m_event=KEY_6;
            return;
        case KEY_NUM8: //8キー押下時
            m_event=KEY_8;
            return;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawNowloading()
    概要:    Now Loading描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawNowloading() {
    try{
        //背景:白
        g.setColor(255,255,255);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //bluesystem■
        g.drawImage(logoImg, 20, 100, Graphics.TOP|Graphics.LEFT);

        //プログレスバー
        g.setColor(216,216,216);
        g.fillRect(20, 220, 2*100, 2);
        g.setColor(192,192,255);
        g.fillRect(20, 220, 2*m_load, 2);

        //文字表示
        Sb.delete(0,Sb.length());
        drawSt = Sb.append(m_load).append(" ％").toString();
        g.setColor(160,160,160);
        g.drawString(drawSt,220-f.stringWidth(drawSt),210,Graphics.BASELINE|Graphics.LEFT);

    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawTitle()
    概要:    タイトル画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawTitle() {
    try{
        //背景
        g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
        //地面
        g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

        //リンゴ
        for(int i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]!=NONE && m_appleState[i]!=SPECIAL ){
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            }
        }


        //キャラクタ
        getImgIndex();
        if( !m_isLeft ){
            gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, m_myX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        }else{
            gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_MIRROR, m_myX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        }

        g.setColor(255,255,255);
        g.drawLine(0,20,SCREEN_WIDTH,20);
        g.drawLine(0,85,SCREEN_WIDTH,85);

        g.setColor(255,128,0);
        g.drawLine(0,21,SCREEN_WIDTH,21);
        g.drawLine(0,86,SCREEN_WIDTH,86);

        //title
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_TITLE1, img[IMAGE_LANG].getWidth(), NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 60, 40, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_TITLE2, img[IMAGE_LANG].getWidth(), NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 95, 60, Graphics.TOP|Graphics.LEFT);

        g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
        g.drawImage(img[IMAGE_PRESENTS], 24, SCREEN_HEIGHT-FOODER_HEIGHT+12, Graphics.TOP|Graphics.LEFT);

        if( m_time!=0 )
            g.drawImage(img[IMAGE_PRESS], 120-72, 128, Graphics.TOP|Graphics.LEFT);


    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawModeSelect()
    概要:    モードセレクト画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawModeSelect() {
    try{
        //背景
        g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
        //地面
        g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

        //リンゴ
        for(int i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]!=NONE ){
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            }
        }

        g.setColor(255,255,255);
        g.drawLine(0,20,SCREEN_WIDTH,20);
        g.drawLine(0,85,SCREEN_WIDTH,85);

        g.setColor(255,128,0);
        g.drawLine(0,21,SCREEN_WIDTH,21);
        g.drawLine(0,86,SCREEN_WIDTH,86);

        //title
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_TITLE1, img[IMAGE_LANG].getWidth(), NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 60, 40, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_TITLE2, img[IMAGE_LANG].getWidth(), NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 95, 60, Graphics.TOP|Graphics.LEFT);

        //GAME
        if ( m_cursorTime<CURSOR_TIME ){
            g.setColor(224+m_cursorTime*16,224+m_cursorTime*16,224+m_cursorTime*16);
            g.fillRect(40,100+29-m_cursorTime*8,160,1+m_cursorTime*16);
            g.setColor(192+m_cursorTime*16,192+m_cursorTime*16,192+m_cursorTime*16);
            g.drawRect(40,100+29-m_cursorTime*8,160,1+m_cursorTime*16);
        }else{
            g.drawImage(img[IMAGE_GAME], 40, 100, Graphics.TOP|Graphics.LEFT);
            gUtil.drawRegion(g,img[IMAGE_STAGE], 32*m_stage, 0,32, 32, GraphicsUtil.TRANS_NONE, 160, 120, Graphics.TOP|Graphics.LEFT);
        }

        //Cursor
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_LEFT,  0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 20-m_time,  125+m_cursor*45, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_RIGHT, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 208+m_time, 125+m_cursor*45, Graphics.TOP|Graphics.LEFT);

        //option
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*(m_option+L_RECORD), img[IMAGE_LANG].getWidth(), NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 170, Graphics.TOP|Graphics.LEFT);

        //フッダ
        g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
        if( m_cursor==0 ){
            drawSt="『ゲームを開始します』";
        }else{
            drawSt=MODE_HELP[m_option];
        }
        g.setColor(255,255,255);
        g.drawString(drawSt,120-f.stringWidth(drawSt)/2,230,Graphics.BASELINE|Graphics.LEFT);


    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawRecord()
    概要:    レコード画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawRecord() {
    try{
        //背景画像
        g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);

        //リンゴ
        for(int i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]!=NONE ){
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            }
        }

        //RECORD
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_RECORD, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);

        //score,combo
        gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SCORE, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE,  96, 46, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_COMBO, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 176, 46, Graphics.TOP|Graphics.LEFT);

        //文字表示
        for(int i=0;i<HISCORE_SIZE;i++){
            gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*(i+1), 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 20, 60+24*i, Graphics.TOP|Graphics.LEFT);
            gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_SLASH, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 40, 60+24*i, Graphics.TOP|Graphics.LEFT);
            drawScore(m_hiScore[m_cursor][i],160,60+24*i,8);
            drawScore(m_hiCombo[m_cursor][i],200,60+24*i,3);
        }
        
        g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*(m_cursor+1), 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 170, 220, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_SLASH, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 190, 220, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*STAGE_SIZE, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 210, 220, Graphics.TOP|Graphics.LEFT);

    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawHelp()
    概要:    ヘルプ画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawHelp() {
    try{
        //背景
        g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
        //地面
        g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

        //リンゴ
        for(int i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]!=NONE ){
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            }
        }

        //HELP
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_HELP, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);

        g.setColor(64,32,0);//茶色
        g.fillRect(10,40,220,160);

        g.setColor(128,64,0);
        g.drawRect(10,40,220,160);

        g.setColor(255,255,255);
        for(int i=0;i<HELP_TEXT[m_cursor].length; i++ ){
            g.setColor(128,64,0);//茶色
            g.drawString(HELP_TEXT[m_cursor][i],20,(68+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
            g.setColor(255,255,255);
            g.drawString(HELP_TEXT[m_cursor][i],20,68+20*i,Graphics.BASELINE|Graphics.LEFT);
        }

        g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*(m_cursor+1), 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 170, 220, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_SLASH, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 190, 220, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*HELP_TEXT.length, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 210, 220, Graphics.TOP|Graphics.LEFT);


    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawBonus()
    概要:    ヘルプ画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawBonus(){
    try{
        //背景
        g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
        //地面
        g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

        //リンゴ
        for(int i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]!=NONE ){
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE,
                     m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            }
        }

        //ITEM LIST
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_ITEM, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);

        //アイテム
        for(int j=0; j<3; j++){
            for(int i=0; i<4; i++){
                if( m_cursor==(j*4+i) ){
                    g.setColor(255-m_time*16,255-m_time*16,255-m_time*16);//オレンジ
                    g.drawArc((40+48*i)-3-m_time, (40+36*j)-3-m_time, 29+m_time*2,29+m_time*2, 0,360);
                }else{
                    g.setColor(255,255,255);//白
                    g.fillArc(40+48*i, 40+36*j, 24,24, 0,360);
                }

                if( m_bonusGet[j*4+i]!=0 ){
                    if( m_cursor==(j*4+i) ){
                        //拡大
                        gUtil.drawRegion(g,img[IMAGE_BONUS2], (j*4+i)*32, 0, 32, 32, GraphicsUtil.TRANS_NONE,
                            (44+48*i)-8, (44+36*j)-8, Graphics.TOP|Graphics.LEFT);
                    }else{
                        gUtil.drawRegion(g,img[IMAGE_BONUS], (j*4+i)*16, 0, 16, 16, GraphicsUtil.TRANS_NONE,
                            44+48*i, 44+36*j, Graphics.TOP|Graphics.LEFT);
                    }
                    //New
                    if( m_bonusNew[(j*4+i)]==1 ){
                        if( (m_time%4)<2 )
                            gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*3, 45, 9, GraphicsUtil.TRANS_NONE, 48+48*i, 60+36*j+(m_time%4), Graphics.TOP|Graphics.LEFT);
                        else
                            gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*3, 45, 9, GraphicsUtil.TRANS_NONE, 48+48*i, 60+2+36*j-(m_time%4), Graphics.TOP|Graphics.LEFT);
                    }
                }
            }
        }

        //ウィンドウ
        g.setColor(64,32,0);//茶色
        g.fillRect(10,150,220,50);

        g.setColor(128,64,0);
        g.drawRect(10,150,220,50);

        g.setColor(255,255,255);
        for(int i=0;i<ITEM_TEXT[m_cursor].length; i++ ){
            if( m_bonusGet[m_cursor]!=0 ){
                g.setColor(128,64,0);//茶色
                g.drawString(ITEM_TEXT[m_cursor][i],20+1,(170+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
                g.setColor(255,255,255);
                g.drawString(ITEM_TEXT[m_cursor][i],20,170+20*i,Graphics.BASELINE|Graphics.LEFT);
            }else{
                g.setColor(128,64,0);//茶色
                g.drawString(ITEM_NO[i],20+1,(170+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
                g.setColor(255,255,255);
                g.drawString(ITEM_NO[i],20,170+20*i,Graphics.BASELINE|Graphics.LEFT);

            }
        }

        g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
        drawScore((m_cursor+1),150, 220,2);
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_SLASH, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 170, 220, Graphics.TOP|Graphics.LEFT);
        drawScore(BONUS_SIZE,210,220,2);

    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawEnding()
    概要:    タイトル画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawEnding(){
    try{
        //背景
        g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
        //地面
        //g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
        
        //gUtil.drawRegion(g,img[IMAGE_GROUND3], m_teropX, 0, 240-m_teropX, 19, GraphicsUtil.TRANS_NONE, 0-m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
        //gUtil.drawRegion(g,img[IMAGE_GROUND3], m_teropX, 0, 240-m_teropX, 19, GraphicsUtil.TRANS_NONE, 240-m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
        g.drawImage(img[IMAGE_GROUND3], 0-2*m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
        g.drawImage(img[IMAGE_GROUND3], 240-2*m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
        
        //リンゴ
        for(int i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]!=NONE && m_appleState[i]!=SPECIAL ){
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            }
        }

        for(int i=0;i<END_TEXT.length; i++ ){
            g.setColor(128,64,0);//茶色
            g.drawString(END_TEXT[i],120-f.stringWidth(END_TEXT[i])/2,(32+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
            g.setColor(255,255,255);
            g.drawString(END_TEXT[i],120-f.stringWidth(END_TEXT[i])/2,32+20*i,Graphics.BASELINE|Graphics.LEFT);
        }

        //ぬこ
        if( (m_time%4)<2 ){
            g.drawImage(img[IMAGE_NEKO], -240+4*m_teropX, 140+(m_time%4), Graphics.TOP|Graphics.LEFT);
        }else{
            g.drawImage(img[IMAGE_NEKO], -240+4*m_teropX, 144-(m_time%4), Graphics.TOP|Graphics.LEFT);
        }

        //キャラクタ
        getImgIndex();
        //for(int i=-1;i<10;i++)
        //    gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, i*24+2*m_teropX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, -24+6*m_teropX, m_myY-24, Graphics.TOP|Graphics.LEFT);

        g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
        drawSt="『作者からの手紙』";
        g.setColor(255,255,255);
        g.drawString(drawSt,120-f.stringWidth(drawSt)/2,230,Graphics.BASELINE|Graphics.LEFT);

    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawGameStart()
    概要:    ゲームスタート画面描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawGameStart() {
    try{
        if( m_time<10 ){
            m_speed-=4;
        }else{
            m_speed+=5;
        }
        m_teropX-=m_speed;

        //背景画像
        g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);

        //キャラクタ
        getImgIndex();
        if( !m_isLeft ){
            gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, m_myX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        }else{
            gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_MIRROR, m_myX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        }
/*
        g.setColor(255,255,255);
        g.drawString("GAME_START",60,120,Graphics.BASELINE|Graphics.LEFT);
*/
        //TIME_UP
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_START, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, m_teropX, 80, Graphics.TOP|Graphics.LEFT);


    }catch(Exception e){
System.out.println(e.toString());
    }
}



////////////////////////////////////////////////////////////////
/**
    名前:    drawPlaying()
    概要:    ゲーム中描画処理
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawPlaying(){
    int i=0;

    try{
        //背景画像
        g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);


        //波紋
        for(i=0;i<3;i++){
            if( m_effTime[i]>0 ){
                gUtil.drawRegion(g,img[IMAGE_SHINE], 32*(m_effTime[i]-1), 0, 32, 32, GraphicsUtil.TRANS_NONE, m_effX[i]-8, m_effY[i]-24, Graphics.TOP|Graphics.LEFT);
            }
        }

        //リンゴ
        for(i=0; i<APPLE_SIZE; i++){
            if( m_appleState[i]==SPECIAL )
                //BONUS1
                gUtil.drawRegion(g,img[IMAGE_BONUS], 16*m_bonusIndex, 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
            else if( m_appleState[i]!=NONE )
                gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
        }

        //スロット
        if( m_slotTime>0 ){
            drawSlot();
        }

        //コンボ
        if( m_combo>0 ){
            //HOP,STEP,JUMP
            if( m_combo==1 )
                gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*0, 45, 9, GraphicsUtil.TRANS_NONE, m_myX-8, m_myY-35, Graphics.TOP|Graphics.LEFT);
            else if( m_combo==2 )
                gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*1, 45, 9, GraphicsUtil.TRANS_NONE, m_myX-8, m_myY-35, Graphics.TOP|Graphics.LEFT);
            else
                gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*2, 45, 9, GraphicsUtil.TRANS_NONE, m_myX-8, m_myY-35, Graphics.TOP|Graphics.LEFT);

            //コンボ
            if( m_comboTime==0 )
                drawScore(m_combo,228-NUM_WIDTH,16,2);
            else
                drawScoreB(m_combo,228-NUM_WIDTH*2,16,2);//大
            gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_COMBO, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 228-44, 4, Graphics.TOP|Graphics.LEFT);
        }

//ver1.1.0
        if( m_myY<0 ){
            g.setColor(255,255-m_time*16,255);
            g.drawString("▲",m_myX+4,16,Graphics.BASELINE|Graphics.LEFT);
        }else{
        //キャラクタ
        getImgIndex();
        if( !m_isLeft ){
            gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, m_myX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        }else{
            gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_MIRROR, m_myX, m_myY-24, Graphics.TOP|Graphics.LEFT);
        }

        }
//end of ver1.1.0

        //debug
        //g.drawString(""+runtime.freeMemory()+"/"+runtime.totalMemory(),0,0,Graphics.TOP|Graphics.LEFT);
        //g.drawString("index:"+m_bonusIndex,0,0,Graphics.TOP|Graphics.LEFT);

        //PAUSE
        if( m_isPause ){
            drawMenu();
        }

    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawFooder()
    概要:    ゲーム中描画処理
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawFooder(){

    //背景
    g.drawImage(imgFd, 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);

    //スコア
    drawScore(m_score,221,214,8);

    //時間バー
    g.setColor(255,255,255);
    g.fillRect(121,233,m_lv,3);

    //スロット枠
    g.setColor(255,128+(m_lv%4)*24,0+(m_lv%4)*48);
    g.drawRect(8+20*((m_combo)%3), 220, 17, 17);

    //スロット
    for(int i=0;i<3;i++){
        if( m_slot[i]!=NONE )
            gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_slot[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, 9+20*i, 221, Graphics.TOP|Graphics.LEFT);
    }

    //描画終了
    m_isDrawFooder=false;
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawSlot()
    概要:    左上のスロット描画
    機能:    -
    戻り値:    -
    引数:    -
*/
private void drawSlot(){

    int slotTime=SLOT_TIME-m_slotTime;

    if( slotTime<5 )
        gUtil.drawRegion(g,img[IMAGE_SLOT], 0, 28*m_slotState,64, 28, GraphicsUtil.TRANS_NONE, 0-80+16*slotTime, 0, Graphics.TOP|Graphics.LEFT);
    else if( slotTime>15 )
        gUtil.drawRegion(g,img[IMAGE_SLOT], 0, 28*m_slotState,64, 28, GraphicsUtil.TRANS_NONE, 0-16*(slotTime-15), 0, Graphics.TOP|Graphics.LEFT);
    else
        gUtil.drawRegion(g,img[IMAGE_SLOT], 0, 28*m_slotState,64, 28, GraphicsUtil.TRANS_NONE, 0, 0, Graphics.TOP|Graphics.LEFT);
}

////////////////////////////////////////////////////////////////
/**
    名前:    drawScore()
    概要:    スコア描画
    機能:    -
    戻り値:  -
    引数:    -
*/
private void drawScore(int score,int x,int y,int size){
    int tmp=score;
    int num=0;
    int i=0;

    //得点
    for(i=0;i<size;i++){
        num=tmp%10;
        gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*num, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, x-(NUM_WIDTH+2)*i, y, Graphics.TOP|Graphics.LEFT);
        tmp/=10;
        if(tmp==0){
            break;
        }
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    drawScoreB()
    概要:    大きいスコア描画
    機能:    -
    戻り値:  -
    引数:    -
*/
private void drawScoreB(int score,int x,int y,int size){
    int tmp=score;
    int num=0;
    int i=0;

    //得点
    for(i=0;i<size;i++){
        num=tmp%10;
        gUtil.drawRegion(g,img[IMAGE_NUMB], NUM_WIDTH*2*num, 0, NUM_WIDTH*2, NUM_HEIGHT*2, GraphicsUtil.TRANS_NONE, x-(NUM_WIDTH*2+4)*i, y, Graphics.TOP|Graphics.LEFT);
        tmp/=10;
        if(tmp==0){
            break;
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawCal()
    概要:    月日描画
    機能:    -
    戻り値:  -
    引数:    -

private void drawCal(int score,int x,int y){
    int tmp=score;
    int num=0;
    int i=0;

    //得点
    for(i=0;i<(2+1+2);i++){
        //スラッシュ
        if(i==2 || i==5 ){
            gUtil.drawRegion(g,img[IMAGE_CAL], CAL_WIDTH*N_SLASH, 0, CAL_WIDTH, CAL_HEIGHT, GraphicsUtil.TRANS_NONE, x-(CAL_WIDTH+1)*i, y, Graphics.TOP|Graphics.LEFT);
            continue;
        }
        num=tmp%10;
        gUtil.drawRegion(g,img[IMAGE_CAL], CAL_WIDTH*num, 0, CAL_WIDTH, CAL_HEIGHT, GraphicsUtil.TRANS_NONE, x-(CAL_WIDTH+1)*i, y, Graphics.TOP|Graphics.LEFT);
        tmp/=10;
    }
}
*/


////////////////////////////////////////////////////////////////
/**
    名前:    drawMenu()
    概要:    メニュー描画
    機能:    -
    戻り値:  -
    引数:    -
*/
private void drawMenu(){

    //HELP
    gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_PAUSE, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 80, 80, Graphics.TOP|Graphics.LEFT);

    //ウィンドウ
    g.setColor(64,32,0);//茶色
    g.fillRect(10,100,220,50);

    g.setColor(128,64,0);
    g.drawRect(10,100,220,50);

    g.setColor(255,255,255);
    for(int i=0;i<MENU_TEXT.length; i++ ){
        g.setColor(128,64,0);//茶色
        g.drawString(MENU_TEXT[i],120-f.stringWidth(MENU_TEXT[i])/2+1,(120+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
        if( m_cursor==i ){
            g.setColor(255,255,255);
            g.drawString(MENU_TEXT[i],120-f.stringWidth(MENU_TEXT[i])/2,120+20*i,Graphics.BASELINE|Graphics.LEFT);
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    drawGameEnd()
    概要:    ゲーム終了描画
    機能:    -
    戻り値:  -
    引数:    -
*/
private void drawGameEnd(){
    if( m_time<10 ){
        m_speed-=4;
    }else{
        m_speed+=5;
    }
    m_teropX-=m_speed;
    //g.drawImage(img[IMAGE_TIMEUP], m_teropX, 80, Graphics.TOP|Graphics.LEFT);
    //TIME_UP
    gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_TIME_UP, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, m_teropX, 80, Graphics.TOP|Graphics.LEFT);

}


////////////////////////////////////////////////////////////////
/**
    名前:    drawGameResult()
    概要:    ゲーム結果画面描画
    機能:    -
    戻り値:  -
    引数:    -
*/
private void drawGameResult(){
    try{
        //背景画像
        g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);

        //RECORD
        gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_RECORD, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);
        //gUtil.drawRegion(g,img[IMAGE_STAGE], 32*m_stage, 0,32, 32, GraphicsUtil.TRANS_NONE, 160, 20, Graphics.TOP|Graphics.LEFT);

        //score,combo
        gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SCORE, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE,  96, 46, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_COMBO, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 176, 46, Graphics.TOP|Graphics.LEFT);

        //文字表示
        for(int i=0;i<HISCORE_SIZE;i++){
            if( m_hiScoreNum==i && m_time==0 ){
            }else{
                gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*(i+1), 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 20, 60+24*i, Graphics.TOP|Graphics.LEFT);
                gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_SLASH, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 40, 60+24*i, Graphics.TOP|Graphics.LEFT);
                drawScore(m_hiScore[m_stage][i],160,60+24*i,8);
                drawScore(m_hiCombo[m_stage][i],200,60+24*i,2);
                //drawCal(m_calendar[m_stage][i], 220,62+24*i);
            }
        }
    }catch(Exception e){
        System.out.println(e.toString());
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyTitle()
    概要:    タイトルキー処理
    機能:    -
    戻り値:  -
    引数:    -
*/
public void keyTitle(){
    switch( m_event ){
        case KEY_5:
        case KEY_SELECT:
            m_nextState=MODE_SELECT;
            break;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyModeSelect()
    概要:    タイトルキー処理
    機能:    -
    戻り値:  -
    引数:    -
*/
public void keyModeSelect(){
    switch( m_event ){
        case KEY_UP:
        case KEY_2:
        case KEY_DOWN:
        case KEY_8:
            if(m_cursor==0){
                m_cursor=1;
            }else{
                m_cursor=0;
            }
            playCursor();
            break;

        case KEY_LEFT:
        case KEY_4:
            if( m_cursor==0 ){
                m_stage--;
                if( m_stage<0 ){
                    m_stage=STAGE_SIZE-1;
                }
                m_cursorTime=0;
            }else{
                m_option--;
                if( m_option<0 ){
                    m_option=MODE_SIZE-1;
                }
            }
            playCursor();
            break;

        case KEY_RIGHT:
        case KEY_6:
            if( m_cursor==0 ){
                m_stage++;
                if( m_stage>STAGE_SIZE-1 ){
                    m_stage=0;
                }
                m_cursorTime=0;
            }else{
                m_option++;
                if( m_option>MODE_SIZE-1 ){
                    m_option=0;
                }
            }
            playCursor();
            break;

        case KEY_5:
        case KEY_SELECT:
            if( m_cursor==0 ){
                m_nextState=GAME_START;
            }else{
                switch( m_option ){
                    case 0:
                        m_nextState=RECORD;
                        break;
                    case 1:
                        m_nextState=BONUS;
                        break;
                    case 2:
                        m_nextState=HELP;
                        break;
                }
                //playCursor();
            }
            break;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyRecord()
    概要:    レコードキー処理
    機能:    -
    戻り値:  -
    引数:    -
*/
private void keyRecord(){
    switch( m_event ){
        case KEY_UP:
        case KEY_2:
        case KEY_LEFT:
        case KEY_4:
            m_cursor--;
            if( m_cursor<0 ){
                m_cursor=STAGE_SIZE-1;
            }
            setStageImage(m_cursor);
            playCursor();
            break;

        case KEY_DOWN:
        case KEY_8:
        case KEY_RIGHT:
        case KEY_6:
            m_cursor++;
            if( m_cursor>STAGE_SIZE-1 ){
                m_cursor=0;
            }
            setStageImage(m_cursor);
            playCursor();
            break;

        case KEY_5:
        case KEY_SELECT:
            m_nextState=MODE_SELECT;
            break;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyBonus()
    概要:    ボーナスアイテムキー処理
    機能:    -
    戻り値:  -
    引数:    -
*/
private void keyBonus(){
    switch( m_event ){
        case KEY_UP:
        case KEY_2:
        case KEY_LEFT:
        case KEY_4:
            m_bonusNew[m_cursor]=0;
            m_cursor--;
            if( m_cursor<0 ){
                m_cursor=BONUS_SIZE-1;
            }
            playCursor();
            break;

        case KEY_DOWN:
        case KEY_8:
        case KEY_RIGHT:
        case KEY_6:
            m_bonusNew[m_cursor]=0;
            m_cursor++;
            if( m_cursor>BONUS_SIZE-1 ){
                m_cursor=0;
            }
            playCursor();
            break;

        case KEY_5:
        case KEY_SELECT:
            if( m_cursor==BONUS_SIZE-1 && m_bonusGet[BONUS_SIZE-1]==1 ){
                m_nextState=ENDING;
            }else{
                m_nextState=MODE_SELECT;
            }
            break;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyHelp()
    概要:    ヘルプキー処理
    機能:    -
    戻り値:  -
    引数:    -
*/
private void keyHelp(){
    switch( m_event ){
        case KEY_UP:
        case KEY_2:
        case KEY_LEFT:
        case KEY_4:
            m_cursor--;
            if( m_cursor<0 ){
                m_cursor=HELP_TEXT.length-1;
            }
            playCursor();
            break;

        case KEY_DOWN:
        case KEY_8:
        case KEY_RIGHT:
        case KEY_6:
            m_cursor++;
            if( m_cursor>HELP_TEXT.length-1 ){
                m_cursor=0;
            }
            playCursor();
            break;

        case KEY_5:
        case KEY_SELECT:
            m_nextState=MODE_SELECT;
            break;
    }
}
////////////////////////////////////////////////////////////////
/**
    名前:    keyEnding()
    概要:    エンディングキー処理
    機能:    -
    戻り値:    -
    引数:    -
*/
private void keyEnding(){
    switch( m_event ){
        case KEY_5:
        case KEY_SELECT:
            m_nextState=BONUS;
            break;
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    keyGameResult()
    概要:    ゲーム結果キー処理
    機能:    -
    戻り値:    -
    引数:    -
*/
private void keyGameResult(){
    switch( m_event ){
        case KEY_5:
        case KEY_SELECT:
            m_nextState=TITLE;
            break;
    }
}



////////////////////////////////////////////////////////////////
/**
    名前:    keyPlaying()
    概要:    ゲーム中キー処理
    機能:    -
    戻り値:    -
    引数:    -
*/
public void keyPlaying(){

    //左キー
    if ( (key&KEY_LEFT)!=0 || (key&KEY_4)!=0) {
        if( m_myState!=JUMP ){
            m_myState=WALK;
        }
        m_isLeft=true;
        m_speed=WALK_SPEED;
    }
    // 右キー
    else if ( (key&KEY_RIGHT)!=0 || (key&KEY_6)!=0) {
        if( m_myState!=JUMP ){
            m_myState=WALK;
        }
        m_isLeft=false;
        m_speed=WALK_SPEED;
    }
    //左・右を押していない
    else{
        if( m_myState!=JUMP ){
            m_myState=STOP;
        }
        m_speed/=2;
    }

    // 下キー
    if( (key&KEY_DOWN)!=0 || (key&KEY_8)!=0){
        if( m_myState!=JUMP && m_ground!=GROUND_BOTTOM ){
            m_myState=JUMP;
            if( m_ground==GROUND_MIDDLE ){
                m_ground=GROUND_BOTTOM;
            }
            else if( m_ground==GROUND_TOP ){
                m_ground=GROUND_MIDDLE;
            }
        }
    }
    //ジャンプキー
    if( (key&KEY_5)!=0 || (key&KEY_SELECT)!=0 ){
        if( m_myState!=JUMP ){
            m_myState=JUMP;
            m_walkCount=0;
            m_addY=-JUMP_HEIGHT;
            m_myY--;
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    onMenu(),offMenu()
    概要:    メニュー切り替え
    機能:    -
    戻り値:    -
    引数:    -
*/
private void onMenu(){
    m_isPause=true;
    m_cursor=0;
    removeCommand(pauseCmd);
    addCommand(resumeCmd);
}
private void offMenu(){
    m_isPause=false;
    removeCommand(resumeCmd);
    addCommand(pauseCmd);
}


////////////////////////////////////////////////////////////////
/**
    名前:    keyMenu()
    概要:    メニュー画面キー処理
    機能:    -
    戻り値:    -
    引数:    -
*/
public void keyMenu() {
    switch (m_event) {
        case KEY_2:
        case KEY_UP:
        case KEY_8:
        case KEY_DOWN:
            if( m_cursor==0 )
                m_cursor=1;
            else
                m_cursor=0;
            break;

        case KEY_5:
        case KEY_SELECT:
            switch (m_cursor) {
                //再開
                case 0:
                    break;
                //タイトル
                case 1:
                    m_nextState=TITLE;
                    break;
            }
            m_isPause=false;
            removeCommand(resumeCmd);
            addCommand(pauseCmd);
            break;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    getImgIndex()
    概要:    キャラクタのイメージ番号取得
    機能:    -
    戻り値:    -
    引数:    -
*/
private void getImgIndex(){

    switch (m_myState) {
        case STOP:
            m_imgIndex=IMAGE_STOP;
            break;

        case WALK:
            switch (m_walkCount%3) {
                case 0:
                    m_imgIndex=IMAGE_STOP;
                    break;
                case 1:
                    m_imgIndex=IMAGE_WALK1;
                    break;
                case 2:
                    m_imgIndex=IMAGE_WALK2;
                    break;
            }
            break;

        case JUMP:
            m_imgIndex=IMAGE_WALK1;
            break;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    myAction()
    概要:    キャラクタの処理
    機能:    -
    戻り値:    -
    引数:    -
*/
private void myAction() {

    //とまっていれば減速
    if (m_myState==STOP) {
        m_walkCount=0;
    } else {
        m_walkCount++;
    }

    checkGround();

    //キャラクタの移動
    if (!m_isLeft) {//右
        m_myX += m_speed;
        //端
        if(m_myX > SCREEN_WIDTH-MYCHARA_WIDTH){
            m_myX = SCREEN_WIDTH-MYCHARA_WIDTH;
        }

    } else {//左
        m_myX -= m_speed;
        if(m_myX < 0){
            m_myX = 0;
        }
    }

    if (m_myY < m_ground) {
        //地面取得
        getGround();
        m_addY+=4;
        m_myY+=m_addY;
        if (m_myY >= m_ground) {
            m_addY=0;
            m_myY=m_ground;

            if (m_combo > m_comboMax) {
                m_comboMax = m_combo;
            }
            m_combo=0;

            //スロット初期化
            for(int i=0; i<m_slot.length; i++) {
                m_slot[i]=NONE;
            }
            m_myState=STOP;
            m_speed=0;
            m_isDrawFooder=true;
        }
    }

    //フルーツを踏んだとき
    if( m_myState==JUMP ){
        for(int i=0;i<APPLE_SIZE; i++){
            if( m_myX+MYCHARA_WIDTH>m_appleX[i] && m_myX<m_appleX[i]+16 &&
                m_myY>m_appleY[i]-16 && m_myY<m_appleY[i]+16 && m_appleState[i]!=NONE )
            {
                //ジャンプ
                m_addY=-JUMP_HEIGHT;
                //コンボ追加
                m_combo++;
                m_comboTime=COMBO_TIME;
                if( m_combo>MAX_COMBO ){
                    m_combo=MAX_COMBO;
                }
                //スコア追加
                m_score+=10*m_combo;
                if( m_score>MAX_SCORE ){
                    m_score=MAX_SCORE;
                }

                //スペシャルアイテム
                if(m_appleState[i]==SPECIAL){
                    m_slotTime=SLOT_TIME;
                    m_slotState=GET_ITEM;
                    m_score+=10000;
                    if( m_score>MAX_SCORE ) m_score=MAX_SCORE;
                    m_appleState[i]=NONE;
                    m_isBonusGet=true;
                    m_isDrawFooder=true;
                    //ver1.2.0
                    playBonus();
                    continue;
                }else{
                    //ver1.2.0
                    playEnter();
                }

                //スロットに格納して
                m_slot[(m_combo-1)%3]=m_appleState[i];
// 波紋 ver1.1.0
                m_effX[(m_combo-1)%3]=m_appleX[i];
                m_effY[(m_combo-1)%3]=m_appleY[i];
                m_effTime[(m_combo-1)%3]=1;

                checkSlot();
                m_appleState[i]=NONE;

                m_isDrawFooder=true;
                break;
            }
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    moveApple()
    概要:    キャラクタの処理
    機能:    -
    戻り値:    -
    引数:    -
*/
private void moveApple(){
    int i=0;

    for(i=0; i<APPLE_SIZE; i++){
        if( m_appleState[i]!=NONE ){
            //キャラクタの移動
            if( !m_appleIsLeft[i] ){//右
                m_appleX[i]+=m_appleSpeedX[i];
                //端
                if( m_appleX[i]>SCREEN_WIDTH+MYCHARA_WIDTH ){
                    m_appleState[i]=NONE;
                }

            }else{//左
                m_appleX[i]-=m_appleSpeedX[i];
                //端
                if( m_appleX[i]<0-MYCHARA_WIDTH ){
                    m_appleState[i]=NONE;
                }
            }

            //落下
            m_appleSpeedY[i]+=4;
            m_appleY[i]+=m_appleSpeedY[i];

            //衝突
            if( m_appleY[i]>GROUND_BOTTOM ){
                m_appleSpeedY[i]=-28-(m_appleState[i]*4);
                m_appleY[i]=GROUND_BOTTOM;
            }
        }
    }//end of for(i=0; i<APPLE_SIZE; i++)
}


////////////////////////////////////////////////////////////////
/**
    名前:    appearApple()
    概要:    りんごの出現
    機能:    -
    戻り値:    -
    引数:    -
*/
private void appearApple(){
    int i=0;

    for(i=0; i<APPLE_SIZE; i++){
        if(m_appleState[i]==NONE){
            if( !m_isAppearBonus && m_lv<BONUS_APPEAR ){
                m_appleState[i]=SPECIAL;
                m_isAppearBonus=true;
            }else{
                m_appleState[i]=(Math.abs(r.nextInt())%3);
            }

            m_appleX[i]=Math.abs(r.nextInt())%SCREEN_WIDTH;
            m_appleY[i]=0;
            m_appleSpeedX[i]=(Math.abs(r.nextInt())%3)*2+2;
            m_appleSpeedY[i]=4;
            if( m_appleX[i]>(SCREEN_WIDTH/2) ){
                m_appleIsLeft[i]=true;
            }else{
                m_appleIsLeft[i]=false;
            }
            break;
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    getGround()
    概要:    地面取得
    機能:    -
    戻り値:    -
    引数:    -
*/
private void getGround(){

    if( m_myY<GROUND_TOP ){
        if(    ( GROUND_TOP1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
            ( GROUND_TOP2X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP2X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ))
        {
            m_ground=GROUND_TOP;
        }
    }
    else if( m_myY<GROUND_MIDDLE ){
        if(    ( GROUND_MIDDLE1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_MIDDLE1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
            ( GROUND_MIDDLE2X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_MIDDLE2X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ))
        {
        
            m_ground=GROUND_MIDDLE;
        }
    }
    else{
        m_ground=GROUND_BOTTOM;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    walkCheckGround()
    概要:    地面取得
    機能:    -
    戻り値:    -
    引数:    -
*/
private void checkGround(){

    if( m_ground==GROUND_TOP ){
        if(    ( GROUND_TOP1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
            ( GROUND_TOP2X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP2X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) )
        {
        }else{
            m_myState=JUMP;
            m_ground=GROUND_MIDDLE;
        }
    }
    else if( m_ground==GROUND_MIDDLE ){
        if(    ( GROUND_MIDDLE1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_MIDDLE1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
            ( GROUND_MIDDLE2X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_MIDDLE2X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ))
        {
        }else{
            m_myState=JUMP;
            m_ground=GROUND_BOTTOM;
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    initTitle()
    概要:    タイトル初期化
    機能:    -
    戻り値:    -
    引数:    -
*/
private void initTitle(){
    m_myState=WALK;
    m_isLeft=false;
    m_myX=60;
    m_myY=GROUND_BOTTOM;
    m_speed=WALK_SPEED;
}

////////////////////////////////////////////////////////////////
/**
    名前:    initGame()
    概要:    ゲーム初期化
    機能:    -
    戻り値:    -
    引数:    -
*/
private void initGame(){

    //ゲーム
    m_time            =0;
    m_lv            =TIME_LIMIT;
    m_score            =0;
    m_combo            =0;
    m_comboMax        =0;
    m_comboTime        =0;

    //キャラクタ
    m_myX            =120;
    m_myY            =GROUND_BOTTOM;
    m_isLeft        =false;
    m_myState        =STOP;
    m_addY            =0;
    m_walkCount        =0;
    m_imgIndex        =IMAGE_STOP;
    m_speed            =0;
    m_ground        =GROUND_BOTTOM;

    //スロット
    for(int i=0;i<m_slot.length;i++){
        m_slot[i]=NONE;
    }
    m_slotTime=0;
    m_slotState=NONE;
    m_isAppearBonus=false;
    m_isBonusGet=false;

    //フルーツ
    for(int i=0;i<APPLE_SIZE;i++){
        m_appleState[i]    =NONE;
        m_appleX[i]        =0;
        m_appleY[i]        =0;
        m_appleSpeedX[i]=4;
        m_appleSpeedY[i]=4;
        m_appleIsLeft[i]=false;
    }

    //ver1.1.0 波紋
    for(int i=0;i<3;i++){
        m_effX[i]        =0;
        m_effY[i]        =0;
        m_effTime[i]    =0;
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    moveTitle()
    概要:    タイトルでのひよこ
    機能:    -
    戻り値:    -
    引数:    -
*/
private void moveTitle(){

    if( m_myX<40-MYCHARA_WIDTH ){
        m_isLeft=false;
    }
    else if( m_myX>200 ){
        m_isLeft=true;
    }

    if( (Math.abs(r.nextInt())%100)<10 ){
        if(!m_isLeft){
            m_isLeft=true;
        }else{
            m_isLeft=false;
        }
    }
}


////////////////////////////////////////////////////////////////
/**
    名前:    checkSlot()
    概要:    スロット
    機能:    -
    戻り値:    -
    引数:    -
*/
private void checkSlot(){

    //スロットが空なら終了
    if( m_slot[0]==NONE || m_slot[1]==NONE || m_slot[2]==NONE ){
        return;
    }

    //same bonus
    if( m_slot[0]==m_slot[1] && m_slot[0]==m_slot[2] ){
        m_score+=BONUS_SCORE+m_slot[0]*500;
        if( m_score>MAX_SCORE ){
            m_score=MAX_SCORE;
        }
        m_slotTime=SLOT_TIME;
        m_slotState=SLOT_BONUS;
        playBonus();
    }

    //same bonus
    else if( m_slot[0]!=m_slot[1] && m_slot[1]!=m_slot[2] && m_slot[2]!=m_slot[0]){
        m_lv+=BONUS_TIME;
        if( m_lv>TIME_LIMIT ){
            m_lv=TIME_LIMIT;
        }
        m_slotTime=SLOT_TIME;
        m_slotState=TIME_BONUS;
        playBonus();
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    sortHiscore()
    概要:    ハイスコア並び替え
    機能:    -
    戻り値:    -
    引数:    -
*/
private void sortHiscore(){
    int i=0;
    int j=HISCORE_SIZE-2;
/*
    Calendar cal=Calendar.getInstance();
    int year=cal.get(Calendar.YEAR);
    int month=cal.get(Calendar.MONTH)+1;
    int day=cal.get(Calendar.DAY_OF_MONTH);

    //YYYY年MM月DD日
    int now=year*10000+month*100+day;
*/
    m_hiScoreNum=-1;

    for(i=0;i<HISCORE_SIZE;i++){
        if( m_score>=m_hiScore[m_stage][i] ){
            while( j>=0 && j>=i){
                m_hiScore[m_stage][j+1]=m_hiScore[m_stage][j];
                m_hiCombo[m_stage][j+1]=m_hiCombo[m_stage][j];
                j--;
            }
            m_hiScore[m_stage][i]=m_score;
            //m_calendar[m_stage][i]=now;
            m_hiCombo[m_stage][i]=m_comboMax;
            m_hiScoreNum=i;
            break;
        }
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    setStageImage()
    概要:    レコードストアから読み出す関数
    機能:    -
    戻り値:  -
    引数:    stage:ステージ
*/
private void setStageImage(int stage) {
    Graphics g3=imgBg.getGraphics();

    try{
        g3.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
        g3.drawImage(img[IMAGE_GROUND1], GROUND_TOP1X[stage], GROUND_TOP, Graphics.TOP|Graphics.LEFT);
        g3.drawImage(img[IMAGE_GROUND1], GROUND_TOP2X[stage], GROUND_TOP, Graphics.TOP|Graphics.LEFT);
        g3.drawImage(img[IMAGE_GROUND2], GROUND_MIDDLE1X[stage], GROUND_MIDDLE, Graphics.TOP|Graphics.LEFT);
        g3.drawImage(img[IMAGE_GROUND2], GROUND_MIDDLE2X[stage], GROUND_MIDDLE, Graphics.TOP|Graphics.LEFT);
        g3.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
    }catch(Exception e){
        System.out.println(e.toString());
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    setFooderImage()
    概要:    フッダをセットする関数
    機能:    -
    戻り値:  -
    引数:    -
*/
private void setFooderImage() {
    Graphics g3=imgFd.getGraphics();

    try{
        g3.drawImage(img[IMAGE_FOODER], 0, 0, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g3,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SLOT, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 8, 211-PLAYING_HEIGHT, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g3,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SCORE, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 72, 211-PLAYING_HEIGHT, Graphics.TOP|Graphics.LEFT);
        gUtil.drawRegion(g3,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_TIME, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 72, 231-PLAYING_HEIGHT, Graphics.TOP|Graphics.LEFT);

        //時間バー
        g3.setColor(  0,128,  0);
        g3.fillRect(119,231-PLAYING_HEIGHT,TIME_LIMIT+3,6);
        g3.setColor(255,255,255);
        g3.drawRect(119,231-PLAYING_HEIGHT,TIME_LIMIT+3,6);

        //SLOT
        for(int i=0;i<3;i++){
            //スロット枠
            g3.setColor(255,255,255);
            g3.fillRect(8+20*i, 220-PLAYING_HEIGHT, 17, 17);
            g3.setColor(192,255,192);
            g3.drawRect(8+20*i, 220-PLAYING_HEIGHT, 17, 17);
        }
    }catch(Exception e){
        System.out.println(e.toString());
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    getBonusIndex()
    概要:    レコードストアから読み出す関数
    機能:    -
    戻り値:  -
    引数:    -
*/
private void getBonusIndex() {
    int bonusOn[]={0,1,0,0,0,0,0,0,0,0,0,0};
    int indexSize=2;
    Calendar cal=Calendar.getInstance();
    int week=cal.get(Calendar.DAY_OF_WEEK);
    int hour=cal.get(Calendar.HOUR);
    int minute=cal.get(Calendar.MINUTE);

    //3.メロン
    if( (Math.abs(r.nextInt())%1)==0 ){
        bonusOn[indexSize]=2;
        indexSize++;
    }
    //4.さくらんぼ
    if( m_playTime>=25 ){
        bonusOn[indexSize]=3;
        indexSize++;
    }
    //5.ラフランス
    if(    ( m_playTime>=100 ) ||
        ( week==Calendar.SUNDAY )    ){
        bonusOn[indexSize]=4;
        indexSize++;
    }
    //6.甘いみかん
    if(    ( m_playTime>=300 ) ||
        ( hour==12 )    ){
        bonusOn[indexSize]=5;
        indexSize++;
    }
    //7.星のかけら
    if(    ( m_hiScore[m_stage][0]>=30000 )||
        ( hour==5 )    ){
        bonusOn[indexSize]=6;
        indexSize++;
    }
    //8.ガンデム
    if(    ( m_hiScore[m_stage][0]>=60000 ) ||
        ( m_playTime>=500 )    ){
        bonusOn[indexSize]=7;
        indexSize++;
    }
    //9.モーター
    if(    ( m_hiScore[m_stage][0]>=90000 ) ||
        ( minute%10==0 )    ){
        bonusOn[indexSize]=8;
        indexSize++;
    }
    //10.ゲムボイ
    if(    ( m_hiScore[0][0]>=30000 && m_hiScore[1][0]>=30000 && m_hiScore[2][0]>=30000) ||
        ( week==Calendar.WEDNESDAY )    ){
        bonusOn[indexSize]=9;
        indexSize++;
    }
    //11.八星球
    if(    ( m_hiScore[0][0]>=60000 && m_hiScore[1][0]>=60000 && m_hiScore[2][0]>=60000) ||
        ( week==Calendar.MONDAY && hour==9 )    ){
        bonusOn[indexSize]=10;
        indexSize++;
    }

    //12.手紙
    if( indexSize>=10 ){
        bonusOn[indexSize]=11;
        indexSize++;
    }
    m_bonusIndex=bonusOn[(Math.abs(r.nextInt())%indexSize)];

    /** debug
        for(int i=0;i<BONUS_SIZE;i++){
            m_bonusGet[i]=1;
            m_bonusNew[i]=1;
        }
    */
}

////////////////////////////////////////////////////////////////
/**
    名前:    loadRecordStore()
    概要:    レコードストアから読み出す関数
    機能:    -
    戻り値:    -
    引数:    -
*/
private synchronized void loadRecordStore() {
    RecordStore rs=null;
    byte[] w=null;

    int p=0;

    int i,j;

    //読み込み
    try{
        //レコードストアを開く
        rs=RecordStore.openRecordStore(RS_GAMEDATA,false);

        w=rs.getRecord(1);

//--    読込変換        --------------------------------------//

        m_playTime=((w[p+0]&0xFF)<<24)+((w[p+1]&0xFF)<<16)+((w[p+2]&0xFF)<<8)+((w[p+3]&0xFF)<<0);
        p+=4;

        for(j=0;j<STAGE_SIZE;j++){
            for(i=0;i<HISCORE_SIZE;i++){
                m_hiScore[j][i]=((w[p+0]&0xFF)<<24)+((w[p+1]&0xFF)<<16)+((w[p+2]&0xFF)<<8)+((w[p+3]&0xFF)<<0);
                p+=4;

                m_hiCombo[j][i]=((w[p+0]&0xFF)<<24)+((w[p+1]&0xFF)<<16)+((w[p+2]&0xFF)<<8)+((w[p+3]&0xFF)<<0);
                p+=4;
            }
        }

        for(i=0;i<BONUS_SIZE;i++){
            m_bonusGet[i]=(w[p]&0xFF);
            p++;
            m_bonusNew[i]=(w[p]&0xFF);
            p++;
        }

        //ver1.5.0
        m_isMute=(w[p]&0xFF);
        p++;
        m_isShare=(w[p]&0xFF);
        p++;

//--    変換終了        --------------------------------------//

        //レコードストアを閉じる
        rs.closeRecordStore();

    //例外処理
    }catch(Exception e){
        System.out.println(e.toString());
        try{
            if( rs!=null ) rs.closeRecordStore();
        }catch(Exception e2){}
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    saveRecordStore()
    概要:    レコードストアに書き込む関数
    機能:    -
    戻り値:  -
    引数:    -
*/
private synchronized void saveRecordStore() {
    RecordStore rs=null;
    byte[] w=new byte[RS_SIZE];
    int p=0;
    int i,j,k;

    try{
        //レコードストアを開く
        rs=RecordStore.openRecordStore(RS_GAMEDATA,true);

//--    書込変換        --------------------------------------//

        //書込開始
        for(k=0;k<4;k++){
            w[p+k]=(byte)((m_playTime>>(24-8*k))&0xFF);
        }
        p+=4;
        for(j=0;j<STAGE_SIZE;j++){
            for(i=0;i<HISCORE_SIZE;i++){
                for(k=0;k<4;k++){
                    w[p+k]=(byte)((m_hiScore[j][i]>>(24-8*k))&0xFF);
                }
                p+=4;

                for(k=0;k<4;k++){
                    w[p+k]=(byte)((m_hiCombo[j][i]>>(24-8*k))&0xFF);
                }
                p+=4;
            }
        }
        for(i=0;i<BONUS_SIZE;i++){
                w[p]=(byte)(m_bonusGet[i]&0xFF);
                p++;
                w[p]=(byte)(m_bonusNew[i]&0xFF);
                p++;
        }

        //ver1.5.0
        w[p]=(byte)(m_isMute&0xFF);
        p++;
        w[p]=(byte)(m_isShare&0xFF);
        p++;


//--    変換終了        --------------------------------------//
        //レコードストアに書き込み
        if( rs.getNumRecords()==0 ){
            rs.addRecord(w,0,p);
        }else{
            rs.setRecord(1,w,0,p);
        }

        //レコードストアを閉じる
        rs.closeRecordStore();

    //例外処理
    }catch(Exception e){
System.out.println(e.toString());
        try{
            if( rs!=null ) rs.closeRecordStore();
        }catch(Exception e2){}
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    initSound()
    概要:    音の準備をする関数
    機能:    -
    戻り値:  -
    引数:    -
*/
private void initSound(){
    try{
        Phrase cursor    =new Phrase("resource:bonus1.spf");
        Phrase jump        =new Phrase("resource:jump.spf");
        Phrase bg1        =new Phrase("resource:bg1.spf");
        Phrase bg2        =new Phrase("resource:bg2.spf");

        tr1.setPhrase(bg1);
        tr2.setPhrase(bg2);
        tr3.setPhrase(jump);
        tr4.setPhrase(cursor);
        tr2.setSubjectTo(tr1);
    }catch(Exception e){
System.out.println(e.toString());
    }
}
private void setTitleSound(){
    try{
        tr3.stop();
        tr4.stop();
        tr3.removePhrase();
        tr4.removePhrase();

        Phrase enter    =new Phrase("resource:bonus1.spf");
        Phrase cursor    =new Phrase("resource:cursor.spf");

        tr3.setPhrase(enter);
        tr4.setPhrase(cursor);
    }catch(Exception e){
System.out.println(e.toString());
    }
}
private void setStageSound(){
    try{
        tr3.stop();
        tr4.stop();
        tr3.removePhrase();
        tr4.removePhrase();

        Phrase jump    =new Phrase("resource:jump.spf");
        Phrase cursor    =new Phrase("resource:cursor.spf");

        tr3.setPhrase(jump);
        tr4.setPhrase(cursor);
    }catch(Exception e){
System.out.println(e.toString());
    }
}

private void playEnter() {
    if( m_isMute==1 ){
        return;
    }
    try{
        tr3.stop();
        tr3.play();
    }catch(Exception e){
System.out.println(e.toString());
    }
}
private void playCursor() {
    if( m_isMute==1 ){
        return;
    }
    try{
        tr4.stop();
        tr4.play();
    }catch(Exception e){
System.out.println(e.toString());
    }
}
private void playBgm() {
    if( m_isMute==1 ){
        return;
    }
    try{
        tr1.play(0);
    }catch(Exception e){
System.out.println(e.toString());
    }
}
private void stopBgm() {
    try{
        tr1.stop();
    }catch(Exception e){
System.out.println(e.toString());
    }
}
private void playBonus() {
    if( m_isMute==1 ){
        return;
    }
    try{
        tr4.stop();
        tr4.removePhrase();
        Phrase bonus=new Phrase("resource:bonus"+m_slotState+".spf");
        tr4.setPhrase(bonus);
        tr4.play();
    }catch(Exception e){
System.out.println(e.toString());
    }
}

////////////////////////////////////////////////////////////////
/**
    名前:    switchMute()
    概要:    ミュートをONする関数
    機能:    -
    戻り値:  -
    引数:    -
*/
private void switchMute(){
    //offからon
    if( m_isMute==0 ){
        removeCommand(onSoundCmd);
        addCommand(offSoundCmd);
        m_isMute=1;
        stopBgm();
    //offからon
    }else{
        removeCommand(offSoundCmd);
        addCommand(onSoundCmd);
        m_isMute=0;
        //BGM再生
        switch( m_modeState ){
            case GAME_START:
            case GAME_END:
            case GAME_PLAYING:
                playBgm();
                break;
        }
    }
}

////////////////////////////////////////////////////////////////
}//end of class
