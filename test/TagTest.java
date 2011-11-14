import models.Tag;
import models.Url;
import models.Usuario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class TagTest extends UnitTest {

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
    public void tagFailsWhenIsNull() {
    	Tag t = new Tag("");
    	assertFalse(t.validateAndCreate());
    }
	
	@Test
    public void tagFailsWhenIsTooShort() {
    	Tag t = new Tag("t");
    	assertFalse(t.validateAndCreate());
    }
	
	@Test
    public void tagFailsWhenIsWrongTooLong() {
    	Tag t = new Tag("ElTagTieneDemasiadasLetras");
    	assertFalse(t.validateAndCreate());
    }
}
