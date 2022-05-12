package ch.fhnw.palletpals.data.domain;

import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;

import javax.persistence.*;
import java.util.List;

@Entity
public class ServiceProvider {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String jSONString;

    @OneToMany
    @JoinColumn(name = "serviceProviderId", referencedColumnName = "id")
    private List<ShoppingSession> shoppingSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJSONString() {
        return jSONString;
    }

    public void setJSONString(String kmArray) {
        this.jSONString = kmArray;
    }



}
