package Modelo.Busquedas;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import Modelo.Juegos.*;

public class CosteUniforme implements Busqueda{

	@Override
	public Juego resuelve(Juego inicial) {
		boolean fin=false;
		int generados=1;
		int expandidos=0;
		List<Juego> listaCerrados=new Vector<Juego>(0,1);
		PriorityQueue<Juego> colaAbiertos=new PriorityQueue<Juego>(1,new ComparatorCosteUniforme());
		colaAbiertos.offer(inicial);
		Juego juego=null;
		try{
			while(!fin){
				juego=colaAbiertos.poll();
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
					// si uno de los sucesores ya aparece en la lista de nodos cerrados, no se a�ade a la cola de abiertos, as� se evitan ciclos
					for (int i=0;i<sucesores.size();i++){
						if (!listaCerrados.contains(sucesores.elementAt(i))){
							colaAbiertos.offer(sucesores.elementAt(i));
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
	
	public static void main(String[] args){
		Juego inicial=new Garrafas();
		CosteUniforme busqueda=new CosteUniforme();
		System.out.print("B�squeda coste uniforme:\nEstado inicial: "+inicial.toString());
		Juego solucion=(Garrafas)busqueda.resuelve(inicial);
		System.out.print(solucion.getCamino());
		System.out.print("\nSoluci�n: "+solucion.toString());
	}
	
}