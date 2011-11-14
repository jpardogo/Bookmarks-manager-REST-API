package controllers;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.omg.PortableInterceptor.SUCCESSFUL;

import com.google.gson.Gson;
import com.sun.net.httpserver.Authenticator.Success;

import play.data.validation.Error;
import play.mvc.Controller;
import models.Tag;
import models.Url;
import models.Usuario;

public class Tags extends CRUD {

	//Recibe la key de busqueda que sera una tag
	public static void buscar(){
		
		String tag = params.get("key");
		
		Tag t = Tag.find("byTag",tag).first();
		
		if (t == null){
							
			notFound();
		}
		
		if (t.urls.size() == 0){
			
			
			renderArgs.put("info", "no webs");
		} else {
			
			renderArgs.put("info", t.urls.size());
			
		} 
		
		setContentType(request.format);
		render(t);
		
	}
	
	
	//Recibe tag y dir
	public static void crear(){
		
		
		Tag tag = new Gson().fromJson(new InputStreamReader(request.body), Tag.class);
		
		notFoundIfNull(tag);
		
		String d="";
		for(Url url : tag.urls){
			
			d = url.dir;
			break;
		}
		
		StringBuffer dir1 = new StringBuffer("http://");
		String dir = dir1.append(d).toString();
		
		
		Url url = Url.find("byDir", dir).first();
		
		if(url==null){
			notFound();
			
			
		}
				
		Tag t= Tag.find("byTag", tag.tag).first();
				
		
		if(t == null){
			
			t = new Tag(tag.tag);
			url.tags.add(t);
			
			if (!t.validateAndCreate()) {
				renderError();
			}
			if(!url.validateAndSave()){
				renderError();
			}
			
		
		}else {
			
			if (url.tags.contains(t)){
				response.status=500;
				renderArgs.put("info", "la web ya tiene este tag");
				setContentType(request.format);
				render(url);
			}else{
				
				url.tags.add(t);
				if (!url.validateAndSave()) {
					renderError();
				}
				
			}
			
			
		}
		
		
		renderArgs.put("info", "ok");
		setContentType(request.format);
		render(url);
		
	}
	
	
	//Recibe tag y String dir
	public static void borrar(){
		
		
		
		String tag = params.get("tag");
		StringBuffer dir1 = new StringBuffer("http://");
		String dir = dir1.append(params.get("dir")).toString();
		
		Tag t = Tag.find("byTag", tag).first();
		
		if(t==null){
			
			notFound();
			
		}
		
		Url url = Url.find("byDir",dir).first();
			
		if(url == null){
			
			notFound();
		}
		
		if(!url.removeTgUr(t)){
			response.status=500;
			renderArgs.put("info", "Tag no definida en esta web, luego no se puede borrar");
			setContentType(request.format);
			render(url);
		}
					
		
		renderArgs.put("info", "eliminada");
		setContentType(request.format);
		render(url);
				
	}
	
	private static void setContentType(String format) {
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
