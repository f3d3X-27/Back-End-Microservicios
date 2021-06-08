package com.formacionbdi.microservicios.commons.examenes.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="examenes")
public class Examen {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(min = 4, max = 30) // Para validar que tenga un minimo de 4 caracteres y un maximo de 30 caracteres
	private String nombre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_at")
	private Date createAt;
	 
	// OrphanRemoval cualquier pregunta que no este asignada a un examen, sera eliminada 
	
	@JsonIgnoreProperties(value = {"examen"}, allowSetters = true) // por cada pregunta de esta lista, se suprime el atributo examen, para evitar entrar en un ciclo infinito
	@OneToMany(mappedBy="examen" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true ) //Cuando se borra un examen, tambien se borran sus preguntas. (Persist para que se cree el examen con sus preguntas)
	private List<Pregunta> preguntas;
	
	@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY) // Los objetos se validan con NotNull
	@NotNull
	private Asignatura asignaturaPadre;
	
	@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY) // Los objetos se validan con NotNull
	@NotNull
	private Asignatura asignaturaHija;
	
	@Transient
	private boolean respondido;

	public Examen() {
		this.preguntas = new ArrayList<>();
	}

	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<Pregunta> getPreguntas() { 
		return preguntas;
		
	}

	public void setPreguntas(List<Pregunta> preguntas) { // Relaciona las preguntas con el examen, para que no queden null
		this.preguntas.clear();
		preguntas.forEach(this::addPregunta);  // Asignamos cada pregunta al examen, para construir la relacion inversa
		
		
	}
	
	public void addPregunta(Pregunta pregunta) { 
		this.preguntas.add(pregunta);
		pregunta.setExamen(this); // Establecemos la relacion inversa, de lo contrario la foreing key sera nula, (no tiene el examen)
	}
	
	public void removePregunta(Pregunta pregunta) {  // Quitamos la referencia de la pregunta con el examen  y esta se elimina
		this.preguntas.remove(pregunta);
		pregunta.setExamen(null);
    }
	
	

	public Asignatura getAsignaturaPadre() {
		return asignaturaPadre;
	}

	public void setAsignaturaPadre(Asignatura asignaturaPadre) {
		this.asignaturaPadre = asignaturaPadre;
	}

	public Asignatura getAsignaturaHija() {
		return asignaturaHija;
	}

	public void setAsignaturaHija(Asignatura asignaturaHija) {
		this.asignaturaHija = asignaturaHija;
	}

	public boolean isRespondido() { // Como es booleano pone IS en lugar de GET 
		return respondido;
	}

	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}

	@Override
	public boolean equals(Object obj) {
		if(this== obj){
			return true;
		}
		
		if(!(obj instanceof Examen)) {
			return false;
		}
		
		Examen a = (Examen) obj;
		
		return this.id != null && this.id.equals(a.getId());
	}
	
	
	
}
