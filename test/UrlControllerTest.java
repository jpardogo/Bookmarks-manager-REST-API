import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ning.http.client.providers.jdk.ResponseStatus;

import play.db.jpa.JPAPlugin;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;


public class UrlControllerTest extends FunctionalTest {

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
	
	
//  !!!Hare los test para json porque para xml es
	
//  igual excepto que se cambia la extension y el  contentType !!!!!!!!!!!!
	
	//Las paginas web se pasan normalmente con el formato www.web.(es/com/org....) y en la funcion correspondiente
	//se tratan para que sigan un formato de URL para validarlo.
	
	//******************************************
	//BUSCAR
	
	@Test
    public void buscarShouldWorkIfUserIsPresented() {
    	Response response = GET("/Urls/buscar/www.web1.es/json");

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void buscarShouldFailBecauseUserIsNotPresent() {
    	tearDown();//method to cleanup objects . The JUnit framework automatically invokes the setUp() method before a each test is run and the	tearDown() method after each test is run.
        Response response = GET("/Urls/buscar/www.web1.es/json");
        assertIsNotFound(response);
    }
	
	//******************************************************************
	//CREAR
	
	@Test
    public void crearJSONShouldWorkIfRequestIsCorrect() {
		//Se puede pasar o no el email , es indiferente
    	String JsonText = "{\"nick\":\"nick2\",\"email\":\"emai2@email.com\",\"urls\":[{\"dir\":\"www.web1.es\"}]}";
				
		Response response = POST("/Urls/crear/json", null, JsonText);

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void crearFailIfJsonIsNotSentInTheRequest() {
		String JsonText = "";
		
		Response response = POST("/Urls/crear/json", null, JsonText);

    	assertIsNotFound(response);
        
    }
	
	@Test
    public void crearJSONFailIfWebDataIsEmpty() {
		String JsonText = "{\"nick\":\"nick2\",\"email\":\"emai2@email.com\",\"urls\":[{\"dir\":\"\"}]}";
		
		Response response = POST("/Urls/crear/json", null, JsonText);

    	assertStatus(500, response);
    	
	}
	
	@Test
    public void crearJSONFailIfNickDataIsEmpty() {
		String JsonText = "{\"nick\":\"\",\"email\":\"\",\"urls\":[{\"dir\":\"http://www.web1.es\"}]}";
		
		Response response = POST("/Urls/crear/json", null, JsonText);

    	assertIsNotFound(response);
    	
	}
	
	@Test
    public void crearJSONFailIfUserAlreadyFollowTheWeb() {
		String JsonText = "{\"nick\":\"nick1\",\"email\":\"emai2@email.com\",\"urls\":[{\"dir\":\"www.web1.es\"}]}";
		
		Response response = POST("/Urls/crear/json", null, JsonText);

		assertStatus(500,response);
    	
	}
	
	//***********************************************************************
	//BORRAR
	
	@Test
    public void borrarShouldWorkIfUserFollowTheWeb(){
		    		
		Response response = DELETE ("/Urls/borrar/nick1/www.web1.es/json");

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void borrarShouldNotWorkIfUserDoesntFollowTheWeb(){
		
		Response response = DELETE ("/Urls/borrar/nick2/www.web1.es/json");

		assertStatus(500,response);
    }
	
	@Test
    public void borrarShouldNotWorkIfUserIsNotPresented(){
		 tearDown();
		Response response = DELETE ("/Urls/borrar/nick1/www.web1.es/json");

    	assertIsNotFound(response);
    }
}
