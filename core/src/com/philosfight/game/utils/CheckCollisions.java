package com.philosfight.game.utils;

import com.badlogic.gdx.math.Rectangle;
import com.philosfight.game.game.Arena;
import com.philosfight.game.game.objects.Player;
import com.philosfight.game.game.objects.Wall;


public class CheckCollisions {
    //Rettangoli per il riconoscimento delle collisioni
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private Rectangle r3 = new Rectangle();
    private Arena arena;

    /**
     * Funzione per controllare le collisioni fra gli oggetti.
     */
    public void CheckCollisions(Arena arena) {
        this.arena = arena;
        r1.set(arena.player1.position.x, arena.player1.position.y, arena.player1.bounds.width, arena.player1.bounds.height);
        r2.set(arena.player2.position.x, arena.player2.position.y, arena.player2.bounds.width, arena.player2.bounds.height);
        for(Wall wall : arena.walls) {
            r3.set(wall.position.x, wall.position.y, wall.bounds.width, wall.bounds.height);
            if (r1.overlaps(r2)) {
                onCollisionPlayerWithWall(arena.player1, wall);
            }
            if(r2.overlaps(r3)){
                onCollisionPlayerWithWall(arena.player2, wall);
            }
        }
    }

    private void onCollisionPlayerWithWall(Player player, Wall wall) {
        if(wall.position.y == 1) {
            player.position.y = wall.position.y + wall.bounds.height;
        }
        else if(wall.position.y == (arena.pixmap.getHeight() - 2)) {
            player.position.y = wall.position.y - player.bounds.height;
        }
        else if(wall.position.x == 1){
            player.position.x = wall.position.x + wall.bounds.width;
        }
        else if(wall.position.x == (arena.pixmap.getWidth() - 2)){
            player.position.x = wall.position.x - player.bounds.width;
        }
        return;
    }

}
