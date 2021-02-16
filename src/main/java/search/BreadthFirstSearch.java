package search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch<S> extends AbstractSearchAlgorithm<S> {

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new LinkedList<>();
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        var queue = (Queue<SearchNode<S>>) open;
        return queue.remove();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open) {
        var queue = (Queue<SearchNode<S>>) open;
        queue.add(node);
    }

    @Override
    public String toString() {
        return "Breadth-first search";
    }
}
