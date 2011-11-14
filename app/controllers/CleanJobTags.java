package controllers;

import java.util.List;

import models.Tag;
import models.Url;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@Every("15min")
public class CleanJobTags extends Job {

	
	
	@Override
	public void doJob() throws Exception {
        
		List<Tag> tags = Tag.findAll();
        for(Tag t : tags) {
        	if (Tag.find("tag = ? AND size(urls) = 0", t.tag).first() != null) {
    	        
        		
    	    	t.delete();
    	    }
        }
    }
}
