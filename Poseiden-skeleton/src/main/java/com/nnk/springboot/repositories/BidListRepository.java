package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BidListRepository extends JpaRepository<BidList, Integer> {

    List<BidList> findAll();
}
