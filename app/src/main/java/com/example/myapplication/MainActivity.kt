package com.example.myapplication

import android.content.Intent
import android.graphics.*
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.*
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import android.graphics.RectF


class MainActivity : AppCompatActivity(), GeoObjectTapListener, InputListener {
    private var mapview: MapView? = null
    private var win: Intent? = null
    private var userLocation: UserLocationLayer? = null
    private val ROUTE_START_LOCATION = Point(53.32739975386613, 83.80091094546437)
    private val ROUTE_END_LOCATION = Point(53.33549350179344, 83.79042045529683)
    private val SCREEN_CENTER = Point(
        (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
        (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
    )

    //    private var mapObjects: MapObjectCollection? = null
//    private var drivingRouter: DrivingRouter? = null
//    private var drivingSession: DrivingSession? = null
    private val tours = mapOf<String, List<Point>>(
        "Барнаул" to listOf(
            Point(53.32850203028269, 83.79879769202715),
            Point(53.32880069617321, 83.79799839374071),
            Point(53.32956822576019, 83.79707571383962),
            Point(53.329328135837585, 83.79836146734193),
            Point(53.33035256326821, 83.80123143098784),
            Point(53.332761732890305, 83.79861284399702),
            Point(53.331409488626775, 83.79521060376716),
            Point(53.335957741429546, 83.79028576126338),
            Point(53.335758664633936, 83.78972786178828),
            Point(53.32787619944548, 83.79820494353297),
            Point(53.32749563264143, 83.79784552752494),
            Point(53.32745548826491, 83.7976524084759),
            Point(53.32734468958875, 83.79767923056603),
            Point(53.3271044456681, 83.79608668956321),
            Point(53.32755423412678, 83.79588678030736),
            Point(53.32762768644042, 83.79582670741135),
            Point(53.326678024334676, 83.79307848992337),
            Point(53.32635071334362, 83.79333551311218),
            Point(53.32630093296883, 83.79333953642566),
            Point(53.32588990023997, 83.79368472617314),
            Point(53.3258810681464, 83.7937578163688),
            Point(53.32581844052115, 83.79379536729503),
            Point(53.32576705368272, 83.79379737895175),
            Point(53.3254617440438, 83.79401033440897),
            Point(53.32544086797283, 83.79396004298992),
            Point(53.32544086797283, 83.79396004298992),
            Point(53.325322813373106, 83.7940623467533),
            Point(53.325322411909, 83.79406100564874),
            Point(53.325349711460504, 83.79415220075525),
            Point(53.32522183106379, 83.79426354041676),
            Point(53.32518730505349, 83.79416698089224),
            Point(53.32506188520776, 83.79427252034014),
            Point(53.3250883819941, 83.79436639765568),
            Point(53.324965934585535, 83.79447435656853),
            Point(53.32492899956019, 83.79436639765568),
            Point(53.32479370468213, 83.7944777093298),
            Point(53.32480735463003, 83.79453336516687),
            Point(53.3247535577511, 83.79457963327235),
            Point(53.32397059884121, 83.79523552621569)
        ),
        "Новосибирск" to listOf(
            Point(55.006225843079775, 82.94069730921176),
            Point(55.007714346104265, 82.93714010781603),
            Point(55.007931706202925, 82.93638104266482),
            Point(55.008441530773766, 82.93529124774221),
            Point(55.010180460252705, 82.93110060667625),
            Point(55.009462128784456, 82.93009209608664),
            Point(55.01034664189847, 82.92787648931981),
            Point(55.010560904757774, 82.92795159117225),
            Point(55.01125883242222, 82.92629775625369),
            Point(55.011160566244726, 82.92605635744235),
            Point(55.011085421357585, 82.92533953708312),
            Point(55.011085035998825, 82.92517055791517),
            Point(55.01116950877384, 82.92494938844561),
            Point(55.01154844757915, 82.92544842608665)
        ),
        "Бийск" to listOf(
            Point(52.538690484350624, 85.22397126163578),
            Point(52.539026995663086, 85.22360054908829),
            Point(52.53916437264643, 85.22341949997978),
            Point(52.53905806906617, 85.2232236987217),
            Point(52.53897036241025, 85.22320624177075),
            Point(52.538854245663096, 85.22291388098813),
            Point(52.53886651153126, 85.22286023680782),
            Point(52.53869296210774, 85.22251052259973),
            Point(52.53830359466074, 85.223029760679),
            Point(52.538107337828194, 85.22329529937149),
            Point(52.537814015932874, 85.22359946107865),
            Point(52.53728703145911, 85.2241090208809),
            Point(52.53696892399744, 85.22441479270856),
            Point(52.536901791339304, 85.22448603208078),
            Point(52.53585442325718, 85.22570595098094),
            Point(52.535836023185105, 85.2256543184574),
            Point(52.53580249414495, 85.2256878460701)
        )
    )
    private val marks =
        mapOf<String, List<Point>>(
            "Барнаул" to listOf(
                Point(53.3284053750807, 83.79825557222476),
                Point(53.32935263085038, 83.7990272427424),
                Point(53.332379799342284, 83.79915356238976),
                Point(53.331552189700446, 83.79547199317182),
                Point(53.335493370879746, 83.79041905700814),
                Point(53.33541630816207, 83.78950978815209),
                Point(53.32761754267105, 83.79805263987825),
                Point(53.327424688604886, 83.7969415787309),
                Point(53.32395410795057, 83.7952740257019)
            ),
            "Новосибирск" to listOf(
                Point(55.00628855629863, 82.93993571182631),
                Point(55.00747681143226, 82.93907084730152),
                Point(55.01012210665186, 82.92855031701133),
                Point(55.01176481965466, 82.92556586909825)
            ),
            "Бийск" to listOf(
                Point(52.536694418147064, 85.22464060213663),
                Point(52.53654160646039, 85.22482475006424),
                Point(52.53724399714357, 85.22395826915779),
                Point(52.53905716137412, 85.22309123856925)
            )

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("7d666571-4338-4d6b-bc96-ae826ff9b3b0")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapview = findViewById<View>(R.id.mapview) as MapView
        mapview!!.map.move(
            CameraPosition(
                SCREEN_CENTER, 10F, 0F, 0F
            )
        )
        val mapKit: MapKit? = MapKitFactory.getInstance()
        userLocation = mapKit?.createUserLocationLayer(mapview!!.mapWindow)
        userLocation!!.isVisible = true
        userLocation!!.isHeadingEnabled = true
        mapview!!.map.addTapListener(this)
        mapview!!.map.addInputListener(this)
        val geopos = findViewById<Button>(R.id.myLocation)
        geopos.setOnClickListener {
            if (userLocation?.cameraPosition()?.target != null) {
                mapview!!.map.move(
                    CameraPosition(userLocation?.cameraPosition()!!.target, 15.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0F),
                    null
                )
            }
        }


        val tours = findViewById<Button>(R.id.tours)
        tours.setOnClickListener {
            win = Intent(this, ToursActivity::class.java)
            startActivityForResult(win, 10)
        }

    }

    private fun drawMarks(marks: List<Point>) {
        for (mark in marks) {
            mapview!!.map.mapObjects.addPlacemark(
                mark,
                ImageProvider.fromBitmap(drawSimpleBitmap())
            )
        }
    }

    private fun drawTour(points: List<Point>) {
        mapview?.map?.mapObjects?.addPolyline(Polyline(points))

    }

    private fun drawSimpleBitmap(): Bitmap? {
        val bitmap = Bitmap.createBitmap(200, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val mark = BitmapFactory.decodeResource(resources, R.drawable.mark)
        canvas.drawBitmap(mark, null, Rect(0, 0, 200, 100), paint)
        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            val city: String? = data?.getStringExtra("city")
            drawTour(tours[city]!!)
            drawMarks(marks[city]!!)
            mapview!!.map.move(
                CameraPosition(
                    tours[city]!![0], 18F, 0F, 0F
                )
            )


        }

    }

    override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {
        val selectionMetadata: GeoObjectSelectionMetadata = geoObjectTapEvent
            .geoObject
            .metadataContainer
            .getItem<GeoObjectSelectionMetadata>(GeoObjectSelectionMetadata::class.java)
        val name: String? = geoObjectTapEvent.geoObject.name
        win = Intent(this, describeActivity::class.java)
        win!!.putExtra("describe", name)
        startActivity(win)



        mapview?.map?.selectGeoObject(selectionMetadata.id, selectionMetadata.layerId)
        return true


    }

    override fun onMapTap(map: Map, point: Point) {
        mapview?.getMap()?.deselectGeoObject()
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        TODO("Not yet implemented")
    }


}

//    private fun submitRequest() {
//        val drivingOptions = DrivingOptions()
//        val vehicleOptions = VehicleOptions()
//        val requestPoints = ArrayList<RequestPoint>()
//        requestPoints.add(
//            RequestPoint(
//                ROUTE_START_LOCATION,
//                RequestPointType.WAYPOINT,
//                null
//            )
//        )
//        requestPoints.add(
//            RequestPoint(
//                ROUTE_END_LOCATION,
//                RequestPointType.WAYPOINT,
//                null
//            )
//        )
//
//        drivingSession =
//            drivingRouter!!.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)


//    override fun onStop() {
//        mapview?.onStop()
//        MapKitFactory.getInstance().onStop()
//        super.onStop()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        MapKitFactory.getInstance().onStart()
//        mapview?.onStart()
//    }
