package search;

import java.util.*;
import java.util.function.*;

public abstract class AbstractSearchAlgorithm<S> implements SearchAlgorithm<S> {

    @Override
    public SearchResult<S> search(
            Supplier<S> initial,
            Consumer<S> consumer,
            Function<S, List<S>> successors,
            ToDoubleBiFunction<S, S> weight,
            Predicate<S> goal
    ) {
        var open = createOpen();
        open.add(SearchNode.initial(initial.get()));

        var visited = new LinkedHashMap<S, SearchNode<S>>();

        while (!open.isEmpty()) {
            var node = removeNode(open);
            var state = node.getState();

            if (visited.containsKey(state)) continue;

            consumer.accept(state);
            visited.put(state, node);

            if (goal.test(state)) return new SearchResult<>(node, visited);

            for (var successor : successors.apply(state)) {
                var cost = node.getCost() + weight.applyAsDouble(state, successor);
                insertNode(new SearchNode<>(successor, cost, node), open, visited);
            }
        }

        return new SearchResult<>(null, visited);
    }

    protected abstract Collection<SearchNode<S>> createOpen();
    protected abstract SearchNode<S> removeNode(Collection<SearchNode<S>> open);
    protected abstract void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open, Map<S, SearchNode<S>> visited);
}
