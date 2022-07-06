
import com.ced.commons.util.log.Logger
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Vyron Arediano
 * @since 1.0.0
 */
object DateTimeUtil {
    private val TAG = DateTimeUtil::class.java.simpleName

    const val SECOND_IN_MILLIS: Long = 1000

    private val WEB_UI_DATE_FORMAT = "MMM dd, yyyy"
    private val MOBILE_UI_DATE_FORMAT = "yyyy-MM-dd"
    private val MOBILE_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"

    private const val TIME_FORMAT_12H_PATTERN = "h:mm aa"
    private const val TIME_FORMAT_24H_PATTERN = "H:mm"

    val TIME_FORMAT_UTC_12H: DateFormat
        get() = SimpleDateFormat(TIME_FORMAT_12H_PATTERN, Locale.ENGLISH)
    val TIME_FORMAT_UTC_24H: DateFormat
        get() = SimpleDateFormat(TIME_FORMAT_24H_PATTERN, Locale.ENGLISH)

    val WEB_UI_DATE_FORMATTER = SimpleDateFormat(WEB_UI_DATE_FORMAT, Locale.getDefault())
    val WEB_DATE_FORMATTER = SimpleDateFormat(MOBILE_UI_DATE_FORMAT, Locale.getDefault())


    //ordered by precision desc
    init {
        WEB_UI_DATE_FORMATTER.timeZone = TimeZone.getTimeZone("UTC")
        WEB_DATE_FORMATTER.timeZone = TimeZone.getTimeZone("UTC")
    }

    /**
     *
     * @param timeInSeconds
     * @param isMilitaryTime
     * @return
     */
    fun toTimeFormatFromSeconds(timeInSeconds: Long?, isMilitaryTime: Boolean): String {
        val time = timeInSeconds ?: 0L
        return toTimeFormatFromMilli(time * SECOND_IN_MILLIS, isMilitaryTime)
    }

    /**
     *
     * @param timeInMilliSeconds
     * @param isMilitaryTime
     * @return
     */
    fun toTimeFormatFromMilli(timeInMilliSeconds: Long, isMilitaryTime: Boolean): String {
        val date = Date(timeInMilliSeconds)
        return toTimeFormat(date, isMilitaryTime, TimeZone.getTimeZone("UTC"))
    }

    fun toTimeFormat(date: Date, isMilitaryTime: Boolean, timeZone: TimeZone): String {
        return if (isMilitaryTime) {
            TIME_FORMAT_UTC_24H.let {
                it.timeZone = timeZone
                it.format(date)
            }
        } else {
            TIME_FORMAT_UTC_12H.let {
                it.timeZone = timeZone
                it.format(date)
            }
        }
    }

    private fun isDateValid(stringDate: String, dateFormat: String): Boolean {
        val sdf = SimpleDateFormat(dateFormat)
        sdf.isLenient = false

        try {
            sdf.parse(stringDate)

        } catch (ex: ParseException) {
            return false
        }
        return true

    }

    fun parseDate(dateToParse: String): Date? {
        if (dateToParse.isBlank()) {
            return null
        }

        var sdf = SimpleDateFormat(MOBILE_DATE_TIME, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        var stringDate = dateToParse
        var format = MOBILE_DATE_TIME
        try {
            if (dateToParse.indexOf('+') > -1) {
                stringDate = parseDate(dateToParse, '+')
                return sdf.parse(stringDate)
            } else if (dateToParse.indexOf('.') > -1) {
                stringDate = parseDate(dateToParse, '.')
                return sdf.parse(stringDate)
            } else {

                val isMobileUIFormat = isDateValid(stringDate, MOBILE_UI_DATE_FORMAT)
                if (isMobileUIFormat) {
                    format = MOBILE_UI_DATE_FORMAT
                    sdf = SimpleDateFormat(MOBILE_UI_DATE_FORMAT, Locale.US)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    return sdf.parse(stringDate)
                } else {
                    format = WEB_UI_DATE_FORMAT
                    sdf = SimpleDateFormat(WEB_UI_DATE_FORMAT, Locale.US)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    return sdf.parse(stringDate)
                }

            }
        } catch (e: ParseException) {
            Logger.w(TAG, "Error parsing date, date : $stringDate, format :$format")
        }
        return null
    }

    private fun parseDate(dateToParse: String, indicator: Char): String {
        val stringDate: String

        if (dateToParse.indexOf(dateToParse, indicator.toInt()) > -1) {
            if (dateToParse.length > 23) {
                stringDate = dateToParse.substring(0, 23)
            } else {
                stringDate = dateToParse.substring(
                    0,
                    dateToParse.lastIndexOf(dateToParse, indicator.toInt())
                )
            }
        } else {
            stringDate = dateToParse
        }

        return stringDate
    }

    fun toWebUiDateFormat(dateToFormat: Date?): String? {
        return if (dateToFormat != null) {
            WEB_UI_DATE_FORMATTER.format(dateToFormat)
        } else null
    }


}
