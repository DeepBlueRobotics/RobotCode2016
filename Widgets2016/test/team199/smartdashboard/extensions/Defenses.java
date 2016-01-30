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
    MOAT(0),CHEVAL_DE_FRISE(1),ROCKWALL(2);
    public final int value;
    Defenses(int value) {
        this.value = value;
    }
}
