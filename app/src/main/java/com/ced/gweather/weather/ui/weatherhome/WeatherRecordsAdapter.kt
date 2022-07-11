package com.ced.gweather.weather.ui.weatherhome

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ced.commons.util.toDateTimeString
import com.ced.gweather.R
import com.ced.gweather.weather.features.weatherhome.WeatherRecordsViewModel
import com.ced.gweather_core.domain.model.WeatherModel

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

class WeatherRecordsAdapter(private val weatherRecordsViewModel: WeatherRecordsViewModel) :
    RecyclerView.Adapter<WeatherRecordsAdapter.WeatherRecordViewHolder>() {

    private var weatherRecords: MutableList<WeatherModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherRecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherRecordViewHolder(parent, weatherRecordsViewModel, inflater)

    }

    override fun onBindViewHolder(holder: WeatherRecordViewHolder, position: Int) {
        holder.bind(weatherRecords[position])
    }

    override fun getItemCount(): Int = weatherRecords.size

    fun setWeatherRecords(weatherRecords: List<WeatherModel>) {
        this.weatherRecords.clear()
        this.weatherRecords.addAll(weatherRecords)
        notifyDataSetChanged()
    }

    class WeatherRecordViewHolder(
        val parent: ViewGroup,
        private val weatherRecordsViewModel: WeatherRecordsViewModel,
        inflater: LayoutInflater
    ) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.weather_record_row_item, parent, false)) {

        private var weatherRecordImgView: ImageView? = null
        private var weatherRecordInfoTv: AppCompatTextView? = null
        private var weatherRecordDateCreatedTv: AppCompatTextView? = null

        init {
            weatherRecordImgView = itemView.findViewById(R.id.ivWeatherRecordIcon)
            weatherRecordInfoTv = itemView.findViewById(R.id.tvWeatherRecordInfo)
            weatherRecordDateCreatedTv = itemView.findViewById(R.id.tvWeatherRecordDateCreated)
        }

        fun bind(weather: WeatherModel) {
            val imageUrl =
                "http://openweathermap.org/img/wn/${weather.weather?.first()?.icon}@2x.png"
            parent.context.let { context ->
                weatherRecordImgView?.apply {
                    Glide.with(context)
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .into(this)
                }
            }

            val loc = weather.name
            val desc = weather.weather?.first()?.description?.capitalize()
            val deg = "${weather.main?.temp.toString()} \u2103"
            weatherRecordInfoTv?.text = "$loc | $desc | $deg ".capitalize()

            weatherRecordDateCreatedTv?.text = weather.dateCreated?.toDateTimeString()
        }

    }
}
