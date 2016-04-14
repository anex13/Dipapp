package com.anex13.dipapp;

/**
 * Created by namel on 14.04.2016.
 */
public enum Pageenum {
    WIN(R.array.win_array), NIX(R.array.nix_array), LAN(R.array.lan_array), MOB(R.array.mob_array);
    private final int resId;

    Pageenum(int resId) {
        this.resId = resId;
    }

    public int getList() {
        return resId;
    }
}
