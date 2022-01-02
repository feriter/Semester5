package ru.nsu.ccfit.GH;

import javax.swing.*;

public class HitListButton extends JButton {
    private Hit hit;

    public HitListButton() {
        super();
    }

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
    }
}
