package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {
    List<CurvePoint> findAll();
}
