/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team199.smartdashboard.extensions;

/**
 *
 * @author adamstafford
 */
public enum Defenses {
    PORTCULLIS(1),CHEVAL_DE_FRISE(2),MOAT(3),RAMPARTS(4),DRAWBRIDGE(5),SALLY_PORT(6),ROCK_WALL(7),ROUGH_TERRAIN(8);
    public final int value;
    Defenses(int value) {
        this.value = value;
    }
}
