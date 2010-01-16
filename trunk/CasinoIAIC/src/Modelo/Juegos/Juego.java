package Modelo.Juegos;

import java.util.Vector;

/**
 * Clase abstracta Juego. Proporciona la estructura com�n de todos los juegos, que despu�s implementar�n los m�todos abstractos seg�n
 * los estados y operadores de cada uno. Cada instancia de �sta clase, o de las que heredan de ella, representa un �nico estado de juego.
 * @author Pablo Acevedo
 *
 */
public abstract class Juego{
	/**
	 * valor heur�stico del juego. -1 si el juego no soporta heur�sticas
	 */
	protected int valorHeur;
	/**
	 * coste acumulado desde el estado inicial
	 */
	protected int coste;
	/**
	 * profundidad del nodo en el �rbol de b�squeda
	 */
	protected int profundidad;
	/**
	 * operadores que se han aplicado desde el estado inicial
	 */
	protected String camino;
	
	/**
	 * @return valor heur�stico del estado
	 */
	public int getValorHeur(){
		return valorHeur;
	}
	
	/**
	 * @return coste acumulado hasta llegar a este estado
	 */
	public int getCoste(){
		return coste;
	}
	
	/**
	 * @return profundidad del estado en el �rbol de b�squeda
	 */
	public int getProfundidad(){
		return profundidad;
	}
	
	/**
	 * @return camino desde el inicio hasta este estado 
	 */
	public String getCamino(){
		return camino;
	}
	
	/**
	 * @param s string que sobreescribe el camino que habia antes
	 */
	public void setCamino(String s){
		camino=s;
	}
	
	/**
	 * @return vector con todos los posibles sucesores del estado
	 */
	public abstract Vector expandir();
	
	/**
	 * cambiar el estado del juego al objetivo
	 */
	public abstract void setGoal();
	
	public abstract boolean equals(Object o);

	public abstract String toString();
}