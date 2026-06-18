package com.employee.service;

import com.employee.client.AddressClient;
import com.employee.exception.BadRequestException;
import com.employee.exception.CustomException;
import com.employee.exception.ResourceNotFoundException;
import com.employee.model.dto.AddressDto;
import com.employee.model.dto.EmployeeDto;
import com.employee.model.entity.Employee;
import com.employee.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressClient addressClient;

    @Autowired
    Environment environment;

//    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
//        this.employeeRepository = employeeRepository;
//        this.modelMapper = modelMapper;
//    }   

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto)  {
        if(employeeDto.getId() != null){
            throw new ResourceNotFoundException("Employee Already Exists");
        }

        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee saveEntity = employeeRepository.save(employee);

        return modelMapper.map(saveEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {

        if(id == null || employeeDto.getId() == null){
            throw new ResourceNotFoundException("Service.EMPLOYEE_VALID_ID");
        }

        if(!Objects.equals(id, employeeDto.getId())){
            throw new BadRequestException("Service.EMPLOYEE_ID_MISMATCH");
        }

        employeeRepository.findById(id).orElseThrow(() -> new CustomException("Service.EMPLOYEE_NOT_FOUND " + id));
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee updatedEmployee = employeeRepository.save(employee);

        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service.EMPLOYEE_NOT_FOUND " + id));
        employeeRepository.delete(employee);

    }

    @Override
    public EmployeeDto getSingleEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("service.EMPLOYEE_NOT_FOUND"));
        List<AddressDto> addresses = new ArrayList<>();
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        try{
            addresses = addressClient.getAddressByEmpId(employee.getId());
            dto.setAddress(addresses);
        } catch (Exception e){
            log.error("Address not found with employee id: {}", employee.getId());
        }

        return dto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new ResourceNotFoundException("Service.NO_EMPLOYEES");
        }
        List<EmployeeDto> employeeDtoList = employees.stream().map(emp -> modelMapper.map(emp, EmployeeDto.class)).toList();
        List<EmployeeDto> response = new ArrayList<>();
        for(EmployeeDto employee : employeeDtoList){
            List<AddressDto> addresses = new ArrayList<>();
            try{
                addresses = addressClient.getAddressByEmpId(employee.getId());
                employee.setAddress(addresses);
            } catch (Exception e) {
                log.error("Address not found with employee id: {}", employee.getId());
            }
            response.add(employee);
        }

        return response;
    }

    @Override
    public EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode, String companyName) {
        Employee employee =  employeeRepository.findByEmpCodeAndCompanyName(empCode, companyName).orElseThrow(() -> new ResourceNotFoundException("Employee not found with " + empCode + " " + companyName));

        return modelMapper.map(employee, EmployeeDto.class);
    }
}
