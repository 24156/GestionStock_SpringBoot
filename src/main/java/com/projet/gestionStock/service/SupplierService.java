package com.projet.gestionStock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projet.gestionStock.dto.request.SupplierRequest;
import com.projet.gestionStock.dto.response.SupplierResponse;
import com.projet.gestionStock.exception.ResourceNotFoundException;
import com.projet.gestionStock.model.Supplier;
import com.projet.gestionStock.model.User;
import com.projet.gestionStock.repository.SupplierRepository;
import com.projet.gestionStock.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers(String username) {
        return supplierRepository.findByUserUsername(username).stream()
                .map(sup -> new SupplierResponse(
                        sup.getId(),
                        sup.getName(),
                        sup.getPhone(),
                        sup.getEmail(),
                        sup.getAddress(),
                        sup.getUser().getId(),
                        sup.getCreatedAt()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id, String username) {
        Supplier sup = supplierRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found or access denied for id: " + id));

        return new SupplierResponse(
                sup.getId(),
                sup.getName(),
                sup.getPhone(),
                sup.getEmail(),
                sup.getAddress(),
                sup.getUser().getId(),
                sup.getCreatedAt()
        );
    }

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Supplier sup = new Supplier();
        sup.setName(request.getName());
        sup.setPhone(request.getPhone());
        sup.setEmail(request.getEmail());
        sup.setAddress(request.getAddress());
        sup.setUser(user);

        Supplier savedSup = supplierRepository.save(sup);

        return new SupplierResponse(
                savedSup.getId(),
                savedSup.getName(),
                savedSup.getPhone(),
                savedSup.getEmail(),
                savedSup.getAddress(),
                savedSup.getUser().getId(),
                savedSup.getCreatedAt()
        );
    }

    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request, String username) {
        Supplier sup = supplierRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found or access denied for id: " + id));

        sup.setName(request.getName());
        sup.setPhone(request.getPhone());
        sup.setEmail(request.getEmail());
        sup.setAddress(request.getAddress());

        Supplier updatedSup = supplierRepository.save(sup);

        return new SupplierResponse(
                updatedSup.getId(),
                updatedSup.getName(),
                updatedSup.getPhone(),
                updatedSup.getEmail(),
                updatedSup.getAddress(),
                updatedSup.getUser().getId(),
                updatedSup.getCreatedAt()
        );
    }

    @Transactional
    public void deleteSupplier(Long id, String username) {
        Supplier sup = supplierRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found or access denied for id: " + id));

        supplierRepository.delete(sup);
    }
}