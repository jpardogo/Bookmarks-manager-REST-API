package controllers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;

import oauth.signpost.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import models.Url;





import models.Usuario;


import play.data.validation.Email;
import play.data.validation.Error;
import play.data.validation.Required;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.results.NotFound;
import play.mvc.results.RenderTemplate;
import play.utils.HTTP;




public class Usuarios extends CRUD {
	
	
	//Recibe la key de busqueda que sera un nick
	public static void buscar(){
			
			
			
			String key = params.get("key");
			
			Usuario u = Usuario.find("byNick",key).first();
			if (u == null){
				
				setContentType(request.format);
				notFound(key);
			}
			
			if (u.urls.size() == 0){
				
				
				renderArgs.put("info", "No webs");
			} else {
				
				renderArgs.put("info", u.urls.size());
				
			} 
			
			setContentType(request.format);
			render(u);
			
	}
	
	//Recibe un JSON de usuario con su nick e email
	public static void crear(){
		
		
		Usuario user = new Gson().fromJson(new InputStreamReader(request.body), Usuario.class);
			
		notFoundIfNull(user);
		
		if (Usuario.find("email = ? or nick = ?", user.email, user.nick).first() != null) {
			
			response.status=500;
			renderArgs.put("info", "repetido");
			setContentType(request.format);
			render(user);
			
		}else{
				
				
				user = new Usuario(user.nick,user.email);
				
				if (!user.validateAndCreate()) {
					renderError();
				}
								
				renderArgs.put("info", "ok");  		
				setContentType(request.format);//Content-Type pertenece al protocolo HTTP (por eso lo de 'http-equiv', que podría ser 'tomá esta etiqueta META como el equivalente a una cabecera http').
												//Es la forma en la que quien envía datos (normalmente el servidor) le pueda decir a quien los recibe (el navegador, por lo general) qué tipo de datos está enviando.
		    	render(user);
					
		   }
	}
		
	
	
	
	
	
	
	
	
	//Recibe el nick del usuario a borrar
	public static void borrar(){
		
		int flag =1;
		String nick = params.get("nick");
		
		Usuario u = Usuario.find("byNick",nick).first();
		
		notFoundIfNull(u);
		
		if(u.urls.size()!=0){
			for(Url url : u.urls){
				
				
				url.removeUsUr(u,flag);
				
			}
		}else{
			
			u.delete();
		}
		
		
		
		renderArgs.put("info", "eliminado");
		setContentType(request.format);
		
		
		render(u);
			
	}
	
	public static void actualizar() {
		
		
		Long id = Long.parseLong(params.get("id"));
		
		Usuario user = new Gson().fromJson(new InputStreamReader(request.body), Usuario.class);
		
		notFoundIfNull(user);
		
		Usuario u = setColumnsWithParams(Usuario.<Usuario>findById(id),user.nick,user.email);
		
		if (u == null) {
			notFound();
			
		} else {
			if (!u.validateAndSave()) {
				renderError();
			}
			renderArgs.put("info", "actualizado");
			setContentType(request.format);
			render(u);
		}
	}
	
	private static Usuario setColumnsWithParams(Usuario u,String nick,String email) {
		if (u != null) {
			
			if (email != null) {
				u.email = email;
			}

			
			if (nick != null) {
				u.nick = nick; 
			}
		}
		return u;
	}
	
	private static String setContentType(String format) {
		String contentType;

		if (format.equalsIgnoreCase("json")) {
			contentType = "application/json; charset=utf-8";
			
		} else if (format.equalsIgnoreCase("xml")) {
			contentType = "text/xml; charset=utf-8";
		} else {
			contentType = null;
		}

		if (contentType != null) {
			response.setContentTypeIfNotSet(contentType);
			
		}
		return contentType;
	}
	
	private static void renderError() {
		response.status = 500;
		StringBuffer buf = new StringBuffer();
		for(Error error : validation.errors()) {
			buf.append(error.message());
			buf.append("\n");
		}
		renderText(buf);
	}
	
	
}
