package controllers;

import java.sql.Savepoint;
import java.util.List;

import models.Tag;
import models.Url;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;


@Every("5h")
@OnApplicationStart(async=true)
public class CleanJobUrls extends Job {
	
	
	
	@Override
	public void doJob() {
        List<Url> urls = Url.findAll();
        for(Url url : urls) {
        	if (Url.find("dir = ? AND size(users) = 0", url.dir).first() != null) {
    	        
        		url.tags.removeAll(url.tags);
        		
    	    	url.delete();
    	    }
        }
        new CleanJobTags().now();
    }
	
	
}
