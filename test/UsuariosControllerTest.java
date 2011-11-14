import models.Usuario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ning.http.client.Request;
import com.sun.org.glassfish.external.statistics.annotations.Reset;

import play.db.jpa.JPAPlugin;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;


public class UsuariosControllerTest extends FunctionalTest {

	
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
	
	
	//BUSCAR
	
	@Test
    public void buscarJSONShouldWorkIfUserIsPresented() {
    	Response response = GET("/Usuarios/buscar/nick1/json");

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void buscarXMLShouldWorkIfUserIsPresented() {
    	Response response = GET("/Usuarios/buscar/nick1/xml");

    	assertIsOk(response);
        assertContentType("text/xml", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void buscarJSONShouldFailBecauseUserIsNotPresent() {
    	tearDown();//method to cleanup objects . The JUnit framework automatically invokes the setUp() method before a each test is run and the	tearDown() method after each test is run.
        Response response = GET("/Usuarios/buscar/nick1/json");
        assertIsNotFound(response);
       
    }
	
	@Test
    public void buscarXMLShouldFailBecauseUserIsNotPresent() {
    	tearDown();//method to cleanup objects . The JUnit framework automatically invokes the setUp() method before a each test is run and the	tearDown() method after each test is run.
        Response response = GET("/Usuarios/buscar/nick1/xml");
        assertIsNotFound(response);
        
    }
	
		
	//*******************************
	
	//CREAR
	
	@Test
    public void crearJSONShouldWorkIfRequestIsCorrect() {
    	String JsonText = "{\"nick\":\"nick\",\"email\":\"email@email.com\",\"urls\":[]}";
				
		Response response = POST("/Usuarios/crear/json", null, JsonText);

    	assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	
	
	@Test
    public void crearXMLShouldWorkIfRequestIsCorrect() {
		String JsonText = "{\"nick\":\"nick\",\"email\":\"email@email.com\",\"urls\":[]}";
		
		Response response = POST("/Usuarios/crear/xml", null, JsonText);

    	assertIsOk(response);
        assertContentType("text/xml", response);
        assertCharset("utf-8", response);
    }
	
	
	
	@Test
    public void crearJSONFailIfJsonIsNotSentInTheRequest() {
		String JsonText = "";
		
		Response response = POST("/Usuarios/crear/json", null, JsonText);

    	assertIsNotFound(response);
        
    }
	
	@Test
    public void crearXMLFailIfJsonIsNotSentInTheRequest() {
		String JsonText = "";
		
		Response response = POST("/Usuarios/crear/xml", null, JsonText);

    	assertIsNotFound(response);
        
    }
	
	
//    !!!A partir de aqui lo hare los test para json porque para xml es
	
//       igual excepto que se cambia la extension y el  contentType !!!!!!!!!!!!
	
	
	@Test
    public void crearJSONFailIfNickAlreadyExit() {
		String JsonText = "{\"nick\":\"nick1\",\"email\":\"email@email.com\",\"urls\":[]}";
		
		Response response = POST("/Usuarios/crear/json", null, JsonText);
    	assertStatus(500, response);
    	assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void crearJSONLFailIfEmailAlreadyExit() {
		String JsonText = "{\"nick\":\"nick\",\"email\":\"email1@email.com\",\"urls\":[]}";
		
		Response response = POST("/Usuarios/crear/json", null, JsonText);

    	assertStatus(500, response);
    	assertContentType("application/json", response);
        assertCharset("utf-8", response);
    }
	
	@Test
    public void crearJSONFailIfDataIsEmpty() {
		String JsonText = "{\"nick\":\"\",\"email\":\"\",\"urls\":[]}";
		
		Response response = POST("/Usuarios/crear/json", null, JsonText);

    	assertStatus(500, response);
    	
	}
    	
   //************************
    	
    	//BORRAR
	
    	@Test
        public void borrarShouldWorkIfUserIsPresented(){
    		    		
    		Response response = DELETE ("/Usuarios/borrar/nick1/json");

        	assertIsOk(response);
            assertContentType("application/json", response);
            assertCharset("utf-8", response);
        }
    	
    	@Test
        public void borrarShouldNotWorkIfUserIsNotPresented(){
    		 tearDown();
    		Response response = DELETE ("/Usuarios/borrar/nick1/json");

        	assertIsNotFound(response);
        }
    	
   //************************************
    	
    	//ACTUALIZAR
    	
    	@Test
        public void actualizarJSONShouldWorkIfUserIsNotPresented() {
    		
    		
    		String JsonText = "{\"nick\":\"nickACT\",\"email\":\"email1ACT@email.com\",\"urls\":[]}";
        	Response response = PUT("/Usuarios/actualizar/1/json",null,JsonText);
        	
        	assertIsNotFound(response);
            
        }
    	
    	//Para el metodo en el que la repuesta fuera ok , lo consegui pero descubriendo el nick que asignaba
    	//el test al elemento en la base de datos, comentando la linea de codigo, "Fixtures.deleteAllModels();" 
    	//de la funcion tearDown(). Asi supe el siguiente id que se asignaria al modelo en la base de datos y me
    	//lo encontraba.
    	
    	//Tambien lo intente recuperando el nick en tiempo de ejecucion con una JPAQuery , pero 
    	//el contexto de JPA no esta en ejecucion Test.
    	
    	/*@Test
        public void actualizarJSONShouldWorkIfUserIsPresented() {
    		
    		
    		String JsonText = "{\"nick\":\"nickACT\",\"email\":\"email1ACT@email.com\",\"urls\":[]}";
        	Response response = PUT("/Usuarios/actualizar/"IdDelUsuarioModeloIntroducidoPorElTest"/json",null,JsonText);
        	
        	assertIsOk(response);
            assertContentType("application/json", response);
            assertCharset("utf-8", response);
            
        }*/
    	  	
}
	
	

