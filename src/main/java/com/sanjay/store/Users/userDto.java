package com.sanjay.store.Users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class userDto {

    //Converting a java object to JSON is Serialization
    //@JsonIgnore it doesn't include a variable in the response
    //@JsonProperty is used to rename a field
    //@JsonInclude gives us the option to include a particular field or not
    //@JsonFormat user telling the format to represent a field
    @JsonProperty("user_id")
    Long id;

    String name;
    String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String phn;

    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //private LocalDateTime createdAt;


}
