package me.zsr.feeder.source;

/**
 * @description:
 * @author: Zhangshaoru
 * @date: 11/15/15
 */
public interface OnActionListener {

    void success();

    void error(String msg);
}
