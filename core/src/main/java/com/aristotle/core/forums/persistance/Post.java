package com.aristotle.core.forums.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.aristotle.core.persistance.User;



@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;
    @Column(name = "thread_id", insertable = false, updatable = false)
    private Long threadId;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    
    @Column(name="date_created")
    private Date dateCreated;
    @Column(name="date_modified")
    private Date dateModified;
    @Column(name="title", nullable = false)
    private String title;
    @Column(name="body")
    private String body;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Thread getThread() {
        return thread;
    }
    public void setThread(Thread thread) {
        this.thread = thread;
    }
    public Long getThreadId() {
        return threadId;
    }
    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public Date getDateModified() {
        return dateModified;
    }
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Post other = (Post) obj;
        if (id != other.id)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Post [id=" + id + ", thread=" + thread + ", threadId="
                + threadId + ", user=" + user + ", userId=" + userId
                + ", dateCreated=" + dateCreated + ", dateModified="
                + dateModified + ", title=" + title + ", body=" + body + "]";
    }
    
    

}
