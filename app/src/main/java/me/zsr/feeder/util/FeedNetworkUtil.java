package me.zsr.feeder.util;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

import java.util.ArrayList;
import java.util.List;

import me.zsr.feeder.dao.FeedItem;
import me.zsr.feeder.dao.FeedSource;

/**
 * @description: Fetch data from network.
 * @author: Zhangshaoru
 * @date: 15-6-11
 */
public class FeedNetworkUtil {
    public static void fetchAll() {
        List<FeedSource> feedSourceList = FeedDBUtil.getInstance().loadAll();
        for (FeedSource source : feedSourceList) {
            fetchItem(source);
        }
    }

    public static void fetchItem(final FeedSource feedSource) {
        if (feedSource == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                RSSReader reader = new RSSReader();
                RSSFeed feed;
                try {
                    feed = reader.load(feedSource.getUrl());
                    FeedDBUtil.getInstance().saveFeedItem(parseItem(feed, feedSource.getId()));
                } catch (RSSReaderException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void verifyFeedSource(final String url, final OnVerifyFeedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new RSSReader().load(url);
                    if (listener != null) {
                        listener.onResult(true);
                    }
                } catch (RSSReaderException e) {
                    if (listener != null) {
                        listener.onResult(false);
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void addFeedSource(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RSSFeed feed = new RSSReader().load(url);
                    FeedSource feedSource = new FeedSource(
                            null,
                            feed.getTitle(),
                            url,
                            feed.getPubDate(),
                            feed.getLink().toString(),
                            feed.getLink().toString() + "/favicon.ico",
                            feed.getDescription()
                    );
                    FeedDBUtil.getInstance().saveFeedSource(feedSource);
                    FeedDBUtil.getInstance().saveFeedItem(parseItem(feed, feedSource.getId()));
                } catch (RSSReaderException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface OnVerifyFeedListener {
        void onResult(boolean isValid);
    }

    private static List<FeedItem> parseItem(RSSFeed rssFeed, long feedSourceId) {
        List<FeedItem> feedItemList = new ArrayList<>();

        if (rssFeed != null) {
            for (RSSItem item : rssFeed.getItems()) {
                FeedItem feedItem = new FeedItem(
                        null,
                        item.getTitle(),
                        item.getLink().toString(),
                        item.getDescription(),
                        "unread",
                        item.getPubDate(),
                        feedSourceId);
                feedItemList.add(feedItem);
            }
        }

        return feedItemList;
    }
}
