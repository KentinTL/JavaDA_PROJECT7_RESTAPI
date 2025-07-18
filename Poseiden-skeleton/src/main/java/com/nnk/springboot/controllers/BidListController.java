package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.UserInfos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@Validated
public class BidListController {
    @Autowired
    private UserInfos userInfos;

    @Autowired
    BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        var user = userInfos.getUserInfos();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        model.addAttribute("bidList", new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Validated BidList bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }

        bidListService.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListService.findById(id);
        if (bidList == null) {
            throw new IllegalArgumentException("BidList invalide : " + id);
        }
        model.addAttribute("bidList", bidList);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Validated BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            bidList.setBidListId(id);
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        }

        bidListService.update(id, bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            bidListService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bid successfully deleted");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bidList/list";
    }
}
