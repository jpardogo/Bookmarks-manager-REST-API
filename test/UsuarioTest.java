
import javax.persistence.PersistenceException;

import models.*;

import org.junit.*;

import play.db.jpa.Transactional;
import play.test.*;


public class UsuarioTest extends UnitTest {

	
	
	
	
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
    public void nickFailsWhenIsNull() {
    	Usuario u = new Usuario("","email@email.com");
    	assertFalse(u.validateAndCreate());
    }
	
	
	@Test
    public void nickFailsWhenIsTooShort() {
    	Usuario u = new Usuario("n","email@email.com");
    	assertFalse(u.validateAndCreate());
    }
	
	@Test
    public void nickFailsWhenIsWrongTooLong() {
    	Usuario u = new Usuario("ElNickTieneDemasiadasLetras","email@email.com");
    	assertFalse(u.validateAndCreate());
    }
	
	@Test
    public void emailFailsWhenIsNull() {
    	Usuario u = new Usuario("nick","");
    	assertFalse(u.validateAndCreate());
    }
	
	@Test
    public void emailFailsWhenIsWrongFormatted() {
    	Usuario u = new Usuario("nick","email@");
    	assertFalse(u.validateAndCreate());
    }
	
	@Test
    public void emailAndNickWinWhenAreWellFormatted() {
    	Usuario u = new Usuario("nick","email@email.com");
    	assertTrue(u.validateAndCreate());
    }
	
	
	@Test(expected=PersistenceException.class) //Se produce una excepcion de java por 
												//introducir un email ya existente y con esta 
												//intruccion la capturo para concluir es test correctamente
    public void emailFailsWhenIsRepeated() {
    	Usuario u = new Usuario("nick","email1@email.com");
    	
    	assertFalse(u.validateAndCreate());
    }
	
	@Test(expected=PersistenceException.class)//Se produce una excepcion de java por 
												//introducir un nick ya existente y con esta 
												//intruccion la capturo para concluir es test correctamente
    public void nickFailsWhenIsRepeated() {
    	Usuario u = new Usuario("nick1","email@email.com");
    	
    	assertFalse(u.validateAndCreate());
    }
	
}
