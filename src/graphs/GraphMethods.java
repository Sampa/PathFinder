package graphs;
import java.util.*;
/**
 * Created by Sampa on 2013-12-26.
 */
public class GraphMethods{
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
	public static <TYPE>  boolean pathExists(TYPE from,TYPE to, Graph graph){
		// if(!graph.neuronsExist(from,to))
//          return false; // early exit if the neurons isn't in the graph
		NeuronPair<TYPE> temp  = graph.getNeuronPair(from, to);
		if(temp != null)
			return true; //early exit if direct link exists
		HashSet<TYPE> visitedNeurons = new HashSet();
		boolean first=true;
		depthFirstSearch(from,visitedNeurons,graph);
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
	public static <TYPE>  List<Edge> getPath(TYPE from, TYPE to,Graph graph){
		List<Edge> path = new ArrayList<>(); // we prepare this to build it before returning it
		if(!pathExists(from,to,graph))//early exit
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

//        public static <TYPE> void shortestPath(TYPE neuron,TYPE endNeuron, Graph graph,Map infoMap, Set visitedNeurons){
//        List<Edge> list = graph.getEdgesFrom(neuron);
//        TYPE resolve = null;
//        for (int i = 0; i < list.size(); i++) {
//            Edge e = list.get(i);
//            if (e.getDestination().equals(neuron)){
//                resolve = (TYPE) e.getStart();
//
//            }else {
//                resolve = (TYPE) e.getDestination();
//            }
//            DataHolder<TYPE> resolveData = (DataHolder) infoMap.get(resolve);
//            DataHolder<TYPE> neuronData = (DataHolder) infoMap.get(neuron);
//            neuronData.setHasWeightSet(true);
//            infoMap.put(neuron,neuronData);
//
//         if(!visitedNeurons.contains(resolve)){
//            System.out.println("ska till :" + resolve);
//                System.out.println("från:" + neuron);
//                System.out.println("från har " + neuronData.getWeight() + e.getWeight() + "<e> till har" + resolveData.getWeight());
//                if(neuronData.getWeight()+e.getWeight() < resolveData.getWeight()){
//                    System.out.println("var lägre");
//                    resolveData.setWeight(neuronData.getWeight() + e.getWeight());
//                    resolveData.setCameFrom(neuron);
//                    infoMap.put(resolve, resolveData);
//                }
//         }    }
//        int lowestWeight = Integer.MAX_VALUE;
//        Iterator it = infoMap.entrySet().iterator();
//        TYPE nextTo = resolve;
//        System.out.println("vår nästa är :"+resolve);
//        HashMap<TYPE,DataHolder<TYPE>> temp = new HashMap<>();
//        while (it.hasNext()) {
//            Map.Entry pairs = (Map.Entry)it.next();
//            TYPE thisNeuron = (TYPE) pairs.getKey();
//            DataHolder data = (DataHolder) pairs.getValue();
////            System.out.println(thisNeuron);
//            System.out.println("neuron:"+pairs.getKey()+data.isHasWeightSet());
//            if(!data.isHasWeightSet()) {
//                    System.out.println("lägst är"+lowestWeight+"dennas är "+data.getWeight());
//                lowestWeight = data.getWeight();
//                temp.put(thisNeuron,data);
//                nextTo= thisNeuron;
//                System.out.println("skojja,den är är :"+nextTo);
//                if (lowestWeight > data.getWeight() || lowestWeight ==Integer.MAX_VALUE) {
//                }
//            }
//             }
//        //infoMap.putAll(temp);
//        visitedNeurons.add(neuron);
//        System.out.println("adderade:" + neuron);
//        if(visitedNeurons.contains(endNeuron)) {
//            System.exit(0);
//        }
//        DataHolder<TYPE> temp2 = (DataHolder<TYPE>) infoMap.get(endNeuron);
//        System.out.println(resolve.equals(endNeuron));
//        System.out.println("slutnodsshit" + endNeuron + temp2.isHasWeightSet());
//        shortestPath(resolve, endNeuron, graph, infoMap, visitedNeurons);
//    }
}
