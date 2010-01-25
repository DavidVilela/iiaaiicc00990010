package Modelo.Busquedas;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import Modelo.Juegos.*;

public class EscaladaMaximaPendiente implements Busqueda{

	public String toString() {
		return "Escalada m�xima pendiente";
	}
	
	@Override
	public Juego resuelve(Juego inicial){
		if (inicial.getValorHeur()==-1 || inicial.isGoal())
			return inicial;
		boolean fin=false;
		int generados=1;
		int expandidos=0;
		List<Juego> listaCerrados=new Vector<Juego>(0,1);
		Queue<Juego> colaAbiertos=new LinkedList<Juego>();
		colaAbiertos.offer(inicial);
		Juego juego=null;
		// La cola de abiertos nunca tiene mas de 1 nodo, cuando est� vac�a es que no se ha encontrado un sucesor mejor y se acaba la b�squeda
		while (!fin && !colaAbiertos.isEmpty()){
			juego=colaAbiertos.poll();
			if (juego.isGoal()){
				fin=true;
				String camino=juego.getCamino()+"Nodos generados:"+generados+" ; Nodos expandidos:"+expandidos+" ; Coste: "+juego.getCoste()+
								" ; Profundidad:"+juego.getProfundidad();
				juego.setCamino(camino);
			}
			else{
				// como no es estado final, se cierra, ya no se va a mirar m�s
				listaCerrados.add(juego);
				// expandir un nuevo nodo
				expandidos++;
				// sucesores del nodo que se acaba de cerrar
				Vector<Juego> sucesores=juego.expandir();
				// si uno de los sucesores ya aparece en la lista de nodos cerrados, no se a�ade a la cola de abiertos, as� se evitan ciclos
				int elegido=0;
				double valorH=juego.getValorHeur();
				for (int i=0;i<sucesores.size();i++){
					if (!listaCerrados.contains(sucesores.elementAt(i))){
						// comprobar cual es el nodo de menor heuristica
						if (sucesores.elementAt(i).getValorHeur()<valorH){
							generados++;
							valorH=sucesores.elementAt(i).getValorHeur();
							elegido=i;
						}
					}
				}
				// Si se ha encontrado un nodo con menor valor heuristico, insertarlo en la cola de abiertos
				if (valorH<juego.getValorHeur())
					colaAbiertos.offer(sucesores.elementAt(elegido));
			}
		}
		return juego;
	}
	
	public static void main(String[] args){
		Juego inicial=new Puzzle8();
		EscaladaMaximaPendiente busqueda=new EscaladaMaximaPendiente();
		Juego solucion=(Puzzle8)busqueda.resuelve(inicial);
		System.out.println("B�squeda por escalada en m�xima pendiente, Puzzle8\n");
		if (solucion.isGoal()){
			System.out.println(solucion.getCamino());
			System.out.println(solucion.toString());
		}
		else
			System.out.println("No se encontr� soluci�n");
	}
	
}