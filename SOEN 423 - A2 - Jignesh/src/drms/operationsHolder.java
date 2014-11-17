package drms;

/**
 * Holder class for : operations
 * 
 * @author OpenORB Compiler
 */
final public class operationsHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal operations value
     */
    public drms.operations value;

    /**
     * Default constructor
     */
    public operationsHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public operationsHolder(drms.operations initial)
    {
        value = initial;
    }

    /**
     * Read operations from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = operationsHelper.read(istream);
    }

    /**
     * Write operations into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        operationsHelper.write(ostream,value);
    }

    /**
     * Return the operations TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return operationsHelper.type();
    }

}
