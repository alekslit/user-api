package ru.telros.uapi.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.telros.uapi.exception.AlreadyExistException;
import ru.telros.uapi.exception.AuthenticationException;
import ru.telros.uapi.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static ru.telros.uapi.exception.AlreadyExistException.ALREADY_EXIST_EXCEPTION_REASON;
import static ru.telros.uapi.exception.AlreadyExistException.DUPLICATE_USER_EMAIL_MESSAGE;
import static ru.telros.uapi.exception.AuthenticationException.AUTHENTICATION_EXCEPTION_REASON;
import static ru.telros.uapi.exception.AuthenticationException.USER_EMAIL_NOT_FOUND_MESSAGE;
import static ru.telros.uapi.exception.NotFoundException.NOT_FOUND_EXCEPTION_REASON;
import static ru.telros.uapi.exception.NotFoundException.USER_NOT_FOUND_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /*--------------------Основные методы--------------------*/
    @Override
    public User saveUser(NewUserRequest userRequest) {
        log.debug("{} = {}.", "Попытка сохранить новый объект User с email", userRequest.getEmail());
        User user = UserMapper.newUserRequestToUser(userRequest);

        return saveUserObject(user);
    }

    @Override
    public User getUserById(Long userId) {
        log.debug("{} = {}.", "Попытка получить объект User по id", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(),
                    USER_NOT_FOUND_MESSAGE, userId);
            return new NotFoundException(NOT_FOUND_EXCEPTION_REASON,
                    USER_NOT_FOUND_MESSAGE + userId);
        });
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        log.debug("{} = {}.", "Попытка обновить объект User c id", userId);
        // нашли пользователя:
        User userFromDb = getUserById(userId);
        // обновляем объект:
        updateUserObject(request, userFromDb);

        // сохраняем в БД:
        return saveUserObject(userFromDb);
    }

    @Override
    public List<User> getAllUsers(String firstName,
                                  String lastName,
                                  String patronymic,
                                  Integer yearOfBirth,
                                  Integer from,
                                  Integer size) {
        log.debug("{}.", "Попытка получить все объекты User");
        // пагинация:
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        // фильтры поиска:
        List<BooleanExpression> conditions = getUserFilter(firstName,
                lastName, patronymic, yearOfBirth);
        if (!conditions.isEmpty()) {
            BooleanExpression finalCondition = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();
            return userRepository.findAll(finalCondition, pageRequest).getContent();
        }

        return userRepository.findAll(pageRequest).getContent();
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("{} = {}.", "Попытка удалить объект User по id", userId);
        // проверим, существует ли такой пользователь:
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    /*--------------------Вспомогательные методы--------------------*/
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.debug("{}: {}{}.", AuthenticationException.class.getSimpleName(),
                    USER_EMAIL_NOT_FOUND_MESSAGE, email);
            return new AuthenticationException(AUTHENTICATION_EXCEPTION_REASON,
                    USER_EMAIL_NOT_FOUND_MESSAGE + email);
        });
    }

    private User saveUserObject(User user) {
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.debug("{}: {}{}.", AlreadyExistException.class.getSimpleName(),
                    DUPLICATE_USER_EMAIL_MESSAGE, user.getEmail());
            throw new AlreadyExistException(ALREADY_EXIST_EXCEPTION_REASON,
                    DUPLICATE_USER_EMAIL_MESSAGE + user.getEmail());
        }

        return user;
    }

    private List<BooleanExpression> getUserFilter(String firstName,
                                                  String lastName,
                                                  String patronymic,
                                                  Integer yearOfBirth) {
        QUser user = QUser.user;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (firstName != null && !firstName.isBlank()) {
            firstName = String.format("%s%s%s", "%", firstName.toLowerCase(), "%");
            conditions.add(user.firstName.likeIgnoreCase(firstName));
        }
        if (lastName != null && !lastName.isBlank()) {
            lastName = String.format("%s%s%s", "%", lastName.toLowerCase(), "%");
            conditions.add(user.lastName.likeIgnoreCase(lastName));
        }
        if (patronymic != null && !patronymic.isBlank()) {
            patronymic = String.format("%s%s%s", "%", patronymic.toLowerCase(), "%");
            conditions.add(user.patronymic.likeIgnoreCase(patronymic));
        }
        if (yearOfBirth != null) {
            conditions.add(user.birthday.year().eq(yearOfBirth));
        }

        return conditions;
    }

    private void updateUserObject(UpdateUserRequest request, User user) {
        // firstName:
        user.setFirstName(request.getFirstName() != null ?
                request.getFirstName() : user.getFirstName());
        // lastName:
        user.setLastName(request.getLastName() != null ?
                request.getLastName() : user.getLastName());
        // patronymic:
        user.setPatronymic(request.getPatronymic() != null ?
                request.getPatronymic() : user.getPatronymic());
        // birthday:
        user.setBirthday(request.getBirthday() != null ?
                request.getBirthday() : user.getBirthday());
        // email:
        user.setEmail(request.getEmail() != null ?
                request.getEmail() : user.getEmail());
        // phone:
        user.setPhone(request.getPhone() != null ?
                request.getPhone() : user.getPhone());
    }
}