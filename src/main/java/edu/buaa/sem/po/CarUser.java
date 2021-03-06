package edu.buaa.sem.po;
// Generated 2018-5-2 23:58:39 by Hibernate Tools 4.3.1.Final

/**
 * CarUser generated by hbm2java
 */
public class CarUser implements java.io.Serializable {

	private Long id;
	private String userNumber;
	private String carNumber;
	private String description;

	public CarUser() {
	}

	public CarUser(String userNumber, String carNumber, String description) {
		this.userNumber = userNumber;
		this.carNumber = carNumber;
		this.description = description;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserNumber() {
		return this.userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getCarNumber() {
		return this.carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
