import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class JumpingFruits extends MIDlet {
	/** DrawCanvas�̕ێ� */
	private DrawCanvas drawCanvas;

	/** �R���X�g���N�^ */
	public JumpingFruits(){
		drawCanvas = new DrawCanvas(this);
		Display.getDisplay(this).setCurrent(drawCanvas);
	}

	/** MIDlet�̊J�n���ɌĂ΂�郁�\�b�h */
	protected void startApp() throws MIDletStateChangeException {}

	/** MIDlet�̈ꎞ��~���ɌĂ΂�郁�\�b�h */
	protected void pauseApp() {
		drawCanvas.resumeExe();
	}

	/** MIDlet�̏I�����ɌĂ΂�郁�\�b�h */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

	/** MIDlet���I�������郁�\�b�h */
	void doExit() {
		try {
			destroyApp(false);
			notifyDestroyed();
		}catch(MIDletStateChangeException e) {}
	}
}