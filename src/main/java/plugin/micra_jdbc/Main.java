package plugin.micra_jdbc;

import command.EnemyDownCommand;
import command.EnemySpawnCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sql_data.Database;


public final class Main extends JavaPlugin implements Listener {

  private Player player;
  private Database database;


  @Override
  public void onEnable() {
    EnemyDownCommand enemyDownCommand = new EnemyDownCommand(this);
    EnemySpawnCommand enemySpawnCommand = new EnemySpawnCommand();
    SetMagic setMagic = new SetMagic();
    database = new Database();

    Bukkit.getPluginManager().registerEvents(enemyDownCommand, this);
    Bukkit.getPluginManager().registerEvents(this, this);

    getCommand("enemyDown").setExecutor(enemyDownCommand);
    getCommand("enemySpawn").setExecutor(enemySpawnCommand);
    getCommand("setmagic").setExecutor(setMagic);


  }

  @Override
  public void onDisable() {
//    プラグインが無効になったら、データベースの接続を閉じる
    if (database != null) {
      database.close();
    }
  }

  /**
   * データベースインスタンスを返します。
   *
   * @return Database instance
   */
  public Database getDatabase() {
    return this.database;
  }


  // プレイヤーがマイクラサーバーに参加した時のイベント
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();

    // 少し遅延させてタイトルを表示
    new BukkitRunnable() {
      @Override
      public void run() {
        player.sendTitle("JDBC", "", 10, 90, 30);
      }
    }.runTaskLater(this, 20L); // 1秒遅延
  }

}