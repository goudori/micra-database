package sql_data;

import java.sql.DriverManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database implements CommandExecutor {

  private Connection connection;

  public Database() {
    try {
      String url = "jdbc:mysql://localhost:3306/spigot_server";
      String user = "root";
      String password = "udedamepon175!";
      connection = DriverManager.getConnection(url, user, password);
      System.out.println("データベースへの接続成功");
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println("データベース接続に失敗しました: " + e.getMessage());
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public void close() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("このコマンドはプレイヤーからのみ実行できます。");
      return true;
    }

    Player player = (Player) sender;
    if (label.equalsIgnoreCase("enemylist")) {
      showPlayerScores(player);
      return true;
    }

    return false;
  }

  private void showPlayerScores(Player player) {
    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM player_scores ORDER BY score DESC")) {
      ResultSet rs = stmt.executeQuery();
      player.sendMessage(ChatColor.GOLD + "--- プレイヤースコアリスト ---");
      while (rs.next()) {
        String playerName = rs.getString("player_name");
        int score = rs.getInt("score");
        String date = rs.getString("date");
        player.sendMessage(ChatColor.YELLOW + playerName + ": " + score + "点 (" + date + ")");
      }
    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "データベースエラーが発生しました。");
      e.printStackTrace();
    }
  }
}
