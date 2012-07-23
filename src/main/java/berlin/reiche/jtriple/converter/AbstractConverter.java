package berlin.reiche.jtriple.converter;

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

    /**
     * Default constructor.
     * 
     * @param priority
     *            the priority for this converter.
     */
    public AbstractConverter(int priority) {
        this.priority = priority;
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
