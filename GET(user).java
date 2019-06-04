import com.github.passwd.group.Group;
import com.github.passwd.group.GroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;
    private final GroupService groupService;

    public UserController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     *
     * @return list of users
     * @throws IOException if file cannot be processed
     */
    @GetMapping
    public List<User> getAll() throws IOException {
        return userService.getAll();
    }

    /**
     * Returns a list of users filtered by the queries provided
     *
     * @param name    if present, filters the user by name
     * @param uid     if present, filters the user by user id
     * @param gid     if present, filters the user by group id
     * @param comment if present, filters the user by comment
     * @param home    if present, filters the user by home
     * @param shell   if present, filters the user by shell
     */
    
    @GetMapping(path = "/query")
    public List<User> query(Optional<String> name, Optional<Long> uid, Optional<Long> gid, Optional<String> comment,
                            Optional<String> home, Optional<String> shell) throws IOException {
        return userService.query(name, uid, gid, comment, home, shell);
    }

    /**
     * @param uid user id
     * @return user with the specified id
     * @throws IOException           if file cannot be processed
     * @throws UserNotFoundException if the user cannot be found
     */
    @GetMapping(path = "/{uid}")
    
    public User get(@PathVariable long uid) throws IOException {
        Optional<User> optionalUser = userService.get(uid);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }

    @GetMapping(path = "/{uid}/groups")
    public List<Group> getUserGroups(@PathVariable long uid) throws IOException {
        User user = get(uid);
        String username = user.getName();
        return groupService.query(Optional.empty(), Optional.empty(), Collections.singletonList(username));
    }
}