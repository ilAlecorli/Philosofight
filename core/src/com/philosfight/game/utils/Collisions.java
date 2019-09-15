package com.philosfight.game.utils;

import com.philosfight.game.game.Arena;
import com.philosfight.game.game.objects.Player;
import com.philosfight.game.game.objects.Wall;

import java.awt.Rectangle;

public class Collisions {

    private void Collisions() {
    }

    public static Arena ofArena(Arena arena) {
        arena.player1.bounds.setPosition(arena.player1.position.x, arena.player1.position.y);
        arena.player2.bounds.setPosition(arena.player2.position.x, arena.player2.position.y);
        for(Wall wall : arena .walls) {
            wall.bounds.setPosition(wall.position.x, wall.position.y);
            if (arena.player1.bounds.overlaps(wall.bounds)) {
                onCollisionPlayerWithWall(arena.player1, wall, arena.pixmap.getHeight() - 2, arena.pixmap.getWidth() - 2);
            }
            if(arena.player2.bounds.overlaps(wall.bounds)){
                onCollisionPlayerWithWall(arena.player2, wall, arena.pixmap.getHeight() - 2, arena.pixmap.getWidth() - 2);
            }
        }
        return arena;
    }
    private static Player onCollisionPlayerWithWall(Player player, Wall wall, int ArenaHeight, int ArenaWidth) {
        if(wall.position.y == 1) {
            player.position.y = wall.position.y + wall.bounds.height;
        }
        else if(wall.position.y == ArenaHeight) {
            player.position.y = wall.position.y - player.bounds.height;
        }
        else if(wall.position.x == 1){
            player.position.x = wall.position.x + wall.bounds.width;
        }
        else if(wall.position.x == ArenaWidth){
            player.position.x = wall.position.x - player.bounds.width;
        }
        return player;
    }
}
