package com.acme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.logging.Logger;

@Entity
@Table(name = "users")
@NamedQuery(name = "Users.findAll", query = "SELECT f FROM User f ORDER BY f.username", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@NamedQuery(name = "Users.findOneByName", query = "SELECT u FROM User u where username = :username", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
@UserDefinition
public class User {

    private static final Logger log = Logger.getLogger(User.class.getName());

    @Id
    @SequenceGenerator(name = "osebeZaporedje", sequenceName = "znane_osebe_id_zaporedje", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "osebeZaporedje")
    private Integer id;

    @Username
    @NotNull
    public String username;

    @Password
    @JsonIgnore
    private String password;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Roles
    public String role;

    @Column(name="DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    public Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User() {
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", created=" + created +
                '}';
    }
}
