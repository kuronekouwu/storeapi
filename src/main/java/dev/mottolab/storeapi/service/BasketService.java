package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.BasketEntity;
import dev.mottolab.storeapi.repository.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasketService {
    private final BasketRepository repo;

    public BasketService(BasketRepository repo) {
        this.repo = repo;
    }

    public Optional<BasketEntity> getBasketByUserIdAndProductId(Integer userId, UUID productId) {
        return this.repo.findByUserIdAndProductId(userId, productId);
    }

    public List<BasketEntity> getAllBasketByUserId(Integer userId){
        return this.repo.findAlLByUserId(userId);
    }

    public void updateBasket(BasketEntity entity){
        this.repo.save(entity);
    }
}
