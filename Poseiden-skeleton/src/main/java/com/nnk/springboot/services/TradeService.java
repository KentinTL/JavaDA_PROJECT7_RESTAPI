package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    public Trade update(Integer id, Trade newTrade) {
        Trade existing = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No trade with id: " + id));

        existing.setAccount(newTrade.getAccount());
        existing.setType(newTrade.getType());
        existing.setBuyQuantity(newTrade.getBuyQuantity());
        existing.setSellQuantity(newTrade.getSellQuantity());
        existing.setBuyPrice(newTrade.getBuyPrice());
        existing.setSellPrice(newTrade.getSellPrice());
        existing.setBenchmark(newTrade.getBenchmark());
        existing.setTradeDate(newTrade.getTradeDate());
        existing.setSecurity(newTrade.getSecurity());
        existing.setStatus(newTrade.getStatus());
        existing.setTrader(newTrade.getTrader());
        existing.setBook(newTrade.getBook());
        existing.setCreationName(newTrade.getCreationName());
        existing.setCreationDate(newTrade.getCreationDate());
        existing.setRevisionName(newTrade.getRevisionName());
        existing.setRevisionDate(newTrade.getRevisionDate());
        existing.setDealName(newTrade.getDealName());
        existing.setDealType(newTrade.getDealType());
        existing.setSourceListId(newTrade.getSourceListId());
        existing.setSide(newTrade.getSide());

        return tradeRepository.save(existing);
    }

    public void delete(Integer id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No trade with id: " + id));
        tradeRepository.delete(trade);
    }

    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No trade with id: " + id));
    }

    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }
}
