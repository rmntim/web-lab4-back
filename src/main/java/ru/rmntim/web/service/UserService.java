package ru.rmntim.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.dao.PointDAO;
import ru.rmntim.web.dao.UserDAO;
import ru.rmntim.web.dto.PointDTO;
import ru.rmntim.web.entity.PointEntity;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.exceptions.PointNotFoundException;
import ru.rmntim.web.exceptions.UserNotFoundException;
import ru.rmntim.web.utils.AreaChecker;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Slf4j
public class UserService {
    @EJB
    private UserDAO userDAO;

    @EJB
    private PointDAO pointDAO;

    public List<PointDTO> getUserPoints(Long userId) throws UserNotFoundException {
        List<PointEntity> points = pointDAO.getPointsByUserId(userId);
        return points.stream()
                .map(p -> new PointDTO(p.getX(), p.getY(), p.getR(), p.isResult()))
                .collect(Collectors.toList());
    }

    public PointDTO addUserPoint(Long userId, PointDTO pointDTO) throws UserNotFoundException {
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isInsideArea = AreaChecker.isInArea(pointDTO.getX(), pointDTO.getY(), pointDTO.getR());
        PointEntity pointEntity = PointEntity.builder()
                .x(pointDTO.getX())
                .y(pointDTO.getY())
                .r(pointDTO.getR())
                .result(isInsideArea)
                .user(user)
                .build();

        pointDAO.addPointByUserId(userId, pointEntity);
        return PointDTO.builder()
                .x(pointEntity.getX())
                .y(pointEntity.getY())
                .r(pointEntity.getR())
                .result(pointEntity.isResult())
                .build();
    }

    public void deleteUserPoints(Long userId) throws UserNotFoundException {
        pointDAO.removeAllPointsByUserId(userId);
    }

    public void deleteSinglePoint(Long userId, PointDTO pointDTO) throws UserNotFoundException, PointNotFoundException {
        pointDAO.removePointByUserId(userId, pointDTO);
    }

    public void updateLastActivity(Long userId) {
        userDAO.updateLastActivity(userId);
    }
}
