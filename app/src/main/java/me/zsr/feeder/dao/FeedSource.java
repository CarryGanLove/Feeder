package me.zsr.feeder.dao;

import java.util.List;
import me.zsr.feeder.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table FEED_SOURCE.
 */
public class FeedSource {

    private Long id;
    /** Not-null value. */
    private String title;
    private String url;
    private java.util.Date date;
    private String link;
    private String favicon;
    private String description;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FeedSourceDao myDao;

    private List<FeedItem> feedItems;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public FeedSource() {
    }

    public FeedSource(Long id) {
        this.id = id;
    }

    public FeedSource(Long id, String title, String url, java.util.Date date, String link, String favicon, String description) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.date = date;
        this.link = link;
        this.favicon = favicon;
        this.description = description;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFeedSourceDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<FeedItem> getFeedItems() {
        if (feedItems == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FeedItemDao targetDao = daoSession.getFeedItemDao();
            List<FeedItem> feedItemsNew = targetDao._queryFeedSource_FeedItems(id);
            synchronized (this) {
                if(feedItems == null) {
                    feedItems = feedItemsNew;
                }
            }
        }
        return feedItems;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetFeedItems() {
        feedItems = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here

    @Override
    public String toString() {
        return "FeedSource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", date=" + date +
                ", link='" + link + '\'' +
                ", favicon='" + favicon + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setFeedItems(List<FeedItem> list) {
        feedItems = list;
    }
    // KEEP METHODS END

}
