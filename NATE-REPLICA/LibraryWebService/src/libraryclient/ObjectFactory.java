
package libraryclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the libraryclient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateAccountResponse_QNAME = new QName("http://libraryserver/", "createAccountResponse");
    private final static QName _GetNonreturnersResponse_QNAME = new QName("http://libraryserver/", "getNonreturnersResponse");
    private final static QName _IntitutionReserve_QNAME = new QName("http://libraryserver/", "intitutionReserve");
    private final static QName _IntitutionReserveResponse_QNAME = new QName("http://libraryserver/", "intitutionReserveResponse");
    private final static QName _UdpServer_QNAME = new QName("http://libraryserver/", "udpServer");
    private final static QName _ReserveBook_QNAME = new QName("http://libraryserver/", "reserveBook");
    private final static QName _GetNonreturners_QNAME = new QName("http://libraryserver/", "getNonreturners");
    private final static QName _ReserveInterLibraryResponse_QNAME = new QName("http://libraryserver/", "reserveInterLibraryResponse");
    private final static QName _ReserveBookResponse_QNAME = new QName("http://libraryserver/", "reserveBookResponse");
    private final static QName _ReserveInterLibrary_QNAME = new QName("http://libraryserver/", "reserveInterLibrary");
    private final static QName _CreateAccount_QNAME = new QName("http://libraryserver/", "createAccount");
    private final static QName _UdpServerResponse_QNAME = new QName("http://libraryserver/", "udpServerResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: libraryclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReserveInterLibraryResponse }
     * 
     */
    public ReserveInterLibraryResponse createReserveInterLibraryResponse() {
        return new ReserveInterLibraryResponse();
    }

    /**
     * Create an instance of {@link UdpServerResponse }
     * 
     */
    public UdpServerResponse createUdpServerResponse() {
        return new UdpServerResponse();
    }

    /**
     * Create an instance of {@link ReserveInterLibrary }
     * 
     */
    public ReserveInterLibrary createReserveInterLibrary() {
        return new ReserveInterLibrary();
    }

    /**
     * Create an instance of {@link ReserveBookResponse }
     * 
     */
    public ReserveBookResponse createReserveBookResponse() {
        return new ReserveBookResponse();
    }

    /**
     * Create an instance of {@link CreateAccount }
     * 
     */
    public CreateAccount createCreateAccount() {
        return new CreateAccount();
    }

    /**
     * Create an instance of {@link IntitutionReserve }
     * 
     */
    public IntitutionReserve createIntitutionReserve() {
        return new IntitutionReserve();
    }

    /**
     * Create an instance of {@link GetNonreturnersResponse }
     * 
     */
    public GetNonreturnersResponse createGetNonreturnersResponse() {
        return new GetNonreturnersResponse();
    }

    /**
     * Create an instance of {@link CreateAccountResponse }
     * 
     */
    public CreateAccountResponse createCreateAccountResponse() {
        return new CreateAccountResponse();
    }

    /**
     * Create an instance of {@link ReserveBook }
     * 
     */
    public ReserveBook createReserveBook() {
        return new ReserveBook();
    }

    /**
     * Create an instance of {@link GetNonreturners }
     * 
     */
    public GetNonreturners createGetNonreturners() {
        return new GetNonreturners();
    }

    /**
     * Create an instance of {@link UdpServer }
     * 
     */
    public UdpServer createUdpServer() {
        return new UdpServer();
    }

    /**
     * Create an instance of {@link IntitutionReserveResponse }
     * 
     */
    public IntitutionReserveResponse createIntitutionReserveResponse() {
        return new IntitutionReserveResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAccountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "createAccountResponse")
    public JAXBElement<CreateAccountResponse> createCreateAccountResponse(CreateAccountResponse value) {
        return new JAXBElement<CreateAccountResponse>(_CreateAccountResponse_QNAME, CreateAccountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNonreturnersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "getNonreturnersResponse")
    public JAXBElement<GetNonreturnersResponse> createGetNonreturnersResponse(GetNonreturnersResponse value) {
        return new JAXBElement<GetNonreturnersResponse>(_GetNonreturnersResponse_QNAME, GetNonreturnersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntitutionReserve }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "intitutionReserve")
    public JAXBElement<IntitutionReserve> createIntitutionReserve(IntitutionReserve value) {
        return new JAXBElement<IntitutionReserve>(_IntitutionReserve_QNAME, IntitutionReserve.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntitutionReserveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "intitutionReserveResponse")
    public JAXBElement<IntitutionReserveResponse> createIntitutionReserveResponse(IntitutionReserveResponse value) {
        return new JAXBElement<IntitutionReserveResponse>(_IntitutionReserveResponse_QNAME, IntitutionReserveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UdpServer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "udpServer")
    public JAXBElement<UdpServer> createUdpServer(UdpServer value) {
        return new JAXBElement<UdpServer>(_UdpServer_QNAME, UdpServer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveBook }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "reserveBook")
    public JAXBElement<ReserveBook> createReserveBook(ReserveBook value) {
        return new JAXBElement<ReserveBook>(_ReserveBook_QNAME, ReserveBook.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNonreturners }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "getNonreturners")
    public JAXBElement<GetNonreturners> createGetNonreturners(GetNonreturners value) {
        return new JAXBElement<GetNonreturners>(_GetNonreturners_QNAME, GetNonreturners.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveInterLibraryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "reserveInterLibraryResponse")
    public JAXBElement<ReserveInterLibraryResponse> createReserveInterLibraryResponse(ReserveInterLibraryResponse value) {
        return new JAXBElement<ReserveInterLibraryResponse>(_ReserveInterLibraryResponse_QNAME, ReserveInterLibraryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveBookResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "reserveBookResponse")
    public JAXBElement<ReserveBookResponse> createReserveBookResponse(ReserveBookResponse value) {
        return new JAXBElement<ReserveBookResponse>(_ReserveBookResponse_QNAME, ReserveBookResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveInterLibrary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "reserveInterLibrary")
    public JAXBElement<ReserveInterLibrary> createReserveInterLibrary(ReserveInterLibrary value) {
        return new JAXBElement<ReserveInterLibrary>(_ReserveInterLibrary_QNAME, ReserveInterLibrary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "createAccount")
    public JAXBElement<CreateAccount> createCreateAccount(CreateAccount value) {
        return new JAXBElement<CreateAccount>(_CreateAccount_QNAME, CreateAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UdpServerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://libraryserver/", name = "udpServerResponse")
    public JAXBElement<UdpServerResponse> createUdpServerResponse(UdpServerResponse value) {
        return new JAXBElement<UdpServerResponse>(_UdpServerResponse_QNAME, UdpServerResponse.class, null, value);
    }

}
