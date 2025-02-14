package akia.net.playerNexus.storage;

import java.sql.*;
import java.util.*;

public class MysqlPersistentStorage implements PersistentStorage {
    private final Connection connection;
    private final List<String> modelKeys;
    private final String tableName = "player_data";

    public MysqlPersistentStorage(String host, int port, String database, String username, String password, List<String> modelKeys) throws Exception {
        this.modelKeys = modelKeys;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, username, password);
            if (!connection.isValid(5)) {
                throw new Exception("Connexion MySQL invalide.");
            }
            // Création (ou mise à jour) dynamique de la table en fonction du schéma
            createOrUpdateTable();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'initialisation MySQL : " + e.getMessage());
        }
    }

    /**
     * Crée la table player_data avec une colonne par clé définie dans le modèle.
     * Si la table existe déjà, on vérifie que toutes les colonnes du modèle y sont présentes,
     * sinon on les ajoute via ALTER TABLE.
     */
    private void createOrUpdateTable() throws SQLException {
        // Construction de la requête de création
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        createTableSQL.append("uuid VARCHAR(36) NOT NULL, ");
        for (String key : modelKeys) {
            createTableSQL.append("`").append(key).append("` VARCHAR(100) NOT NULL DEFAULT '0', ");
        }
        createTableSQL.append("PRIMARY KEY (uuid))");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL.toString());
        }

        // Vérifier les colonnes existantes dans la table
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, tableName, null);
        Set<String> existingColumns = new HashSet<>();
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            existingColumns.add(columnName.toLowerCase());
        }
        rs.close();

        // Pour chaque clé du modèle, si la colonne n'existe pas, on l'ajoute
        for (String key : modelKeys) {
            if (!existingColumns.contains(key.toLowerCase())) {
                String alterSQL = "ALTER TABLE " + tableName + " ADD COLUMN `" + key + "` VARCHAR(100) NOT NULL DEFAULT '0'";
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(alterSQL);
                }
            }
        }
    }

    @Override
    public boolean playerDataExists(UUID uuid) {
        String query = "SELECT 1 FROM " + tableName + " WHERE uuid = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, String> loadPlayerData(UUID uuid) {
        Map<String, String> data = new HashMap<>();
        String query = "SELECT * FROM " + tableName + " WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    for (String key : modelKeys) {
                        data.put(key, rs.getString(key));
                    }
                } else {
                    // Si aucune ligne n'existe, on initialise avec des valeurs par défaut
                    for (String key : modelKeys) {
                        data.put(key, "0");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void savePlayerData(UUID uuid, Map<String, String> data) {
        // Construction d'une requête INSERT ... ON DUPLICATE KEY UPDATE dynamique
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (uuid");
        for (String key : modelKeys) {
            sql.append(", `").append(key).append("`");
        }
        sql.append(") VALUES (?");
        for (int i = 0; i < modelKeys.size(); i++) {
            sql.append(", ?");
        }
        sql.append(") ON DUPLICATE KEY UPDATE ");
        for (String key : modelKeys) {
            sql.append("`").append(key).append("` = VALUES(`").append(key).append("`), ");
        }
        // Supprimer la virgule et l'espace final
        sql.setLength(sql.length() - 2);
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            ps.setString(1, uuid.toString());
            int index = 2;
            for (String key : modelKeys) {
                String value = data.getOrDefault(key, "0");
                ps.setString(index++, value);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getValue(UUID uuid, String key) {
        if (!modelKeys.contains(key)) return null;
        String query = "SELECT `" + key + "` FROM " + tableName + " WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(key);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    @Override
    public void setValue(UUID uuid, String key, String value) {
        if (!modelKeys.contains(key)) return;
        String query = "UPDATE " + tableName + " SET `" + key + "` = ? WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, value);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if(connection != null) {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
