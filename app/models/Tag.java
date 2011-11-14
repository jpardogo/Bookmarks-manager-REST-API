package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;





@Entity
public class Tag extends Model {
	
	@Required
	@MinSize(2)
	@MaxSize(20)
	public String tag;
	
	@ManyToMany(mappedBy="tags")
	public Set<Url> urls = new HashSet<Url>() ;
	
	public Tag(String tag) {
		super();
		this.tag = tag;
	}
	
	
}
