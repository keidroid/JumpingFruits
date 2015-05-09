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

/** �萔 */
final static int		KEY_NO			=0x00000000,
						KEY_0			=0x00000001,
						KEY_1			=0x00000002,
						KEY_2			=0x00000004,
						KEY_3			=0x00000008,
						KEY_4			=0x00000010,
						KEY_5			=0x00000020,
						KEY_6			=0x00000040,
						KEY_7			=0x00000080,
						KEY_8			=0x00000100,
						KEY_9			=0x00000200,
						KEY_ASTERISK	=0x00000400,
						KEY_POUND		=0x00000800,
						KEY_UP			=0x00001000,
						KEY_LEFT		=0x00002000,
						KEY_RIGHT		=0x00004000,
						KEY_DOWN		=0x00008000,
						KEY_SELECT		=0x00010000,

						SCREEN_WIDTH	=240,			//�X�N���[���̕�
						SCREEN_HEIGHT	=240,			//�X�N���[���̍���
						FOODER_HEIGHT	=32,			//�t�b�_�̍���
						PLAYING_HEIGHT	=208,
						NUM_WIDTH		=14,			//�����̕�
						NUM_HEIGHT		=11,			//�����̍���
						CAL_WIDTH		=8,				//�����̕�
						CAL_HEIGHT		=10,			//�����̍���
						F_WIDTH			=9,				//�����̕�
						F_HEIGHT		=7,				//�����̍���

						NOW_LOADING		=0,
						TITLE			=1,
						MODE_SELECT		=2,
						GAME_START		=4,
						GAME_PLAYING	=5,
						GAME_END		=6,
						GAME_RESULT		=7,
						RECORD			=8,
						HELP			=9,
						BONUS			=10,
						ENDING			=11,

						SLEEP_TIME		=70,			//�X���[�v�^�C��
						MAX_SCORE		=9999999,
						MAX_COMBO		=99,

						COMBO_TIME		=4,
						TIME_LIMIT		=112,//20,
						CURSOR_TIME		=2,

						HISCORE_SIZE	=5,
						BONUS_SIZE		=12,
						STAGE_SIZE		=3,
						RS_SIZE			=256,			//���R�[�h�X�g�A
						MODE_SIZE		=3,

						STOP			=0,
						WALK			=1,
						JUMP			=2,
						WALK_SPEED		=8,
						JUMP_HEIGHT		=22,
						MYCHARA_WIDTH	=24,
						MYCHARA_HEIGHT	=24,

						GROUND_TOP		=93,
						GROUND_MIDDLE	=141,
						GROUND_BOTTOM	=189,
						GROUND_WIDTH	=96,
						GROUND_HEIGHT	=48,

						APPLE_SIZE		=6,				//�o�Ă���t���[�c�̑傫��
						NONE			=-1,
						APPLE			=0,
						ORANGE			=1,
						LEMON			=2,
						SPECIAL			=3,

						BONUS_SCORE		=2000,
						BONUS_TIME		=8,
						BONUS_APPEAR	=16,

						SLOT_TIME		=20,
						SLOT_BONUS		=0,
						TIME_BONUS		=1,
						GET_ITEM		=2,

						IMAGE_MYCHARA	=0,			//ImageIndex
						IMAGE_BG		=1,
						IMAGE_GROUND3	=2,
						IMAGE_GROUND2	=3,
						IMAGE_GROUND1	=4,
						IMAGE_FOODER	=5,
						IMAGE_APPLE		=6,
						IMAGE_BONUS		=7,
						IMAGE_LANG		=8,
						IMAGE_NUM		=9,
						IMAGE_LANG_FD	=10,
						IMAGE_JUMP		=11,
						IMAGE_SHINE		=12,
						IMAGE_PRESENTS	=13,
						IMAGE_STAGE		=14,
						IMAGE_GAME		=15,
						IMAGE_SLOT		=16,
						IMAGE_BONUS2	=17,
						IMAGE_PRESS		=18,
						IMAGE_NUMB		=19,
						IMAGE_NEKO		=20,
						IMAGE_SIZE		=21,			//Image�T�C�Y

						IMAGE_STOP		=0,
						IMAGE_WALK1		=2,
						IMAGE_WALK2		=1,

						L_TITLE1		=0,				//����
						L_TITLE2		=1,
						L_START			=2,
						L_PAUSE			=3,
						L_TIME_UP		=4,
						L_RECORD		=5,
						L_ITEM			=6,
						L_HELP			=7,
						L_STAGE			=8,
						N_SLASH			=10,
						N_PLUS			=11,
						N_LEFT			=12,
						N_RIGHT			=13,
						F_SCORE			=0,
						F_TIME			=1,
						F_COMBO			=2,
						F_SLOT			=3,

						FINAL_END		=-1;			//�錾�I��

final static String		RS_GAMEDATA		="A";

final static int[]		GROUND_TOP1X	={ 241,  12, 208},
						GROUND_TOP2X	={ 241, 241, -64},
						GROUND_MIDDLE1X	={ 241,  48, -32},
						GROUND_MIDDLE2X	={ 241, 241, 176};

final static String[]	MODE_HELP		=	{
													"�w�n�C�X�R�A��\�����܂��x","�w�擾�����A�C�e����\�����܂��x","�w�w���v��\�����܂��x"
											};

final static String[]	MENU_TEXT		=	{
													"�Q�[���ĊJ","�^�C�g���ɖ߂�"
											};

final static String[][]	HELP_TEXT		=	{	{
													"���͂��߂�",
													"Jumping Fruits�͂Ђ悱�𑀍삵��",
													"�t���[�c���L���b�`����Q�[���ł��B",
													"",
													"�t���[�c���L���b�`����ƃX�R�A��",
													"���Z����܂��B�������Ԃ��O�ɂȂ��",
													"�Q�[���I�[�o�[�B�S�R�X�e�[�W�ł��B"
												},{
													"��������@",
													"����: �ړ�",
													"����: �W�����v",
													" �� : �~���",
													"",
													"�����L�[�͂Q,�S,�U,�W�L�[,",
													"����L�[�͂T�L�[�ő�p�ł��܂��B"
												},{
													"���A���W�����v",
													"�t���[�c���L���b�`����Ƃ����",
													"�Ђ悱���W�����v���܂�",
													"�A���W�����v�̉񐔂ɂ���ĉ��Z����",
													"�链�_��������̂ŁA�A���W�����v��",
													"�����_��_���Ă��������I",
													""
												},{
													"���{�[�i�X�P�wSLOT BONUS�x",
													"�擾�����t���[�c�͍����̃X���b�g��",
													"�i�[����܂��B",
													"�����̃X���b�g���R���낤��",
													"�wSLOT BONUS�x�ƂȂ�A����ɍ����_",
													"�����Z����܂��B",
													""
												},{
													"���{�[�i�X�Q�wTIME BONUS�x",
													"�����̃X���b�g���R�Ⴄ�ꍇ�́A",
													"�wTIME BONUS�x�ƂȂ�܂��B",
													"�������Ԃ��������т�̂ŃX���b�g��",
													"���Ɋ��p���Ă��������B",
													"",
													""
												},{
													"���{�[�i�X�R�wGET ITEM�x",
													"��������𖞂����ƕςȂ��̂�������",
													"���܂��B�����ɃL���b�`����ƈꖜ�_",
													"���Z����܂��B",
													"�擾�����A�C�e����ITEM LIST����",
													"�{���ł��܂��B",
													"�ڎw���S�A�C�e���R���v���[�g�I"
												},{
													"����������",
													"�wJumping Fruits�x��DL���Ē�����",
													"���肪�Ƃ��������܂����B",
													"���ӌ��E�v�]��Web�T�C�g�ւǂ����B",
													"",
													"Copyright by bluesystem��",
													"          All right reserved 2006."
												}};
