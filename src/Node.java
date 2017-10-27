import template.Template;

import java.util.ArrayList;

public class Node {
    private Template type;
    private int i;
    private int j;
    private ArrayList<Node> adjacentNodes;

    public Node() {
        adjacentNodes = new ArrayList<>();
        type = Template.Null;
        i = 0;
        j = 0;
    }

    public Node(Template type, int i, int j, Integer number) {
        adjacentNodes= new ArrayList<>();
        this.type = type;
        this.i = i;
        this.j = j;
    }

    public void setType(Template type) {
        this.type = type;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Template getType() {
        return type;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public ArrayList<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    @Override
    public boolean equals(Object o){
        if (o==this)
            return true;
        if(o.getClass()==this.getClass()){
            Node node = (Node)o;
            return (node.i==i)&&(node.j==j)
                    &&(node.adjacentNodes.equals(adjacentNodes))
                    &&(node.type==type);
        } else return false;
    }
}
