package com.capstone.popup.store;

import jakarta.persistence.EntityExistsException;
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

        checkDuplicationStore(dto.getName());

        storeRepository.save(store);
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("스토어를 찾을 수 없습니다."));
    }

    public List<Store> getAllStore() {
        // TODO: paging 추가 필요
        return storeRepository.findAll();
    }

    public void updateStoreById(Long id, StoreCreateRequestDto dto) {
        Store store = getStoreById(id);

        store.toBuilder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .build();

        storeRepository.save(store);
    }

    public void deleteStoreById(Long id) {
        storeRepository.deleteById(id);
    }

    private void checkDuplicationStore(String name) {
        storeRepository.findByName(name).ifPresent(store -> {
                    throw new EntityExistsException("이미 존재하는 스토어 입니다.");
                }
        );
    }
}
