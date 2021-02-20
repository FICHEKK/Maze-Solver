package search;

import java.util.Collection;
import java.util.Map;
import java.util.Stack;

public class DepthFirstSearch<S> extends AbstractSearchAlgorithm<S> {

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new Stack<>();
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        return ((Stack<SearchNode<S>>) open).pop();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open, Map<S, SearchNode<S>> visited) {
        if (visited.containsKey(node.getState())) return;
        ((Stack<SearchNode<S>>) open).push(node);
    }

    @Override
    public String toString() {
        return "Depth-first search (DFS)";
    }
}
