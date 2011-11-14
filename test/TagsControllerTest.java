import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPAPlugin;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;


public class TagsControllerTest extends FunctionalTest{

	@Before
	public void setUp() {
		JPAPlugin.startTx(false); 
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data.yml");   
		JPAPlugin.closeTx(false); 
	}
	
	@After
	public void tearDown() {
		JPAPlugin.startTx(false);
		Fixtures.deleteAllModels();
		JPAPlugin.closeTx(false); 
	}
	
	//***********************************
	
	//BUSCAR
	
	@Test
    public void buscarShouldWorkIfUserIsPresented() {
    	Response response = GET("/Tags/buscar/tag1/json");

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void buscarShouldFailBecauseUserIsNotPresent() {
    	tearDown();//method to cleanup objects . The JUnit framework automatically invokes the setUp() method before a each test is run and the	tearDown() method after each test is run.
        Response response = GET("/Tags/buscar/tag1/json");
        assertIsNotFound(response);
    }
	
	//*************************************
	
	//CREAR
	
	@Test
    public void crearJSONShouldWorkIfRequestIsCorrect() {
		
    	String JsonText = "{\"tag\":\"tag1\",\"urls\":[{\"dir\":\"www.web2.es\"}]}";
				
		Response response = POST("/Tags/crear/json", null, JsonText);

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void crearFailIfJsonIsNotSentInTheRequest() {
		String JsonText = "";
		
		Response response = POST("/Tags/crear/json", null, JsonText);

    	assertIsNotFound(response);
        
    }
	
	@Test
    public void crearJSONFailIfWebDataIsEmpty() {
		String JsonText = "{\"tag\":\"tag1\",\"urls\":[{\"dir\":\"\"}]}";
		
		Response response = POST("/Tags/crear/json", null, JsonText);

    	assertIsNotFound(response);
    	
	}
	
	@Test
    public void crearJSONFailIfTagDataIsEmpty() {
		String JsonText = "{\"tag\":\"\",\"urls\":[{\"dir\":\"www.web2.es\"}]}";
		
		Response response = POST("/Tags/crear/json", null, JsonText);

    	assertStatus(500,response);
    	
	}
	
	@Test
    public void crearJSONFailIfTagsAlreadyExistInTheWeb() {
		String JsonText = "{\"tag\":\"tag1\",\"urls\":[{\"dir\":\"www.web1.es\"}]}";
		
		Response response = POST("/Tags/crear/json", null, JsonText);

		assertStatus(500,response);
    	
	}
	
	//*****************************************
	//BORRAR
	
	@Test
    public void borrarShouldWorkIfTagExisteInTheWeb(){
		    		
		Response response = DELETE ("/Tags/borrar/tag1/www.web1.es/json");

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void borrarShouldNotWorkIfTagExistTheWeb(){
		
		Response response = DELETE ("/Tags/borrar/tag1/www.web2.es/json");

		assertStatus(500,response);
    }
	
	@Test
    public void borrarShouldNotWorkIfTagIsNotPresented(){
		 tearDown();
		Response response = DELETE ("/Tags/borrar/tag1/www.web1.es/json");

    	assertIsNotFound(response);
    }
	
}
