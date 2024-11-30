package com.example.Elite.Edge.Properties.Controller;

import com.example.Elite.Edge.Properties.DTO.TenantDto;
import com.example.Elite.Edge.Properties.DTO.UnitDto;
import com.example.Elite.Edge.Properties.Enums.PropertyType;
import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Enums.unitType;
import com.example.Elite.Edge.Properties.Exceptions.UnitException;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import com.example.Elite.Edge.Properties.Model.Tenants;
import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Service.UnitService;
import com.example.Elite.Edge.Properties.Wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/units")
public class UnitController {


    private final UnitService unitService;

    public UnitController(UnitService unitService){
        this.unitService = unitService;
    }
    @GetMapping(value = "all-units")
    public ResponseEntity<ApiResponse<List<Units>>> retrieveUnits(){
        try{
           List<Units> fetchUnits = unitService.retrieveUnits();
           return new ResponseEntity<>(new ApiResponse<>("success", fetchUnits), HttpStatus.OK);
        }catch(RuntimeException runtimeException){
            throw new UnitException("Units could not be found");
        }
    }

    //fetch Archive units - units that soft deleted due to property deletion
    //can only be done by an admin to recover for historical data
    @GetMapping(path = "archived/units")
    public ResponseEntity<Object> fetchArchivedUnits(){
        try{
            List<Units> archivedUnits = unitService.fetchArchivedUnits();
            return new ResponseEntity<>(archivedUnits, HttpStatus.OK);
        }catch(RuntimeException runtimeException){
            throw new UnitException("Archived properties could not be fetched");
        }

    }


    @GetMapping(path = "property/{id}/units")
    public ResponseEntity<ApiResponse<Object>> retrievePropertyUnits(@PathVariable("id")
                                                                     Long propertyId){
        try{
            List<Units> retrieveProperty = unitService.retrievePropertyUnits(propertyId);
            return new ResponseEntity<>(new ApiResponse<>("success", retrieveProperty),
                    HttpStatus.OK);
        }catch (RuntimeException runtimeException){
            throw new UnitException("units for property: " + propertyId + " were not found");
        }
    }

    @GetMapping(value = "units/price/range")
    public ResponseEntity<ApiResponse<Object>> fetchUnitsByPrice(
            @RequestParam("Property_id")Long id,
            @RequestParam("minimum_rent_price")double min,
            @RequestParam("maximum_rent_price")double max){
        try {
            List<Units> unitsInRange = unitService.retrieveUnitsInRange(id, min, max);
            return ResponseEntity.ok(new ApiResponse<>("success", unitsInRange));
        }catch (RuntimeException runtimeException){
            throw new UnitException("enter a different range");
        }

    }

    //retrieve all units from a property that are Apartment or studio depending on what the user inputs
    @GetMapping(value = "property/units/e/type")
    public ResponseEntity<ApiResponse<Object>> fetchUnitType(
            @RequestParam Long id,
            @RequestParam("unit_type") unitType Unittype
            ){
        try{
            List<Units> units = unitService.retrieveByType(id, Unittype);
            if(units.isEmpty()){
                throw new UnitException("No available units of the selected type at the moment.");
            }
            return new ResponseEntity<>(new ApiResponse<>("success", units), HttpStatus.OK);

        }catch (RuntimeException runtimeException){
            throw new UnitException("Ensure a valid and correct type was selected");
        }
    }

    @GetMapping(path = "property/{property_id}/units/{unit_id}/tenants")
    public ResponseEntity<ApiResponse<?>> fetchUnitTenants(
            @PathVariable("property_id")Long propertyId,
            @PathVariable("unit_id") Long unitId){

        TenantDto unitTenants = unitService.fetchTenant(propertyId, unitId);
        return new ResponseEntity<>(new ApiResponse<>("success",unitTenants), HttpStatus.OK);

    }

    @PostMapping(path = "property/{id}/create-unit")
    public ResponseEntity<ApiResponse<Object>> createUnit(
            @PathVariable Long id,
            @RequestBody UnitDto unitDto){
        UnitDto unitDto1 = unitService.createUnit(id, unitDto);
        return new ResponseEntity<>(new ApiResponse<>("success", unitDto1), HttpStatus.CREATED);
    }


}
