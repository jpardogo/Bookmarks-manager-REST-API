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
import javax.persistence.OneToOne;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;


@Entity
public class Url extends Model {

	@Required
	@URL
	
	public String dir;
	
	
	
	@ManyToMany(cascade= CascadeType.ALL)
	public Set<Usuario> users = new HashSet<Usuario>();
	
	@ManyToMany(cascade= CascadeType.ALL)
	public Set<Tag> tags = new HashSet<Tag>();

	

	public Url(String dir) {
		super();
		this.dir = dir;
	}


	public boolean removeUsUr(Usuario usu, Integer flag) {
	    
		if (!this.users.contains(usu)) {
	        return false;
	    }
	    
	    this.users.remove(usu);
	    save();

	    
	    
	    if (flag == 1){
	    	if (Usuario.find("nick = ? AND size(urls) = 0", usu.nick).first() != null) {
	        	
	    		
	    		usu.delete();
	    	}
	    }
	    
	    if (Url.find("dir = ? AND size(users) = 0", this.dir).first() != null) {
	        
	    	/*for(Tag t: this.tags){
	    		
	    		this.removeTgUr(t);
	    	}*/
	    	//ESTE FOR NO LO EJECUTO PORQUE ME DA UNA EXCEPTION en el this.tags.remove de la funcion removeTgUr(t)
	    	//--> Asi que utlizao un Job para los tags que queden sueltos al borrar la web<--
	    	
	    	this.tags.removeAll(tags);
	    	save();
	    	this.delete();
	    }

	    return true;
	}


	public boolean removeTgUr(Tag tag) {
		if (!this.tags.contains(tag)) {
	        return false;
	    }
	    
	    this.tags.remove(tag);
	    save();
	    
	    if (Tag.find("tag = ? AND size(urls) = 0", tag.tag).first() != null) {
	        
	    	tag.delete();
	    }

	    return true;
		
	}

	

	
	
	
}
