package org.example.mybooklibrary.Contribute;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContributionService {

    private final List<Contribution> contributions = new ArrayList<>();

    public void saveContribution(ContributionDto dto) {
        Contribution contribution = new Contribution(
                null,
                dto.getBookName(),
                dto.getAuthorName(),
                dto.getCategory(),
                dto.getLanguage(),
                dto.getContributorName(),
                dto.getContributionDate(),
                dto.getAvailableFormats(),
                ContributionStatus.PENDING // default status
        );
        contributions.add(contribution);
    }


    public List<ContributionDto> getAllContributions() {
        return contributions.stream().map(c -> {
            ContributionDto dto = new ContributionDto();
            dto.setBookName(c.getBookName());
            dto.setAuthorName(c.getAuthorName());
            dto.setCategory(c.getCategory());
            dto.setLanguage(c.getLanguage());
            dto.setContributorName(c.getContributorName());
            dto.setContributionDate(c.getContributionDate());
            dto.setAvailableFormats(c.getAvailableFormats());
            return dto;
        }).toList();
    }
}
