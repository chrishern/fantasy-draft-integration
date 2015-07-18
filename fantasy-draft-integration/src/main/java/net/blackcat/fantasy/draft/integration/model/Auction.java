package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Auction implements Serializable {

    private static final long serialVersionUID = -8773358797616427822L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Bid> bids;

    public Auction() {

        bids = new ArrayList<Bid>();
    }

    /**
     * Add a set of new bids to this auction.
     * 
     * @param newBids
     *            New bids to add to the auction.
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
}
