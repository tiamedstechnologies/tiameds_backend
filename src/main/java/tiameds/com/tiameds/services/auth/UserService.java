package tiameds.com.tiameds.services.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tiameds.com.tiameds.entity.Role;
import tiameds.com.tiameds.entity.User;
import tiameds.com.tiameds.repository.RoleRepository;
import tiameds.com.tiameds.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }

    @Transactional
    public void saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Find or create the USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    try {
                        return roleRepository.save(newRole);
                    } catch (Exception e) {
                        log.error("Error saving role: {}", newRole.getName(), e);
                        throw new RuntimeException("Role could not be saved", e);
                    }
                });

        // Assign the USER role to the new user
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        log.info("Assigning roles to user: {} with roles: {}", user.getUsername(), roles);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error saving user: {}", user.getUsername(), e);
            throw new RuntimeException("User could not be saved", e);
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //admin service

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }


    public User assignRole(Long userId, Integer roleId) {
        // Fetch user by id and handle the case where the user is not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Fetch role by id and handle the case where the role is not found
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        // Add role to the user's set of roles only if it's not already present
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);  // No need to fetch and re-set the roles manually
        } else {
            throw new RuntimeException("User already has this role assigned");
        }
        // Save the updated user with the newly added role
        return userRepository.save(user);
    }

    public User removeRole(Long userId, Integer roleId) {
        // Fetch user by id and handle the case where the user is not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Fetch role by id and handle the case where the role is not found
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        // Remove role from the user's set of roles only if it's present
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);  // No need to fetch and re-set the roles manually
        } else {
            throw new RuntimeException("User does not have this role assigned");
        }
        // Save the updated user with the newly removed role
        return userRepository.save(user);
    }


    public User deleteUser(Long userId) {
        // Fetch user by id and handle the case where the user is not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Delete the user
        userRepository.delete(user);
        return user;
    }

    //get user by id
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }



}
