package search;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractSearchAlgorithm<S> implements SearchAlgorithm<S> {

    @Override
    public SearchNode<S> findSolution(Supplier<S> initial, Consumer<S> consumer, Function<S, List<S>> successors, Predicate<S> goal) {
        var open = createOpen();
        open.add(SearchNode.initial(initial.get()));

        var visited = new HashSet<S>();

        while (!open.isEmpty()) {
            var node = removeNode(open);
            var state = node.getState();

            if (visited.contains(state)) continue;
            consumer.accept(state);

            if (goal.test(state)) return node;
            visited.add(state);

            for (var successor : successors.apply(state)) {
                if (visited.contains(successor)) continue;
                insertNode(new SearchNode<>(successor, node.getCost() + 1, node), open);
            }
        }

        return null;
    }

    protected abstract Collection<SearchNode<S>> createOpen();
    protected abstract SearchNode<S> removeNode(Collection<SearchNode<S>> open);
    protected abstract void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open);
}
