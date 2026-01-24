package com.example.JavaSprint5_1BlackJack.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("players") // R2DBC 的表映射注解
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @Column("play_id")
    private Long playId;

    @Column("player_name") // 明确指定数据库字段名
    private String playerName;

    @Column("total_win")
    private Integer totalWin;

    @Column("total_games")
    private Integer totalGames;

}
