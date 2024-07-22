package com.codewithprojects.services.customer;

import com.codewithprojects.dto.BookACarDto;
import com.codewithprojects.dto.CarDto;
import com.codewithprojects.dto.CarDtoListDto;
import com.codewithprojects.dto.SearchCarDto;
import com.codewithprojects.entity.BookACar;
import com.codewithprojects.entity.Car;
import com.codewithprojects.entity.User;
import com.codewithprojects.enums.BookCarStatus;
import com.codewithprojects.repository.BookACarRepository;
import com.codewithprojects.repository.CarRepository;
import com.codewithprojects.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;



    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }


    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar=carRepository.findById(bookACarDto.getCarId());
        Optional<User>optionalUser=userRepository.findById(bookACarDto.getUserId());
        if (optionalCar.isPresent()&& optionalUser.isPresent()) {
            Car existingCar=optionalCar.get();
            BookACar bookACar=new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existingCar);
            bookACar.setBookCarStatus(BookCarStatus.PENDING);
            long diffInMilliSecond=bookACarDto.getToDate().getTime()- bookACarDto.getFromDate().getTime();
            long days= TimeUnit.MICROSECONDS.toDays(diffInMilliSecond);
            bookACar.setDays(days);
            bookACar.setPrice(existingCar.getPrice()*days);
            bookACarRepository.save(bookACar);
            return true;
        }
        return false;
    }

    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar=carRepository.findById(carId);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }


    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        Car car=new Car();
        car.setBrand(searchCarDto.getBrand());
        car.setType(searchCarDto.getType());
        car.setTransmission(searchCarDto.getTransmission());
        car.setColor(searchCarDto.getColor());
        ExampleMatcher exampleMatcher=
                ExampleMatcher.matchingAll().
                        withMatcher("brand",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()
                        ).withMatcher("type",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("transmission",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("color",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Car> carExample=Example.of(car,exampleMatcher);
        List<Car> carList=carRepository.findAll(carExample);
        CarDtoListDto carDtoListDto =new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
        return carDtoListDto;
    }


}
