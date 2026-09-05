package com.room209.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "plan_rsvps", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"plan_id", "user_id"})
})
public class PlanRsvp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnore
    private Plan plan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RsvpStatus rsvpStatus = RsvpStatus.ATTENDING;

    public enum RsvpStatus {
        ATTENDING,
        TENTATIVE,
        DECLINED
    }

    public PlanRsvp() {}

    public PlanRsvp(Plan plan, User user, RsvpStatus rsvpStatus) {
        this.plan = plan;
        this.user = user;
        this.rsvpStatus = rsvpStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public RsvpStatus getRsvpStatus() { return rsvpStatus; }
    public void setRsvpStatus(RsvpStatus rsvpStatus) { this.rsvpStatus = rsvpStatus; }
}
