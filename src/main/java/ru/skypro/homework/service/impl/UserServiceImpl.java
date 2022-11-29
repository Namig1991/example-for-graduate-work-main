package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mappers.UsersMapper;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UsersMapper usersMapper;

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository,
                           UsersMapper usersMapper,
                           UserDetailsManager manager) {
        this.userRepository = userRepository;
        this.usersMapper = usersMapper;
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Получение пользователя по его идентификатору.
     * @param id - идентификатор пользователя.
     * @return - найденный пользователь.
     */
    @Override
    public ResponseEntity<UserDto> getUser(Long id) {
        LOGGER.info("Was invoked method get user by id.");
        Users user = userRepository.findById(id).orElseThrow();
        UserDto userDto = usersMapper.userToUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Получение пользователя по его логину.
     * @param username - логин пользователя.
     * @return - найденный пользователь.
     */
    @Override
    public Users findByUsername(String username) {
        LOGGER.info("Was invoked method get user by username.");
        return userRepository.findByUsername(username);
    }

    /**
     * Получение списка всех пользователей.
     * @return - список всех пользователей.
     */
    @Override
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        LOGGER.info("Was invoked method get all users.");
        List<Users> usersList = userRepository.findAll();
        ResponseWrapperUserDto responseWrapperUserDto = new ResponseWrapperUserDto();
        responseWrapperUserDto.setCount(usersList.size());
        responseWrapperUserDto.setResults(usersMapper.listUsersToListUserDto(usersList));
        return ResponseEntity.ok(responseWrapperUserDto);
    }

    /**
     * Редактирование данных пользователя.
     * @param userDto - изменения в данных пользователя.
     * @param authentication - аутентификация текущего пользователя.
     * @return - изменный пользователь.
     */
    @Override
    public ResponseEntity<UserDto> updateUser(UserDto userDto, Authentication authentication) {
        LOGGER.info("Was invoked method for update user.");
        Users user = userRepository.findByUsername(authentication.getName());
        if (userDto.getEmail() != null && !(userDto.getEmail().equals(user.getUsername()))) {
            user.setUsername(userDto.getEmail());
        }
        if (userDto.getFirstName() != null && !(userDto.getFirstName().equals(user.getFirstName()))) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null && !(userDto.getLastName().equals(user.getLastName()))) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getPhone() != null && !(userDto.getPhone().equals(user.getPhone()))) {
            user.setPhone(userDto.getPhone());
        }
        userRepository.save(user);
        UserDto returnedUserDto = usersMapper.userToUserDto(user);
        return ResponseEntity.ok(returnedUserDto);
    }

    /**
     * Изменение пароля текущего пользователя.
     * @param newPasswordDto - текущий и новый пароли пользователя.
     */
    @Override
    public ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto newPasswordDto) {
        LOGGER.info("Was invoked method for set new password of user.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = manager.loadUserByUsername(authentication.getName());
        Users user = userRepository.findByUsername(authentication.getName());
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        if (encoder.matches(newPasswordDto.getCurrentPassword(),
                encryptedPasswordWithoutEncryptionType)) {
            user.setPassword("{bcrypt}" + encoder.encode(newPasswordDto.getNewPassword()));
            userRepository.save(user);
        }
        return ResponseEntity.ok(newPasswordDto);
    }

    @Override
    public void setAdminInUsers() {
        LOGGER.info("Was invoked method for set ADMIN.");
        if (userRepository.findByRole(Role.ADMIN) == null) {
            Users admin = new Users();
            admin.setId(4L);
            admin.setFirstName("Администратор");
            admin.setLastName("Интернет-магазина");
            admin.setPhone("+7(812)777-55-33");
            admin.setUsername("admin@mail.ru");
            admin.setPassword("{bcrypt}" + encoder.encode("Password2022"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
        }
    }
}
