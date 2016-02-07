/**
 * 
 */
package net.blackcat.fantasy.draft.integration.repository;

import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.League;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository used for storing {@link League} objects.
 * 
 * @author Chris Hern
 * 
 */
public interface LeagueRepository extends CrudRepository<League, Integer> {

    // @Query("SELECT phase FROM League league INNER JOIN league.auction.phases phase WHERE league.auction.status = 'ACTIVE' AND phase.status = 'ACTIVE'")
    // AuctionPhase getOpenAuctionPhase();
    //

    // @formatter:off
    
    @Query(nativeQuery = true,
           value = "SELECT phase.id FROM League league " +
                   "JOIN Auction AS auction ON league.auction_id = auction.id " +
                   "JOIN Auction_AuctionPhase auctionPhaseBridge ON auctionPhaseBridge.Auction_id = auction.id " +
                   "JOIN AuctionPhase phase ON phase.id = auctionPhaseBridge.phases_id " +
                   "WHERE league.id = :id " +
                   "AND auction.status = 'OPEN' " +
                   "AND phase.status = 'OPEN'") 
    Object[] getOpenAuctionPhase(@Param("id") int id);
    
    @Query("SELECT phase FROM AuctionPhase phase WHERE phase.id = :id")
    AuctionPhase getAuctionPhase(@Param("id") int id);
    
    
    // @formatter:on
}
