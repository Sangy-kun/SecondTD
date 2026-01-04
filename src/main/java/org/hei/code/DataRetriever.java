package org.hei.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
}
