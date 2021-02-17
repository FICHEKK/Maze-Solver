package search;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;

public class GreedyBestFirstSearch<S> extends AbstractSearchAlgorithm<S> {
    private final Function<S, Double> heuristicFunction;

    public GreedyBestFirstSearch(Function<S, Double> heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
    }

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new PriorityQueue<>(Comparator.comparingDouble(node -> heuristicFunction.apply(node.getState())));
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        var priorityQueue = (PriorityQueue<SearchNode<S>>) open;
        return priorityQueue.remove();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open) {
        var priorityQueue = (PriorityQueue<SearchNode<S>>) open;
        priorityQueue.add(node);
    }

    @Override
    public String toString() {
        return "Greedy best-first search";
    }
}