final static String[][]	ITEM_TEXT		=	{	{
													"�Â����ȃo�i�i�B�t�B���s���Y�B",
													"�̂͋M�d�i�������������B"
												},{
													"�オ���т��p�C�i�b�v���B",
													"�썑�o�g�ŕ��i������܂��B�������B"
												},{
													"�����i�̑�\�A�������B",
													"���܂Ƀn�����̂��Ă��邪����͌��B"
												},{
													"�������ځB���g�������ς��l�܂���",
													"�Â��Â���ł��B�������I"
												},{
													"���E�t�����X�B���܂ɃK���ŐH�ׂ�",
													"���x�Ȃ�ł悭�m��܂��񂪁c�B"
												},{
													"�������Â��݂���B�ӂ��݂̂���",
													"�Ɣ����ɐF���Ⴂ�܂��B"
												},{
													"�w���̂�����x�Ƃ����X�i�b�N�َq�B",
													"�Ђ悱�E�̍��N��Ԃ̃q�b�g���i�B"
												},{
													"�w�K���f���x�̃v�����̂̂��c�B",
													"���̌`�͂����ƕ��҃K���f�����c�B"
												},{
													"�w���C�p�[�_�b�V�����[�^�[�x���I",
													"�R�[�X�A�E�g�ɋC�����悤�B"
												},{
													"���܂��Ől�C�́w�Q�[���{�E�B�x�B",
													"���Ȃ���60�b�ŉ��_�Ƃ�c(�ȉ���)"
												},{
													"�`���́w�������x���c�B",
													"���W�߂Ă��肢�͊��������ɂȂ��B"
												},{
													"�w��҂���̎莆�x",
													"����L�[�������ĉ�����(*^-')�m"	
												}};
final static String[]	ITEM_NO			=	{		"���̃A�C�e���͂܂�",
													"              ��ɓ���Ă��܂���B"};
final static String[]	END_TEXT		=	{
													"��Thank You For Playing!!��",
													"�S�A�C�e���R���v���[�g",
													"�{���ɂ��߂łƂ��������܂�!!",
													"�ꉞ���̃Q�[���͂����ŏI���ł��B",
													"�����l�ł��B���񂽂͂������I",
													"�����ɂ����҂�������(*'-')�m"
											};

/** ���C���I�u�W�F�N�g */
private JumpingFruits	jumpingFruits;

/** �C���[�W */
private Image[]			img				=new Image[IMAGE_SIZE];

private Image			logoImg;

/** �_�u���o�b�t�@�����O */
private Image 			mainImg;			//�I�t�C���[�W
private Graphics		g;					//�I�t�O���t�B�N�X

/** �w�i�摜 */
private Image 			imgBg=Image.createImage(SCREEN_WIDTH,SCREEN_HEIGHT-FOODER_HEIGHT);				//�w�i�摜
private Image 			imgFd=Image.createImage(SCREEN_WIDTH,FOODER_HEIGHT);	

/**�O���t�B�b�N���[�e�B���e�B*/
private GraphicsUtil	gUtil;

/** �f�o�C�X�R���g���[�� */
private DeviceControl	devCtl = DeviceControl.getDefaultDeviceControl();

/** �X�g�����O�o�b�t�@ */
private StringBuffer	Sb				=new StringBuffer();
private String			drawSt			=null;

/** �L�[�C�x���g */
private int 			m_event			=KEY_NO;
/** �L�[��� */
private int 			key				=KEY_NO;

/** �t�H���g��ێ�����ϐ� */
private Font			f				=Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
private int 			stHeight		=f.getHeight();

/** ���� */
private	Random			r				=new Random();

/** �R�}���h�ϐ� */
//private Command 		noneCmd			=new Command("",		Command.SCREEN,1);
private Command 		exitCmd			=new Command("�I��",	Command.SCREEN,2);
private Command 		pauseCmd		=new Command("���f",	Command.SCREEN,3);
private Command 		resumeCmd		=new Command("�ĊJ",	Command.SCREEN,4);
private Command 		backCmd			=new Command("�߂�",	Command.SCREEN,5);

//ver1.2.0
private Command 		onSoundCmd		=new Command("��",	Command.SCREEN,1);
private Command 		offSoundCmd		=new Command("��~",	Command.SCREEN,1);

/** �V�X�e���ϐ� */
private long			m_sleepTime		=0;	//�X���[�v�^�C��
private boolean			m_isPause		=false;
private boolean			m_isDrawFooder	=false;
private boolean			m_isDrawBg		=false;
private int				m_hcenter		=getWidth()/2;
private int				m_vcenter		=getHeight()/2;
private int				m_load			=0;
private int				m_cursor		=0;
private int				m_cursorTime	=0;

/** �Q�[���ϐ� */
private int				m_modeState		=NOW_LOADING;
private int				m_nextState		=NOW_LOADING;
private int				m_time			=0;
private int				m_lv			=0;
private int				m_score			=0;
private int				m_combo			=0;
private int				m_comboTime		=0;
private int				m_comboMax		=0;
private int				m_stage			=0;
private int				m_option		=0;
private int				m_teropX		=0;
private int				m_slot[]		={NONE,NONE,NONE};
private int				m_slotTime		=0;
private int				m_slotState		=0;
private boolean			m_isAppearBonus	=false;
private int				m_bonusIndex	=0;
private boolean			m_isBonusGet	=false;
private int				m_hiScore[][]	={	{	0,	0,	0,	0,	0},
											{	0,	0,	0,	0,	0},
											{	0,	0,	0,	0,	0}};
