package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.repository.RoleRepository;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Page<User> getAllUser(Pageable page) {
        return this.userRepository.findAll(page);
    }

    public List<User> getAllUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User get1UserByID(long id) {
        return this.userRepository.findTop1ById(id);
    }

    public void deleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleSaveUser(User user) {
        User trung = this.userRepository.save(user);
        System.out.println(trung);
        return trung;
    }

    public Role getRolebyName(String name) {
        return this.roleRepository.findByName(name);
    }

    public User getRegistertoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstname() + " " +
                registerDTO.getLastname());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserbyEmail(String email) {
        return this.userRepository.findTop1ByEmail(email);
    }

    public long countOrder() {
        return this.orderRepository.count();
    }

    public long countProduct() {
        return this.productRepository.count();
    }

    public long countUser() {
        return this.userRepository.count();
    }
}
