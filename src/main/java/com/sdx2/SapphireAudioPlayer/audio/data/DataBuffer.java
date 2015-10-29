package main.java.com.sdx2.SapphireAudioPlayer.audio.data;

public class DataBuffer {
    protected static final int DEFAULT_BUFFER_SIZE = 2048;
    protected volatile int bufferSize = 0;
    protected byte[] buffer = null;
    protected volatile int putHere = 0;
    protected volatile int getHere = 0;
    protected volatile boolean eof = false;
    protected final Object signal = new Object();

    public DataBuffer(int size) {
        bufferSize = size;
        buffer = new byte[size];
    }

    public DataBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public int size() {
        return buffer.length;
    }

    public void resize(int newSize) {
        if (bufferSize >= newSize) return;
        byte[] newBuffer = new byte[newSize];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        buffer = newBuffer;
        bufferSize = newSize;
    }

    public int putAvailable() {
        if (putHere == getHere) return bufferSize - 1;
        if (putHere < getHere) return getHere - putHere - 1;
        return bufferSize - (putHere - getHere) - 1;
    }

    public void empty() {
        synchronized (signal) {
            putHere = 0;
            getHere = 0;
            eof = true;
            signal.notifyAll();
        }
    }

    public void put(byte[] data, int offset, int len) {
        if (len == 0) return;
        eof = false;

        synchronized (signal) {
            while (putAvailable() < len) {
                try {
                    signal.wait();
                } catch (Exception e) {
                    System.out.println("Put.Signal.wait:" + e);
                }
            }
            if (putHere >= getHere) {
                int l = Math.min(len, bufferSize - putHere);
                System.arraycopy(data, offset, buffer, putHere, l);
                putHere += l;
                if (putHere >= bufferSize) putHere = 0;
                if (len > l) put(data, offset + l, len - l);
            } else {
                int l = Math.min(len, getHere - putHere - 1);
                System.arraycopy(data, offset, buffer, putHere, l);
                putHere += l;
                if (putHere >= bufferSize) putHere = 0;
            }
            signal.notify();
        }
    }

    public int getAvailable() {
        if (putHere == getHere) return 0;
        if (putHere < getHere) return bufferSize - (getHere - putHere);
        return putHere - getHere;
    }

    public int get(byte[] data, int offset, int len) {
        if (len == 0) return 0;
        int dataLen;

        synchronized (signal) {
            while (getAvailable() <= 0) {
                if (eof) return (-1);
                try {
                    signal.wait(1000);
                } catch (Exception e) {
                    System.out.println("Get.Signal.wait:" + e);
                }
            }
            len = Math.min(len, getAvailable());

            if (getHere < putHere) {
                int l = Math.min(len, putHere - getHere);
                System.arraycopy(buffer, getHere, data, offset, l);
                getHere += l;
                if (getHere >= bufferSize) getHere = 0;
                dataLen = l;
            } else {
                int l = Math.min(len, bufferSize - getHere);
                System.arraycopy(buffer, getHere, data, offset, l);
                getHere += l;
                if (getHere >= bufferSize) getHere = 0;
                dataLen = l;
                if (len > l) dataLen += get(data, offset + l, len - l);
            }
            signal.notify();
        }

        return dataLen;
    }

    public boolean isEOF() {
        return eof;
    }

    public void setEOF(boolean eof) {

        this.eof = eof;
    }

    public void removeTail(long bytes) {
        synchronized (signal) {
            putHere -= bytes;
            if (putHere < 0)
                putHere += size();
        }
    }

    public void show() {
        for (int i = 0; i < buffer.length; i++) {
            System.out.println(buffer[i]);
        }
    }
}