package com.edu.eci;

import com.edu.eci.repository.User;
import com.edu.eci.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Limpiar la base de datos antes de cada prueba
    }

    @Test
    void testFindByEmail() {
        // Crear y guardar un usuario de prueba
        User user = new User("1", "test@example.com", "password123");
        userRepository.save(user);

        // Buscar por email
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Verificar que el usuario fue encontrado correctamente
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Buscar un email que no existe
        Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");

        // Verificar que el usuario no fue encontrado
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveUser() {
        // Crear un nuevo usuario
        User user = new User("2", "newuser@example.com", "securePass");
        User savedUser = userRepository.save(user);

        // Verificar que se ha guardado correctamente
        assertNotNull(savedUser.getId());
        assertEquals("newuser@example.com", savedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        // Crear y guardar un usuario
        User user = new User("3", "delete@example.com", "deletePass");
        userRepository.save(user);

        // Eliminar el usuario
        userRepository.deleteById(user.getId());

        // Verificar que el usuario ha sido eliminado
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testFindById() {
        // Crear y guardar un usuario
        User user = new User("4", "find@example.com", "findPass");
        userRepository.save(user);

        // Buscar el usuario por ID
        Optional<User> foundUser = userRepository.findById(user.getId());

        // Verificar que el usuario fue encontrado
        assertTrue(foundUser.isPresent());
        assertEquals("find@example.com", foundUser.get().getEmail());
    }

    @Test
    void testUpdateUser() {
        // Crear y guardar un usuario
        User user = new User("5", "update@example.com", "oldPass");
        userRepository.save(user);

        // Actualizar el usuario
        user.setPassword("newPass");
        userRepository.save(user);

        // Verificar que el usuario fue actualizado
        Optional<User> updatedUser = userRepository.findById(user.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("newPass", updatedUser.get().getPassword());
    }
}