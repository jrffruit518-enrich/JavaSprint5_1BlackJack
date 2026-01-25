package com.example.JavaSprint5_1BlackJack.entities;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("players") // R2DBC 的表映射注解
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @Column("play_id")
    private Long playerId;

    @Setter
    @NotBlank
    @Column("player_name") // 明确指定数据库字段名
    private String playerName;

    @Column("total_win")
    private Integer totalWins = 0;

    @Column("total_games")
    private Integer totalGames =0;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerId, player.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }
}
