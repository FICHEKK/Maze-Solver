package search;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface SearchAlgorithm<S> {
    SearchNode<S> findSolution(Supplier<S> initial, Consumer<S> consumer, Function<S, List<S>> successors, Predicate<S> goal);
}
