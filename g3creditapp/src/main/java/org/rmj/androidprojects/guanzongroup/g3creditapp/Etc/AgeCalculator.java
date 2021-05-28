package org.rmj.androidprojects.guanzongroup.g3creditapp.Etc;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgeCalculator {

    @SuppressLint("NewApi")
    public int getAge(String BirthDate){
        int years = 0;
        int month = 0;
        int day = 0;
        try{
            @SuppressLint("SimpleDateFormat") Date parseDate = (new SimpleDateFormat("yyyy-mm-dd").parse(BirthDate));
            @SuppressLint("SimpleDateFormat")String birthdate = new SimpleDateFormat("dd/mm/yyyy").format(parseDate);
            @SuppressLint("SimpleDateFormat")SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            Date birthDate = sdf.parse(birthdate);

            Calendar birthday = Calendar.getInstance();
            birthday.setTimeInMillis(birthDate.getTime());

            long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(currentTime);

            years = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
            int currentMonth = now.get(Calendar.MONTH) + 1;
            int birthMonth = birthday.get(Calendar.MONTH) + 1;

            month = currentMonth - birthMonth;

            if(month < 0){
                years--;
                month = 12 - birthMonth + currentMonth;
                if(now.get(Calendar.DATE) < birthday.get(Calendar.DATE))
                    month--;
            } else if(month == 0 && now.get(Calendar.DATE) < birthday.get(Calendar.DATE)){
                years--;
                month = 11;
            }

            //Calculate the days
            if (now.get(Calendar.DATE) > birthday.get(Calendar.DATE))
                day = now.get(Calendar.DATE) - birthday.get(Calendar.DATE);
            else if (now.get(Calendar.DATE) < birthday.get(Calendar.DATE))
            {
                int today = now.get(Calendar.DAY_OF_MONTH);
                now.add(Calendar.MONTH, -1);
                day = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH) + today;
            }
            else {
                month = 0;
                if (month == 12) {
                    years++;
                    month = 0;
                }
            }

            return years;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
