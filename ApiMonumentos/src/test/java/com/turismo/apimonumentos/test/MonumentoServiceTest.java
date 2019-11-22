package com.turismo.apimonumentos.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RunWith(JUnitPlatform.class)
@TestMethodOrder(OrderAnnotation.class)
public class MonumentoServiceTest {

	HttpResponse<JsonNode> jsonResponse;
	
	@Test
	@Order(1)
	public void obtenerMonumentos() {
		jsonResponse=null;
		try {
			jsonResponse = Unirest
					.get("http://localhost:8080/ApiMonumentos/api/monumentos")
					.header("accept", "application/json").asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(jsonResponse);
		assertEquals(200, jsonResponse.getStatus());
	}
	
	@Test
	@Order(2)
	public void guardarMonumentoTest() {
		jsonResponse=null;
		HashMap<String, Object> bodyRequest= new HashMap<String, Object>(){
			{
				put("contador_usuarios", 0);
				put("nombre_construccion", "Monumento Prueba");
				put("fecha_construccion", new Date().toString());
				put("duracion_construccion", "1 día");
				put("tipo_arquitectura", "Prueba");
				put("arquitecto", "Usuario");
				put("informacion", "Este es un registro para testing");
				put("leyendas", "Testing de los servicios de Monumento");
				put("contiene_restauraciones", false);
				put("uso_edificio", "Para testing de los servicios de monumento");
	            put("foto", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3"
	            		+ "AANd9GcQwxKnUeLv8j0QWnouDugT1qiZ-Cw_FioEScsAqhrWVpVsbZ76l");
			}
		};
		//JSONObject jsonBody=new JSONObject(bodyRequest);
		try {
			jsonResponse= Unirest
					.post("http://localhost:8080/ApiMonumentos/api/monumentos")
					.header("Content-Type", "application/json")
					.body(new JSONObject(bodyRequest)).asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(jsonResponse);
		assertEquals(201, jsonResponse.getStatus());
	}

	@Test
	@Order(3)
	public void obtenerMonumentoPrueba() {
		jsonResponse=null;
		try {
			jsonResponse=Unirest
					.get("http://localhost:8080/ApiMonumentos/api/monumentos/monumento_prueba")
					.header("accept", "application/json").asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonArray= jsonResponse.getBody().getObject().getJSONObject("data");		
		String nombre=jsonArray.getString("nombre_construccion");
		assertNotNull(jsonResponse);
		assertEquals("Monumento Prueba", nombre);
	}
	
}
