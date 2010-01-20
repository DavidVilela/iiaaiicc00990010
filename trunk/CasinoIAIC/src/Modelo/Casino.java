package Modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import Controlador.Controlador;
import Modelo.Busquedas.*;
import Modelo.Juegos.*;
import Swing.Swing;

/**
 * @author jga
 *
 */
public class Casino {

	protected LinkedList<ObservadorPartida>observadores= new LinkedList<ObservadorPartida>();
	private int vidas;
	private int zonaActual;
	private ArrayList<Zona> zonas;
	private Juego juegos[];
	private Busqueda busquedas[];
	private FicheroTxt txt;
	
	/**
	 * N�mero m�ximo de b�squedas para resolver el juego
	 */
	private final int MAXbusquedas=8;
	
	/**
	 * N�mero m�ximo de juegos disponibles para resolver
	 */
	private final int MAXJuegos=5;
	
	/**
	 * N�mero m�ximo de zonas del casino
	 */
	private final int MAXZonas=this.MAXJuegos*this.MAXbusquedas;
	
	/**
	 * N�mero de salidas
	 */
	private final int MAXSalidas=3;
	
	public Casino(){
		this.vidas=29000;
		this.zonaActual=-1;
		this.initJuegos();
		this.initBusquedas();
		this.initZonas();
		this.generaCaminos();
		txt= new FicheroTxt();
	}
	
	private void initBusquedas() {
		this.busquedas=new Busqueda[this.MAXbusquedas];
		for (int i=0; i<this.MAXbusquedas; i++){
			switch (i){
			case 0:
				this.busquedas[i]=new AEstrella();
				break;
			case 1:
				this.busquedas[i]=new CosteUniforme();
				break;
			case 2:
				this.busquedas[i]=new EscaladaMaximaPendiente();
				break;
			case 3: 
				this.busquedas[i]=new PrimeroAnchura();
				break;
			case 4: 
				this.busquedas[i]=new PrimeroProfundidad();
				break;
			case 5: 
				this.busquedas[i]=new ProfundidadIterativa();
				break;
			case 6: 
				this.busquedas[i]=new ProfundidadLimitada();
				break;
			case 7: 
				this.busquedas[i]=new Voraz();
				break;
			}
		}
	}

	private void initJuegos() {
		this.juegos=new Juego[this.MAXJuegos];
		for (int i=0; i<this.MAXJuegos; i++){
			switch (i){
			case 0:
				this.juegos[i]=new Garrafas();
				break;
			case 1:
				this.juegos[i]=new Hanoi();
				break;
			case 2:
				this.juegos[i]=new LoboCabraCol();
				break;
			case 3: 
				this.juegos[i]=new Misioneros();
				break;
			case 4: 
				this.juegos[i]=new Puzzle8();
				break;
			}
		}
	}

	public boolean jugar(){
		escribeEstado("fas");
		escribeEstado("asfasf");
		escribeEstado("fas");
		escribeEstado("asfasf");
		escribeEstado("fas");
		escribeEstado("asfasf");
		escribeEstado("fas");
		escribeEstado("asfasf");
		txt.escribeFichero("probando");
		txt.escribeFichero("probando2");
		txt.escribeFichero("probando3");
		txt.escribeFichero("probando4");
		return true;
	}
	
