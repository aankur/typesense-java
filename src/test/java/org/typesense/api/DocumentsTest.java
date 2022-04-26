package org.typesense.api;

import junit.framework.TestCase;
import org.typesense.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DocumentsTest extends TestCase {

    public Client client;
    private Helper helper;

    public void setUp() throws Exception {
        super.setUp();
        helper = new Helper();
        this.client = helper.getClient();
        helper.createTestCollection();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        helper.teardown();
    }

    public void testRetrieveDocument(){
        helper.createTestDocument();
        System.out.println(client.collections("books").documents("1").retrieve());
    }

    public void testCreateDocument(){

        String[] authors = {"shakspeare","william"};
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("title","Romeo and juliet");
        hmap.put("authors",authors);
        hmap.put("image_url","fgfg");
        hmap.put("publication_year",1666);
        hmap.put("ratings_count",124);
        hmap.put("average_rating",3.2);
        hmap.put("publication_year_facet","dff");
        hmap.put("authors_facet",authors);
        hmap.put("id","1");

        System.out.println(client.collections("books").documents().create(hmap));
    }

    public void testUpsertDocument(){

        String[] authors = new String[]{"jk", "Rowling"};
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("title","harry potter");
        hmap.put("authors",authors);
        hmap.put("image_url","fgfg");
        hmap.put("publication_year",2001);
        hmap.put("ratings_count",231);
        hmap.put("average_rating",5.6);
        hmap.put("publication_year_facet","2001");
        hmap.put("authors_facet",authors);
        hmap.put("id","3");

        System.out.println(client.collections("books").documents().upsert(hmap));

    }

    public void testDeleteDocument(){
        helper.createTestDocument();
        System.out.println(client.collections("books").documents("1").delete());
    }

    public void testDeleteDocumentByQuery(){
        helper.createTestDocument();
        DeleteDocumentsParameters deleteDocumentsParameters = new DeleteDocumentsParameters();
        deleteDocumentsParameters.filterBy("publication_year:=[1666]");
        deleteDocumentsParameters.batchSize(10);
        System.out.println(client.collections("books").documents().delete(deleteDocumentsParameters));
    }

    public void testUpdateDocument(){
        helper.createTestDocument();
        String[] authors = new String[]{"Shakespeare", "william"};
        HashMap<String , Object> document = new HashMap<>();
        document.put("title","Romeo and juliet");
        document.put("authors",authors);
        document.put("id","1");
        client.collections("books").documents("1").update(document);
        //System.out.println(client.collections("books").documents("1").update(document));
    }

    public void testSearchDocuments(){
        helper.createTestDocument();
        SearchParameters searchParameters = new SearchParameters()
                                                .q("romeo")
                                                .queryBy("title,authors")
                                                .prefix("false,true");
        org.typesense.model.SearchResult searchResult = client.collections("books").documents().search(searchParameters);

        System.out.println(searchResult);
    }

    public void testImport(){
        HashMap<String, Object> document1 = new HashMap<>();
        HashMap<String, Object> document2 = new HashMap<>();
        ImportDocumentsParameters queryParameters = new ImportDocumentsParameters();
        ArrayList<HashMap<String, Object>> documentList = new ArrayList<>();

        document1.put("countryName","India");
        document1.put("capital","Delhi");
        document1.put("gdp",23);
        document2.put("countryName","Us");
        document2.put("capital","Washington");
        document2.put("gdp",233);

        documentList.add(document1);
        documentList.add(document2);

        queryParameters.action("create");
        System.out.println(this.client.collections("books").documents().import_(documentList, queryParameters));
    }

    public void testImportAsString(){
        ImportDocumentsParameters queryParameters = new ImportDocumentsParameters();
        queryParameters.action("create");
        String documentList = "{\"countryName\": \"India\", \"capital\": \"Washington\", \"gdp\": 5215}\n" +
                "{\"countryName\": \"Iran\", \"capital\": \"London\", \"gdp\": 5215}";
        System.out.println(this.client.collections("books").documents().import_(documentList, queryParameters));
    }

    public void testExportDocuments(){
        helper.createTestDocument();
        ExportDocumentsParameters exportDocumentsParameters = new ExportDocumentsParameters();
        exportDocumentsParameters.setExcludeFields("id,publication_year,authors");
        System.out.println(client.collections("books").documents().export(exportDocumentsParameters));
    }

    public void testImportFromFile() throws FileNotFoundException {
        File myObj = new File("/tmp/books.jsonl");
        ImportDocumentsParameters queryParameters = new ImportDocumentsParameters();
        Scanner myReader = new Scanner(myObj);
        StringBuilder data = new StringBuilder();
        while (myReader.hasNextLine()) {
            data.append(myReader.nextLine()).append("\n");
        }
        client.collections("books").documents().import_(data.toString(), queryParameters);
    }

    public void testDirtyCreate(){
        helper.createTestDocument();
        ImportDocumentsParameters queryParameters = new ImportDocumentsParameters();
        queryParameters.dirtyValues(ImportDocumentsParameters.DirtyValuesEnum.COERCE_OR_REJECT);
        queryParameters.action("upsert");
        String[] authors = {"shakspeare","william"};
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("title", 111);
        hmap.put("authors",authors);
        hmap.put("publication_year",1666);
        hmap.put("ratings_count",124);
        hmap.put("average_rating",3.2);
        hmap.put("id","2");

        System.out.println(this.client.collections("books").documents().create(hmap,queryParameters));
    }
}