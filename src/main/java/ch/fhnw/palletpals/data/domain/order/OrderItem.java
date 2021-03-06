package ch.fhnw.palletpals.data.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Code by: Tibor Haller
 * <p>
 * Serves as a SNAPSHOT for orders made by users.
 * Consists of both ProductItem and ShippingItem and is used by the Order class.
 * <p>
 * Based on single table inheritance of JPA:
 * https://en.wikibooks.org/wiki/Java_Persistence/Inheritance#Single_Table_Inheritance
 * <p>
 * In single table inheritance a single table is used to store all of the instances of the entire inheritance hierarchy.
 * The table will have a column for every attribute of every class in the hierarchy.
 * A discriminator column is used to determine which class the particular row belongs to,
 * each class in the hierarchy defines its own unique discriminator value.
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "ORDER_TYPE")
public abstract class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public OrderItem() {
    }

    public OrderItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
