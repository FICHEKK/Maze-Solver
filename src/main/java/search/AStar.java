package search;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;

public class AStar<S> extends AbstractSearchAlgorithm<S> {
    private final ToDoubleFunction<S> heuristicFunction;

    public AStar(ToDoubleFunction<S> heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
    }

    @Override
    protected Collection<SearchNode<S>> createOpen() {
        return new PriorityQueue<>(
                Comparator.<SearchNode<S>>comparingDouble(node -> node.getCost() + heuristicFunction.applyAsDouble(node.getState()))
                        .thenComparingDouble(node -> heuristicFunction.applyAsDouble(node.getState()))
        );
    }

    @Override
    protected SearchNode<S> removeNode(Collection<SearchNode<S>> open) {
        return ((PriorityQueue<SearchNode<S>>) open).remove();
    }

    @Override
    protected void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open, Map<S, SearchNode<S>> visited) {
        var visitedNode = visited.get(node.getState());

        if (visitedNode == null) {
            open.add(node);
        }
        else if (node.getCost() < visitedNode.getCost()) {
            visited.put(node.getState(), node);
        }
    }

    @Override
    public String toString() {
        return "A* Search Algorithm";
    }
}
