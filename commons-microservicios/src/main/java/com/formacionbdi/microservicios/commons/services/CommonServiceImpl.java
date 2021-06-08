package com.formacionbdi.microservicios.commons.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;




public class CommonServiceImpl<E, R extends PagingAndSortingRepository<E,Long>> implements CommonService<E> { //Todos nuestros repositories estan heredando de crud Repositori
     
	@Autowired
	protected R repository;
	
	@Override
	@Transactional(readOnly = true) // Los metodos de consulta siempre se anotan con transactional  de lectura.
	public Iterable<E> findAll() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<E> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional // Transactional solo, permite modificar la tabla 
	public E save(E entity) {
		return repository.save(entity);
	}

	@Override
	@Transactional // Transactional solo, permite modificar la tabla 
	public void deleteById(Long id) {
		repository.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<E> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

}