package graph;

import parking.Parking;
import parking.template.*;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.util.*;

public class Graph implements Iterable<Node> {

    private Node entry;
    private Node departure;
    private ArrayList<Node> nodesList = new ArrayList<>();

    public Node getEntry() {
        return entry;
    }


    public Node getDeparture() {
        return departure;
    }

    public ArrayList<Node> getNodesList() {
        return nodesList;
    }


    public Graph(Parking parking) {  //Построение графа по парковке
        for (int i=0; i<parking.getFunctionalBlockH();i++) {
            for (int j=0; j<parking.getFunctionalBlockV(); j++) {
                if (parking.getParking()[i][j] instanceof Road){
                    Node node = new Node(Template.Road, i, j);
                    nodesList.add(node);
                }
                if (parking.getParking()[i][j] instanceof ParkingPlace){
                    Node node = new Node(Template.ParkingPlace, i, j);
                    nodesList.add(node);
                    freeParkingPlaces.add(node);
                }
                if (parking.getParking()[i][j] instanceof Entry){
                    Node node = new Node(Template.Entry, i, j);
                    this.entry = node;
                    nodesList.add(node);
                }
                if (parking.getParking()[i][j] instanceof Departure){
                    Node node = new Node(Template.Departure, i, j);
                    this.departure = node;
                    nodesList.add(node);
                }
            }
        }
        int size = nodesList.size();
        for (int i=0; i<size; i++) {
            int indexJ = nodesList.get(i).getJ();
            int indexI = nodesList.get(i).getI();
            if (indexI>0){
                for (int j=0; j<size;j++){
                    if ((nodesList.get(j).getI() == indexI-1)&&(nodesList.get(j).getJ() == indexJ)) {
                        if (nodesList.get(j).getType()!=Template.ParkingPlace||nodesList.get(i).getType()!=Template.ParkingPlace)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                    }
                }
            }
            if(indexI<parking.getFunctionalBlockH()-1){
                for (int j=0; j<size;j++){
                    if ((nodesList.get(j).getI() == indexI+1)&&(nodesList.get(j).getJ() == indexJ))                                                                                                                        //КАКАШКА
                        if (nodesList.get(j).getType()!=Template.ParkingPlace||nodesList.get(i).getType()!=Template.ParkingPlace)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                }
            }
            if (indexJ>0){
                for (int j=0; j<size;j++){
                    if ((nodesList.get(j).getI() == indexI)&&(nodesList.get(j).getJ() == indexJ-1))
                        if (nodesList.get(j).getType()!=Template.ParkingPlace||nodesList.get(i).getType()!=Template.ParkingPlace)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                }
            }
            if(indexJ<parking.getFunctionalBlockV()-1){
                for (int j=0; j<size;j++){
                    if ((nodesList.get(j).getI() == indexI)&&(nodesList.get(j).getJ() == indexJ+1))
                        if (nodesList.get(j).getType()!=Template.ParkingPlace||nodesList.get(i).getType()!=Template.ParkingPlace)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                }
            }
        }
    }


    public boolean isReachable(Node first, Node last) {
        Queue<Node> queue = new LinkedList<>();
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        queue.offer(first);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            if (nodes.contains(currentNode)) {
                nodes.remove(currentNode);
                for (Node node : currentNode.getAdjacentNodes()
                        ) {
                    if (node.equals(last)) {
                        return true;
                    }
                    queue.offer(node);
                }
            }
        }
        return false;
    }

    public ArrayList<Node> getPath1(Node first, Node last) {
        Queue<Node> queue = new LinkedList<>();
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        LinkedHashMap<Node, Node> map = new LinkedHashMap<Node, Node>();
        queue.offer(first);
        nodes.remove(first);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            for (Node node : currentNode.getAdjacentNodes()
                    ) {
                if (node.equals(last)) {
                    ArrayList<Node> path = new ArrayList<>();
                    if (currentNode.equals(first)){
                        path.add(last);
                        return path;
                    }
                    path.add(last);
                    path.add(0, currentNode);
                    while (map.get(currentNode)!=first){
                        path.add(0, map.get(currentNode));
                        currentNode=map.get(currentNode);
                    }
                    return path;
                }
                if (nodes.contains(node)) {
                    queue.offer(node);
                    nodes.remove(node);
                    map.put(node, currentNode);
                }
            }
        }
        return null;
    }

    public ArrayList<Node> getPath(Node first, Node last){
        ArrayList<Node> path = new ArrayList<>();
        if (first.equals(last)){
            path.add(first);
            return path;
        }
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        nodes.remove(first);
        if (getNext(nodes, path, first.getAdjacentNodes(), last)) {
            path.add(0, first);
            return path;
        }
        else return null;
    }

    private boolean getNext(List<Node> nodes, List<Node> path, ArrayList<Node> adjacentNodes, Node last){
        if (adjacentNodes.contains(last)){
            path.add(0, last);
            return true;
        }
        for (Node currentNode: adjacentNodes
             ) {
            if (nodes.contains(currentNode)) {
                nodes.remove(currentNode);
                if (getNext(nodes, path, currentNode.getAdjacentNodes(), last)){
                    path.add(0, currentNode);
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Node> freeParkingPlaces = new ArrayList<>();

    public ArrayList<Node> getFreeParkingPlaces() {
        return freeParkingPlaces;
    }

    public ArrayList<Node> getPathToFreeParkingPlace(){

        if (freeParkingPlaces.size()>0) {
            return getPath1(entry, freeParkingPlaces.remove(0));
        }
        else return null;
    }



    /*public static void main(String[] s){
        Parking parking = new Parking(3, 3, null, 0);
        parking.setParking(new FunctionalBlock[][]{
                new FunctionalBlock[]{new ParkingPlace(null), new ParkingPlace(null), new ParkingPlace(null)},
                new FunctionalBlock[]{new Lawn(null), new Lawn(null), new Lawn(null)},
                new FunctionalBlock[]{new Entry(null), new Departure(null), new Lawn(null)}
        });
        Graph g = new Graph(parking);
        for (int i=0; i<g.nodesList.size(); i++) {
            for (int j = 0; j < g.nodesList.size(); j++) {
                System.out.print(g.isReachable(g.nodesList.get(i), g.nodesList.get(j)) + " ");
            }
            System.out.println();
        }

        for (int i=0; i<g.nodesList.size(); i++) {
            for (int j = 0; j < g.nodesList.size(); j++) {
                System.out.print(g.getPath(g.nodesList.get(i), g.nodesList.get(j)) + " ");
            }
            System.out.println("***");
        }
    }*/

    @Override
    public Iterator<Node> iterator() {
        return nodesList.iterator();
    }


}
