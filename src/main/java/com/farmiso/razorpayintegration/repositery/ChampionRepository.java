package com.farmiso.razorpayintegration.repositery;

import com.farmiso.razorpayintegration.entity.Champion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionRepository  extends JpaRepository<Champion, Integer> {
}
