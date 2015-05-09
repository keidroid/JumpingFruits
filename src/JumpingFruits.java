import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class JumpingFruits extends MIDlet {
    /** DrawCanvasの保持 */
    private DrawCanvas drawCanvas;

    /** コンストラクタ */
    public JumpingFruits(){
        drawCanvas = new DrawCanvas(this);
        Display.getDisplay(this).setCurrent(drawCanvas);
    }

    /** MIDletの開始時に呼ばれるメソッド */
    protected void startApp() throws MIDletStateChangeException {}

    /** MIDletの一時停止時に呼ばれるメソッド */
    protected void pauseApp() {
        drawCanvas.resumeExe();
    }

    /** MIDletの終了時に呼ばれるメソッド */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

    /** MIDletを終了させるメソッド */
    void doExit() {
        try {
            destroyApp(false);
            notifyDestroyed();
        }catch(MIDletStateChangeException e) {}
    }
}