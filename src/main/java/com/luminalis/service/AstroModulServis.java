package com.luminalis.service;

import com.luminalis.entity.AstroModul;
import com.luminalis.repository.AstroModulRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AstroModulServis {

    private final AstroModulRepository modulRepository;

    public AstroModulServis(AstroModulRepository modulRepository) {
        this.modulRepository = modulRepository;
    }

    public List<AstroModul> tumModulleriGetir() {
        return modulRepository.findAll();
    }
}
