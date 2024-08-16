package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.dto.request.user.address.AddUserAddressDTO;
import dev.mottolab.storeapi.entity.AddressSubDistrictsEntity;
import dev.mottolab.storeapi.entity.UserAddressEntity;
import dev.mottolab.storeapi.entity.UserInfoEntity;
import dev.mottolab.storeapi.repository.UserAddressRepository;
import dev.mottolab.storeapi.user.UserInfoDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAddressService {
    private final UserAddressRepository repo;

    public UserAddressService(UserAddressRepository userAddressEntity) {
        this.repo = userAddressEntity;
    }

    public List<UserAddressEntity> getAllAddress(Integer userId){
        return repo.findAllByUserId(userId);
    }

    public Optional<UserAddressEntity> getAddressByUserId(Integer userId, Integer addressId){
        return repo.findByUserIdAndId(userId, addressId);
    }

    public UserAddressEntity saveAddress(
            AddUserAddressDTO userAddress,
            AddressSubDistrictsEntity subDistrict,
            UserInfoDetail user
    ){

        // Create user
        UserInfoEntity userEntity = new UserInfoEntity();
        userEntity.setId(user.getUserId());

        // Create address
        UserAddressEntity entity = new UserAddressEntity();
        entity.setFullName(userAddress.fullName());
        entity.setLine1(userAddress.addressLine1());
        entity.setLine2(userAddress.addressLine2());
        entity.setPhoneNumber(userAddress.phoneNumber());
        entity.setAddress(subDistrict);
        entity.setUser(userEntity);

        // Save it
        return updateAddress(entity);
    }

    public UserAddressEntity updateAddress(UserAddressEntity entity){
        return repo.save(entity);
    }

    public void deleteAddress(UserAddressEntity entity){
        repo.delete(entity);
    }
}
