package com.rideshare.repository;

import com.rideshare.entity.Ride;
import com.rideshare.entity.User;
import com.rideshare.util.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByRider(User rider);

    List<Ride> findByDriver(User driver);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByRiderAndStatus(User rider, RideStatus status);

    List<Ride> findByDriverAndStatus(User driver, RideStatus status);
    
    @Query("SELECT r FROM Ride r WHERE r.status = :status AND r.driver IS NULL")
    List<Ride> findAvailableRides(RideStatus status);
    
    @Query("SELECT r FROM Ride r WHERE (r.rider = :user OR r.driver = :user) ORDER BY r.createdAt DESC")
    List<Ride> findUserRides(User user);
    
    @Query("SELECT r FROM Ride r WHERE r.scheduledTime BETWEEN :start AND :end")
    List<Ride> findRidesBetweenDates(LocalDateTime start, LocalDateTime end);
}