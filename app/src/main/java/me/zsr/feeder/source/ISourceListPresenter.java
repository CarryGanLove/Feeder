package me.zsr.feeder.source;

/**
 * @description:
 * @author: Zhangshaoru
 * @date: 11/12/15
 */
public interface ISourceListPresenter {

    void setOnSourceSelectedListener(OnSourceSelectedListener listener);

    void sourceSelected(long sourceId);

    void loadSource();

    void refresh();

    void markAllAsRead(long sourceId);

    void deleteSource(long sourceId);
}
