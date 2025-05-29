package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService {
    @Autowired
    CurvePointRepository curvePointRepository;

    public CurvePoint save(BidList bid) {

        CurvePoint newCurve = new CurvePoint();
//        newCurve.setAccount(bid.getAccount());
//        newCurve.setType(bid.getType());
//        newCurve.setBidQuantity(bid.getBidQuantity());

        return curvePointRepository.save(newCurve);
    }

    public CurvePoint update(Integer id, BidList bidList) {

        var updatedCurve = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Bid to update with id : " + id));

//        updatedCurve.setAccount(bidList.getAccount());
//        updatedCurve.setType(bidList.getType());
//        updatedCurve.setBidQuantity(bidList.getBidQuantity());

        return curvePointRepository.saveAndFlush(updatedCurve);
    }


    public void delete(Integer id) {
        CurvePoint bid = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No curve to delete with this id : " + id));

        curvePointRepository.delete(bid);
    }

    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No curve founded with this id : " + id));
    }

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }
}
