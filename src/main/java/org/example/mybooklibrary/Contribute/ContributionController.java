package org.example.mybooklibrary.Contribute;


    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/contributions")
    public class ContributionController{

        @Autowired
        private ContributionService contributionService;

        @PostMapping
        public ResponseEntity<String> addContribution(@RequestBody ContributionDto dto) {
            contributionService.saveContribution(dto);
            return ResponseEntity.ok("Contribution saved successfully!");
        }

        @GetMapping
        public ResponseEntity<List<ContributionDto>> getContributions() {
            return ResponseEntity.ok(contributionService.getAllContributions());
        }
    }


