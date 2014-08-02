/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.bid.Bid;
import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the {@link DraftRoundDataService}.
 * 
 * @author Chris
 *
 */
@Component(value = "draftRoundDataServiceJpa")
public class DraftRoundDataServiceJpa implements DraftRoundDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addBids(final int leagueId, final List<Bid> bids) {
		// Get the LeagueEntity
		final LeagueEntity league = entityManager.find(LeagueEntity.class, leagueId);
		
		// Now get the open DraftRoundEntity
		TypedQuery<DraftRoundEntity> query = entityManager.createQuery("SELECT d FROM DraftRoundEntity d WHERE d.key.league = :league AND d.status = :status", DraftRoundEntity.class);
		
		query.setParameter("league", league);
		query.setParameter("status", DraftRoundStatus.OPEN);
		
		final DraftRoundEntity draftRound = query.getSingleResult();
		
		// Convert model bids into entity bids (we can get the team here as all bids will relate to the same ID).
		final TeamEntity team = entityManager.find(TeamEntity.class, bids.get(0).getTeamId());
		final List<BidEntity> entityBids = new ArrayList<BidEntity>();
		for (final Bid modelBid : bids) {
			entityBids.add(convertModelBidToEntity(modelBid, team));
		}
		
		draftRound.addBids(entityBids);
		
		entityManager.merge(draftRound);
	}
	
	/**
	 * Convert a model bid into an equivalent entity object.
	 * 
	 * @param modelBid The model object version of the bid.
	 * @param team The team relating to the bid.
	 * @return Converted entity bid.
	 */
	private BidEntity convertModelBidToEntity(final Bid modelBid, final TeamEntity team) {
		final BidEntity entityBid = new BidEntity();
		
		final PlayerEntity player = entityManager.find(PlayerEntity.class, modelBid.getPlayerId());
		
		entityBid.setAmount(modelBid.getAmount());
		entityBid.setPlayer(player);
		entityBid.setTeam(team);
		
		return entityBid;
	}
}
