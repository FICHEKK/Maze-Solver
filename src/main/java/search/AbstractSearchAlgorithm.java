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

        var visited = new HashSet<S>();

        while (!open.isEmpty()) {
            var node = removeNode(open);
            var state = node.getState();

            if (visited.contains(state)) continue;
            consumer.accept(state);

            if (goal.test(state)) return constructPath(node);
            visited.add(state);

            for (var successor : successors.apply(state)) {
                if (visited.contains(successor)) continue;

                var cost = node.getCost() + weight.applyAsDouble(state, successor);
                insertNode(new SearchNode<>(successor, cost, node), open);
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
    protected abstract void insertNode(SearchNode<S> node, Collection<SearchNode<S>> open);
}
