package com.rideshare.mapper;

import com.rideshare.dto.user.UserProfileDto;
import com.rideshare.dto.user.UserSummaryDto;
import com.rideshare.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-10T19:39:21+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.41.0.v20250213-1140, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserSummaryDto userToUserSummaryDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserSummaryDto userSummaryDto = new UserSummaryDto();

        return userSummaryDto;
    }

    @Override
    public List<UserSummaryDto> usersToUserSummaryDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserSummaryDto> list = new ArrayList<UserSummaryDto>( users.size() );
        for ( User user : users ) {
            list.add( userToUserSummaryDto( user ) );
        }

        return list;
    }

    @Override
    public UserProfileDto userToUserProfileDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserProfileDto userProfileDto = new UserProfileDto();

        return userProfileDto;
    }

    @Override
    public List<UserProfileDto> usersToUserProfileDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserProfileDto> list = new ArrayList<UserProfileDto>( users.size() );
        for ( User user : users ) {
            list.add( userToUserProfileDto( user ) );
        }

        return list;
    }
}
