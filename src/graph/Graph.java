package graph;

import parking.Parking;
import parking.Verificator;
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
                    if ((nodesList.get(j).getI() == indexI+1)&&(nodesList.get(j).getJ() == indexJ))
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

    public void fillFreeParkingPlaces(){
        try {
            for (Node node : nodesList
                    ) {
                if (node.getType() == Template.ParkingPlace)
                    freeParkingPlaces.add(new ParkingPlaceNode(node));
            }
        }catch (NullPointerException e){
            System.out.println("Топология некорректна!");
        }
    }
    public boolean isReachable(Node first, Node last) {
        Queue<Node> queue = new LinkedList<>();
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        queue.offer(first);
        nodes.remove(first);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            for (Node node : currentNode.getAdjacentNodes()
                    ) {
                if (node.equals(last)) {
                    return true;
                }
                if (nodes.contains(node) && (node.getType() != Template.ParkingPlace) && (node.getType() != Template.Departure)) {
                    queue.offer(node);
                    nodes.remove(node);
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
                    if (currentNode.equals(first)) {
                        path.add(last);
                        return path;
                    }
                    path.add(last);
                    path.add(0, currentNode);
                    while (map.get(currentNode) != first) {
                        path.add(0, map.get(currentNode));
                        currentNode = map.get(currentNode);
                    }
                    return path;
                }
                if (nodes.contains(node) && (node.getType() != Template.ParkingPlace) && (node.getType() != Template.Departure)) {
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

    private PriorityQueue<ParkingPlaceNode> freeParkingPlaces = new PriorityQueue<>();


    public ParkingPlaceNode getFreeParkingPlace(){

        if (freeParkingPlaces.size()>0) {
            return freeParkingPlaces.remove();
        }
        else return null;
    }

    public boolean hasFreeParkingPlaces(){
        return freeParkingPlaces.size()>0;
    }

    public void freeParkingPlace(ParkingPlaceNode parkingPlace){
        freeParkingPlaces.add(parkingPlace);
    }

    public class ParkingPlaceNode implements Comparable<ParkingPlaceNode>{

        private Node parkingPlace;
        private ArrayList<Node> pathToEntry;
        private ArrayList<Node> pathToDeparture;

        public ArrayList<Node> getPathToEntry() {
            return pathToEntry;
        }

        public ArrayList<Node> getPathToDeparture() {
            return pathToDeparture;
        }

        public ParkingPlaceNode(Node parkingPlace){
            this.parkingPlace = parkingPlace;
            pathToEntry = Graph.this.getPath1(Graph.this.entry, parkingPlace);
            pathToDeparture = Graph.this.getPath1(parkingPlace, Graph.this.departure);
        }

        public int getI(){
            return parkingPlace.getI();
        }

        public int getJ(){
            return parkingPlace.getJ();
        }

        @Override
        public int compareTo(ParkingPlaceNode o) {
            return pathToEntry.size()-o.pathToEntry.size();
        }

        @Override
        public String toString(){
            return parkingPlace.toString();
        }
    }

    @Override
    public Iterator<Node> iterator() {
        return nodesList.iterator();
    }
}
