package javaCA.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	
	@Id
	@Column(name = "categoryid")
	private String categoryId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "daysOff")
	private int daysOff;

	private int differenceForAdmin;
	
	public Category() {}
	
	public Category(String categoryId, String name, String description, int daysOff) {
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
		this.daysOff = daysOff;
		this.differenceForAdmin = 0;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDaysOff() {
		return daysOff;
	}

	public void setDaysOff(int daysOff) {
		this.daysOff = daysOff;
	}

	public int getDifferenceForAdmin() {
		return differenceForAdmin;
	}

	public void setDifferenceForAdmin(int differenceForAdmin) {
		this.differenceForAdmin = differenceForAdmin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(categoryId, other.categoryId);
	}
	
	
}
