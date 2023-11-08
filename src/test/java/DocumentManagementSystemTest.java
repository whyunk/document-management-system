import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static attributes.Attributes.*;
import static org.junit.jupiter.api.Assertions.*;

class DocumentManagementSystemTest {
    private static final String RESOURCES = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
    private static final String LETTER = RESOURCES + "patient.letter";
    private static final String REPORT = RESOURCES + "patient.report";
    private static final String XRAY = RESOURCES + "xray.jpg";
    private static final String INVOICE = RESOURCES + "patient.invoice";

    private DocumentManagementSystem documentManagementSystem = new DocumentManagementSystem();

    @Test
    public void shouldImportFile() throws Exception {
        documentManagementSystem.importFile(LETTER);
        List<Document> contents = documentManagementSystem.contents();
        assertEquals(1,contents.size());
    }

    @Test
    public void shouldImportLetterAttributes() throws Exception {
        documentManagementSystem.importFile(LETTER);
        List<Document> contents = documentManagementSystem.contents();
        Document document = contents.get(0);

        assertEquals(" Joe Bloggs", document.getAttribute(PATIENT));
        assertEquals("123 Fake Street\n" + "Westminster\n" + "London\n" + "United Kingdom", document.getAttribute(ADDRESS));
        assertEquals("We are writing to you to confirm the re-scheduling of your appointment\n"
                + "with Dr. Avaj from 29th December 2016 to 5th January 2017.", document.getAttribute(BODY));
        assertEquals("LETTER",document.getAttribute(TYPE));
    }

    @Test
    public void shouldImportImageAttributes() throws Exception {
        documentManagementSystem.importFile(XRAY);
        List<Document> contents = documentManagementSystem.contents();
        Document document = contents.get(0);

        assertEquals(RESOURCES + "xray.jpg",document.getAttribute(PATH));
        assertEquals("320",document.getAttribute(WIDTH));
        assertEquals("179", document.getAttribute(HEIGHT));
        assertEquals("IMAGE", document.getAttribute(TYPE));

    }

    @Test
    public void shouldImportReportAttributes() throws Exception {
        documentManagementSystem.importFile(REPORT);
        List<Document> contents = documentManagementSystem.contents();
        Document document = contents.get(0);

        assertEquals("Joe Bloggs", document.getAttribute(PATIENT));
        assertEquals("On 5th January 2017 I examined Joe's teeth.\n"
                + "We discussed his switch from drinking Coke to Diet Coke.\n"
                + "No new problems were noted with his teeth.", document.getAttribute(BODY));
        assertEquals("REPORT", document.getAttribute(TYPE));

    }

    @Test
    public void shouldImportInvoiceAttributes() throws Exception {
        documentManagementSystem.importFile(INVOICE);
        List<Document> contents = documentManagementSystem.contents();
        Document document = contents.get(0);

        assertEquals("Joe Bloggs", document.getAttribute(PATIENT));
        assertEquals("$100", document.getAttribute(AMOUNT));
        assertEquals("INVOICE", document.getAttribute(TYPE));

    }

    @Test
    public void shouldBeAbleToSearchFilesByAttributes() throws Exception {
        documentManagementSystem.importFile(LETTER);
        documentManagementSystem.importFile(XRAY);
        documentManagementSystem.importFile(REPORT);

        List<Document> search = documentManagementSystem.search("patient:Joe,body:Diet Coke");
        assertEquals(1,search.size());
        assertEquals("Joe Bloggs", search.get(0).getAttribute(PATIENT));
        assertEquals("On 5th January 2017 I examined Joe's teeth.\n"
                + "We discussed his switch from drinking Coke to Diet Coke.\n"
                + "No new problems were noted with his teeth.", search.get(0).getAttribute(BODY));
        assertEquals("REPORT", search.get(0).getAttribute(TYPE));
    }

    @Test
    public void shouldNotImportMissingFile() throws Exception {
        assertThrows(FileNotFoundException.class,
                () -> {documentManagementSystem.importFile("hyunwoo.txt");});
    }

    @Test
    public void shouldNotImportUnknownFile() throws Exception {
        assertThrows(UnKnownFileTypeException.class,
                () -> documentManagementSystem.importFile(RESOURCES + "unknown.txt"));
    }
}