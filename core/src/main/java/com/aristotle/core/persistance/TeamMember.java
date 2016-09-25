package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "team_member")
public class TeamMember extends BaseEntity{

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @Column(name = "team_id", insertable = false, updatable = false)
    private Long teamId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "responsbility")
    private String responsbility;

    @Column(name = "post")
    private String post;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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

    public String getResponsbility() {
        return responsbility;
    }

    public void setResponsbility(String responsbility) {
        this.responsbility = responsbility;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

}
