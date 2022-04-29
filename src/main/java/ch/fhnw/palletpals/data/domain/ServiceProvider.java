package ch.fhnw.palletpals.data.domain;

import javax.persistence.*;

@Entity
public class ServiceProvider {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String kmArray;
    private String palletArray;
    private String priceMatrix;

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

    public String getKmArray() {
        return kmArray;
    }

    public void setKmArray(String kmArray) {
        this.kmArray = kmArray;
    }

    public String getPalletArray() {
        return palletArray;
    }

    public void setPalletArray(String palletArray) {
        this.palletArray = palletArray;
    }

    public String getPriceMatrix() {
        return priceMatrix;
    }

    public void setPriceMatrix(String priceMatrix) {
        this.priceMatrix = priceMatrix;
    }
}