private int				m_hiCombo[][]	={	{	0,	0,	0,	0,	0},
											{	0,	0,	0,	0,	0},
											{	0,	0,	0,	0,	0}};

private int				m_bonusGet[]	=	{	0,	0,	0,	0,	0,	0,
												0,	0,	0,	0,	0,	0};
private int				m_bonusNew[]	=	{	0,	0,	0,	0,	0,	0,
												0,	0,	0,	0,	0,	0};
private int				m_playTime		=0;


/** �L�����N�^�ϐ� */
private int 			m_myX			=120;
private int 			m_myY			=GROUND_BOTTOM;
private boolean			m_isLeft		=false;
private int				m_myState		=STOP;
private int 			m_addY			=0;
private int				m_walkCount		=0;
private int				m_imgIndex		=IMAGE_STOP;
private int				m_speed			=0;
private int				m_ground		=GROUND_BOTTOM;
private int				m_hiScoreNum	=-1;

/** �t���[�c�ϐ� */
private int[]			m_appleState	={NONE,NONE,NONE,NONE,NONE,NONE};
private int[]			m_appleX		={0,0,0,0,0,0};
private int[]			m_appleY		={0,0,0,0,0,0};
private int[]			m_appleSpeedX	={4,4,4,4,4,4};
private int[]			m_appleSpeedY	={4,4,4,4,4,4};
private boolean[]		m_appleIsLeft	={false,false,false,false,false,false};

//ver1.1.0
private int				m_effX[]		={0,0,0};
private int				m_effY[]		={0,0,0};
private int				m_effTime[]		={0,0,0};

//debug
//private Runtime			runtime=Runtime.getRuntime();

//ver1.2.0 sound
/** �t���[�Y�v���C���[ */
static PhrasePlayer		phrasePlayer = null;
//�g���b�N
static PhraseTrack		tr1 = null;
static PhraseTrack		tr2 = null;
static PhraseTrack		tr3 = null;
static PhraseTrack		tr4 = null;

//ver1.5.0
private int				m_isMute		=0;
private int				m_isShare		=0;

////////////////////////////////////////////////////////////////
/**
	���O:	DrawCanvas(JumpingFruits jumpingFruits)
	�T�v:	�R���X�g���N�^
	�@�\:	-
	�߂�l:	-
	����:	-
*/
DrawCanvas(JumpingFruits jumpingFruits) {

	//���C���I�u�W�F�N�g�쐬
	this.jumpingFruits = jumpingFruits;

	//�R�}���h���X�i�[�o�^
	setCommandListener(this);

	//�_�u���o�b�t�@�����O
	mainImg=Image.createImage(SCREEN_WIDTH,SCREEN_HEIGHT);
	g=mainImg.getGraphics();

	g.setFont(f);

	//�X���b�h�J�n
	new Thread(this).start();
}


