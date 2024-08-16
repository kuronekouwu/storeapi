package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.user.address.AddUserAddressDTO;
import dev.mottolab.storeapi.dto.response.user.UserAddressDTO;
import dev.mottolab.storeapi.entity.AddressSubDistrictsEntity;
import dev.mottolab.storeapi.entity.UserAddressEntity;
import dev.mottolab.storeapi.exception.AddressNotExist;
import dev.mottolab.storeapi.service.AddressService;
import dev.mottolab.storeapi.service.UserAddressService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
public class UserAddressController {
    private final UserAddressService userAddressService;
    private final AddressService addressService;

    public UserAddressController(
            UserAddressService userAddressService,
            AddressService addressService
    ) {
        this.userAddressService = userAddressService;
        this.addressService = addressService;
    }

    @GetMapping
    public List<UserAddressDTO> getAddress(
            @AuthenticationPrincipal UserInfoDetail user
    ) {
        return this.userAddressService.getAllAddress(user.getUserId())
                .stream()
                .map(UserAddressDTO::new)
                .toList();
    }

    @PostMapping
    public UserAddressDTO addAddress(
            @Valid @RequestBody AddUserAddressDTO payload,
            @AuthenticationPrincipal UserInfoDetail user
    ) {
        // Find address
        AddressSubDistrictsEntity address = addressService.getSubDistrictById(payload.addressId())
                .orElseThrow(AddressNotExist::new);

        return new UserAddressDTO(this.userAddressService.saveAddress(payload, address, user));
    }

    @GetMapping("/{id}")
    public UserAddressDTO getInfoAddress(
            @AuthenticationPrincipal UserInfoDetail user,
            @PathVariable Integer id
    ) {
        return new UserAddressDTO(this.userAddressService.getAddressByUserId(user.getUserId(), id).orElseThrow(AddressNotExist::new));
    }

    @PutMapping("/{id}")
    public UserAddressDTO updateAddress(
            @PathVariable Integer id,
            @Valid @RequestBody AddUserAddressDTO payload,
            @AuthenticationPrincipal UserInfoDetail user
    ) {
        // Find user address
        UserAddressEntity userAddress = this.userAddressService.getAddressByUserId(user.getUserId(), id)
                .orElseThrow(AddressNotExist::new);
        // Find address
        AddressSubDistrictsEntity address = addressService.getSubDistrictById(payload.addressId())
                .orElseThrow(AddressNotExist::new);

        userAddress.setFullName(payload.fullName());
        userAddress.setLine1(payload.addressLine1());
        userAddress.setLine2(payload.addressLine2());
        userAddress.setPhoneNumber(payload.phoneNumber());
        userAddress.setAddress(address);
        return new UserAddressDTO(this.userAddressService.updateAddress(userAddress));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAddress(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserInfoDetail user
    ) {
        // Find user address
        UserAddressEntity userAddress = this.userAddressService.getAddressByUserId(user.getUserId(), id)
                .orElseThrow(AddressNotExist::new);

        this.userAddressService.deleteAddress(userAddress);
    }
}
