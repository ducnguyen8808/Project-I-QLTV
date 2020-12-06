import java.sql.*;

public class ConnectDatabase {
    public Connection CreatConnect() {
        String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLThuVien";
        String USER = "admin";
        String PASSWORD = "1212";
        Connection connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public Statement getStatement(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
        }
        return statement;
    }
    public String getUsername() {
        String username = "";
        Statement stm = null;
        try {
            stm = getStatement(CreatConnect());
            ResultSet resultSet = stm.executeQuery("select * from THUTHU");
            while (resultSet.next()) {
                username += resultSet.getString(4) + ",";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }
    public String getPass(String user) {
        String password = new String("");
        Statement statement = null;
        try {
            statement = getStatement(CreatConnect());
            String sql = new String("select * from THUTHU where USERNAME = '" + user + "'");
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                password = resultSet.getString(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    public void insertData(String str) throws SQLException {
        Connection connection = CreatConnect();
        Statement statement = getStatement(connection);
        statement.executeUpdate(str);
        statement.close();
        connection.close();
    }

    private String[][] dsDOCGIAMUON = new String[50][6];
    private String[][] dsSACH = new String[50][9];
    private String[][] dsDOCGIA = new String[50][5];
    private String[] listMaDG = new String[50];
    private String[][] dsMUON = new String[50][6];
    public String[] getListMaDG() {
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select MATHE from DOCGIA");
            int index =0;
            while (resultSet.next()) {
                listMaDG[index] = resultSet.getString(1);
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMaDG;
    }
    public String[][]getDsSACH() {
        for (int i = 0; i < dsSACH.length; i++) {
            for (int j = 0; j < dsSACH[0].length; j++)
                dsSACH[i][j] = null;
        }
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select MAKHO, MASACH, TENSACH, TACGIA,TENNXB, SOLUONG, NGONNGU, MONLOAI, GIATIEN from SACH, NXB where SACH.MANXB = NXB.MANXB");
            int index =0;
            while (resultSet.next()) {
                dsSACH[index][0] = resultSet.getString(1);
                dsSACH[index][1] = resultSet.getString(2);
                dsSACH[index][2] = resultSet.getString(3);
                dsSACH[index][3] = resultSet.getString(4);
                dsSACH[index][4] = resultSet.getString(5);
                dsSACH[index][5] = String.valueOf(resultSet.getInt(6));
                dsSACH[index][6] = resultSet.getString(7);
                dsSACH[index][7] = resultSet.getString(8);
                dsSACH[index][8] = resultSet.getString(9);
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsSACH;
    }
    private String[][] listSum = new String[50][2];
    public void updateListSum(String[][] listSum) {
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select MATHE, sum(SOLUONG) as SOLUONG from MUONTRA group by MATHE");
            int index = 0;
            while (resultSet.next()) {
                listSum[index][0] = resultSet.getString(1);
                listSum[index][1] = resultSet.getString(2);
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String[][] getDocGiaMuon() {
        for (int i = 0; i < dsDOCGIAMUON.length; i++) {
            for (int j = 0; j < dsDOCGIAMUON[0].length; j++)
                dsDOCGIAMUON[i][j] = null;
        }
        try {
            updateListSum(listSum);
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select HOTEN, GIOITINH, CMND, MATHE, HANDUNG\n" +
                    "from DOCGIA");
            int index = 0;
            while (resultSet.next()) {
                dsDOCGIAMUON[index][0] = resultSet.getString(1);
                dsDOCGIAMUON[index][1] = resultSet.getString(2);
                dsDOCGIAMUON[index][2] = resultSet.getString(3);
                dsDOCGIAMUON[index][3] = resultSet.getString(4);
                dsDOCGIAMUON[index][4] = resultSet.getString(5);
                for (int i = 0; i < listSum.length; i++) {
                    if (dsDOCGIAMUON[index][3].equals(listSum[i][0])) {
                        dsDOCGIAMUON[index][5] = listSum[i][1];
                        break;
                    } else {
                        dsDOCGIAMUON[index][5] = "0";
                        break;
                    }
                }
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsDOCGIAMUON;
    }
    public String[][] getDocGia() {
        for (int i = 0; i < dsDOCGIA.length; i++) {
            for (int j = 0; j < dsDOCGIA[0].length; j++)
                dsDOCGIA[i][j] = null;
        }
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select HOTEN, GIOITINH, CMND, DOCGIA.MATHE, HANDUNG\n" +
                    "from DOCGIA");
            int index = 0;
            while (resultSet.next()) {
                dsDOCGIA[index][0] = resultSet.getString(1);
                dsDOCGIA[index][1] = resultSet.getString(2);
                dsDOCGIA[index][2] = resultSet.getString(3);
                dsDOCGIA[index][3] = resultSet.getString(4);
                dsDOCGIA[index][4] = resultSet.getString(5);
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsDOCGIA;
    }
    public String getMATHUTHU(String str) {
        String temp = "";
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select MATHUTHU from THUTHU where USERNAME = '" + str + "'");
            while (resultSet.next()) {
                temp = resultSet.getString(1);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public String getTenThuThu(String str) {
        String temp = "";
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select HOTEN from THUTHU where USERNAME = '" + str + "'");
            while (resultSet.next()) {
                temp = resultSet.getString(1);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public String getTenDocGia(String str) {
        String temp = "";
        try {
            Connection connection = CreatConnect();
            Statement statement = getStatement(connection);
            ResultSet resultSet = statement.executeQuery("select HOTEN from DOCGIA where MATHE = " + str);
            while (resultSet.next()) {
                temp = resultSet.getString(1);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public String[][] getDsMUON(String str) {
        int index = 0;
        for (int i = 0; i < 50; i++) {
            dsMUON[i] = new String[]{null, null, null, null, null, null};
        }
        try {
            Connection connection = CreatConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select MUONTRA.MASACH, TENSACH, TACGIA, MUONTRA.SOLUONG, NGAYMUON, DKTRA\n" +
                    "from MUONTRA, SACH\n" +
                    "where MUONTRA.MASACH = SACH.MASACH and MUONTRA.MATHE = " + str);
            while (resultSet.next()) {
                dsMUON[index][0] = resultSet.getString(1);
                dsMUON[index][1] = resultSet.getString(2);
                dsMUON[index][2] = resultSet.getString(3);
                dsMUON[index][3] = resultSet.getString(4);
                dsMUON[index][4] = resultSet.getString(5);
                dsMUON[index][5] = resultSet.getString(6);
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsMUON;
    }
    public void deleteDataSach(String str) {
        try {
            Connection connection = CreatConnect();
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from SACH where MASACH = '" + str + "'");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteDataDocGia(String str) {
        try {
            Connection connection = CreatConnect();
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from DOCGIA where MATHE = '" + str + "'");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
