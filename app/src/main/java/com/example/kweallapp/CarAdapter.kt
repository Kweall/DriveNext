package com.example.kweallapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kweallapp.R

class CarAdapter(private var carList: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    // ViewHolder для хранения ссылок на виджеты
    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carImage: ImageView = itemView.findViewById(R.id.car_image)
        val carModel: TextView = itemView.findViewById(R.id.car_model)
        val carBrand: TextView = itemView.findViewById(R.id.car_brand)
        val carPrice: TextView = itemView.findViewById(R.id.car_price)
        val dailyPrice: TextView = itemView.findViewById(R.id.daily_price)
//        val transmissionIcon: ImageView = itemView.findViewById(R.id.characteristics_layout).getChildAt(0) as ImageView
//        val transmissionText: TextView = itemView.findViewById(R.id.characteristics_layout).getChildAt(1) as TextView
//        val fuelIcon: ImageView = itemView.findViewById(R.id.characteristics_layout).getChildAt(2) as ImageView
//        val fuelText: TextView = itemView.findViewById(R.id.characteristics_layout).getChildAt(3) as TextView
        val bookButton: Button = itemView.findViewById(R.id.book_button)
        val detailsButton: Button = itemView.findViewById(R.id.details_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        // Привязка данных к виджетам
        val car = carList[position]
        holder.carImage.setImageResource(car.image)
        holder.carModel.text = car.model
        holder.carBrand.text = car.brand
        holder.carPrice.text = car.pricePerDay.toString()
        holder.dailyPrice.text = R.string.text_per_day.toString()
//        holder.transmissionText.text = car.transmission
//        holder.fuelText.text = car.fuelType

        holder.bookButton.setOnClickListener {

        }
        holder.detailsButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return carList.size
    }

}