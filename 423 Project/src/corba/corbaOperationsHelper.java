package corba;

/** 
 * Helper class for : corbaOperations
 *  
 * @author OpenORB Compiler
 */ 
public class corbaOperationsHelper
{
    /**
     * Insert corbaOperations into an any
     * @param a an any
     * @param t corbaOperations value
     */
    public static void insert(org.omg.CORBA.Any a, corba.corbaOperations t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract corbaOperations from an any
     *
     * @param a an any
     * @return the extracted corbaOperations value
     */
    public static corba.corbaOperations extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return corba.corbaOperationsHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the corbaOperations TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "corbaOperations" );
        }
        return _tc;
    }

    /**
     * Return the corbaOperations IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:corba/corbaOperations:1.0";

    /**
     * Read corbaOperations from a marshalled stream
     * @param istream the input stream
     * @return the readed corbaOperations value
     */
    public static corba.corbaOperations read(org.omg.CORBA.portable.InputStream istream)
    {
        return(corba.corbaOperations)istream.read_Object(corba._corbaOperationsStub.class);
    }

    /**
     * Write corbaOperations into a marshalled stream
     * @param ostream the output stream
     * @param value corbaOperations value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, corba.corbaOperations value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to corbaOperations
     * @param obj the CORBA Object
     * @return corbaOperations Object
     */
    public static corbaOperations narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof corbaOperations)
            return (corbaOperations)obj;

        if (obj._is_a(id()))
        {
            _corbaOperationsStub stub = new _corbaOperationsStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to corbaOperations
     * @param obj the CORBA Object
     * @return corbaOperations Object
     */
    public static corbaOperations unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof corbaOperations)
            return (corbaOperations)obj;

        _corbaOperationsStub stub = new _corbaOperationsStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
