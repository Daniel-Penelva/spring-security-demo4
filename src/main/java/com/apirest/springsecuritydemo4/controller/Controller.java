package com.apirest.springsecuritydemo4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.springsecuritydemo4.entity.OurUser;
import com.apirest.springsecuritydemo4.entity.Product;
import com.apirest.springsecuritydemo4.repository.OurUserRepository;
import com.apirest.springsecuritydemo4.repository.ProductRepository;

@RestController
@RequestMapping()
public class Controller {

    @Autowired
    private OurUserRepository ourUserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // http://localhost:3030/
    @GetMapping("/")
    public String goHome() {
        return "Isto é acessível publicamente dentro da necessidade de autenticação";
    }

    // http://localhost:3030/user/save
    @PostMapping("/user/save")
    public ResponseEntity<Object> saveUSer(@RequestBody OurUser ourUser) {
        ourUser.setPassword(passwordEncoder.encode(ourUser.getPassword()));
        OurUser result = ourUserRepository.save(ourUser);
        if (result.getId() > 0) {
            return ResponseEntity.ok("O usuário foi salvo!");
        }
        return ResponseEntity.status(404).body("Erro, usuário não salvo!");
    }

    // Somente o administrador pode salvar um produto
    // http://localhost:3030/admin/product/save
    @PostMapping("/admin/product/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
        return ResponseEntity.ok(productRepository.save(product));
    }

    // http://localhost:3030/product/all
    @GetMapping("/product/all")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // Somente o administrador pode buscar todos os usuarios
    // http://localhost:3030/users/all
    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUSers() {
        return ResponseEntity.ok(ourUserRepository.findAll());
    }

    //Tanto o administrador quanto o usuario podem buscar detalhes
    // http://localhost:3030/users/single
    @GetMapping("/users/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails() {
        return ResponseEntity.ok(ourUserRepository.findByEmail(getLoggedInUserDetails().getUsername()));
    }

    /*Método usado para obter as informações do usuário atualmente autenticado em uma aplicação Spring Security.
     * Ou seja ele obtêm as informações do usuário autenticado. */
    public UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  // obtém a autenticação atual do contexto de segurança atual usando SecurityContextHolder.getContext().getAuthentication().
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {    // Verifica se a autenticação não é nula e se o principal da autenticação é uma instância de UserDetails.
            return (UserDetails) authentication.getPrincipal();                                  // retorna o objeto UserDetails, que contém as informações do usuário autenticado, como o nome de usuário, senha e funções.
        }
        return null;
    }

}
