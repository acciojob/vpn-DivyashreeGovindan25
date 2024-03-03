package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin(username,password);
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        ServiceProvider serviceProvider = new ServiceProvider();
        Admin admin = adminRepository1.findById(adminId).orElse(null);
        serviceProvider.setAdmin(admin);
        serviceProvider.setName(providerName);
        serviceProviderRepository1.save(serviceProvider);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        String countryNamUpper = countryName.toUpperCase();
        boolean isPresent = false;
        for(CountryName countryName1 : CountryName.values()){
            if(countryName1.toString().equals(countryNamUpper)){
                isPresent = true;
                break;
            }
        }
        if(!isPresent) throw new Exception("Country name not found");
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        Country country = new Country();
        country.setCountryName(CountryName.valueOf(countryNamUpper));
        country.setCode(CountryName.valueOf(countryNamUpper).toCode());
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);
        serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
