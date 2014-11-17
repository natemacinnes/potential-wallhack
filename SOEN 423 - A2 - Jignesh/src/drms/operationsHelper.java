package drms;

/** 
 * Helper class for : operations
 *  
 * @author OpenORB Compiler
 */ 
public class operationsHelper
{
    /**
     * Insert operations into an any
     * @param a an any
     * @param t operations value
     */
    public static void insert(org.omg.CORBA.Any a, drms.operations t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract operations from an any
     *
     * @param a an any
     * @return the extracted operations value
     */
    public static drms.operations extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return drms.operationsHelper.narrow( a.extract_Object() );
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
     * Return the operations TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "operations" );
        }
        return _tc;
    }

    /**
     * Return the operations IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:drms/operations:1.0";

    /**
     * Read operations from a marshalled stream
     * @param istream the input stream
     * @return the readed operations value
     */
    public static drms.operations read(org.omg.CORBA.portable.InputStream istream)
    {
        return(drms.operations)istream.read_Object(drms._operationsStub.class);
    }

    /**
     * Write operations into a marshalled stream
     * @param ostream the output stream
     * @param value operations value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, drms.operations value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to operations
     * @param obj the CORBA Object
     * @return operations Object
     */
    public static operations narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof operations)
            return (operations)obj;

        if (obj._is_a(id()))
        {
            _operationsStub stub = new _operationsStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to operations
     * @param obj the CORBA Object
     * @return operations Object
     */
    public static operations unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof operations)
            return (operations)obj;

        _operationsStub stub = new _operationsStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
