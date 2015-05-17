package jp.co.spajam.androidapp.listener;

import jp.co.spajam.androidapp.data.Rotate;

public interface OnRotate {
    /**
     *
     * @param rotate 回転の強さ
     */
    public void onRotate(Rotate rotate);
}