package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {
    @Autowired
    BidListRepository bidListRepository;

    public BidList save(BidList bid){

        BidList newBid = new BidList();
        newBid.setAccount(bid.getAccount());
        newBid.setType(bid.getType());
        newBid.setBidQuantity(bid.getBidQuantity());

        return bidListRepository.save(newBid);
    }

    public BidList update(Integer id, BidList bidList){

        var updatedBid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Bid to update with id : " + id));

        updatedBid.setAccount(bidList.getAccount());
        updatedBid.setType(bidList.getType());
        updatedBid.setBidQuantity(bidList.getBidQuantity());

        return bidListRepository.saveAndFlush(updatedBid);
    }


    public void delete(Integer id) {
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No bid to delete with this id : " + id));

        bidListRepository.delete(bid);
    }

    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No bid founded with this id : " + id));
    }

    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }
}
