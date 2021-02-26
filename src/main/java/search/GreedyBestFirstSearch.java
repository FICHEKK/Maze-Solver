package search;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;

public class GreedyBestFirstSearch<S> extends AbstractSearchAlgorithm<S> {
    private final ToDoubleFunction<S> heuristicFunction;

    public GreedyBestFirstSearch(ToDoubleFunction<S> heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
    }

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new PriorityQueue<>(Comparator.comparingDouble(node -> heuristicFunction.applyAsDouble(node.getState())));
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        return ((PriorityQueue<SearchNode<S>>) open).remove();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open, Map<S, SearchNode<S>> visited) {
        if (visited.containsKey(node.getState())) return;
        open.add(node);
    }

    @Override
    public String toString() {
        return "Greedy Best-First Search";
    }
}
