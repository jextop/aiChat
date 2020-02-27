package com.walle.audio;

public interface TimeListener {
    void timeUpdated(long seconds);
    void stopped(long seconds);
}
