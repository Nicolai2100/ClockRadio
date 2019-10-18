package dk.dtu.philipsclockradio;

import java.util.ArrayList;
import java.util.Collections;

public class StateRadioOn extends StateAdapter {
    //todo am og fm frekvens gemmes og aktiveres når der skiftes
    private static StateRadioOn instant = null;

    private ArrayList<Double> radioChannelsList;

    private Double currentRadioChannel = null;
    private String currentRadioFrequency;
    private Double fmRadioChannel = 0.0;
    private Double amRadioChannel = 0.0;

    StateRadioOn() {
        radioChannelsList = getRadioChannelsList();

        if (currentRadioChannel == null) {
            currentRadioChannel = 0.0;
            fmRadioChannel = currentRadioChannel;
            currentRadioFrequency = "FM";
        }
    }

    public static StateRadioOn getInstant() {
        if (instant == null) {
            instant = new StateRadioOn();
        }
        return instant;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.showDisplayFrequencyRadio(currentRadioFrequency);
        context.setRadio(currentRadioChannel);
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        context.setState(StateStandby.getInstance(context.getTime()));
    }

    //Skifter mellem AM og FM
    @Override
    public void onClick_Power(ContextClockradio context) {
        if (currentRadioFrequency.equalsIgnoreCase("FM")) {
            fmRadioChannel = currentRadioChannel;
            currentRadioFrequency = "AM";
            currentRadioChannel = amRadioChannel;

        } else {
            amRadioChannel = currentRadioChannel;
            currentRadioFrequency = "FM";
            currentRadioChannel = fmRadioChannel;
        }
        context.showDisplayFrequencyRadio(currentRadioFrequency);
        context.setRadio(currentRadioChannel);
    }

    @Override
    public void onExitState(ContextClockradio context) {
        //context.ui.turnOffTextBlink();
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        currentRadioChannel = currentRadioChannel + 1;
        context.setRadio(currentRadioChannel);
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        currentRadioChannel = currentRadioChannel - 1;
        context.setRadio(currentRadioChannel);
    }

    //Søger efter næste kanal
    @Override
    public void onLongClick_Min(ContextClockradio context) {
        int index = radioChannelsList.indexOf(currentRadioChannel);
        double newChannel = 0;

        if (!radioChannelsList.contains(currentRadioChannel)) {
            currentRadioChannel = findClosest(radioChannelsList, currentRadioChannel);
            context.setRadio(currentRadioChannel);
            return;
        }

        try {
            newChannel = radioChannelsList.get(index + 1);
            if (newChannel > currentRadioChannel) {
                currentRadioChannel = newChannel;
            } else
                currentRadioChannel = radioChannelsList.get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(0);
        } catch (IndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(0);
        }
        context.setRadio(currentRadioChannel);
    }

    //Søger efter forrige kanal
    @Override
    public void onLongClick_Hour(ContextClockradio context) {
        int index = radioChannelsList.indexOf(currentRadioChannel);
        double newChannel;

        try {
            newChannel = radioChannelsList.get(index - 1);
            if (newChannel != 0) {
                currentRadioChannel = newChannel;
            } else
                currentRadioChannel = radioChannelsList.get(radioChannelsList.size() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(radioChannelsList.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(radioChannelsList.size() - 1);
        }

        if (currentRadioFrequency.equalsIgnoreCase("am")) {
            amRadioChannel = currentRadioChannel;
        } else {
            fmRadioChannel = currentRadioChannel;
        }
        context.setRadio(currentRadioChannel);

    }

    @Override
    public void onClick_Preset(ContextClockradio context) {
/*
        context.setState(new StateStandby(context.getTime()));
*/
    }

    private ArrayList<Double> getRadioChannelsList() {
        radioChannelsList = new ArrayList<>();
        radioChannelsList.add(101.5);
        radioChannelsList.add(97.0);
        radioChannelsList.add(103.2);
        radioChannelsList.add(102.7);
        radioChannelsList.add(99.4);
        radioChannelsList.add(106.6);
        radioChannelsList.add(90.1);
        radioChannelsList.add(101.5);
        radioChannelsList.add(100.6);
        Collections.sort(radioChannelsList);
        return radioChannelsList;
    }

    //Taget fra: https://www.geeksforgeeks.org/find-closest-number-array/

    public static double findClosest(ArrayList<Double> arr, double target) {
        int n = arr.size();

        // Corner cases todo gør færdig
        if (target <= arr.get(0))
            return arr.get(0);
        if (target >= arr.get(n - 1))
            return arr.get(n - 1);

        // Doing binary search
        int i = 0, j = n, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (arr.get(mid) == target)
                return arr.get(mid);

            /* If target is less than array element,
               then search in left */
            if (target < arr.get(mid)) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > arr.get(mid - 1))
                    return getClosest(arr.get(mid - 1),
                            arr.get(mid - 1), target);

                /* Repeat for left half */
                j = mid;
            }

            // If target is greater than mid
            else {
                if (mid < n - 1 && target < arr.get(mid + 1))
                    return getClosest(arr.get(mid),
                            arr.get(mid + 1), target);
                i = mid + 1; // update i
            }
        }

        // Only single element left after search
        return arr.get(mid);
    }

    public static double getClosest(double val1, double val2,
                                    double target) {
        if (target - val1 >= val2 - target)
            return val2;
        else
            return val1;
    }
}

