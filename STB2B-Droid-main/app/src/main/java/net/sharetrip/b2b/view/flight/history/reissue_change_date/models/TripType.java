package net.sharetrip.b2b.view.flight.history.reissue_change_date.models;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({TripType.ROUND_TRIP, TripType.ONE_WAY, TripType.MULTI_CITY})
@Retention(RetentionPolicy.SOURCE)
public @interface TripType {
    String ROUND_TRIP = "Return";
    String ONE_WAY = "OneWay";
    String MULTI_CITY = "Other";
}
