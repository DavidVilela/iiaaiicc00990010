/**
 * 
 */
package Modelo;

import java.util.Random;

/**
 * @author Pablo Acevedo Montserrat
 * @author Alfredo D�ez Zamarro
 * @author Jorge Guirado Alonso
 * Representa un juego de b�squeda en espacio de estados. Cada juego implementa
 * 8 b�squedas distintas para resolver el problema. �stas b�squedas se van 
 * asignando aleatoriamente a petici�n.
 */
public abstract class Juego {
	/**
	 * N�mero m�ximo de b�squedas para resolver el juego
	 */
	private final int MAXbusquedas=8;
	/**
	 * N�mero de b�squedas asignadas aleatoriamente.
	 */
	private int NUMbusquedas;
	/**
	 * Indica cuales de las 8 b�squedas han sido asignadas
	 */
	private boolean usadas[];
	
	
	/**
	 * Constructora. Inicializa los atributos.
	 */
	public Juego(){
		this.usadas=new boolean[this.MAXbusquedas];
		this.NUMbusquedas=0;
		for (int i=0; i<this.MAXbusquedas; i++){
			this.usadas[i]=false;
		}
	}
	
	/**
	 * Indica si est�n asignadas todas las busquedas.
	 * @return true si no quedan busquedas a asignar.
	 */
	public boolean completo(){
		return this.NUMbusquedas==0;
	}
	
	/**
	 * Asigna una busqueda aleatoriamente
	 * @return integer de 0 a 7 que indica la busqueda asignada. -1 en caso de 
	 * estar todas asignadas.
	 */
	public int getNumBus(){
		if (this.completo()) return -1;
		/*
		 * Generamos aleatoriamente un numero entre 0 y 7
		 */
		Random r=new Random();
		int indice=r.nextInt(this.MAXbusquedas);
		/*
		 * Seguimos generando numeros hasta encontrar una busqueda libre
		 */
		while (this.usadas[indice]==true){
			indice=r.nextInt(this.MAXbusquedas);
		}
		return indice;
	}
	
	/**
	 * Lanza la resolucion del juego con el tipo de busqueda especificada.
	 * @param busqueda integer (0-7) con el tipo de busqueda
	 * @return true si se ha encontrado solucion.
	 */
	public abstract boolean lanzaJuego(int busqueda);
}
