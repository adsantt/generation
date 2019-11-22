package com.turismo.apimonumentos.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "monumentos_historicos")
public class Monumento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	@PositiveOrZero(message = "El campo \"Contador Usuarios\" debe ser entero y mayor o igual a 0.")
	private int contador_usuarios;
	@Column
	@NotEmpty(message = "El campo \"Nombre\" no puede estar vacio.")
	private String nombre_construccion;
	@Column
	@NotEmpty(message = "El campo \"Fecha de construccion\" no puede estar vacio.")
	private String fecha_construccion;
	@Column
	@NotEmpty(message = "El campo \"Duracion de la construccion\" no puede estar vacio.")
	private String duracion_construccion;
	@Column
	@NotEmpty(message = "El campo \"Tipo de arquitectura\" no puede estar vacio.")
	private String tipo_arquitectura;
	@Column
	@NotEmpty(message = "El campo \"Arquitecto\" no puede estar vacio.")
	private String arquitecto;
	@Column
	@NotEmpty(message = "El campo \"Informacion\" no puede estar vacio.")
	private String informacion;
	@Column
	@NotEmpty(message = "El campo \"Fun facts/Leyendas\" no puede estar vacio.")
	private String leyendas;
	@Column
	@NotNull(message = "El campo \"Restauraciones\" no puede estar vacio.")
	private boolean contiene_restauraciones;
	@Column
	@NotEmpty(message = "El campo \"Uso\" no puede estar vacio.")
	private String uso_edificio;
	@Column
	private boolean eliminado;
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fecha_registro;
	@Column
	private String foto;
	
	
	public Monumento() {}
	
	/**
	 * 
	 * @param id (int)
	 * @param contador_usuarios (int)
	 * @param nombre_construccion (String)
	 * @param fecha_construccion (String)
	 * @param duracion_construccion (String)
	 * @param tipo_arquitectura (String)
	 * @param arquitecto (String)
	 * @param informacion (String)
	 * @param leyendas (String)
	 * @param contiene_restauraciones (boolean)
	 * @param uso_edificio (String)
	 * @param eliminado (boolean)
	 * @param fecha_registro (Date)
	 * @param foto (String)
	 */
	public Monumento(int id, int contador_usuarios, String nombre_construccion, String fecha_construccion,
			String duracion_construccion, String tipo_arquitectura, String arquitecto, String informacion,
			String leyendas, boolean contiene_restauraciones, String uso_edificio, boolean eliminado,
			Date fecha_registro, String foto) {
		this.id = id;
		this.contador_usuarios = contador_usuarios;
		this.nombre_construccion = nombre_construccion;
		this.fecha_construccion = fecha_construccion;
		this.duracion_construccion = duracion_construccion;
		this.tipo_arquitectura = tipo_arquitectura;
		this.arquitecto = arquitecto;
		this.informacion = informacion;
		this.leyendas = leyendas;
		this.contiene_restauraciones = contiene_restauraciones;
		this.uso_edificio = uso_edificio;
		this.eliminado = eliminado;
		this.fecha_registro = fecha_registro;
		this.foto = foto;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getContador_usuarios() {
		return contador_usuarios;
	}
	public void setContador_usuarios(int contador_usuarios) {
		this.contador_usuarios = contador_usuarios;
	}
	public String getNombre_construccion() {
		return nombre_construccion;
	}
	public void setNombre_construccion(String nombre_construccion) {
		this.nombre_construccion = nombre_construccion;
	}
	public String getFecha_construccion() {
		return fecha_construccion;
	}
	public void setFecha_construccion(String fecha_construccion) {
		this.fecha_construccion = fecha_construccion;
	}
	public String getDuracion_construccion() {
		return duracion_construccion;
	}
	public void setDuracion_construccion(String duracion_construccion) {
		this.duracion_construccion = duracion_construccion;
	}
	public String getTipo_arquitectura() {
		return tipo_arquitectura;
	}
	public void setTipo_arquitectura(String tipo_arquitectura) {
		this.tipo_arquitectura = tipo_arquitectura;
	}
	public String getArquitecto() {
		return arquitecto;
	}
	public void setArquitecto(String arquitecto) {
		this.arquitecto = arquitecto;
	}
	public String getInformacion() {
		return informacion;
	}
	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}
	public String getLeyendas() {
		return leyendas;
	}
	public void setLeyendas(String leyendas) {
		this.leyendas = leyendas;
	}
	public boolean isContiene_restauraciones() {
		return contiene_restauraciones;
	}
	public void setContiene_restauraciones(boolean contiene_restauraciones) {
		this.contiene_restauraciones = contiene_restauraciones;
	}
	public String getUso_edificio() {
		return uso_edificio;
	}
	public void setUso_edificio(String uso_edificio) {
		this.uso_edificio = uso_edificio;
	}
	public boolean isEliminado() {
		return eliminado;
	}
	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
	public Date getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
	/**
	 * Este metodo permite que el monumento se actualice
	 * con los parametros de otro monumento
	 * 
	 * @param m (Monumento)
	 */
	public void actualizar(Monumento m) {
		this.id = m.getId();
		this.contador_usuarios = m.getContador_usuarios();
		this.nombre_construccion = m.getNombre_construccion();
		this.fecha_construccion = m.getFecha_construccion();
		this.duracion_construccion = m.getDuracion_construccion();
		this.tipo_arquitectura = m.getTipo_arquitectura();
		this.arquitecto = m.getArquitecto();
		this.informacion = m.getInformacion();
		this.leyendas = m.getLeyendas();
		this.contiene_restauraciones = m.isContiene_restauraciones();
		this.uso_edificio = m.getUso_edificio();
		this.eliminado = m.isEliminado();
		this.fecha_registro = m.getFecha_registro();
		this.foto = m.getFoto();
	}
	
	@Override
	public String toString() {
		return "Monumento [id=" + id + ", contador_usuarios=" + contador_usuarios + ", nombre_construccion="
				+ nombre_construccion + ", fecha_construccion=" + fecha_construccion + ", duracion_construccion="
				+ duracion_construccion + ", tipo_arquitectura=" + tipo_arquitectura + ", arquitecto=" + arquitecto
				+ ", informacion=" + informacion + ", leyendas=" + leyendas + ", contiene_restauraciones="
				+ contiene_restauraciones + ", uso_edificio=" + uso_edificio + ", eliminado=" + eliminado
				+ ", fecha_registro=" + fecha_registro + ", foto=" + foto + "]";
	}

}