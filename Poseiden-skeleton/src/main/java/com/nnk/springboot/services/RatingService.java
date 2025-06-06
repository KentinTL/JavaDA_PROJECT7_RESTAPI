package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating update(Integer id, Rating newRating) {
        Rating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No rating with id: " + id));

        existing.setMoodysRating(newRating.getMoodysRating());
        existing.setSandPRating(newRating.getSandPRating());
        existing.setFitchRating(newRating.getFitchRating());
        existing.setOrderNumber(newRating.getOrderNumber());

        return ratingRepository.save(existing);
    }

    public void delete(Integer id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No rating with id: " + id));
        ratingRepository.delete(rating);
    }

    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No rating with id: " + id));
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }
}
