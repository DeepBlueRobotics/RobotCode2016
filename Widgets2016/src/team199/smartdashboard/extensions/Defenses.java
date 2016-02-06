/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team199.smartdashboard.extensions;
import java.lang.Enum;

/**
 *
 * @author adamstafford
 */
public enum Defenses {
    PORTCULLIS(0),CHEVAL_DE_FRISE(1),MOAT(2),RAMPARTS(3),DRAWBRIDGE(4),SALLY_PORT(5),ROCK_WALL(6),ROUGH_TERRAIN(7);
    public final int value;
    Defenses(int value) {
        this.value = value;
    }
}
