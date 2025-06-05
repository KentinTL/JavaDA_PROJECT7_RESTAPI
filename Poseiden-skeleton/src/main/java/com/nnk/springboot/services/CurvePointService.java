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

    public CurvePoint save(CurvePoint curvePoint) {

        CurvePoint newCurve = new CurvePoint();

        newCurve.setCurveId(curvePoint.getCurveId());
        newCurve.setTerm(curvePoint.getTerm());
        newCurve.setValue(curvePoint.getValue());
        newCurve.setCreationDate(curvePoint.getCreationDate());
        newCurve.setAsOfDate(curvePoint.getAsOfDate());

        return curvePointRepository.save(newCurve);
    }

    public CurvePoint update(Integer id, CurvePoint newCurve) {
        CurvePoint existing = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No curve to update with id : " + id));

        existing.setCurveId(newCurve.getCurveId());
        existing.setTerm(newCurve.getTerm());
        existing.setValue(newCurve.getValue());
        existing.setAsOfDate(newCurve.getAsOfDate());
        existing.setCreationDate(newCurve.getCreationDate());

        return curvePointRepository.save(existing);
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
