package Modelo.Juegos;

import java.util.Vector;

/**
 * Juego de las garrafas. Tenemos 2 garrafas, una de 4L y otra de 3L. El juego consiste en obtener 2L en la garrafa grande y 0L en la peque�a.
 * @author Pablo Acevedo, Alberto D�ez, Jorge Guirado
 *
 */
public class Garrafas extends Juego{
	/**
	 * gra guarda el numero de litros que contiene la garrafa grande
	 */
	private int gra;
	/**
	 * peq guarda el numero de litros que contiene la garrafa peque�a
	 */
	private int peq;
	
	/**
	 * Constructora de estado inicial.
	 */
	public Garrafas(){
		gra=0;
		peq=0;
		valorHeur=this.Heuristica();
		coste=0;
		profundidad=0;
		camino="";
	}
	
	/**
	 * Constructora de sucesor. Solo se usa cada vez que se va a crear un sucesor. Obtiene los valores del estado
	 * padre, y los incrementa de forma adecuada para el �rbol de b�squeda.
	 * 
	 * @param g estado padre
	 * @param cos coste acumulado hasta este estado
	 * @param cam camino para llegar a este estado
	 */
	private Garrafas(Garrafas g,int cos,String cam){
		gra=g.gra;
		peq=g.peq;
		coste=g.coste+cos;
		profundidad=g.profundidad+1;
		camino=g.camino+cam;
	}

	/**
	 * Exploraci�n del estado, seg�n el operador que se quiera aplicar, se devuelve si ser�a posible o no hacer esa operaci�n.
	 * 
	 * @param s operador que se quiere aplicar
	 * @return true si se puede, false si no
	 */
	private boolean puedo(String s){
		// Llenar grande
		if (s.equals("LG")){
			if (gra<4)
				return true;
		}
		// Llenar peque�a
		if (s.equals("LP")){
			if (peq<3)
				return true;
		}
		// Vaciar grande
		if (s.equals("VG")){
			if (gra>0)
				return true;
		}
		// Vaciar peque�a
		if (s.equals("VP")){
			if (peq>0)
				return true;
		}
		// Trasvase de peque�a a grande
		if (s.equals("PG")){
			if (peq>0 && gra<4)
				return true;
		}
		// Trasvase de grande a peque�a
		if (s.equals("GP")){
			if (gra>0 && peq<3)
				return true;
		}
		return false;
	}

	/**
	 * Operador que llena la garrafa grande
	 */
	private void llenaG(){
		gra=4;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que llena la garrafa peque�a
	 */
	private void llenaP(){
		peq=3;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que vac�a la garrafa grande
	 */
	private void vaciaG(){
		gra=0;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que vacia la garrafa peque�a
	 */
	private void vaciaP(){
		peq=0;
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que trasvasa la garrafa peque�a a la grande
	 */
	private void PtoG(){
		int cantidad=4-gra;
		if (peq>=cantidad){
			gra=4;
			peq-=cantidad;
		}
		else{
			peq=0;
			gra+=peq;
		}
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Operador que trasvasa la garrafa grande a la peque�a
	 */
	private void GtoP(){
		int cantidad=3-peq;
		if (gra>=cantidad){
			peq=3;
			gra-=cantidad;
		}
		else{
			peq+=gra;
			gra=0;
		}
		valorHeur=this.Heuristica();
	}
	
	/**
	 * Funci�n heur�stica.
	 * @return valor absoluto de la suma de la garrafa peque�a, y la garrafa grande -2L
	 */
	private double Heuristica(){
		double d=0;
		d=Math.abs((gra-2)+peq);
		return d;
	}
	
	public Vector<Juego> expandir(){
		Vector<Juego> sucesores=new Vector<Juego>(0,1);
		Garrafas estado;
		if (this.puedo("LG")){
			estado=new Garrafas(this,1,"Llena grande\n");
			estado.llenaG();
			sucesores.add(estado);
		}
		if (this.puedo("LP")){
			estado=new Garrafas(this,1,"Llena peque�a\n");
			estado.llenaP();
			sucesores.add(estado);
		}
		if (this.puedo("GP")){
			estado=new Garrafas(this,1,"Grande a peque�a\n");
			estado.GtoP();
			sucesores.add(estado);
		}
		if (this.puedo("PG")){
			estado=new Garrafas(this,1,"Peque�a a grande\n");
			estado.PtoG();
			sucesores.add(estado);
		}
		if (this.puedo("VG")){
			estado=new Garrafas(this,1,"Vacia grande\n");
			estado.vaciaG();
			sucesores.add(estado);
		}
		if (this.puedo("VP")){
			estado=new Garrafas(this,1,"Vacia peque�a\n");
			estado.vaciaP();
			sucesores.add(estado);
		}
		return sucesores;
	}
	
	public boolean isGoal(){
		return (gra==2 && peq==0);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this==o)
			return true;
		if ((o==null) || (this.getClass()!=o.getClass()))
			return false;
		Garrafas estado=(Garrafas)o;
		return (estado.gra==gra) && (estado.peq==peq);	
	}
	
	@Override
	public String toString(){
		return "Garrafas\nG="+gra+"L P="+peq+"L\n";
	}
	
}