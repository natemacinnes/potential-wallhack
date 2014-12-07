package corba;

/**
 * Holder class for : corbaOperations
 * 
 * @author OpenORB Compiler
 */
final public class corbaOperationsHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal corbaOperations value
     */
    public corba.corbaOperations value;

    /**
     * Default constructor
     */
    public corbaOperationsHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public corbaOperationsHolder(corba.corbaOperations initial)
    {
        value = initial;
    }

    /**
     * Read corbaOperations from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = corbaOperationsHelper.read(istream);
    }

    /**
     * Write corbaOperations into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        corbaOperationsHelper.write(ostream,value);
    }

    /**
     * Return the corbaOperations TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return corbaOperationsHelper.type();
    }

}
