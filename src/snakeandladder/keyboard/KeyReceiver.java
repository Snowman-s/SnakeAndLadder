package snakeandladder.keyboard;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;

public class KeyReceiver implements KeyListener {
    protected Map<Short, Integer> receiveKeyPressedFrame = new HashMap<>();

    public KeyReceiver() {
        this.addReceiveKey(VK_ESCAPE);
    }

    private void addReceiveKey(short key) {
        if (!receiveKeyPressedFrame.containsKey(key)) {
            receiveKeyPressedFrame.put(key, -1);
        }
    }

    public boolean isKeyPressed(short key) {
        if (!receiveKeyPressedFrame.containsKey(key)) {
            throw new IllegalArgumentException("登録されていないKeyを呼び出しました。");
        }
        return receiveKeyPressedFrame.get(key) >= 0;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isAutoRepeat()) {
            return;
        }
        short keycode = keyEvent.getKeyCode();
        if (receiveKeyPressedFrame.containsKey(keycode)) {
            receiveKeyPressedFrame.replace(keycode,
                    receiveKeyPressedFrame.get(keycode) + 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.isAutoRepeat()) {
            return;
        }
        if (receiveKeyPressedFrame.containsKey(keyEvent.getKeyCode())) {
            receiveKeyPressedFrame.put(keyEvent.getKeyCode(), -1);
        }
    }

    private static final BiFunction<Short, Integer, Integer> replaceFunction = (k, v) -> v >= 0 ? v + 1 : -1;

    public void increaseKeyPressedFrame() {
        this.receiveKeyPressedFrame.replaceAll(replaceFunction);
    }
}
