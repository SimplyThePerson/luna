package io.luna.game.event;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of pipelines mapped to their respective event traversal types.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class EventListenerPipelineSet implements Iterable<EventListenerPipeline<?>> {

    /**
     * The map of pipelines.
     */
    private final Map<Class<?>, EventListenerPipeline<?>> pipelines = new HashMap<>();

    /**
     * Adds a new event listener to a pipeline within this set.
     *
     * @param listener The listener to add.
     */
    public void add(EventListener<?> listener) {
        Class<?> eventType = listener.getEventType();
        if (Event.class.isAssignableFrom(eventType)) {
            EventListenerPipeline<?> pipeline = get(eventType);
            pipeline.add(listener);
        }
    }

    /**
     * Retrieves a pipeline from this set.
     *
     * @param eventType The event class to retrieve the pipeline of.
     * @return The pipeline that accepts {@code eventType}.
     */
    public EventListenerPipeline<?> get(Class<?> eventType) {
        return pipelines.computeIfAbsent(eventType, EventListenerPipeline::new);
    }

    /**
     * Retrieves a pipeline with type {@code E} from this set.
     *
     * @param eventType The event class to retrieve the pipeline of.
     * @param <E> The event type.
     * @return The pipeline.
     */
    public <E extends Event> EventListenerPipeline<E> getTyped(Class<E> eventType) {
        //noinspection unchecked
        return (EventListenerPipeline<E>) get(eventType);
    }

    /**
     * Replaces all of the pipelines the backing map with {@code set}. Used for reloading plugins.
     *
     * @param set The new pipeline set.
     */
    public void replaceAll(EventListenerPipelineSet set) {
        pipelines.clear();
        pipelines.putAll(set.pipelines);
    }

    @Override
    public UnmodifiableIterator<EventListenerPipeline<?>> iterator() {
        Collection<EventListenerPipeline<?>> values = pipelines.values();
        return Iterators.unmodifiableIterator(values.iterator());
    }

    /**
     * Returns the amount of pipelines in this set.
     *
     * @return The pipeline count.
     */
    public int size() {
        return pipelines.size();
    }
}
