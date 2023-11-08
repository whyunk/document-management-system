import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentManagementSystem {
    private final List<Document> documents = new ArrayList<>();

    private final Map<String,Importer> extensionToImporter = new HashMap<>();

    public DocumentManagementSystem() {
        extensionToImporter.put("letter", new LetterImporter());
        extensionToImporter.put("report", new ReportImporter());
        extensionToImporter.put("jpg", new ImageImporter());
    }
    {
        extensionToImporter.put("invoice", new InvoiceImporter());
    }

    public void importFile(String path) throws IOException {
        final File file = new File(path);
        if(!file.exists()) {
            throw new FileNotFoundException(path);
        }

        final int separatorIndex = path.lastIndexOf('.');
        if(separatorIndex != -1) {
            if (separatorIndex == path.length()) {
                throw new UnKnownFileTypeException("No extension found For file : " + path);
            }
            final String extension = path.substring(separatorIndex +1);
            final Importer importer = extensionToImporter.get(extension);
            if (importer == null) {
                throw new UnKnownFileTypeException("For file: " + path);
            }

            final Document document = importer.importFile(file);
            documents.add(document);
        } else {
            throw new UnKnownFileTypeException("No extension found For file: " + path);
        }

    }

    public List<Document> contents() {
        return documents;
    }

    public List<Document> search(final String query) {
        return documents.stream()
                .filter(Query.parse(query))
                .collect(Collectors.toList());
    }

}
