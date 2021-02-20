package search;

import java.util.*;
import java.util.function.*;

public abstract class AbstractSearchAlgorithm<S> implements SearchAlgorithm<S> {

    @Override
    public List<S> findPath(
            Supplier<S> initial,
            Consumer<S> consumer,
            Function<S, List<S>> successors,
            ToDoubleBiFunction<S, S> weight,
            Predicate<S> goal
    ) {
        var open = createOpen();
        open.add(SearchNode.initial(initial.get()));

        var visited = new HashMap<S, SearchNode<S>>();

        while (!open.isEmpty()) {
            var node = removeNode(open);
            var state = node.getState();

            if (visited.containsKey(state)) continue;
            consumer.accept(state);

            if (goal.test(state)) return constructPath(node);
            visited.put(state, node);

            for (var successor : successors.apply(state)) {
                var cost = node.getCost() + weight.applyAsDouble(state, successor);
                insertNode(new SearchNode<>(successor, cost, node), open, visited);
            }
        }

        return null;
    }

    private List<S> constructPath(SearchNode<S> head) {
        var path = new LinkedList<S>();

        for (var node = head; node != null; node = node.getParent()) {
            path.addFirst(node.getState());
        }

        return path;
    }

    protected abstract Collection<SearchNode<S>> createOpen();
    protected abstract SearchNode<S> removeNode(Collection<SearchNode<S>> open);
    protected abstract void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open, Map<S, SearchNode<S>> visited);
}
