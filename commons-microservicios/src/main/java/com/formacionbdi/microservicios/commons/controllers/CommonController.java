package com.formacionbdi.microservicios.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import com.formacionbdi.microservicios.commons.services.CommonService;




//@CrossOrigin({"http://localhost:4200"})
public class CommonController<E, S extends CommonService<E>> {
	
	@Autowired
	protected S service;
	
	@GetMapping //Nos permite mapear una ruta URL al metodo.
	public ResponseEntity<?> listar (){ // nos permite pasar objetos, al cuerpo al body del Response
		
		return ResponseEntity.ok().body(service.findAll()); // Pasamos al cuerpo una lista de respuestas
	}
	
	@GetMapping("/pagina") //Nos permite mapear una ruta URL al metodo.
	public ResponseEntity<?> listar (Pageable pageable){ // nos permite pasar objetos, al cuerpo al body del Response
		
		return ResponseEntity.ok().body(service.findAll(pageable)); // Pasamos al cuerpo una lista de respuestas
	}
		
		@GetMapping("/{id}")
		public ResponseEntity<?> ver(@PathVariable Long id){ //Spring extrae de la ruta Url, un fragmento variable de la ruta, usando esta anotacion
			
			Optional<E> o = service.findById(id);
			
			if(o.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(o.get());
		}
		
		@PostMapping  // Peticion
		public ResponseEntity<?> crear(@Valid @RequestBody E entity, BindingResult result){ // indicamos que los datos del request, los coloque en el objeto Alumno
			
			if(result.hasErrors()) {
				return this.validar(result);
				
			}
			E entityDb = service.save(entity);
			return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
				
		}
		
	
		
		@DeleteMapping("/{id}")
		public ResponseEntity<?> eliminar(@PathVariable Long id){
			
			service.deleteById(id);
			return ResponseEntity.noContent().build(); // tipo 204
			
		}
		
		
		protected ResponseEntity<?> validar(BindingResult result){ // Corresponde a cada campo que vamos validando y falla
			
			Map<String, Object> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(),"El campo" + err.getField() + " " + err.getDefaultMessage());
			});
			
			return ResponseEntity.badRequest().body(errores);
		}
		
		
}