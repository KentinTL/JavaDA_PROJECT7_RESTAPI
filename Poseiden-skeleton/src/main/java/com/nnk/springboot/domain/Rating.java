package com.nnk.springboot.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "MoodyRating is mandatory")
    private String moodysRating;

    @NotBlank(message = "SandRating is mandatory")
    private String sandPRating;

    @NotBlank(message = "FitchRating is mandatory")
    private String fitchRating;

    @NotNull(message = "OrderNumber cant be null")
    private Integer orderNumber;
}
