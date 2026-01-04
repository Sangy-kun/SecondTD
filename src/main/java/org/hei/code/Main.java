package org.hei.code;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnection dbConnection = new DBConnection();

        DataRetriever retriever = new DataRetriever(dbConnection);

        try {
            Team team = retriever.findTeamById(1);
            if(team == null) {
                System.out.println("Team not found");
            } else {
                System.out.println("Equipe trouvé : " + team.getName());
                System.out.println("Continent : " + team.getContinent());
                System.out.println("Nombre de joueurs : " + team.getPlayerCount());

                System.out.println("\nListe des joueurs : ");
                for (Player player : team.getPlayers()) {
                    System.out.println("-" + player.getName() +
                                        "(age: " + player.getAge() + ")" +
                                        ", position: " + player.getPosition() + ")");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            Connection conn = dbConnection.getDBConnection();
            System.out.println("Connected to database");

            var statement = conn.createStatement();
            var rs = statement.executeQuery("SELECT COUNT(*) AS total FROM team");
            if (rs.next()){
                System.out.println("Y a  " + rs.getInt("total") + " equipe dans la base");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}