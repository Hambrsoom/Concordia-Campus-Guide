package com.example.concordia_campus_guide;

import android.Manifest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.StringDef;

import com.example.concordia_campus_guide.Models.Coordinates;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;

public class  ClassConstants {
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    // Transport types constants
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TRANSIT, WALKING, BICYCLING, DRIVING})
    public @interface TransportType {}

    public static final String TRANSIT = "transit";
    public static final String WALKING = "walking";
    public static final String BICYCLING = "bicycling";
    public static final String DRIVING = "driving";
    public static final String SHUTTLE = "shuttle";

    public static final Coordinates SGW_SHUTTLE_STOP = new Coordinates(-73.5784985125064, 45.4971190430539);
    public static final Coordinates LOYOLA_SHUTTLE_STOP = new Coordinates(-73.638241700828, 45.4584776424937);

    public static final String ShuttlePolylineSGWLOY = "icutGlxa`MpCzBj@VNDv@T~BkHzC_JrAgEz@x@tJlJ`DvC~@|@`AtAf@_A\\u@l@_@ZIZAb@F\\N`@^tChEjHlL`A`BvDfHV`@FDTDdA~Bz@|An@jA|FnKfPfZ~ClFn@jA`@r@d@n@lBxBx@h@t@t@Zr@VXp@f@j@TbAXnAd@fCdAdAb@\\Xx@rAZb@Z\\v@r@x@t@x@nAn@zAZr@vBzCJLJBbD|ErApBJ\\@N?Jv@Zj@n@hDnFx@rAnApERZp@dBRb@vBjDfBrCVn@Rn@Nx@jBpJXpAZx@n@~AhAjCDh@Z|@Pt@Fb@?h@CtAEfA@h@Fr@f@jA`AvB~BfDHJPFr@zArAfDRf@Pr@^jBXrBnBfPj@rDb@fBNf@^bAPb@@Xb@hAQ\\sBjGuBhGeDnJI\\HPtDzInDhIzClHpBtEZj@lA`Dv@`BNXnAdBJTdDdE";
    public static final String ShuttlePolylineLOYSGW = "armtGrmm`MtCjDbBpBhAnAp@l@`C`BnDjCvAqFj@gBvAeG|A}DLM{@kEy@uEoAuFa@iAsAaCq@kAY[a@iAIoAhBrA~@l@dCzAbBfAxDtBTDx@B~@Cf@Kb@Sx@k@R[HY?]EMi@_A_@u@yBgE[m@uAqBsAuBuEoHu@eA{AcCK[EKC]?OLOd@u@Va@DO@MAQEMgDsFsBkDQWkAaBkBeCqDmFWSOEeAuByA{Cc@}@cCyFuAyD_BwEiHsWu@eCs@cCSu@CCWo@_BmF{D_MiDeKwC}IeBoEiBmE}@cB{BkDy@aAs@y@_@c@c@[}BcB}@aAkCcBsCoAa@Q[_@m@q@_@]_@Sa@S_C}@_D}@{IeCiCw@eBq@oAs@qA{@{AoAwAyA[a@m@u@_@q@yAmCyAqCwCmEoAyBaLaTsHoN}BsDAa@_@y@wCqFo@mAeAsBW_@SWk@_@a@QaAc@]Uc@k@yDuFsBkCi@m@QO]Se@Is@@u@TSJc@Z[\\SVsBgBiF_FaIsHoCiCKXcAbCcCxGyBnFgB~EpAfA";

    //Polyline styling
    public static final List<PatternItem> WALK_PATTERN = Arrays.asList(new Gap(20), new Dash(20));

}
