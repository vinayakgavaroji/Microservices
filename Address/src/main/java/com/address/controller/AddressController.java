package com.address.controller;


import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<List<AddressDto>> saveAddress(@RequestBody AddressRequest addressRequest) {
        List<AddressDto> addressDtoList = addressService.saveAddress(addressRequest);
        return new ResponseEntity<>(addressDtoList, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public  ResponseEntity<List<AddressDto>> updateAddress(@RequestBody AddressRequest addressRequest){
        List<AddressDto> addressDtoList = addressService.updateAddress(addressRequest);
        return new ResponseEntity<>(addressDtoList, HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable  Long Id){
        AddressDto addressDto = addressService.getAddressById(Id);
        return new ResponseEntity<>(addressDto, HttpStatus.OK);
    }

    @GetMapping("/allAddress")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addressDtoList = addressService.getAllAddresses();
        return new ResponseEntity<>(addressDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{Id}")
    public  ResponseEntity<String> deleteAddress(@PathVariable Long Id){
        addressService.deleteAddress(Id);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/empId/{empId}")
    public ResponseEntity<List<AddressDto>> getAddressByEmpId(@PathVariable Long empId) {
        List<AddressDto> response = addressService.getAddressByEmpId(empId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
