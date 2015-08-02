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
     * Return true if this auction phase is open.
     * 
     * @return True if this auction phase is open, false if not.
     */
    public boolean isOpen() {
        return status == AuctionPhaseStatus.OPEN;
    }

    /**
     * Return true if a team identified by the team name has submitted bids in the current transfer window.
     * 
     * @param teamName
     *            Name of the team to check whether bids have been submitted or not.
     * @return True if the team has submitted bids, false otherwise.
     */
    public boolean hasTeamSubmittedBids(final String teamName) {

        for (final Bid bid : bids) {
            if (bid.getTeam().getName().equals(teamName)) {
                return true;
            }
        }

        return false;
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
     * @param status
     *            the status to set
     */
    public void setStatus(AuctionPhaseStatus status) {
        this.status = status;
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
