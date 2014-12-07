package corba;

/**
 * Interface definition: corbaOperations.
 * 
 * @author OpenORB Compiler
 */
public class _corbaOperationsStub extends org.omg.CORBA.portable.ObjectImpl
        implements corbaOperations
{
    static final String[] _ids_list =
    {
        "IDL:corba/corbaOperations:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = corba.corbaOperationsOperations.class;

    /**
     * Operation createAccount
     */
    public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String userName, String password, String institution)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createAccount",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(emailAddress);
                    _output.write_string(phoneNumber);
                    _output.write_string(userName);
                    _output.write_string(password);
                    _output.write_string(institution);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createAccount",_opsClass);
                if (_so == null)
                   continue;
                corba.corbaOperationsOperations _self = (corba.corbaOperationsOperations) _so.servant;
                try
                {
                    return _self.createAccount( firstName,  lastName,  emailAddress,  phoneNumber,  userName,  password,  institution);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation reserveBook
     */
    public String reserveBook(String userName, String password, String bookName, String bookAuthor, String institution)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("reserveBook",true);
                    _output.write_string(userName);
                    _output.write_string(password);
                    _output.write_string(bookName);
                    _output.write_string(bookAuthor);
                    _output.write_string(institution);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reserveBook",_opsClass);
                if (_so == null)
                   continue;
                corba.corbaOperationsOperations _self = (corba.corbaOperationsOperations) _so.servant;
                try
                {
                    return _self.reserveBook( userName,  password,  bookName,  bookAuthor,  institution);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getNonReturners
     */
    public void getNonReturners(String adminUsername, String adminPassword, String educationalInstitution, int numdays)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getNonReturners",true);
                    _output.write_string(adminUsername);
                    _output.write_string(adminPassword);
                    _output.write_string(educationalInstitution);
                    _output.write_long(numdays);
                    _input = this._invoke(_output);
                    return;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNonReturners",_opsClass);
                if (_so == null)
                   continue;
                corba.corbaOperationsOperations _self = (corba.corbaOperationsOperations) _so.servant;
                try
                {
                    _self.getNonReturners( adminUsername,  adminPassword,  educationalInstitution,  numdays);
                    return;
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation reserveInterLibrary
     */
    public String reserveInterLibrary(String username, String password, String bookName, String authorName, String institution)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("reserveInterLibrary",true);
                    _output.write_string(username);
                    _output.write_string(password);
                    _output.write_string(bookName);
                    _output.write_string(authorName);
                    _output.write_string(institution);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reserveInterLibrary",_opsClass);
                if (_so == null)
                   continue;
                corba.corbaOperationsOperations _self = (corba.corbaOperationsOperations) _so.servant;
                try
                {
                    return _self.reserveInterLibrary( username,  password,  bookName,  authorName,  institution);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
