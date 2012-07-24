package berlin.reiche.jtriple.converter;

import berlin.reiche.jtriple.Binding;

/**
 * Provides an initial implementation of the {@link Converter} interface by
 * providing constructor and {@link Comparable} implementation in order to
 * enforce the prioritization.
 * 
 * @author Konrad Reiche
 * 
 */
abstract class AbstractConverter implements Converter {

    /**
     * The priority of this converter.
     */
    private final int priority;

    Binding binding;

    /**
     * Default constructor.
     * 
     * @param priority
     *            the priority for this converter.
     */
    public AbstractConverter(int priority, Binding binding) {
        this.priority = priority;
        this.binding = binding;
    }

    /**
     * Compares the converter based on their priority.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Converter o) {
        return Integer.valueOf(priority).compareTo(o.getPriority());
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
