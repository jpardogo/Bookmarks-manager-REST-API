import static org.junit.Assert.*;
import models.Url;
import models.Usuario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class UrlsTest extends UnitTest {

	@Before
	public void setUp() {
	    Fixtures.deleteAllModels();
	    Fixtures.loadModels("data.yml");
	    
	}
	
	
	@After
	public void tearDown() {
		Fixtures.deleteAllModels();
	}
	
	
	@Test
    public void dirFailsWhenIsNull() {
    	Url url = new Url("");
    	assertFalse(url.validateAndCreate());
    }
	
	@Test
    public void dirFailsWhenIsWrongFormatted() {
    	Url url = new Url("www.web.es");
    	assertFalse(url.validateAndCreate());
    }
	
	@Test
    public void dirWinsWhenIsWellFormatted() {
    	Url url = new Url("http://www.web.es");
    	assertTrue(url.validateAndCreate());
    }
	
	
}
