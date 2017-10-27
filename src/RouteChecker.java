import java.util.*;

public class RouteChecker {

}

   /* public  int[] getPath(int first, int second, Graph graph) {
        Queue<Integer> queue = new LinkedList<>();
        HashSet<Integer> nodes = new HashSet<>();
        path.add(first);
        Collections.addAll(nodes, graph.getNodesList());
        queue.offer(first);
        while (!queue.isEmpty()) {
            int currentNode = queue.remove();
            if (nodes.contains(currentNode)) {
                nodes.remove(currentNode);
                for (Integer node : graph.getAdjacentNodes(currentNode)
                        ) {
                    if (node == second)
                        return true;
                    queue.offer(node);
                }
            }
        }
        return false;
    }
}*/