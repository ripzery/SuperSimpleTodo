package com.onemorebit.supersimpletodo.Utils;

import com.squareup.otto.Bus;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class BusProvider {
    private static Bus bus;

    public static Bus getInstance(){
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }


}
