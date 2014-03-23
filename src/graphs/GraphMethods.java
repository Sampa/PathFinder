package graphs;
import java.io.Serializable;
import java.util.*;
/**
 * Created by Sampa on 2013-12-26.
 */
public class GraphMethods<TYPE> implements Serializable{
	public static int getTotalPathTime(List o ){
		int time = 0;
		for (int i = 0; i < o.size(); i++) {
			try{
				Edge e = (Edge)o.get(i);
				time = time+e.getWeight();
			}catch (NullPointerException npe){}
		}
		return time;
	}
	public static <TYPE> boolean pathExists(GraphMethods<TYPE> graphMethods, TYPE from, TYPE to, Graph graph){
		NeuronPair<TYPE> temp  = graph.getNeuronPair(from, to);
		if(temp != null)
			return true; //early exit if direct link exists
		HashSet<TYPE> visitedNeurons = new HashSet();
		boolean first=true;
		GraphMethods.depthFirstSearch(from, visitedNeurons, graph);
        return visitedNeurons.contains(to);
	}
	private static <TYPE> void depthFirstSearch(TYPE neuron, HashSet<TYPE> visitedNeurons, Graph graph){
		visitedNeurons.add(neuron);
		List<Edge> list = graph.getEdgesFrom(neuron);
		for (int i = 0; i <list.size() ; i++) {
			Edge e = list.get(i);
			TYPE resolve = (TYPE) e.getDestination();
			if (e.getDestination().equals(neuron))
				resolve = (TYPE) e.getStart();
			if (!visitedNeurons.contains(resolve)){
				depthFirstSearch(resolve, visitedNeurons, graph);
			}
		}
	}
	public static <TYPE> List<Edge> getPath(GraphMethods<TYPE> graphMethods, TYPE from, TYPE to, Graph graph){
		List<Edge> path = new ArrayList<>(); // we prepare this to build it before returning it
		if(!pathExists(graphMethods, from,to,graph))//early exit
			return null;

		//initiate lists
		HashSet<TYPE> visitedNeurons = new HashSet<TYPE>();
		HashMap<TYPE,DataHolder> infoMap = new HashMap<>();
		HashMap<TYPE,List<Edge>> nodeMap = graph.getNodes();

		//feed the infoMap with each neuron and the three default start arguments
		for(Map.Entry<TYPE, List<Edge>>  entry : nodeMap.entrySet()){
			TYPE current = entry.getKey();
            infoMap.put(current, new DataHolder<Edge>(Integer.MAX_VALUE-4,false, null));
		}
		infoMap.put(from, new DataHolder<Edge>(0, true, null)); //edit the start node to have special properties
        shortestPath(from,to, graph, infoMap, visitedNeurons); //recursive finds shortest path
		path =  buildPath(infoMap,graph,visitedNeurons,from,to,path);//recursive builds backward list of edges
		Collections.reverse(path);//making sense to humans if we rewerse it
		return path;
	}
	private static <TYPE> List<Edge> buildPath(HashMap<TYPE,DataHolder> infoMap,Graph graph, Set<TYPE> visited,TYPE from, TYPE to,List path) {
        DataHolder<TYPE> data = (DataHolder)infoMap.get(to);
        TYPE  edge = (TYPE) graph.getEdgeBetween(to,data.getCameFrom());
        path.add(edge);
        if(!data.getCameFrom().equals(from)){
            buildPath(infoMap, graph, visited, from, data.getCameFrom(), path);
        }
		return path;
	}
	public static <TYPE> void shortestPath(TYPE neuron, TYPE endNeuron, Graph graph, Map infoMap, Set visitedNeurons){
		//add to visited so we wont check it again
		visitedNeurons.add(neuron);
		DataHolder<TYPE> neuronData  = (DataHolder<TYPE>)infoMap.get(neuron);
		//find all it edges
		List<Edge> list = graph.getEdgesFrom(neuron);
		for (int i = 0; i <list.size() ; i++) {
			Edge e = list.get(i);
			//the node pointing outward
			TYPE resolve = (TYPE) e.getDestination();
			if (e.getDestination().equals(neuron)) {
				resolve = (TYPE) e.getStart();
			}
			//get the neughbour data
		    DataHolder<TYPE> data  = (DataHolder<TYPE>)infoMap.get(resolve);
			//add the neuron we are to the current edge weight
			int compareValue = neuronData.getWeight()+e.getWeight();
			//if its less then the weight set on the neighbour, change it
                if( compareValue < data.getWeight()){
                    data.setCameFrom(neuron);
                    data.setWeight(compareValue);
                    //put the newly freshed into the infomap replacing the old
                    infoMap.put(resolve,data);
                }

        }

		int lowestWeight = Integer.MAX_VALUE;
		Iterator it = infoMap.entrySet().iterator();
		TYPE nextTo = null;
		DataHolder<TYPE> nextData  = null;
		HashMap<TYPE,DataHolder<TYPE>> temp = new HashMap<>();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			DataHolder<TYPE> thisData = (DataHolder<TYPE>)pairs.getValue();
			//try next one if it has weight set
			if(thisData.getWeight()<lowestWeight){
				if(!thisData.isHasWeightSet()){
					nextData = thisData;
					lowestWeight = thisData.getWeight();
					nextTo = (TYPE)pairs.getKey();
				}
			}
		}
		if(nextData != null)
			nextData.setHasWeightSet(true);
		DataHolder<TYPE> endData = (DataHolder<TYPE>) infoMap.get(endNeuron);
		if(endData.isHasWeightSet()&& visitedNeurons.contains(endNeuron)){
			return;
        }
		shortestPath(nextTo,endNeuron,graph,infoMap,visitedNeurons);
	}
}
