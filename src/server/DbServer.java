package server;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DbServer implements IDbService{
    private static DbServer dbServer;
    private Connection con;
    private Statement stm;
    private ResultSet rs;

    private DbServer(){
    }


    public static DbServer getDbServer() {
        if (dbServer == null) dbServer = new DbServer();
        return dbServer;
    }

    @Override
    public boolean addAuthor(Authors author) throws DocumentException {
        Settings setting = new Settings();
        try( Connection connection = DriverManager.getConnection(
                setting.getValue(Settings.URL),
                setting.getValue(Settings.USER),
                setting.getValue(Settings.PASS))){
            if(author.getAuthor_id() != 0
                    && author.getAuthor()!= null
                    && !author.getAuthor().trim().equals("")
                    && author.getNotes() != null){
                if (stm == null) stm = connection.createStatement();
                stm.executeUpdate("INSERT INTO Authors (author_id, author, notes) VALUES (" + author.getAuthor_id() + ",'" + author.getAuthor() + "','" + author.getNotes() + "')");
                return true;
            } else if(author.getAuthor_id() != 0 && author.getAuthor() == null){
                if(stm == null) stm = connection.createStatement();
                stm.executeUpdate("UPDATE Authors SET note = '" + author.getNotes() + "' WHERE author_id = " + author.getAuthor_id());
                return false;
            } else if(author.getAuthor_id() != 0 && author.getNotes() == null){
                if(stm == null) stm = connection.createStatement();
                stm.executeUpdate("UPDATE Authors SET author = '" + author.getAuthor() + "' WHERE author_id = " + author.getAuthor_id());
                return false;
            } else
                throw new DocumentException("Обновление не произошло");
        } catch (SQLException ex) {
            throw new DocumentException("Cоединение не установлено" + ex.getMessage());
        }

    }


    @Override
    public boolean addDocument(Documents doc, Authors author) throws DocumentException {
        Settings setting = new Settings();
        try( Connection connection = DriverManager.getConnection(
                setting.getValue(Settings.URL),
                setting.getValue(Settings.USER),
                setting.getValue(Settings.PASS))){
            if (doc.getDocument_id() != 0
                    && author.getAuthor_id() != 0
                    && doc.getAuthor_id() != 0
                    && doc.getTitle() != null
                    && !doc.getTitle().trim().equals("")
                    && doc.getText() != null
                    && !doc.getText().trim().equals("")
                    && doc.getDate() != null) {
                if (stm == null) stm = connection.createStatement();
                stm.executeUpdate("INSERT INTO Documents (document_id, title, text, date, author_id) " +
                        "VALUES (" + doc.getDocument_id() + ",'" + doc.getTitle() + "', '" + doc.getText() + "', '" + doc.getDate()+ "', " + doc.getAuthor_id() + ")");
                return true;
            }  else if (doc.getDocument_id() != 0
                    && doc.getAuthor_id() != 0
                    && doc.getText() == null) {
                if (stm == null) stm = connection.createStatement();
                stm.executeUpdate("UPDATE Documents SET " + "title ='" + doc.getTitle() + "', " +
                        "date ='" + doc.getDate() + "', " + "author_id =" + doc.getAuthor_id() + " WHERE document_id =" + doc.getDocument_id());
                return false;
            } else throw new DocumentException("Обновление не произошло");
        } catch (SQLException ex) {
            throw new DocumentException("Соединение не установлено" + ex.getMessage());
        }
    }

    @Override
    public Documents[] findDocumentByAuthor(Authors author) throws DocumentException {
        Settings setting = new Settings();
        List<Documents> list = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(
                setting.getValue(Settings.URL),
                setting.getValue(Settings.USER),
                setting.getValue(Settings.PASS))){
            if (author.getAuthor() != null && !author.getAuthor().trim().equals("")) {
                if (stm == null) stm= connection.createStatement();
                rs = stm.executeQuery("SELECT documents.document_id, documents.title, "
                        + "documents.text, documents.date, documents.author_id  FROM Documents  "
                        + "LEFT JOIN Authors ON authors.author_id=documents.author_id WHERE authors.author = '" + author.getAuthor() + "'");
                while (rs.next()) {
                    Documents doc = new Documents(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDate(4),
                            rs.getInt(5));
                    list.add(doc);
                }
                if (list.isEmpty()) return null;
                Documents[] retList = new Documents[list.size()];
                int i = 0;
                for (Documents d : list)
                    retList[i++] = d;
                return retList;
            } else throw new DocumentException("Поиск невозможен");
        } catch (SQLException ex) {
            throw new DocumentException("Соединение не установлено" + ex.getMessage());
        }
    }

    @Override
    public Documents[] findDocumentByContent(String content) throws DocumentException {
        Settings setting = new Settings();
        List<Documents> list = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(
                setting.getValue(Settings.URL),
                setting.getValue(Settings.USER),
                setting.getValue(Settings.PASS))){
            if(content != null){
                if (stm == null) stm = connection.createStatement();
                rs = stm.executeQuery("SELECT * FROM Documents WHERE text LIKE '" + content + "%'");
                while(rs.next()) {
                    Documents doc = new Documents(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDate(4),
                            rs.getInt(5));
                    list.add(doc);
                }
                if (list.isEmpty()) return null;
                Documents[] retList = new Documents[list.size()];
                int i = 0;
                for (Documents d : list)
                    retList[i++] = d;
                return retList;
            }else throw new DocumentException("Наименование несоотвествует");
        } catch (SQLException e) {
            throw new DocumentException("Соединение не установлено");
        }
    }

    @Override
    public boolean deleteAuthor(Authors author) throws DocumentException {
        Settings setting = new Settings();
        try(Connection connection = DriverManager.getConnection(
                setting.getValue(Settings.URL),
                setting.getValue(Settings.USER),
                setting.getValue(Settings.PASS))){
            if (author.getAuthor_id() != 0) {
                if (stm == null) stm = connection.createStatement();
                stm.executeUpdate("DELETE FROM Documents WHERE author_id =" + author.getAuthor_id());
                stm.executeUpdate("DELETE FROM Authors WHERE document_id =" + author.getAuthor_id());
                return true;
            } else if (author.getAuthor() != null && !author.getAuthor().trim().equals("")) {
                if (stm == null) stm = connection.createStatement();
                stm.executeQuery("SELECT document_id FROM Authors WHERE author = '" + author.getAuthor() + "'");
                if (!rs.next()) return false;
                int idAuthor = rs.getInt(1);
                stm.executeUpdate("DELETE FROM Documents WHERE author_id =" + idAuthor);
                stm.executeUpdate("DELETE FROM Authors WHERE document_id=" + idAuthor);
                return true;
            } else throw new DocumentException("Поле заполнено неправильно");
        }catch (SQLException e) {
            throw new DocumentException("Соединение не установлено");
        }
    }

    @Override
    public boolean deleteAuthor(int id) throws DocumentException {
        Settings setting = new Settings();
        try(Connection connection = DriverManager.getConnection(
                setting.getValue(Settings.URL),
                setting.getValue(Settings.USER),
                setting.getValue(Settings.PASS))){
            if(id != 0){
                if (stm == null) stm = connection.createStatement();
                stm.executeUpdate("DELETE FROM Documents WHERE author_id =" + id);
                stm.executeUpdate("DELETE FROM Authors WHERE document_id=" + id);
                return true;
            } else return false;
        }catch (SQLException e) {
            throw new DocumentException("Соединение не установлено");
        }
    }

    @Override
    public void close() throws Exception {
        if (con != null && con.isValid(0)) con.close();
        if (stm != null) stm.close();
        if (rs != null) rs.close();
        System.out.println("Соединение прервано");
    }

}