package com.rideshare.repository;

import com.rideshare.entity.Ride;
import com.rideshare.entity.Token;
import com.rideshare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findByUser(User user);

    List<Token> findByRide(Ride ride);

    List<Token> findByUserAndRedeemed(User user, Boolean redeemed);
    
    @Query("SELECT SUM(t.tokensEarned) FROM Token t WHERE t.user = :user AND t.redeemed = false")
    BigDecimal getAvailableTokens(User user);
    
    @Query("SELECT SUM(t.tokensEarned) FROM Token t WHERE t.user = :user")
    BigDecimal getTotalTokensEarned(User user);
}