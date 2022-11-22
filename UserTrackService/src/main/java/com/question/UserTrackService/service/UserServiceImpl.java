package com.question.UserTrackService.service;

import com.question.UserTrackService.domain.Track;
import com.question.UserTrackService.domain.User;
import com.question.UserTrackService.exception.TrackNotFoundException;
import com.question.UserTrackService.exception.UserAlreadyExistsException;
import com.question.UserTrackService.exception.UserNotFoundException;
import com.question.UserTrackService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User addUser(User user) throws UserAlreadyExistsException {
        if(userRepository.findById(user.getUserId()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    @Override
    public User addTrackForUser(String userId, Track track) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        if(user.getTrackList() == null){
            user.setTrackList(Arrays.asList(track));
        }else{
            List<Track> tracks = user.getTrackList();
            tracks.add(track);
            user.setTrackList(tracks);
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteTrackFromUser(String userId, int trackId) throws UserNotFoundException, TrackNotFoundException {
        boolean result = false;
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Track> products = user.getTrackList();
        result = products.removeIf(obj->obj.getTrackId()==trackId);
        if(!result)
        {
            throw new TrackNotFoundException();
        }
        user.setTrackList(products);
        return userRepository.save(user);
    }

    @Override
    public List<Track> getTrackForUser(String userId) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        return userRepository.findById(userId).get().getTrackList();
    }
}