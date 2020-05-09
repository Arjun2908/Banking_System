package banking;

import java.sql.*;

public class DataBase {

    private String url;

    public DataBase(String fileName) {
        this.url = "jdbc:sqlite:" + fileName;
    }

    private Connection connect() {
        // SQLite connection string

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn == null) {
                System.out.println("A new database NOT been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewTable() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "    id INTEGER PRIMARY KEY ASC,\n"
                + "    number TEXT,\n"
                + "    pin TEXT,\n"
                + "    balance INTEGER DEFAULT 0\n"
                + ");";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String cardNumber, String cardPin, int balance) {

        String sql = "INSERT INTO card(number,pin,balance) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            pstmt.setInt(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void selectAll() {
        String sql = "SELECT id, number, pin, balance FROM card";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("number") + "\t" +
                        rs.getString("pin") + "\t" +
                        rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String selectCardNum(String cardNumber) {

        String resultNumber = "";

        String sql = "SELECT number, pin, balance FROM card WHERE number=" + cardNumber;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                resultNumber = rs.getString("number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultNumber;
    }

    public BankCard selectCard(String cardNumber, String cardPin) {
        BankCard result  = new BankCard();
        String resultNumber = "";
        String resultPin = "";

        String sql = "SELECT number, pin, balance FROM card WHERE number=" + cardNumber
                + " AND pin=" + cardPin;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                resultNumber = rs.getString("number");
                resultPin = rs.getString("pin");
                System.out.println("selectCard() = " + resultNumber + "\n pin=" + rs.getString("pin"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        result.setCardNumber(resultNumber);
        result.setCardPin(resultPin);
        return result;
    }

    public void closeAccount(BankCard bankCard){
        int delId = 0;
        String sql = "SELECT id FROM card WHERE number=" + bankCard.getCardNumber();
        System.out.println("Delete request for account: " + bankCard.getCardNumber());
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                delId = rs.getInt("id");
                System.out.println("Id to delete = " + delId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM card WHERE id = ?";
        System.out.println("try del " + bankCard.getCardNumber() + " id=" + delId);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1,delId);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public BankCard selectDestCard(String cardNumber) {
        BankCard result  = new BankCard();
        String resultNumber = "";
        int resultBalance = 0;

        String sql = "SELECT number, balance FROM card WHERE number=" + cardNumber;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                resultNumber = rs.getString("number");
                resultBalance = rs.getInt("balance");
                System.out.println("selectCard() = " + resultNumber + "\n bal=" + rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        result.setCardNumber(resultNumber);
        result.setCardBalance(resultBalance);
        return result;
    }

    public void addBalance(int balance, BankCard bankCard) {
        int thisId = 0;
        String sql = "SELECT id FROM card WHERE number=" + bankCard.getCardNumber();
        System.out.println("Add money request on: " + bankCard.getCardNumber());
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                thisId = rs.getInt("id");
                System.out.println("Account id = " + thisId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Money Successfully Transferred!");
        sql = "UPDATE card SET balance = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, balance);
            pstmt.setInt(2, thisId);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getBalance(BankCard currentCard) {
        int resultBalance = 0;
        String sql = "SELECT balance FROM card WHERE number=" + currentCard.getCardNumber();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                resultBalance = rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultBalance;
    }
}