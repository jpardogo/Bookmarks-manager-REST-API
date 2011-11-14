package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.event.SaveOrUpdateEvent;

import play.data.validation.Email;
import play.data.validation.Error;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.mvc.Http.Response;
import play.mvc.results.NotFound;
import play.mvc.results.RenderText;



@Entity
public class Usuario extends Model {
	
	@MinSize(2)
	@MaxSize(20)
	@Required
	@Column(unique=true)
	public String nick;
	
	@Required
	@Email
	@Column(unique=true)
	public String email;
	
	@ManyToMany(mappedBy="users")
	public Set<Url> urls = new HashSet<Url>() ;



	public Usuario(String nick, String email) {
		super();
		this.nick = nick;
		this.email = email;
	}

	
	
	
	
			
	
			
			
		
		
		
}
	
	
	