////////////////////////////////////////////////////////////////
/**
	���O:	resumeExe()
	�T�v:	�ꎞ��~���̏���
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public void resumeExe() {
	m_isDrawBg=true;
	if( m_modeState==GAME_PLAYING && !m_isPause ){
		onMenu();
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	run()
	�T�v:	�X���b�h�J�n���ɌĂ΂�郁�\�b�h
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public void run(){

	int i=0;

//--	�X�^�[�g�A�b�v	--------------------------------------//

	//���S�擾
	try {
		logoImg = Image.createImage("/logo.png");
	}catch(Exception e) {}

	//�w�i�`����Z�b�g
	m_isDrawBg=true;

//--	Now Loading		--------------------------------------//

	//�X���[�v������
	m_sleepTime = System.currentTimeMillis();

	drawNowloading();
	repaint();
	//serviceRepaints();

	//�摜�擾
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
		//�g���b�N
		tr1 = phrasePlayer.getTrack(0);
		tr2 = phrasePlayer.getTrack(1);
		tr3 = phrasePlayer.getTrack(2);
		tr4 = phrasePlayer.getTrack(3);
	}catch(Exception e){
System.out.println(e.toString());
	}
	//�T�E���h
	initSound();

	m_load=100;
	//�Ō�ɕ`��
	drawNowloading();
	repaint();

	try{
		Thread.sleep(100);
	}catch(Exception e){
System.out.println(e.toString());
	}
	loadRecordStore();

	//�^�C�g����ʂɑJ��
	m_nextState=TITLE;

	//�����̃\�t�g�L�[�͎g�p���Ȃ�
	//addCommand(noneCmd);
	//ver1.2.0 �T�E���h�R�}���h
	if( m_isMute==0 ){
		addCommand(onSoundCmd);
	}else{
		addCommand(offSoundCmd);
	}

//--	���C�����[�v	--------------------------------------//

	try{
		while(true){
//--	��������		--------------------------------------//

			if( m_modeState!=m_nextState ){

				//��Ԃ𔲂���
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

				//��Ԃɓ���
				switch( m_nextState ){
					case TITLE:
						//�R�}���h�ǉ�
						addCommand(exitCmd);
						initGame();
						initTitle();
						m_stage=0;//�X�e�[�W������
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
						initGame();//�Q�[��������
						getBonusIndex();//�{�[�i�X����
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
							//���߂Ď������V�����A�C�e��
							if( m_bonusGet[m_bonusIndex]==0 )
								m_bonusNew[m_bonusIndex]=1;
							m_bonusGet[m_bonusIndex]=1;
						}
						break;

					case GAME_RESULT:
						m_playTime++;
						//���݃R���{�}�b�N�X
						if( m_combo>m_comboMax ){
							m_comboMax=m_combo;
						}
						sortHiscore();
						saveRecordStore();
						break;
				}
				//������Ԃ�����
				m_modeState=m_nextState;

				m_event=KEY_NO;	//�L�[�C�x���g������
				key=KEY_NO;		//�L�[��ԏ�����

			}

//--	�L�[			--------------------------------------//

			//�L�[��Ԃ̎擾
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

			//�L�[�C�x���g������
			m_event=KEY_NO;
			//�L�[��ԏ�����
			key=KEY_NO;


//--	�`��			--------------------------------------//
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

//--	����			--------------------------------------//

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
// �g�� ver1.1.0
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

//--	�X���[�v		--------------------------------------//
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
	���O:	paint()
	�T�v:	���C���`�揈��
	�@�\:	�I�[�o�[���C�h
	�߂�l:	-
	����:	Graphics g	�O���t�B�b�N�I�u�W�F�N�g
*/
public synchronized void paint(Graphics g2){

	int i=0;

	try{
		//��������
		if( m_isDrawBg ){
			g2.setColor(0,0,0);
			g2.fillRect(0,0,getWidth(),getHeight());
			m_isDrawBg=false;
		}

		//�I�t�C���[�W��`��
		g2.drawImage(mainImg, m_hcenter-120, m_vcenter-120, Graphics.LEFT|Graphics.TOP);

	}catch(Exception e){
System.out.println(e.toString());
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	commandAction(Command c, Displayable s)
	�T�v:	�R�}���h�̃C�x���g
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public synchronized void commandAction(Command c, Displayable s) {
	if( c == exitCmd ){//Exit�R�}���h
		//�I������
		jumpingFruits.doExit();
	}
	else if( c==pauseCmd ){//Pause�R�}���h
		onMenu();
	}
	else if( c==resumeCmd ){//Resume�R�}���h
		offMenu();
	}
	else if( c==backCmd ){
		switch( m_modeState ){
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
//playCursor();
	}
	//ver1.2.0
	else if( c==onSoundCmd ){//�􁛃R�}���h
		switchMute();
	}
	else if( c==offSoundCmd ){//��~�R�}���h
		switchMute();
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	keyPressed()
	�T�v:	�L�[�����C�x���g
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public synchronized void keyPressed(int keyCode) {

	int	gameCode=getGameAction(keyCode);

	switch( gameCode ) {
		case UP: //��L�[������
			m_event=KEY_UP;
			return;
		case DOWN: //���L�[������
			m_event=KEY_DOWN;
			return;
		case LEFT: //���L�[������
			m_event=KEY_LEFT;
			return;
		case RIGHT: //�E�L�[������
			m_event=KEY_RIGHT;
			return;
		case FIRE: //�ǉ� ����
			m_event=KEY_SELECT;
			return;
	}

	switch( keyCode ){
		case KEY_NUM2: //2�L�[������
			m_event=KEY_2;
			return;
		case KEY_NUM4: //4�L�[������
			m_event=KEY_4;
			return;
		case KEY_NUM5: //5�L�[������
			m_event=KEY_5;
			return;
		case KEY_NUM6: //6�L�[������
			m_event=KEY_6;
			return;
		case KEY_NUM8: //8�L�[������
			m_event=KEY_8;
			return;
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	drawNowloading()
	�T�v:	Now Loading�`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawNowloading(){
	try{
		//�w�i:��
		g.setColor(255,255,255);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		//bluesystem��
		g.drawImage(logoImg, 20, 100, Graphics.TOP|Graphics.LEFT);

		//�v���O���X�o�[
		g.setColor(216,216,216);
		g.fillRect(20, 220, 2*100, 2);
		g.setColor(192,192,255);
		g.fillRect(20, 220, 2*m_load, 2);

		//�����\��
		Sb.delete(0,Sb.length());
		drawSt = Sb.append(m_load).append(" ��").toString();
		g.setColor(160,160,160);
		g.drawString(drawSt,220-f.stringWidth(drawSt),210,Graphics.BASELINE|Graphics.LEFT);

	}catch(Exception e){
System.out.println(e.toString());
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	drawTitle()
	�T�v:	�^�C�g����ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawTitle(){
	try{
		//�w�i
		g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
		//�n��
		g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

		//�����S
		for(int i=0; i<APPLE_SIZE; i++){
			if( m_appleState[i]!=NONE && m_appleState[i]!=SPECIAL ){
				gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
			}
		}


		//�L�����N�^
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
	���O:	drawModeSelect()
	�T�v:	���[�h�Z���N�g��ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawModeSelect(){
	try{
		//�w�i
		g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
		//�n��
		g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

		//�����S
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

		//�t�b�_
		g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
		if( m_cursor==0 ){
			drawSt="�w�Q�[�����J�n���܂��x";
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
	���O:	drawRecord()
	�T�v:	���R�[�h��ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawRecord(){
	try{
		//�w�i�摜
		g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);
/*
		//�w�i
		g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
		//�n��
		g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
*/
		//�����S
		for(int i=0; i<APPLE_SIZE; i++){
			if( m_appleState[i]!=NONE ){
				gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
			}
		}

		//�����\��
		//gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_STAGE, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 40, 20, Graphics.TOP|Graphics.LEFT);

		//RECORD
		gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_RECORD, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);

		//score,combo
		gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SCORE, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE,  96, 46, Graphics.TOP|Graphics.LEFT);
		gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_COMBO, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 176, 46, Graphics.TOP|Graphics.LEFT);


		//gUtil.drawRegion(g,img[IMAGE_STAGE], 32*m_cursor, 0,32, 32, GraphicsUtil.TRANS_NONE, 160, 20, Graphics.TOP|Graphics.LEFT);

		//�����\��
		for(int i=0;i<HISCORE_SIZE;i++){
			gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*(i+1), 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 20, 60+24*i, Graphics.TOP|Graphics.LEFT);
			gUtil.drawRegion(g,img[IMAGE_NUM], NUM_WIDTH*N_SLASH, 0, NUM_WIDTH, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 40, 60+24*i, Graphics.TOP|Graphics.LEFT);
			drawScore(m_hiScore[m_cursor][i],160,60+24*i,8);
			drawScore(m_hiCombo[m_cursor][i],200,60+24*i,3);
			//drawCal(m_calendar[m_cursor][i], 200,62+24*i);
		}
/*
		Sb.delete(0,Sb.length());
		drawSt = Sb.append("playTime: ").append(m_playTime).toString();
		g.drawString(drawSt,60,200,Graphics.BASELINE|Graphics.LEFT);
*/
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
	���O:	drawHelp()
	�T�v:	�w���v��ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawHelp(){
	try{
		//�w�i
		g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
		//�n��
		g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

		//�����S
		for(int i=0; i<APPLE_SIZE; i++){
			if( m_appleState[i]!=NONE ){
				gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
			}
		}

		//HELP
		gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_HELP, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);

		g.setColor(64,32,0);//���F
		g.fillRect(10,40,220,160);

		g.setColor(128,64,0);
		g.drawRect(10,40,220,160);

		g.setColor(255,255,255);
		for(int i=0;i<HELP_TEXT[m_cursor].length; i++ ){
			g.setColor(128,64,0);//���F
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
	���O:	drawBonus()
	�T�v:	�w���v��ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawBonus(){
	try{
		//�w�i
		g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
		//�n��
		g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);

		//�����S
		for(int i=0; i<APPLE_SIZE; i++){
			if( m_appleState[i]!=NONE ){
				gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE,
					 m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
			}
		}

		//ITEM LIST
		gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_ITEM, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);

		//�A�C�e��
		for(int j=0; j<3; j++){
			for(int i=0; i<4; i++){
				if( m_cursor==(j*4+i) ){
					g.setColor(255-m_time*16,255-m_time*16,255-m_time*16);//�I�����W
					g.drawArc((40+48*i)-3-m_time, (40+36*j)-3-m_time, 29+m_time*2,29+m_time*2, 0,360);
				}else{
					g.setColor(255,255,255);//��
					g.fillArc(40+48*i, 40+36*j, 24,24, 0,360);
				}

				if( m_bonusGet[j*4+i]!=0 ){
					if( m_cursor==(j*4+i) ){
						//�g��
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

		//�E�B���h�E
		g.setColor(64,32,0);//���F
		g.fillRect(10,150,220,50);

		g.setColor(128,64,0);
		g.drawRect(10,150,220,50);

		g.setColor(255,255,255);
		for(int i=0;i<ITEM_TEXT[m_cursor].length; i++ ){
			if( m_bonusGet[m_cursor]!=0 ){
				g.setColor(128,64,0);//���F
				g.drawString(ITEM_TEXT[m_cursor][i],20+1,(170+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
				g.setColor(255,255,255);
				g.drawString(ITEM_TEXT[m_cursor][i],20,170+20*i,Graphics.BASELINE|Graphics.LEFT);
			}else{
				g.setColor(128,64,0);//���F
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
	���O:	drawEnding()
	�T�v:	�^�C�g����ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawEnding(){
	try{
		//�w�i
		g.drawImage(img[IMAGE_BG], 0, 0, Graphics.TOP|Graphics.LEFT);
		//�n��
		//g.drawImage(img[IMAGE_GROUND3], 0, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
		
		//gUtil.drawRegion(g,img[IMAGE_GROUND3], m_teropX, 0, 240-m_teropX, 19, GraphicsUtil.TRANS_NONE, 0-m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
		//gUtil.drawRegion(g,img[IMAGE_GROUND3], m_teropX, 0, 240-m_teropX, 19, GraphicsUtil.TRANS_NONE, 240-m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
		g.drawImage(img[IMAGE_GROUND3], 0-2*m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
		g.drawImage(img[IMAGE_GROUND3], 240-2*m_teropX, GROUND_BOTTOM, Graphics.TOP|Graphics.LEFT);
		
		//�����S
		for(int i=0; i<APPLE_SIZE; i++){
			if( m_appleState[i]!=NONE && m_appleState[i]!=SPECIAL ){
				gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
			}
		}

		for(int i=0;i<END_TEXT.length; i++ ){
			g.setColor(128,64,0);//���F
			g.drawString(END_TEXT[i],120-f.stringWidth(END_TEXT[i])/2,(32+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
			g.setColor(255,255,255);
			g.drawString(END_TEXT[i],120-f.stringWidth(END_TEXT[i])/2,32+20*i,Graphics.BASELINE|Graphics.LEFT);
		}

		//�ʂ�
		if( (m_time%4)<2 ){
			g.drawImage(img[IMAGE_NEKO], -240+4*m_teropX, 140+(m_time%4), Graphics.TOP|Graphics.LEFT);
		}else{
			g.drawImage(img[IMAGE_NEKO], -240+4*m_teropX, 144-(m_time%4), Graphics.TOP|Graphics.LEFT);
		}

		//�L�����N�^
		getImgIndex();
		//for(int i=-1;i<10;i++)
		//	gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, i*24+2*m_teropX, m_myY-24, Graphics.TOP|Graphics.LEFT);
		gUtil.drawRegion(g,img[IMAGE_MYCHARA], MYCHARA_WIDTH*m_imgIndex, 0, MYCHARA_WIDTH, MYCHARA_HEIGHT, GraphicsUtil.TRANS_NONE, -24+6*m_teropX, m_myY-24, Graphics.TOP|Graphics.LEFT);

		g.drawImage(img[IMAGE_FOODER], 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);
		drawSt="�w��҂���̎莆�x";
		g.setColor(255,255,255);
		g.drawString(drawSt,120-f.stringWidth(drawSt)/2,230,Graphics.BASELINE|Graphics.LEFT);

	}catch(Exception e){
System.out.println(e.toString());
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	drawGameStart()
	�T�v:	�Q�[���X�^�[�g��ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawGameStart(){
	try{
		if( m_time<10 ){
			m_speed-=4;
		}else{
			m_speed+=5;
		}
		m_teropX-=m_speed;

		//�w�i�摜
		g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);

		//�L�����N�^
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
	���O:	drawPlaying()
	�T�v:	�Q�[�����`�揈��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawPlaying(){
	int i=0;

	try{
		//�w�i�摜
		g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);


		//�g��
		for(i=0;i<3;i++){
			if( m_effTime[i]>0 ){
				gUtil.drawRegion(g,img[IMAGE_SHINE], 32*(m_effTime[i]-1), 0, 32, 32, GraphicsUtil.TRANS_NONE, m_effX[i]-8, m_effY[i]-24, Graphics.TOP|Graphics.LEFT);
			}
		}

		//�����S
		for(i=0; i<APPLE_SIZE; i++){
			if( m_appleState[i]==SPECIAL )
				//BONUS1
				gUtil.drawRegion(g,img[IMAGE_BONUS], 16*m_bonusIndex, 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
			else if( m_appleState[i]!=NONE )
				gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_appleState[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, m_appleX[i], m_appleY[i]-16, Graphics.TOP|Graphics.LEFT);
		}

		//�X���b�g
		if( m_slotTime>0 ){
			drawSlot();
		}

		//�R���{
		if( m_combo>0 ){
			//HOP,STEP,JUMP
			if( m_combo==1 )
				gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*0, 45, 9, GraphicsUtil.TRANS_NONE, m_myX-8, m_myY-35, Graphics.TOP|Graphics.LEFT);
			else if( m_combo==2 )
				gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*1, 45, 9, GraphicsUtil.TRANS_NONE, m_myX-8, m_myY-35, Graphics.TOP|Graphics.LEFT);
			else
				gUtil.drawRegion(g,img[IMAGE_JUMP], 0, 9*2, 45, 9, GraphicsUtil.TRANS_NONE, m_myX-8, m_myY-35, Graphics.TOP|Graphics.LEFT);

			//�R���{
			if( m_comboTime==0 )
				drawScore(m_combo,228-NUM_WIDTH,16,2);
			else
				drawScoreB(m_combo,228-NUM_WIDTH*2,16,2);//��
			gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_COMBO, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 228-44, 4, Graphics.TOP|Graphics.LEFT);
		}

//ver1.1.0
		if( m_myY<0 ){
			g.setColor(255,255-m_time*16,255);
			g.drawString("��",m_myX+4,16,Graphics.BASELINE|Graphics.LEFT);
		}else{
		//�L�����N�^
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
	���O:	drawFooder()
	�T�v:	�Q�[�����`�揈��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawFooder(){

	//�w�i
	g.drawImage(imgFd, 0, SCREEN_HEIGHT-FOODER_HEIGHT, Graphics.TOP|Graphics.LEFT);

	//�X�R�A
	drawScore(m_score,221,214,8);

	//���ԃo�[
	g.setColor(255,255,255);
	g.fillRect(121,233,m_lv,3);

	//�X���b�g�g
	g.setColor(255,128+(m_lv%4)*24,0+(m_lv%4)*48);
	g.drawRect(8+20*((m_combo)%3), 220, 17, 17);

	//�X���b�g
	for(int i=0;i<3;i++){
		if( m_slot[i]!=NONE )
			gUtil.drawRegion(g,img[IMAGE_APPLE], 16*m_slot[i], 0, 16, 16, GraphicsUtil.TRANS_NONE, 9+20*i, 221, Graphics.TOP|Graphics.LEFT);
	}

	//�`��I��
	m_isDrawFooder=false;
}


////////////////////////////////////////////////////////////////
/**
	���O:	drawSlot()
	�T�v:	����̃X���b�g�`��
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	drawScore()
	�T�v:	�X�R�A�`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawScore(int score,int x,int y,int size){
	int tmp=score;
	int num=0;
	int i=0;

	//���_
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
	���O:	drawScoreB()
	�T�v:	�傫���X�R�A�`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawScoreB(int score,int x,int y,int size){
	int tmp=score;
	int num=0;
	int i=0;

	//���_
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
	���O:	drawCal()
	�T�v:	�����`��
	�@�\:	-
	�߂�l:	-
	����:	-

private void drawCal(int score,int x,int y){
	int tmp=score;
	int num=0;
	int i=0;

	//���_
	for(i=0;i<(2+1+2);i++){
		//�X���b�V��
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
	���O:	drawMenu()
	�T�v:	���j���[�`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawMenu(){

	//HELP
	gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_PAUSE, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 80, 80, Graphics.TOP|Graphics.LEFT);

	//�E�B���h�E
	g.setColor(64,32,0);//���F
	g.fillRect(10,100,220,50);

	g.setColor(128,64,0);
	g.drawRect(10,100,220,50);

	g.setColor(255,255,255);
	for(int i=0;i<MENU_TEXT.length; i++ ){
		g.setColor(128,64,0);//���F
		g.drawString(MENU_TEXT[i],120-f.stringWidth(MENU_TEXT[i])/2+1,(120+20*i)+1,Graphics.BASELINE|Graphics.LEFT);
		if( m_cursor==i ){
			g.setColor(255,255,255);
			g.drawString(MENU_TEXT[i],120-f.stringWidth(MENU_TEXT[i])/2,120+20*i,Graphics.BASELINE|Graphics.LEFT);
		}
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	drawGameEnd()
	�T�v:	�Q�[���I���`��
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	drawGameResult()
	�T�v:	�Q�[�����ʉ�ʕ`��
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void drawGameResult(){
	try{
		//�w�i�摜
		g.drawImage(imgBg, 0, 0, Graphics.TOP|Graphics.LEFT);

		//RECORD
		gUtil.drawRegion(g,img[IMAGE_LANG], 0, NUM_HEIGHT*L_RECORD, 116, NUM_HEIGHT, GraphicsUtil.TRANS_NONE, 62, 20, Graphics.TOP|Graphics.LEFT);
		//gUtil.drawRegion(g,img[IMAGE_STAGE], 32*m_stage, 0,32, 32, GraphicsUtil.TRANS_NONE, 160, 20, Graphics.TOP|Graphics.LEFT);

		//score,combo
		gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SCORE, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE,  96, 46, Graphics.TOP|Graphics.LEFT);
		gUtil.drawRegion(g,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_COMBO, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 176, 46, Graphics.TOP|Graphics.LEFT);

		//�����\��
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
	���O:	keyTitle()
	�T�v:	�^�C�g���L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public void keyTitle(){
	switch( m_event ){
		case KEY_5:
		case KEY_SELECT:
//playEnter();
			m_nextState=MODE_SELECT;
			break;
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	keyModeSelect()
	�T�v:	�^�C�g���L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	keyRecord()
	�T�v:	���R�[�h�L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
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
			//playCursor();
			break;
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	keyBonus()
	�T�v:	�{�[�i�X�A�C�e���L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
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
//playCursor();
			break;
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	keyHelp()
	�T�v:	�w���v�L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
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
//playCursor();
			break;
	}
}
////////////////////////////////////////////////////////////////
/**
	���O:	keyEnding()
	�T�v:	�G���f�B���O�L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void keyEnding(){
	switch( m_event ){
		case KEY_5:
		case KEY_SELECT:
			m_nextState=BONUS;
//playCursor();
			break;
	}
}

////////////////////////////////////////////////////////////////
/**
	���O:	keyGameResult()
	�T�v:	�Q�[�����ʃL�[����
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void keyGameResult(){
	switch( m_event ){
		case KEY_5:
		case KEY_SELECT:
			m_nextState=TITLE;
//playCursor();
			break;
	}
}



////////////////////////////////////////////////////////////////
/**
	���O:	keyPlaying()
	�T�v:	�Q�[�����L�[����
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public void keyPlaying(){

	//���L�[
	if ( (key&KEY_LEFT)!=0 || (key&KEY_4)!=0) {
		if( m_myState!=JUMP ){
			m_myState=WALK;
		}
		m_isLeft=true;
		m_speed=WALK_SPEED;
	}
	// �E�L�[
	else if ( (key&KEY_RIGHT)!=0 || (key&KEY_6)!=0) {
		if( m_myState!=JUMP ){
			m_myState=WALK;
		}
		m_isLeft=false;
		m_speed=WALK_SPEED;
	}
	//���E�E�������Ă��Ȃ�
	else{
		if( m_myState!=JUMP ){
			m_myState=STOP;
		}
		m_speed/=2;
	}

	// ���L�[
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
	//�W�����v�L�[
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
	���O:	onMenu(),offMenu()
	�T�v:	���j���[�؂�ւ�
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	keyMenu()
	�T�v:	���j���[��ʃL�[����
	�@�\:	-
	�߂�l:	-
	����:	-
*/
public void keyMenu(){
	switch( m_event ){

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
			switch( m_cursor ){
				//�ĊJ
				case 0:
					break;
				//�^�C�g��
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
	���O:	getImgIndex()
	�T�v:	�L�����N�^�̃C���[�W�ԍ��擾
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void getImgIndex(){

	switch( m_myState ){
		case STOP:
			m_imgIndex=IMAGE_STOP;
			break;

		case WALK:
			switch(m_walkCount%3){
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
	���O:	myAction()
	�T�v:	�L�����N�^�̏���
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void myAction(){

	//�Ƃ܂��Ă���Ό���
	if( m_myState==STOP ){
		m_walkCount=0;
	}else{
		m_walkCount++;
	}

	checkGround();

	//�L�����N�^�̈ړ�
	if(!m_isLeft){//�E
		m_myX+=m_speed;
		//�[
		if(m_myX>SCREEN_WIDTH-MYCHARA_WIDTH){
			m_myX=SCREEN_WIDTH-MYCHARA_WIDTH;
		}

	}else{//��
		m_myX-=m_speed;
		if(m_myX<0){
			m_myX=0;
		}
	}

	if(m_myY<m_ground){
		//�n�ʎ擾
		getGround();
		m_addY+=4;
		m_myY+=m_addY;
		if(m_myY>=m_ground){
			m_addY=0;
			m_myY=m_ground;

			if( m_combo>m_comboMax ){
				m_comboMax=m_combo;
			}
			m_combo=0;

			//�X���b�g������
			for(int i=0;i<m_slot.length;i++){
				m_slot[i]=NONE;
			}

			m_myState=STOP;
			m_speed=0;
			m_isDrawFooder=true;
		}
	}

	//�t���[�c�𓥂񂾂Ƃ�
	if( m_myState==JUMP ){
		for(int i=0;i<APPLE_SIZE; i++){
			if( m_myX+MYCHARA_WIDTH>m_appleX[i] && m_myX<m_appleX[i]+16 &&
				m_myY>m_appleY[i]-16 && m_myY<m_appleY[i]+16 && m_appleState[i]!=NONE )
			{
				//�W�����v
				m_addY=-JUMP_HEIGHT;
				//�R���{�ǉ�
				m_combo++;
				m_comboTime=COMBO_TIME;
				if( m_combo>MAX_COMBO ){
					m_combo=MAX_COMBO;
				}
				//�X�R�A�ǉ�
				m_score+=10*m_combo;
				if( m_score>MAX_SCORE ){
					m_score=MAX_SCORE;
				}

				//�X�y�V�����A�C�e��
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

				//�X���b�g�Ɋi�[����
				m_slot[(m_combo-1)%3]=m_appleState[i];
// �g�� ver1.1.0
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
	���O:	moveApple()
	�T�v:	�L�����N�^�̏���
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void moveApple(){
	int i=0;

	for(i=0; i<APPLE_SIZE; i++){
		if( m_appleState[i]!=NONE ){
			//�L�����N�^�̈ړ�
			if( !m_appleIsLeft[i] ){//�E
				m_appleX[i]+=m_appleSpeedX[i];
				//�[
				if( m_appleX[i]>SCREEN_WIDTH+MYCHARA_WIDTH ){
					m_appleState[i]=NONE;
				}

			}else{//��
				m_appleX[i]-=m_appleSpeedX[i];
				//�[
				if( m_appleX[i]<0-MYCHARA_WIDTH ){
					m_appleState[i]=NONE;
				}
			}

			//����
			m_appleSpeedY[i]+=4;
			m_appleY[i]+=m_appleSpeedY[i];

			//�Փ�
			if( m_appleY[i]>GROUND_BOTTOM ){
				m_appleSpeedY[i]=-28-(m_appleState[i]*4);
				m_appleY[i]=GROUND_BOTTOM;
			}
		}
	}//end of for(i=0; i<APPLE_SIZE; i++)
}


////////////////////////////////////////////////////////////////
/**
	���O:	appearApple()
	�T�v:	��񂲂̏o��
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	getGround()
	�T�v:	�n�ʎ擾
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void getGround(){

	if( m_myY<GROUND_TOP ){
		if(	( GROUND_TOP1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
			( GROUND_TOP2X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP2X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ))
		{
			m_ground=GROUND_TOP;
		}
	}
	else if( m_myY<GROUND_MIDDLE ){
		if(	( GROUND_MIDDLE1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_MIDDLE1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
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
	���O:	walkCheckGround()
	�T�v:	�n�ʎ擾
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void checkGround(){

	if( m_ground==GROUND_TOP ){
		if(	( GROUND_TOP1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
			( GROUND_TOP2X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_TOP2X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) )
		{
		}else{
			m_myState=JUMP;
			m_ground=GROUND_MIDDLE;
		}
	}
	else if( m_ground==GROUND_MIDDLE ){
		if(	( GROUND_MIDDLE1X[m_stage]-(MYCHARA_WIDTH/2)<m_myX && m_myX<GROUND_MIDDLE1X[m_stage]+GROUND_WIDTH-(MYCHARA_WIDTH/2) ) ||
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
	���O:	initTitle()
	�T�v:	�^�C�g��������
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	initGame()
	�T�v:	�Q�[��������
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void initGame(){

	//�Q�[��
	m_time			=0;
	m_lv			=TIME_LIMIT;
	m_score			=0;
	m_combo			=0;
	m_comboMax		=0;
	m_comboTime		=0;

	//�L�����N�^
	m_myX			=120;
	m_myY			=GROUND_BOTTOM;
	m_isLeft		=false;
	m_myState		=STOP;
	m_addY			=0;
	m_walkCount		=0;
	m_imgIndex		=IMAGE_STOP;
	m_speed			=0;
	m_ground		=GROUND_BOTTOM;

	//�X���b�g
	for(int i=0;i<m_slot.length;i++){
		m_slot[i]=NONE;
	}
	m_slotTime=0;
	m_slotState=NONE;
	m_isAppearBonus=false;
	m_isBonusGet=false;

	//�t���[�c
	for(int i=0;i<APPLE_SIZE;i++){
		m_appleState[i]	=NONE;
		m_appleX[i]		=0;
		m_appleY[i]		=0;
		m_appleSpeedX[i]=4;
		m_appleSpeedY[i]=4;
		m_appleIsLeft[i]=false;
	}

	//ver1.1.0 �g��
	for(int i=0;i<3;i++){
		m_effX[i]		=0;
		m_effY[i]		=0;
		m_effTime[i]	=0;
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	moveTitle()
	�T�v:	�^�C�g���ł̂Ђ悱
	�@�\:	-
	�߂�l:	-
	����:	-
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
	���O:	checkSlot()
	�T�v:	�X���b�g
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void checkSlot(){

	//�X���b�g����Ȃ�I��
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
	���O:	sortHiscore()
	�T�v:	�n�C�X�R�A���ёւ�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void sortHiscore(){
	int i=0;
	int j=HISCORE_SIZE-2;
/*
	Calendar cal=Calendar.getInstance();
	int year=cal.get(Calendar.YEAR);
	int month=cal.get(Calendar.MONTH)+1;
	int day=cal.get(Calendar.DAY_OF_MONTH);

	//YYYY�NMM��DD��
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
	���O:	setStageImage()
	�T�v:	���R�[�h�X�g�A����ǂݏo���֐�
	�@�\:	-
	�߂�l:	-
	����:	stage:�X�e�[�W
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
	���O:	setFooderImage()
	�T�v:	�t�b�_���Z�b�g����֐�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void setFooderImage() {
	Graphics g3=imgFd.getGraphics();

	try{
		g3.drawImage(img[IMAGE_FOODER], 0, 0, Graphics.TOP|Graphics.LEFT);
		gUtil.drawRegion(g3,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SLOT, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 8, 211-PLAYING_HEIGHT, Graphics.TOP|Graphics.LEFT);
		gUtil.drawRegion(g3,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_SCORE, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 72, 211-PLAYING_HEIGHT, Graphics.TOP|Graphics.LEFT);
		gUtil.drawRegion(g3,img[IMAGE_LANG_FD], 0, F_HEIGHT*F_TIME, 44, F_HEIGHT, GraphicsUtil.TRANS_NONE, 72, 231-PLAYING_HEIGHT, Graphics.TOP|Graphics.LEFT);

		//���ԃo�[
		g3.setColor(  0,128,  0);
		g3.fillRect(119,231-PLAYING_HEIGHT,TIME_LIMIT+3,6);
		g3.setColor(255,255,255);
		g3.drawRect(119,231-PLAYING_HEIGHT,TIME_LIMIT+3,6);

		//SLOT
		for(int i=0;i<3;i++){
			//�X���b�g�g
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
	���O:	getBonusIndex()
	�T�v:	���R�[�h�X�g�A����ǂݏo���֐�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void getBonusIndex() {
	int bonusOn[]={0,1,0,0,0,0,0,0,0,0,0,0};
	int indexSize=2;
	Calendar cal=Calendar.getInstance();
	int week=cal.get(Calendar.DAY_OF_WEEK);
	int hour=cal.get(Calendar.HOUR);
	int minute=cal.get(Calendar.MINUTE);

	//3.������
	if( (Math.abs(r.nextInt())%1)==0 ){
		bonusOn[indexSize]=2;
		indexSize++;
	}
	//4.��������
	if( m_playTime>=25 ){
		bonusOn[indexSize]=3;
		indexSize++;
	}
	//5.���t�����X
	if(	( m_playTime>=100 ) ||
		( week==Calendar.SUNDAY )	){
		bonusOn[indexSize]=4;
		indexSize++;
	}
	//6.�Â��݂���
	if(	( m_playTime>=300 ) ||
		( hour==12 )	){
		bonusOn[indexSize]=5;
		indexSize++;
	}
	//7.���̂�����
	if(	( m_hiScore[m_stage][0]>=30000 )||
		( hour==5 )	){
		bonusOn[indexSize]=6;
		indexSize++;
	}
	//8.�K���f��
	if(	( m_hiScore[m_stage][0]>=60000 ) ||
		( m_playTime>=500 )	){
		bonusOn[indexSize]=7;
		indexSize++;
	}
	//9.���[�^�[
	if(	( m_hiScore[m_stage][0]>=90000 ) ||
		( minute%10==0 )	){
		bonusOn[indexSize]=8;
		indexSize++;
	}
	//10.�Q���{�C
	if(	( m_hiScore[0][0]>=30000 && m_hiScore[1][0]>=30000 && m_hiScore[2][0]>=30000) ||
		( week==Calendar.WEDNESDAY )	){
		bonusOn[indexSize]=9;
		indexSize++;
	}
	//11.������
	if(	( m_hiScore[0][0]>=60000 && m_hiScore[1][0]>=60000 && m_hiScore[2][0]>=60000) ||
		( week==Calendar.MONDAY && hour==9 )	){
		bonusOn[indexSize]=10;
		indexSize++;
	}

	//12.�莆
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
	���O:	loadRecordStore()
	�T�v:	���R�[�h�X�g�A����ǂݏo���֐�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private synchronized void loadRecordStore() {
	RecordStore rs=null;
	byte[] w=null;

	int p=0;

	int i,j;

	//�ǂݍ���
	try{
		//���R�[�h�X�g�A���J��
		rs=RecordStore.openRecordStore(RS_GAMEDATA,false);

		w=rs.getRecord(1);

//--	�Ǎ��ϊ�		--------------------------------------//

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

//--	�ϊ��I��		--------------------------------------//

		//���R�[�h�X�g�A�����
		rs.closeRecordStore();

	//��O����
	}catch(Exception e){
System.out.println(e.toString());
		try{
			if( rs!=null ) rs.closeRecordStore();
		}catch(Exception e2){}
	}
}


////////////////////////////////////////////////////////////////
/**
	���O:	saveRecordStore()
	�T�v:	���R�[�h�X�g�A�ɏ������ފ֐�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private synchronized void saveRecordStore() {
	RecordStore rs=null;
	byte[] w=new byte[RS_SIZE];
	int p=0;
	int i,j,k;

	try{
		//���R�[�h�X�g�A���J��
		rs=RecordStore.openRecordStore(RS_GAMEDATA,true);

//--	�����ϊ�		--------------------------------------//

		//�����J�n
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


//--	�ϊ��I��		--------------------------------------//
		//���R�[�h�X�g�A�ɏ�������
		if( rs.getNumRecords()==0 ){
			rs.addRecord(w,0,p);
		}else{
			rs.setRecord(1,w,0,p);
		}

		//���R�[�h�X�g�A�����
		rs.closeRecordStore();

	//��O����
	}catch(Exception e){
System.out.println(e.toString());
		try{
			if( rs!=null ) rs.closeRecordStore();
		}catch(Exception e2){}
	}
}

////////////////////////////////////////////////////////////////
/**
	���O:	initSound()
	�T�v:	���̏���������֐�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void initSound(){
	try{
		Phrase cursor	=new Phrase("resource:bonus1.spf");
		Phrase jump		=new Phrase("resource:jump.spf");
		Phrase bg1		=new Phrase("resource:bg1.spf");
		Phrase bg2		=new Phrase("resource:bg2.spf");

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

		Phrase enter	=new Phrase("resource:bonus1.spf");
		Phrase cursor	=new Phrase("resource:cursor.spf");

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

		Phrase jump	=new Phrase("resource:jump.spf");
		Phrase cursor	=new Phrase("resource:cursor.spf");

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
	���O:	switchMute()
	�T�v:	�~���[�g��ON����֐�
	�@�\:	-
	�߂�l:	-
	����:	-
*/
private void switchMute(){
	//off����on
	if( m_isMute==0 ){
		removeCommand(onSoundCmd);
		addCommand(offSoundCmd);
		m_isMute=1;
		stopBgm();
	//off����on
	}else{
		removeCommand(offSoundCmd);
		addCommand(onSoundCmd);
		m_isMute=0;
		//BGM�Đ�
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
