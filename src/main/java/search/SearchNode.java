package search;

public class SearchNode<S> {
    private final S state;
    private final double cost;
    private final SearchNode<S> parent;

    public SearchNode(S state, double cost, SearchNode<S> parent) {
        this.state = state;
        this.cost = cost;
        this.parent = parent;
    }

    public static <T> SearchNode<T> initial(T state) {
        return new SearchNode<>(state, 0, null);
    }

    public S getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    public SearchNode<S> getParent() {
        return parent;
    }
}
