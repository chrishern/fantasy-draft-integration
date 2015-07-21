/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model.types.player;

/**
 * Enum storing the different status' a selected player in a team has.
 * 
 * @author Chris Hern
 * 
 */
public enum SelectedPlayerStatus {

    // @formatter:off
    
    STILL_SELECTED,
    PENDING_SALE_TO_POT,
    PENDING_TRANSFER_OUT,
    SOLD_TO_POT,
    TRANSFERRED_OUT;
    
    // @formatter:on
}
