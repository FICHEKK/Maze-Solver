package search;

import java.util.List;
import java.util.function.*;

public interface SearchAlgorithm<S> {
    SearchResult<S> search(
            Supplier<S> initial,
            Consumer<S> consumer,
            Function<S, List<S>> successors,
            ToDoubleBiFunction<S, S> weight,
            Predicate<S> goal
    );
}
