package com.address.service;

import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;

import java.util.List;

public interface AddressService {

    List<AddressDto> saveAddress(AddressRequest addressRequest);

    List<AddressDto> updateAddress(AddressRequest addressRequest);

    AddressDto getAddressById(Long Id);

    List<AddressDto> getAllAddresses();

    void  deleteAddress(Long Id);

    List<AddressDto> getAddressByEmpId(Long empId);

}
