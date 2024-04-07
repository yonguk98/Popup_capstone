package com.capstone.popup.store;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public void registerStore(StoreCreateRequestDto dto) {
        Store store = Store.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .build();
        // 중복체크

        storeRepository.save(store);
    }

    public Store getStoreById(Long id) {
        Optional<Store> storeOp = storeRepository.findById(id);

        if (storeOp.isEmpty()) {
            throw new EntityNotFoundException("스토어를 찾을 수 없습니다.");
        }

        return storeOp.get();
    }

    public List<Store> getAllStore() {
        // TODO: paging 추가 필요
        return storeRepository.findAll();
    }


}
