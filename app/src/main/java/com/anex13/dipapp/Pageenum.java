package com.anex13.dipapp;

/**
 * Created by namel on 14.04.2016.
 */
public enum Pageenum {
    WIN(R.array.win_array,2), NIX(R.array.nix_array,1), LAN(R.array.lan_array,3), MOB(R.array.mob_array,4);
    private final int resId;
    private int position;

    Pageenum(int resId, int position) {
        this.resId = resId;
        this.position = position;
    }

    public int getList() {
        return resId;
    }

    public int getPosition() {
        return position;
    }
}
