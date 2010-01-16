package Modelo.Busquedas;

import Modelo.Juegos.Juego;

/**
 * Interfaz que da un m�todo de resoluci�n general para cualquier b�squeda. Cada tipo de b�squeda implementar� el m�todo seg�n su estrategia.
 * @author Pablo Acevedo
 *
 */
public interface Busqueda{
	/**
	 * Resuelve un juego con una busqueda
	 * @param juego inicial
	 * @param juego objetivo
	 * @return juego al que se ha llegado
	 */
	public Juego resuelve(Juego inicial,Juego goal);
}

