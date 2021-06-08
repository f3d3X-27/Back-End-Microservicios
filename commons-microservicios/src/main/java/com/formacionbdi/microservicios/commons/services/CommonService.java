package com.formacionbdi.microservicios.commons.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface CommonService<E> { // E Seria el tipo generico
	
	public Iterable<E> findAll();
	
	public Page<E> findAll(Pageable pageable); // Contiene toda la informacion de la paginacion que queremos realizar en la base de datos
	
	public Optional<E> findById(Long id);
	
	public E save(E entity);
	
	public void deleteById(Long id);

}
