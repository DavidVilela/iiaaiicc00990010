package Modelo.Busquedas;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

import Modelo.Juegos.*;

/**
 * B�squeda en profundidad iterativa. Sigue el mismo principio que la b�squeda en profundidad limitada, empieza con una profundidad 0, y va incrementandola
 * haciendo tantas iteraciones como necesite para encontrar la soluci�n. Incluye control de ciclos, errores y excepciones.
 * @author Pablo Acevedo
 *
 */
public class ProfundidadIterativa implements Busqueda{

	@Override
	public Juego resuelve(Juego inicial){
		boolean fin=false;
		int generados=1;
		int expandidos=0;
		int profundidad=0;
		List<Juego> listaCerrados=new Vector<Juego>(0,1);
		Stack<Juego> pilaAbiertos=new Stack<Juego>();
		pilaAbiertos.push(inicial);
		Juego juego=null;
		// se hace try&catch porque �sta b�squeda puede no acabar por falta de memoria. imposible por los juegos que se van a usar? A REVISAR
		try{
			while (!fin){
				// si no hay elementos en la pila quiere decir que se ha recorrido el �rbol entero sin encontrar la soluci�n. Se incrementa en 1 la profundidad,
				// se borra la lista de nodos cerrados y se vuelve a empezar desde el nodo inicial.
				if (pilaAbiertos.size()==0){
					listaCerrados.clear();
					pilaAbiertos.push(inicial);
					profundidad++;
				}
				juego=pilaAbiertos.pop();
				if (juego.isGoal()){
					// Si el juego es igual que el estado final, se guarda en su camino toda la informaci�n de la b�squeda para mostrarla despu�s
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
					// por ser una pila se meten al rev�s, para recorrerlos en el orden en que se aplican los operadores. Si un sucesor ya estaba en la
					// lista de cerrados, no se introduce en la pila, para evitar ciclos/ramas infinitas. Adem�s, si la profundidad del nodo que se acaba de
					// cerrar es mayor que la profundidad l�mite, tampoco se introducen sus sucesores en la pila.
					for (int i=sucesores.size();i>0;i--){
						if (!listaCerrados.contains(sucesores.elementAt(i-1))&&juego.getProfundidad()<profundidad){
							pilaAbiertos.push(sucesores.elementAt(i-1));
							generados++;
						}
					}
				}
			}
			return juego;
		}
		catch (Error e){
			return juego;
		}
	}
	
	/**
	 * Main para probar la b�squeda
	 * @param args, no se usa
	 */
	public static void main(String[] args){
		Puzzle8 inicial=new Puzzle8();
		ProfundidadIterativa busqueda=new ProfundidadIterativa();
		Puzzle8 solucion=(Puzzle8)busqueda.resuelve(inicial);
		System.out.print("B�squeda en profundidad iterativa Puzzle8:\nEstado inicial: "+inicial.toString());
		if (solucion.isGoal()){
			System.out.print(solucion.getCamino());
			System.out.print("\nSoluci�n: "+solucion.toString());
		}
		else
			System.out.print("No se encontr� ninguna soluci�n");
	}
}