package com.acme;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "likes")
@NamedQuery(name = "Likes.findAll", query = "SELECT f FROM Like f ORDER BY f.DATE_CREATED", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class Like {

    @Id
    @SequenceGenerator(name = "LikeZaporedje", sequenceName = "znane_like_id_zaporedje", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LikeZaporedje")
    private Integer id;

    @Column(name="DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    public Date DATE_CREATED;
    //@ManyToOne
    public Integer id_giver;
    //@ManyToOne
    public Integer id_geter;

   /* public Integer getId_giver() {
        return id_giver.getId();
    }

    public void setId_giver(User id_giver) {
        this.id_giver = id_giver;
    }

    public Integer getId_geter() {
        return id_geter.getId();
    }

    public void setId_geter(User id_geter) {
        this.id_geter = id_geter;
    }

    public Like(Date DATE_CREATED, User id_giver, User id_geter) {
        this.DATE_CREATED = DATE_CREATED;
        this.id_giver = id_giver;
        this.id_geter = id_geter;
    }*/

    public Like(Date DATE_CREATED, Integer id_giver, Integer id_geter) {
        this.DATE_CREATED = DATE_CREATED;
        this.id_giver = id_giver;
        this.id_geter = id_geter;
    }

    public Integer getId_giver() {
        return id_giver;
    }

    public void setId_giver(Integer id_giver) {
        this.id_giver = id_giver;
    }

    public Integer getId_geter() {
        return id_geter;
    }

    public void setId_geter(Integer id_geter) {
        this.id_geter = id_geter;
    }
    public Like(){

    }
    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", DATE_CREATED=" + DATE_CREATED +
                ", id_giver=" + id_giver +
                ", id_geter=" + id_geter +
                '}';
    }
}
