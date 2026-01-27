/* Script for initializing the MySQL database schema */

CREATE TABLE IF NOT EXISTS players (
    player_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(255) NOT NULL,
    total_win INT DEFAULT 0,
    total_games INT DEFAULT 0
    );