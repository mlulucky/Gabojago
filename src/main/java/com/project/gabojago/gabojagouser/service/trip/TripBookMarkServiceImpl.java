package com.project.gabojago.gabojagouser.service.trip;

import com.project.gabojago.gabojagouser.dto.trip.TripBookmarkDto;
import com.project.gabojago.gabojagouser.mapper.trip.TripBookmarkMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TripBookMarkServiceImpl implements TripBookMarkService{
    TripBookmarkMapper bookmarkMapper;
    @Override
    public List<TripBookmarkDto> list(String uId) {
        List<TripBookmarkDto> list=bookmarkMapper.findByUId(uId);
        return list;
    }

    @Override
    public int register(TripBookmarkDto tripBookmarkDto) {
        int register=bookmarkMapper.insertOne(tripBookmarkDto);
        return register;
    }

    @Override
    public int remove(int tbId) {
        int remove=bookmarkMapper.deleteOne(tbId);
        return remove;
    }
}