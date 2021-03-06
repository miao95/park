package edu.buaa.sem.po;
// Generated 2018-5-2 23:58:39 by Hibernate Tools 4.3.1.Final

/**
 * SysAuthority generated by hbm2java
 */
public class SysAuthority implements java.io.Serializable {

	private Long id;
	private String name;
	private String description;
	private String enabled;

	public SysAuthority() {
	}

	public SysAuthority(String name, String description, String enabled) {
		this.name = name;
		this.description = description;
		this.enabled = enabled;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

}
