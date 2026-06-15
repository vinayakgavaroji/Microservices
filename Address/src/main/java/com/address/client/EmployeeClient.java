package com.address.client;

import com.address.model.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "EMPLOYEE")
public interface EmployeeClient {

    @GetMapping("/employees/employee/{id}")
    EmployeeDto getSingleEmployeeById(@PathVariable Long id);

}
