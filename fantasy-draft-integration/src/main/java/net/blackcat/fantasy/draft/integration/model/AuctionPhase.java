/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;

/**
 * Model class storing information about an individual phase of an auction.
 * 
 * @author Chris Hern
 * 
 */
@Entity
public class AuctionPhase implements Serializable {

    private static final long serialVersionUID = 5706041189244796847L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Bid> bids;

    @Column
    @Enumerated(EnumType.STRING)
    private AuctionPhaseStatus status;

    public AuctionPhase() {

        bids = new ArrayList<Bid>();
        status = AuctionPhaseStatus.OPEN;
    }

    /**
     * Add a set of new bids to this auction phase.
     * 
     * @param newBids
     *            New bids to add to the auction phase.
     */
    public void addBids(final List<Bid> newBids) {

        bids.addAll(newBids);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the bids
     */
    public List<Bid> getBids() {
        return bids;
    }

    /**
     * @return the status
     */
    public AuctionPhaseStatus getStatus() {
        return status;
    }
}
