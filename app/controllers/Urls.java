package controllers;

import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import com.sun.mail.imap.protocol.Status;

import models.Url;
import models.Usuario;
import play.data.validation.Error;
import play.db.helper.SqlQuery.Concat;
import play.mvc.Controller;

public class Urls extends CRUD {

	//Recibe la key de busqueda que sera una dir
	public static void buscar(){
		
		
		StringBuffer dir1 = new StringBuffer("http://");
		String dir = dir1.append(params.get("key")).toString();
		
		Url url = Url.find("byDir",dir).first();
		if (url == null){
							
			notFound();
		}
		
		if (url.users.size() == 0){
			
			
			renderArgs.put("info", "No users");
		} else {
			
			renderArgs.put("info", url.users.size());
			
		} 
		
		setContentType(request.format);
		render(url);
		
	}
	
	//Recibe un JSON de usuario con su nick, email(no es obligatorio) y la dir que se le quiere agregar
	public static void crear(){
		
		Usuario user = new Gson().fromJson(new InputStreamReader(request.body), Usuario.class);
		
		notFoundIfNull(user);
		
		String d="";
		for(Url url : user.urls){
			
			d = url.dir;
			break;
		}
		
		StringBuffer dir1 = new StringBuffer("http://");
		String dir = dir1.append(d).toString();
		
		Usuario u = Usuario.find("byNick", user.nick).first();
		
		if(u==null){
			notFound();
			
		}
		
			
	
		Url url= Url.find("byDir", dir).first();
		
		if(url == null){
			
			url = new Url(dir);
			url.users.add(u);
		
			
			if (!url.validateAndCreate()) {
				renderError();
			}
		}else {
			
			if (url.users.contains(u)){
				response.status=500;
				renderArgs.put("info", "usuario ya sigue la web");
				setContentType(request.format);
				render(u);
				
			}else{
				
				url.users.add(u);
				if (!url.validateAndSave()) {
					renderError();
				}
			}
			
			
		}
		
		
		
		
		renderArgs.put("info", "ok");
		setContentType(request.format);
		render(u);
		
		
	}
	
	
	//Recibe el nick del usuario al que se le borra la dir, que tambien sera recibida
	public static void borrar(){
		
		int flag =0;
		
		String nick = params.get("nick");
				
		StringBuffer dir1 = new StringBuffer("http://");
		String dir = dir1.append(params.get("dir")).toString();
		
		
		
		
		
		Usuario u = Usuario.find("byNick", nick).first();
		
		notFoundIfNull(u);
		
		
		Url url = Url.find("byDir",dir).first();
			
		notFoundIfNull(url);
		
		if(!url.removeUsUr(u,flag)){
			response.status=500;
			renderArgs.put("info", "usuario esta siguiendo esta web luego no se puede eliminar");
			setContentType(request.format);
			render(u);
		}
					
		
		renderArgs.put("info", "eliminada");
		setContentType(request.format);
		render(u);
				
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