	/**
	 * Inicializa las zonas asignando aleatoriamente y sin repetir un juego y una b�squeda 
	 * a cada una. Por lo tanto se crean (MAXJuegos*MAXBusquedas) zonas.
	 */
	private void initZonas(){
		
		/* Array auxiliar que utilizamos para la asignaci�n de juegos y zonas */
		/* De 0 a MAXZonas-1 (todas las combinaciones posibles) */
		boolean usadas[][]=new boolean[this.MAXJuegos][this.MAXbusquedas];
		for (int i=0; i<this.MAXJuegos; i++){
			for (int j=0; j<this.MAXbusquedas; j++){
				usadas[i][j]=false;
			}
		}
		
		/* Inicializamos la lista de zonas */
		zonas=new ArrayList<Zona>(this.MAXZonas);
		/* variables auxiliares 
		 * nJ: n�mero de juego asignado
		 * nB: n�mero de b�squeda asignada
		 * asignada: posici�n generada aleatoriamente entre 0 y MAXZonas-1
		 */
		int nJ, nB, asignada=0;
		/* Para cada zona */
		for (int i=0; i<this.MAXZonas; i++){
			/* Genera un n�mero aleatorio del array usadas */
			Random r=new Random();
			asignada=r.nextInt(this.MAXZonas);
			nJ=asignada%this.MAXJuegos;
			nB=(int)asignada/this.MAXJuegos;
			/* Comprueba que no ha sido usada previamente */
			while (usadas[nJ][nB]){
				asignada=r.nextInt(this.MAXZonas);
				nJ=asignada%this.MAXJuegos;
				nB=(int)asignada/this.MAXJuegos;
			}
			/* asigna el juego y la b�squeda */
			this.zonas.add(new Zona(nJ,nB,0,0));
			/* marcado */
			usadas[nJ][nB]=true;
		}
		
		/* marcamos las tres �ltimas zonas como salida */
		for (int i=this.MAXZonas-this.MAXSalidas; i<this.MAXZonas; i++){
			this.zonas.get(i).setFin(true);
		} 
	}
	
	/**
	 * Genera un grafo conexo entre las zonas de una forma determinada:
	 * (Sea el n�mero de zonas de 0 a MAXZonas-MAXSalidas-1)
	 * - Primeras 20% zonas: cada zona i de este rango enlaza con 4 zonas j tal que j>i
	 * - Siguientes 60% zonas: cada zona i de este rango enlaza con 3 zonas tal que j>i y con una tal que j<i
	 * - Ultimas 20% zonas: cada zona i de este rango enlaza con 4 zonas j tal que j<i
	 * - Cada zona conectada no dista m�s de un 20% de la longitud total de las zonas. Ejemplo: sean 100 zonas y
	 * est� generando la zona 53, no habr� conexiones para esta zona m�s lejos del rango 33-73 (excluyendo la 53).
	 * Por lo tanto, todas las zonas tienen camino a otras zonas, excepto las 3 �ltimas, que salen al exterior.
	 */
	private void generaCaminos() {
		/* rango en el que se van a generar las salidas para una zona. 20% del total */
		int ambito=(int) (this.MAXZonas*0.2);
		int finRango1=ambito;
		int nHijo;
		for (int i=0; i<finRango1; i++){
			Random r=new Random();
			for (int j=0; j<4; j++){
				nHijo=r.nextInt(ambito)+i+1;
				this.zonas.get(i).addHijo(nHijo);
			}
			
		}
	}

	public void mostrarLog() {
		txt.open();
	}
	
	/**
	 * Llama al m�todo escribeEstado de cada uno de los observadores.
	 *
	 */	
	public void escribeEstado(String estado)
	{ 
			for (ObservadorPartida o: observadores)
				o.escribeEstado(estado);
	}
	
	/**
	 * 
	 * @param obspart A�ade un observador a la lista de observadores,
	 * si este observador ya existia no lo introduce.
	 */
	
	public void addObserver(ObservadorPartida obspart)
	{
	   if (!observadores.contains(obspart))
	      if (observadores!=null)
		     observadores.add(obspart);
	}
	
	/**
	 * 
	 * @param obspart Elimina un observador de la lista de observadores.
	 */
	
	public void removeObserver(ObservadorPartida obspart)
	{   if (observadores!=null)
			observadores.remove(obspart);
	
	}
	
	public static void main(String[] args) 
	{
		Casino test=new Casino();
		System.out.println("Fin creaci�n de casino");
	}

}
