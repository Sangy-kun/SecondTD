package org.hei.code;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataRetriever {
    private final DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private Connection getConnection() throws SQLException {
        return dbConnection.getDBConnection();
    }

    // 6) a) Team FindTeamById:

    public Team findTeamById(Integer id) throws SQLException {
        String sqlTeam = "SELECT id, name, continent FROM team WHERE id = ?";
        String sqlPlayers = "SELECT id, name, age, position, id_team FROM player WHERE id_team = ?";

        Connection conn = getConnection();
        Team team = null;

        try (PreparedStatement ps = conn.prepareStatement(sqlTeam)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setContinent(ContinentEnum.valueOf(rs.getString("continent")));
                }
            }
        }
        if (team == null) {
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlPlayers)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setName(rs.getString("name"));
                    player.setAge(rs.getInt("age"));
                    player.setPosition(PlayerPositionEnum.valueOf(rs.getString("position")));
                    player.setTeam(team);
                    team.getPlayers().add(player);

                }
            }
        }
        return team;
    }

    // 6) b) Mtd List findPlayer
    public List<Player> findPlayers(int page, int size) throws SQLException {

        int offset = (page - 1) * size;

        String sql = "SELECT p.id, p.name, p.age, p.position, p.id_team, t.name AS team_name, t.continent FROM player p LEFT JOIN team t ON p.id_team = t.id ORDER BY p.id LIMIT ? OFFSET ? ";

        List<Player> players = new ArrayList<>();


        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setName(rs.getString("name"));
                    player.setAge(rs.getInt("age"));
                    player.setPosition(PlayerPositionEnum.valueOf(rs.getString("position")));


                    Integer teamId = rs.getObject("id_team", Integer.class);
                    if (teamId != null) {
                        Team team = new Team();
                        team.setId(teamId);
                        team.setName(rs.getString("team_name"));
                        String continentStr = rs.getString("continent");
                        if (continentStr != null) {
                            team.setContinent(ContinentEnum.valueOf(continentStr));
                        }
                        player.setTeam(team);
                    }

                    players.add(player);
                }
            }
        }

        return players;
    }

    //6) c) Mtd creation d'un nouveau joueur

    public void createPlayers(List<Player> newPlayers) throws SQLException {

        if (newPlayers == null || newPlayers.size() == 0) {
            return;
        }

        Connection conn = getConnection();
        conn.setAutoCommit(false);

        String sql = "INSERT INTO player (id, name, age, position, id_team) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        PreparedStatement ps = conn.prepareStatement(sql);

        try {
            for (int i = 0; i < newPlayers.size(); i = i + 1) {
                Player p = newPlayers.get(i);

                ps.setInt(1, p.getId());
                ps.setString(2, p.getName());
                ps.setInt(3, p.getAge());
                ps.setString(4, p.getPosition().name());

                if (p.getTeam() != null) {
                    ps.setInt(5, p.getTeam().getId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }

                ps.addBatch();
            }


            int[] resultats = ps.executeBatch();

            for (int j = 0; j < resultats.length; j = j + 1) {
                if (resultats[j] == 0) {
                    throw new RuntimeException("Erreur : un joueur avec le même id existe déjà");
                }
            }

            conn.commit();
            System.out.println("Tous les joueurs ont été ajoutés !");

        } catch (Exception e) {
            conn.rollback();
            System.out.println("Erreur, tout a été annulé");
            throw new RuntimeException("Impossible d'ajouter les joueurs : " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
            ps.close();
        }
    }


}
