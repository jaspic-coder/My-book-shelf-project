package org.example.mybooklibrary.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mybooklibrary.Contribute.ContributionStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(nullable = false)
    private String Title;
    @Column(nullable = false)
    private String Author;
    @Column(nullable = false)
    private LocalDate PublishDate;
    private String language;
    @Column(nullable = false,unique = true)
    private String ISBN;
    private String category;
    private Boolean availabilityStatus=true ;
    private Float rating;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<String> availableFormats;
    @Enumerated(EnumType.STRING)
    private ContributionStatus status;

}
