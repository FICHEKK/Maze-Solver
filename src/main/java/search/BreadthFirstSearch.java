package search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BreadthFirstSearch<S> extends AbstractSearchAlgorithm<S> {

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new LinkedList<>();
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        return ((Queue<SearchNode<S>>) open).remove();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open, Map<S, SearchNode<S>> visited) {
        if (visited.containsKey(node.getState())) return;
        open.add(node);
    }

    @Override
    public String toString() {
        return "Breadth-First Search (BFS)";
    }
}
