package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.dto.request.authen.RegisterDTO;
import dev.mottolab.storeapi.enitity.IdentifyEntity;
import dev.mottolab.storeapi.enitity.UserInfoEntity;
import dev.mottolab.storeapi.exception.AccountAlreadyExist;
import dev.mottolab.storeapi.repository.IdentifyRepository;
import dev.mottolab.storeapi.repository.UserInfoRepository;
import dev.mottolab.storeapi.service.utils.UUIDService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import dev.mottolab.storeapi.user.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {
    private IdentifyRepository idenRepo;
    private UserInfoRepository userInfoRepo;
    private PasswordEncoder passwordEncoder;

    public UserInfoService(
        IdentifyRepository idenRepo,
        UserInfoRepository userRepo,
        PasswordEncoder passwordEncoder
    ){
        this.idenRepo = idenRepo;
        this.userInfoRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<IdentifyEntity> identify = this.idenRepo.findByEmail(username);
        return identify.map(UserInfoDetail::new).orElseThrow(() -> new UsernameNotFoundException("User not exist"));
    }

    public IdentifyEntity registerUser(RegisterDTO register) throws AccountAlreadyExist {
        // Check account has been already used
        Optional<IdentifyEntity> idenExist = idenRepo.findByEmail(register.account());
        if(idenExist.isPresent()){
            throw new AccountAlreadyExist(register.account());
        }

        // Create user
        IdentifyEntity identify = new IdentifyEntity();
        identify.setId(UUIDService.generateUUIDV7());
        identify.setEmail(register.account());
        identify.setPassword(passwordEncoder.encode(register.password()));
        // Create user
        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setDisplayName(register.display_name());
        userInfo.setRoles(UserRole.USER);
        // Set into identify
        identify.setUser(userInfo);
        return idenRepo.save(identify);
    }
}
