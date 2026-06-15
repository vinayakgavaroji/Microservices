package com.address.service;

import com.address.client.EmployeeClient;
import com.address.exception.ResourceNotFoundException;
import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.model.dto.EmployeeDto;
import com.address.model.entity.Address;
import com.address.repository.AddressRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeClient employeeClient;

//    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper) {
//        this.addressRepository = addressRepository;
//        this.modelMapper = modelMapper;
//    }

    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {
        employeeClient.getSingleEmployeeById(addressRequest.getEmpId());
        List<Address> addressList = saveOrUpdateAddressRequest(addressRequest);
        List<Address> savedAddress = addressRepository.saveAll(addressList);
        return savedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {
        employeeClient.getSingleEmployeeById(addressRequest.getEmpId());
        List<Address> addressList = addressRepository.findAddressByEmpId(addressRequest.getEmpId());
        if (addressList.isEmpty()) {
            log.info("No address found for emp id: " + addressRequest.getEmpId());
            log.info("Creating new address for employee id: " + addressRequest.getEmpId());
        }
        List<Address> addressListUpdate = saveOrUpdateAddressRequest(addressRequest);
        List<Long> upcomingNonNullIds = addressListUpdate.stream().map(Address::getId).filter(Objects::nonNull).toList();
        List<Long> exisitingIds = addressList.stream().map(Address::getId).toList();
        List<Long> idsToDelete = exisitingIds.stream().filter(id -> !upcomingNonNullIds.contains(id)).toList();
        if(!idsToDelete.isEmpty()) {
            addressRepository.deleteAllById(idsToDelete);
        }
        List<Address> updatedAddress = addressRepository.saveAll(addressListUpdate);
        return updatedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto getAddressById(Long Id){
        Address address = addressRepository.findById(Id).orElseThrow(() -> new ResourceNotFoundException("No address found for emp id: " + Id));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addressList = addressRepository.findAll();
        if(addressList.isEmpty()) {
            throw new ResourceNotFoundException("No address found");
        }
        return addressList.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public void deleteAddress(Long Id){
        Address address = addressRepository.findById(Id).orElseThrow(() -> new ResourceNotFoundException("No address found for emp id: " + Id));
        addressRepository.delete(address);
    }

    @Override
    public List<AddressDto> getAddressByEmpId(Long empId) {
        List<Address> addressByEmpId = addressRepository.findByEmpId(empId);
//        if(addressByEmpId.isEmpty()){ jack sparrow
//            throw new ResourceNotFoundException("No address found for employee id: " + empId);
//        }
        return addressByEmpId.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    private List<Address> saveOrUpdateAddressRequest(AddressRequest addressRequest){

        List<Address> addressList = new ArrayList<>();

        addressRequest.getAddressRequestDtoList().forEach(addressRequestDto -> {
            Address address = new Address();
            address.setId(addressRequestDto.getId() !=  null ? addressRequestDto.getId() : null);
            address.setStreet(addressRequestDto.getStreet());
            address.setCity(addressRequestDto.getCity());
            address.setPinCode(addressRequestDto.getPinCode());
            address.setCountry(addressRequestDto.getCountry());
            address.setAddressType(addressRequestDto.getAddressType());
            address.setEmpId(addressRequest.getEmpId());

            addressList.add(address);
        });

        return addressList;

    }
}
