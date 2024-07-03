package com.epam.training.gen.ai.semantic.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculatorPlugin {

    private static final String AGE_TEMPLATE = "Your age is: %d years, %d months, and %d days";

    @DefineKernelFunction(name = "calculateAge", description = "Calculate the age based on the birth year, month, and day")
    public String calculateAge(
            @KernelFunctionParameter(name = "year", description = "Year of birth") String birthYear,
            @KernelFunctionParameter(name = "month", description = "Month of birth") String birthMonth,
            @KernelFunctionParameter(name = "day", description = "Day of birth") String birthDay) {
        var currentDate = LocalDate.now();
        var birthDate = LocalDate.of(Integer.parseInt(birthYear),
                Integer.parseInt(birthMonth),
                Integer.parseInt(birthDay));
        var period = Period.between(birthDate, currentDate);
        var years = period.getYears();
        var months = period.getMonths();
        var days = period.getDays();
        return String.format(AGE_TEMPLATE, years, months, days);
    }
}
