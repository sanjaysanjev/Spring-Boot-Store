package com.sanjay.store.Users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class User_Controller {


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    /*@GetMapping
    public List<userDto> getUsers()
    {
        //return userRpository.findAll().stram().map((user) -> new userDto(user.getId(),user.getName(),user.getMail())).collect(Collectors.toList());
        return userRepository.findAll().stream().map((userMapper::toDto)).collect(Collectors.toList());
    }*/
    @GetMapping //this method applies order by
   public List<userDto> getUsersSort(@RequestParam(required = false, defaultValue = "name", name = "sort") String sort) {
        if (!sort.equals("name") && !sort.equals("email")) {
            sort = "name";
        } else {
            sort = "email";
        }
        return userRepository.findAll(Sort.by(sort).descending()).stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<userDto> getById(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        //var userDto=new userDto(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    //if we pass the invalid dto this method will not be calles
    //MethodArgumentNotValidException will be thrown
    public ResponseEntity<?> registerUser(@Valid @RequestBody registerUserRequest request, UriComponentsBuilder uribuilder) {

        if(userRepository.existsByemail(request.getEmail()))
        {
            return ResponseEntity.badRequest().body("Email:Email is already Registered");
        }
        var user = userMapper.registerUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        //URI uri=URI.create("/users");
        var uri = uribuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<userDto> update(@PathVariable(name = "id") Long id,
                                          @RequestBody updateuserDto data_update) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.update(data_update, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_password")
    public ResponseEntity<Void> deleteContent(@PathVariable long id, @RequestBody ChangePasswordRequest change) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(change.getOldpassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(change.getNewpassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }


}

