package ch.fhnw.palletpals.data.domain;
import ch.fhnw.palletpals.data.domain.order.UserOrder;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Code by Daniel Locher
 */
@Entity
public class User {

	@Id
	@GeneratedValue
	private Long id;
	@JsonIgnore
	private String accessCode;
	@NotEmpty(message = "Please provide a name.")
	private String userName;
	@Email(message = "Please provide a valid e-mail.")
	@NotEmpty(message = "Please provide an e-mail.")
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // only create object property from JSON
	private String password;
	@JsonIgnore
	private UserType role;
	@Transient // will not be stored in DB
	private String remember;
	private Language language;
	private Appearance appearance;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private ShippingAddress address;

	//One user has many orders
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserOrder> orderHistory;

	@OneToOne(mappedBy = "user")
	private ShoppingSession shoppingSession;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		String transientPassword = this.password;
		this.password = null;
		return transientPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemember() {
		return remember;
	}

	public void setRemember(String remember) {
		this.remember = remember;
	}

	public void setRole(UserType role){ this.role = role;}

	public UserType getRole() {
		return role;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}

	public ShippingAddress getAddress() {
		return address;
	}

	public void setAddress(ShippingAddress address) {
		this.address = address;
	}

	public List<UserOrder> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(List<UserOrder> orderHistory) {
		this.orderHistory = orderHistory;
	}

	public ShoppingSession getShoppingSession() {
		return shoppingSession;
	}

	public void setShoppingSession(ShoppingSession shoppingSession) {
		this.shoppingSession = shoppingSession;
	}
}
