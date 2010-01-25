package Modelo.Juegos;

import java.util.Vector;

/**
 * Juego del mono. En una habitaci�n, hay un mono, una caja y un pl�tano. El mono se encuentra en la puerta, el pl�tano cuelga del techo en el centro de la
 * habitaci�n, y en el otro extremo, bajo una ventana, hay una caja. El objetivo es que el mono pueda coger el pl�tano, utilizando para ello la caja. El mono
 * puede moverse por la habitaci�n, empujar la caja si est�n en la misma posici�n, y subirse a la caja.
 * @author Pablo Acevedo, Alfredo D�ez, Jorge Guirado
 *
 */
public class Mono extends Juego{
	/**
	 * Posici�n del platano, en horizontal, nunca puede estar en el suelo
	 */
	private int P;
	/**
	 * Posici�n de la caja, en horizontal, siempre est� en el suelo
	 */
	private int C;
	/**
	 * Posici�n del mono, en horizontal
	 */
	private int Mh;
	/**
	 * Posici�n del mono, en vertical
	 */
	private int Mv;
	
	/**
	 * Constructora de estado inicial
	 */
	public Mono(){
		P=2;
		Mh=1;
		Mv=0;
		C=3;
		valorHeur=this.Heuristica();
		coste=0;
		profundidad=0;
		camino="";
	}
	
	/**
	 * Constructora de sucesor
	 * @param juego estado padre
	 * @param cos coste acumulado
	 * @param cam camino hasta el estado
	 */
	private Mono(Mono juego,int cos,String cam){
		P=juego.P;
		Mh=juego.Mh;
		Mv=juego.Mv;
		C=juego.C;
		profundidad=juego.profundidad+1;
		coste=juego.coste+cos;
		camino=juego.camino+cam;
		
	}
	
	/**
	 * M�todo que comprueba si el operador dado por 's' podr�a aplicarse.
	 * @param s operaci�n a comprobar
	 * @return true si se puede hacer la operaci�n, false si no se puede
	 */
	private boolean puedo(String s){
		// mover mono a la derecha
		if (s.equals("Md")){
			if (Mh>3 || Mv==1)
				return false;
		}
		// mover mono a la izquierda
		if (s.equals("Mi")){
			if (Mv<1 || Mv==1)
				return false;
		}
		// subir mono a caja
		if (s.equals("S")){
			if (Mh!=C || Mv==1)
				return false;
		}
		// bajar mono de la caja
		if (s.equals("B")){
			if (Mv==0)
				return false;
		}
		// empujar caja a la derecha
		if (s.equals("Ed")){
			if (Mh!=C || Mh>3 || Mv==1)
				return false;
		}
		// empujar caja a la izquierda
		if (s.equals("Ei")){
			if (Mh!=C || Mh<1 || Mv==1)
				return false;
		}
		return true;
	}
	
	/**
	 * Operador que mueve el mono i posiciones desde donde est�
	 * @param i n�mero de posiciones que se va a mover(+ si derecha, - si izquierda)
	 */
	private void mover(int i){
		Mh+=i;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que mueve el mono junto con la caja i posiciones
	 * @param i n�mero de posiciones que se va a mover(+ si derecha, - si izquierda)
	 */
	private void empujar(int i){
		Mh+=i;
		C+=i;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que sube o baja el mono de la caja
	 * @param i 1 si sube, -1 si baja
	 */
	private void subir(int i){
		Mv+=i;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Funci�n heur�stica. Calcula el n�mero de movimientos que har�an falta para llegar al objetivo, desde la situaci�n actual
	 * @return n�mero m�nimo de movimientos para llegar al estado objetivo
	 */
	private double Heuristica(){
		double d=0;
		// mono sobre la caja
		if (Mv==1){
			// mono y caja no estan debajo del platano
			if (Mh!=P){
				d+=1; // bajar de la caja
				d+=Math.abs(P-Mh); // camino desde la caja hasta el platano
				d+=1; // subir a la caja
			}
		}
		// Mono en el suelo
		else{
			// mono en lugar distinto de la caja
			if (Mh!=C){
				d+=Math.abs(C-Mh); // ir hasta la caja
				d+=Math.abs(P-C); // empujar la caja
				d+=1; // subir a la caja
			}
			// mono esta en el lugar de la caja
			else{
				d+=Math.abs(P-Mh); // empujar la caja
				d+=1; // subir a la caja
			}
		}
		return d;
	}
	
	public Vector<Juego> expandir() {
		Vector<Juego> sucesores=new Vector<Juego>(0,1);
		Mono estado;
		if (this.puedo("Mi")){
			estado=new Mono(this,1,"Mover mono a la izquierda\n");
			estado.mover(-1);
			sucesores.add(estado);
		}
		if (this.puedo("Md")){
			estado=new Mono(this,1,"Mover mono a la derecha\n");
			estado.mover(1);
			sucesores.add(estado);
		}
		if (this.puedo("S")){
			estado=new Mono(this,1,"Subir mono a la caja\n");
			estado.subir(1);
			sucesores.add(estado);
		}
		if (this.puedo("B")){
			estado=new Mono(this,1,"Bajar mono de la caja\n");
			estado.subir(-1);
			sucesores.add(estado);
		}
		if (this.puedo("Ed")){
			estado=new Mono(this,2,"Empujar caja a la derecha\n");
			estado.empujar(1);
			sucesores.add(estado);
		}
		if (this.puedo("Ei")){
			estado=new Mono(this,2,"Empujar caja a la izquierda\n");
			estado.empujar(-1);
			sucesores.add(estado);
		}
		return sucesores;
	}

	public boolean isGoal() {
		return valorHeur==0;
	}

	public boolean equals(Object o) {
		if (this==o)
			return true;
		if ((o==null) || (this.getClass()!=o.getClass()))
			return false;
		Mono estado=(Mono)o;
		return (Mh==estado.Mh && Mv==estado.Mv && P==estado.P && C==estado.C);
	}

	public String toString() {
		return "Mono\nMono horizontal: "+Mh+"; Mono vertical: "+Mv+"; Caja: "+C+"; Pl�tano: "+P+"\n";
	}
	
}