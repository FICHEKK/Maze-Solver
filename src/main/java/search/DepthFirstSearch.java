package search;

import java.util.Collection;
import java.util.Stack;

public class DepthFirstSearch<S> extends AbstractSearchAlgorithm<S> {

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new Stack<>();
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        var stack = (Stack<SearchNode<S>>) open;
        return stack.pop();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open) {
        var stack = (Stack<SearchNode<S>>) open;
        stack.push(node);
    }

    @Override
    public String toString() {
        return "Depth-first search";
    }
}
