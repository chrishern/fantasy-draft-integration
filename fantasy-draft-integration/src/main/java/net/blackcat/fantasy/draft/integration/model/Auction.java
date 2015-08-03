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

import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionStatus;

@Entity
public class Auction implements Serializable {

    private static final long serialVersionUID = -8773358797616427822L;

    private static final int FIRST_AUCTION_PHASE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AuctionPhase> phases;

    @Column
    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    public Auction() {

        final AuctionPhase phase = new AuctionPhase(FIRST_AUCTION_PHASE);

        phases = new ArrayList<AuctionPhase>();
        phases.add(phase);

        status = AuctionStatus.OPEN;
    }

    /**
     * Create a new phase of this auction.
     */
    public void createNewPhase() {

        final AuctionPhase newPhase = new AuctionPhase(phases.size() + 1);

        phases.add(newPhase);
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(AuctionStatus status) {
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
    public List<AuctionPhase> getPhases() {
        return phases;
    }

    /**
     * @return the status
     */
    public AuctionStatus getStatus() {
        return status;
    }
}
