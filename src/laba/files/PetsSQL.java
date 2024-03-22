package laba.files;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import static constants.Components.panelImages;
import static constants.Components.textTime;
import static constants.Parameters.*;
import static laba.files.Habitat.clearSession;
import static laba.files.Habitat.setEnableStart;

public class PetsSQL {
    //Имя пользователя БД
    private final static String userNameSQL = "postgres";
    //Пароль пользователя БД
    private final static String passwordSQL = "user348576";
    //Ссылка на БД
    private final static String connectionUrlSQL = "jdbc:postgresql://localhost:5432/proga-laba";
    //Проверка и создание таблицы
    private static void checkTableCreate(Connection connection, Statement statement) throws SQLException {
        ResultSet tables = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            //Получение таблицы
            tables = metaData.getTables(null, null, "pets_table", null);
            if (!tables.next()) {
                // Если таблица не существует, создаем ее
                statement.executeUpdate("CREATE TABLE pets_table (" +
                        "id INTEGER NOT NULL PRIMARY KEY UNIQUE, " +
                        "type VARCHAR(5) NOT NULL," +
                        "timeBirth INTEGER NOT NULL, " +
                        "speedTraectoryX INTEGER NOT NULL," +
                        "speedTraectoryY INTEGER NOT NULL," +
                        "X INTEGER NOT NULL," +
                        "Y INTEGER NOT NULL)");
                System.out.println("Таблица успешно создана");
            }
        }finally {
            if(tables != null)
                tables.close();
        }
    }
    //Проверка таблицы и вывод ошибки
    private static boolean checkTableError(Connection connection) throws SQLException {
        ResultSet tables = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            //Получение таблицы
            tables = metaData.getTables(null, null, "pets_table", null);
            if (!tables.next()) {
                // Если таблица не существует, то вывести окно с ошибкой
                JOptionPane.showMessageDialog(
                        panelImages,
                        "Таблица не существует. Для создания таблицы необходимо хотя бы раз сохранить объекты",
                        "Таблица не существует",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
            return true;
        }finally {
            if(tables != null)
                tables.close();
        }
    }
    //Сохранение всех объектов
    public static void savePets() throws SQLException {
        PreparedStatement preparedStatement = null;
        //Подключение к бд
        try(Connection connection = DriverManager.getConnection(connectionUrlSQL, userNameSQL, passwordSQL);
            Statement statement = connection.createStatement();){
            //Проверка таблицы на существование
            checkTableCreate(connection, statement);
            //Удаление старых данных
            statement.executeUpdate("DELETE FROM pets_table");
            if(petsList.isEmpty())
                return;
            //Вставка объектов
            preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "pets_table (id, type, timeBirth, speedTraectoryX, speedTraectoryY, X, Y) VALUES (?,?,?,?,?,?,?)");
            for(Pets pet: petsList){
                preparedStatement.setInt(1, pet.getId());
                preparedStatement.setString(2, pet.getType());
                preparedStatement.setInt(3, pet.getTimeBirth());
                preparedStatement.setInt(4, pet.speedTraectoryX);
                preparedStatement.setInt(5, pet.speedTraectoryY);
                preparedStatement.setInt(6, pet.getImageComponent().getX());
                preparedStatement.setInt(7, pet.getImageComponent().getY());
                preparedStatement.executeUpdate();
            }
        }finally {
            if(preparedStatement != null)
                preparedStatement.close();
        }
    }
    //Сохранение всех котов
    public static void saveCats() throws SQLException {
        PreparedStatement preparedStatement = null;
        //Подключение к бд
        try(Connection connection = DriverManager.getConnection(connectionUrlSQL, userNameSQL, passwordSQL);
            Statement statement = connection.createStatement();){
            //Проверка таблицы на существование
            checkTableCreate(connection, statement);
            //Удаление старых данных
            statement.executeUpdate("DELETE FROM pets_table");
            if(petsList.isEmpty())
                return;
            //Вставка объектов
            preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "pets_table (id, type, timeBirth, speedTraectoryX, speedTraectoryY, X, Y) VALUES (?,?,?,?,?,?,?)");
            for(Pets pet: petsList){
                if(pet.getType().equals("cat")) {
                    preparedStatement.setInt(1, pet.getId());
                    preparedStatement.setString(2, pet.getType());
                    preparedStatement.setInt(3, pet.getTimeBirth());
                    preparedStatement.setInt(4, pet.speedTraectoryX);
                    preparedStatement.setInt(5, pet.speedTraectoryY);
                    preparedStatement.setInt(6, pet.getImageComponent().getX());
                    preparedStatement.setInt(7, pet.getImageComponent().getY());
                    preparedStatement.executeUpdate();
                }
            }
        }finally {
            if(preparedStatement != null)
                preparedStatement.close();
        }
    }
    //Сохранение всех собак
    public static void saveDogs() throws SQLException {
        PreparedStatement preparedStatement = null;
        //Подключение к бд
        try(Connection connection = DriverManager.getConnection(connectionUrlSQL, userNameSQL, passwordSQL);
            Statement statement = connection.createStatement();){
            //Проверка таблицы на существование
            checkTableCreate(connection, statement);
            //Удаление старых данных
            statement.executeUpdate("DELETE FROM pets_table");
            if(petsList.isEmpty())
                return;
            //Вставка объектов
            preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "pets_table (id, type, timeBirth, speedTraectoryX, speedTraectoryY, X, Y) VALUES (?,?,?,?,?,?,?)");
            for(Pets pet: petsList){
                if(pet.getType().equals("dog")) {
                    preparedStatement.setInt(1, pet.getId());
                    preparedStatement.setString(2, pet.getType());
                    preparedStatement.setInt(3, pet.getTimeBirth());
                    preparedStatement.setInt(4, pet.speedTraectoryX);
                    preparedStatement.setInt(5, pet.speedTraectoryY);
                    preparedStatement.setInt(6, pet.getImageComponent().getX());
                    preparedStatement.setInt(7, pet.getImageComponent().getY());
                    preparedStatement.executeUpdate();
                }
            }
        }finally {
            if(preparedStatement != null)
                preparedStatement.close();
        }
    }
    //Получение всех объектов
    public static void getPets() throws SQLException {
        ResultSet resultSet = null;
        petsList.clear();
        if(flStart){
            //Проверка на запущенную симуляцию
            textTime.setVisible(false);
            setEnableStart(true);
            //Очистка старой симуляции
            clearSession();
            //Очистка таймера
            if(updateTimer != null)
                updateTimer.cancel();
            //Остановка потока
            if(petsMoveThreads != null && !petsMoveThreads.isShutdown())
                petsMoveThreads.shutdown();
        }
        //Подключение к бд
        try(Connection connection = DriverManager.getConnection(connectionUrlSQL, userNameSQL, passwordSQL);
            Statement statement = connection.createStatement();){
            //Проверка таблицы на существование
            if(!checkTableError(connection))
                return;
            resultSet = statement.executeQuery("SELECT * FROM pets_table");
            ArrayList<Pets> tempList = generationPets(resultSet);
            int correctTime = -1;
            if(tempList.isEmpty())
                return;
            flPreloadSQLPets = true;
            Collections.sort(tempList);
            for(Pets pets: tempList){
                if(correctTime == -1)
                    correctTime = pets.getTimeBirth();
                pets.setTimeBirth(pets.getTimeBirth() - correctTime + 1);
                petsList.add(pets);
                petsIdsSet.add(pets.getId());
                petsTimeBirthMap.put(pets.getId(), pets.getTimeBirth());
            }
        }finally {
            if(resultSet != null)
                resultSet.close();
        }
    }
    //Получение всех котов
    public static void getCats() throws SQLException {
        ResultSet resultSet = null;
        petsList.clear();
        if(flStart){
            //Проверка на запущенную симуляцию
            textTime.setVisible(false);
            setEnableStart(true);
            //Очистка старой симуляции
            clearSession();
            //Очистка таймера
            if(updateTimer != null)
                updateTimer.cancel();
            //Остановка потока
            if(petsMoveThreads != null && !petsMoveThreads.isShutdown())
                petsMoveThreads.shutdown();
        }
        //Подключение к бд
        try(Connection connection = DriverManager.getConnection(connectionUrlSQL, userNameSQL, passwordSQL);
            Statement statement = connection.createStatement();){
            //Проверка таблицы на существование
            if(!checkTableError(connection))
                return;
            resultSet = statement.executeQuery("SELECT * FROM pets_table WHERE type='cat'");
            ArrayList<Pets> tempList = generationPets(resultSet);
            int correctTime = -1;
            if(tempList.isEmpty())
                return;
            flPreloadSQLPets = true;
            Collections.sort(tempList);
            for(Pets pets: tempList){
                if(correctTime == -1)
                    correctTime = pets.getTimeBirth();
                pets.setTimeBirth(pets.getTimeBirth() - correctTime + 1);
                petsList.add(pets);
                petsIdsSet.add(pets.getId());
                petsTimeBirthMap.put(pets.getId(), pets.getTimeBirth());
            }
        }finally {
            if(resultSet != null)
                resultSet.close();
        }
    }
    //Получение всех Собак
    public static void getDogs() throws SQLException {
        ResultSet resultSet = null;
        petsList.clear();
        if(flStart){
            //Проверка на запущенную симуляцию
            textTime.setVisible(false);
            setEnableStart(true);
            //Очистка старой симуляции
            clearSession();
            //Очистка таймера
            if(updateTimer != null)
                updateTimer.cancel();
            //Остановка потока
            if(petsMoveThreads != null && !petsMoveThreads.isShutdown())
                petsMoveThreads.shutdown();
        }
        //Подключение к бд
        try(Connection connection = DriverManager.getConnection(connectionUrlSQL, userNameSQL, passwordSQL);
            Statement statement = connection.createStatement();){
            //Проверка таблицы на существование
            if(!checkTableError(connection))
                return;
            resultSet = statement.executeQuery("SELECT * FROM pets_table WHERE type='dog'");
            ArrayList<Pets> tempList = generationPets(resultSet);
            int correctTime = -1;
            if(tempList.isEmpty())
                return;
            flPreloadSQLPets = true;
            Collections.sort(tempList);
            for(Pets pets: tempList){
                if(correctTime == -1)
                    correctTime = pets.getTimeBirth();
                pets.setTimeBirth(pets.getTimeBirth() - correctTime + 1);
                petsList.add(pets);
                petsIdsSet.add(pets.getId());
                petsTimeBirthMap.put(pets.getId(), pets.getTimeBirth());
            }
        }finally {
            if(resultSet != null)
                resultSet.close();
        }
    }
    private static ArrayList<Pets> generationPets(ResultSet resultSet) throws SQLException {
        ArrayList<Pets> tempList = new ArrayList<>();
        while (resultSet.next()){
            Pets pets = null;
            String type = resultSet.getString("type");
            int timeBirth = resultSet.getInt("timebirth");
            int id = resultSet.getInt("id");
            int speedTraectoryX = resultSet.getInt("speedtraectoryx");
            int speedTraectoryY = resultSet.getInt("speedtraectoryy");
            int X = resultSet.getInt("x");
            int Y = resultSet.getInt("y");
            if(type.equals("cat"))
                pets = new Cats(
                        type,
                        timeBirth,
                        id,
                        speedTraectoryX,
                        speedTraectoryY,
                        X,
                        Y
                );
            else
                pets = new Dogs(
                        type,
                        timeBirth,
                        id,
                        speedTraectoryX,
                        speedTraectoryY,
                        X,
                        Y
                );
            tempList.add(pets);
        }
        return tempList;
    }
}
