package server;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {

        Settings setting = new Settings();
        System.out.println("url " + setting.getValue(Settings.URL));
        System.out.println("user " + setting.getValue(Settings.USER));
        System.out.println("pass " + setting.getValue(Settings.PASS));

        Authors author1 = new Authors(0, null, null);
        Authors author2 = new Authors(6, "Alexander Pushkin", "Poem");
        Authors author3 = new Authors(5, null, "Onegin");
        Authors author4 = new Authors(5, "Sergey Esenin", null);



        Documents document1 = new Documents(6, "Music", "Godsmack", new Date(2022/01/01), 5);
        Documents document2 = new Documents(5, "Music new", null, new Date(2022/01/02), 5);

        try (DbServer dbServer = DbServer.getDbServer()) {
//            System.out.println(dbServer.addAuthor(author1));
//            System.out.println(dbServer.addAuthor(author2));
//            System.out.println(dbServer.addAuthor(author3));
//            System.out.println(dbServer.addAuthor(author4));
//            System.out.println(dbServer.addDocument(document1, author2));
//            System.out.println(dbServer.addDocument(document2, author2));
//
//            Documents [] AuthorName = dbServer.findDocumentByAuthor(new Authors(4, "Mikhail Lermontov", null));
//            if (AuthorName != null) {
//            for (Documents doc : AuthorName)
//            System.out.println(doc);
//            }
//
//            Documents [] Content = dbServer.findDocumentByContent("F");
//            if (Content != null) {
//            for (Documents doc : Content)
//            System.out.println(doc);
//            }
//
//            System.out.println(dbServer.deleteAuthor(new Authors(6,null,null)));

        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
